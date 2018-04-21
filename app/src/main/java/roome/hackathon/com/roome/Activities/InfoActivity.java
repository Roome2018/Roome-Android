package roome.hackathon.com.roome.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.viewpagerindicator.CirclePageIndicator;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import roome.hackathon.com.roome.Adapters.SliderAdapter;
import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.Models.Item;
import roome.hackathon.com.roome.Models.Medium;
import roome.hackathon.com.roome.Adapters.TenantAdapter;
import roome.hackathon.com.roome.R;
import roome.hackathon.com.roome.Utils.VolleyRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InfoActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private CoordinatorLayout mCoordinatorLayout;
    private static int currentPage = 0;
    private int NUM_PAGES;
    private SliderAdapter adapter;
    private ArrayList<Medium> list;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvNumAvailable;
    private TextView tvAvailable;
    private TextView tvPrice;
    private TextView tvNumPrice;
    private TextView tvPeople;
    private TextView tvNumPeople;
    private RecyclerView recyclerview;
    private Button buBook;
    private AVLoadingIndicatorView myProgress;
    private TenantAdapter tenantAdapter;
    private TextView tv_noItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        mViewPager = (ViewPager) findViewById(R.id.pagerhome);
        list = new ArrayList<>();
        final Item item = getIntent().getParcelableExtra(AppConstants.ITEM_PARC);
        list = new ArrayList<Medium>(item.getMedia());
        if (adapter == null)
            adapter = new SliderAdapter(getSupportFragmentManager(), list);
        adapter.notifyDataSetChanged();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(0);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_subTitle);
        tvNumAvailable = (TextView) findViewById(R.id.tv_numAvailable);
        tvAvailable = (TextView) findViewById(R.id.tv_available);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvNumPrice = (TextView) findViewById(R.id.tv_num_price);
        tvPeople = (TextView) findViewById(R.id.tv_people);
        tvNumPeople = (TextView) findViewById(R.id.tv_num_people);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        buBook = (Button) findViewById(R.id.bu_book);
        myProgress = findViewById(R.id.myProgress);
        tv_noItem = findViewById(R.id.tv_noItem);
        AppCompatImageView ic_back = findViewById(R.id.ic_back);

        tvTitle.setText(item.getTitle());
        tvSubTitle.setText(item.getInfo());
        tvNumAvailable.setText(item.getRemain() + "");
        tvNumPrice.setText(item.getPrice() + " $");
        tvNumPeople.setText(item.getMaxTenants() - item.getRemain() + "");

        buBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker(InfoActivity.this, item.getId());
            }
        });
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mViewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(4 * density);
        NUM_PAGES = list.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
        if (item.getTenants() != null || item.getTenants().size() != 0 || !item.getTenants().isEmpty()) {
            tv_noItem.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
            recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            tenantAdapter = new TenantAdapter(InfoActivity.this, item.getTenants());
            recyclerview.setAdapter(tenantAdapter);
        } else {
            tv_noItem.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //    Your booking request has been sent
    public void createDatePicker(Context context, final int roodId) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myTime = dayOfMonth + "-" + (month + 1) + "-" + year;
                startAnim();
                booking(myTime, roodId);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    private void booking(String date, int roodId) {
        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
            @Override
            public void onDataReceived(Object o) {
                boolean status = ((boolean) o);
                if (status) {
                    stopAnim();
//                    Intent intent=new Intent(InfoActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    Toast.makeText(InfoActivity.this, R.string.book_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    stopAnim();
                    Toast.makeText(InfoActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyRequests.ErrorReceiveData() {
            @Override
            public void onErrorDataReceived(VolleyError o) {
                stopAnim();
                Toast.makeText(InfoActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
            }
        }).booking(InfoActivity.this, date, roodId);
    }

    void startAnim() {
        myProgress.setVisibility(View.VISIBLE);
        buBook.setVisibility(View.INVISIBLE);
        myProgress.smoothToShow();
    }

    void stopAnim() {
        myProgress.setVisibility(View.GONE);
        buBook.setVisibility(View.VISIBLE);
        myProgress.smoothToHide();
    }
}