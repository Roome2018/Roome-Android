package roome.hackathon.com.roome.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.Models.Item;
import roome.hackathon.com.roome.R;
import roome.hackathon.com.roome.Utils.SharedPrefUtil;
import roome.hackathon.com.roome.Utils.Util;
import roome.hackathon.com.roome.Utils.VolleyRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class AddBookActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 445;
    private EditText ed_titel, ed_owner, ed_price,
            ed_max_tenant, ed_adress, ed_info;
    private Button bu_add_book, bu_add_pictures;
    private AppCompatImageView ic_back;
    private Double location_latitude;
    private Double location_longitude;
    private ViewGroup addView;
    private int REQUEST_PERMISSIONS = 500;
    private Uri selectedImage = null;
    private String image_str = "";
    private ArrayList<MediaItem> imgList;
    private ArrayList<MediaItem> mMediaSelectedList = new ArrayList<>();
    private ArrayList<File> fileParams = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ed_titel = findViewById(R.id.ed_titel);
        ed_owner = findViewById(R.id.ed_owner);
        ed_price = findViewById(R.id.ed_price);
        ed_max_tenant = findViewById(R.id.ed_max_tenant);
        ed_adress = findViewById(R.id.ed_adress);
        ed_info = findViewById(R.id.ed_info);
        bu_add_book = findViewById(R.id.bu_add_book);
        bu_add_pictures = findViewById(R.id.bu_add_pictures);
        ic_back = findViewById(R.id.ic_back);
        addView = (LinearLayout) findViewById(R.id.addView);
        imgList = new ArrayList<>();

        final PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        ed_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(builder.build(AddBookActivity.this), AppConstants.MAP_ACTIVITY_SELECT_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        bu_add_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermissions(new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        });
        bu_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    String titel = ed_titel.getText().toString();
                    String owner = ed_owner.getText().toString();
                    String info = ed_info.getText().toString();
                    String price = ed_price.getText().toString();
                    String max_tenant = ed_max_tenant.getText().toString();
                    String adress = ed_adress.getText().toString();
                    bookRoom(titel, owner, info, price, max_tenant
                            , adress, location_latitude, location_longitude);
                }
            }
        });
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validation() {
        String titel = ed_titel.getText().toString();
        String owner = ed_owner.getText().toString();
        String price = ed_price.getText().toString();
        String max_tenant = ed_max_tenant.getText().toString();
        String adress = ed_adress.getText().toString();
        String info = ed_info.getText().toString();

        if (TextUtils.isEmpty(titel) || titel.length() < 3) {
            ed_titel.setError(getString(R.string.name_validation));
            return false;
        } else {
            ed_titel.setError(null);
        }
        if (TextUtils.isEmpty(owner) || owner.length() < 3) {
            ed_owner.setError(getString(R.string.name_validation));
            return false;
        } else {
            ed_owner.setError(null);
        }
        if (TextUtils.isEmpty(price)) {
            ed_price.setError(getString(R.string.require));
            return false;
        } else {
            ed_price.setError(null);
        }
        if (TextUtils.isEmpty(max_tenant)) {
            ed_max_tenant.setError(getString(R.string.require));
            return false;
        } else {
            ed_max_tenant.setError(null);
        }
        if (TextUtils.isEmpty(adress) || adress.length() < 3) {
            ed_adress.setError(getString(R.string.name_validation));
            return false;
        } else {
            ed_adress.setError(null);
        }
        if (TextUtils.isEmpty(info) || info.length() < 3) {
            ed_info.setError(getString(R.string.name_validation));
            return false;
        } else {
            ed_info.setError(null);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppConstants.MAP_ACTIVITY_SELECT_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    ed_adress.setText(place.getAddress());
                    LatLng latLng = place.getLatLng();
                    location_latitude = latLng.latitude;
                    location_longitude = latLng.longitude;
                }
                break;
            case RESULT_LOAD_IMAGE:

                if (resultCode == RESULT_OK && null != data) {
                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        int i = 0;
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            imgList.add(mediaItem);
                            String realPath;
                            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                                realPath = Util.getRealPathFromURI(AddBookActivity.this, mediaItem.getUriOrigin());
                            } else {
                                realPath = Util.getRealPathFromURI_API19(AddBookActivity.this, mediaItem.getUriOrigin());
                            }
                            File filePath = new File(realPath);
                            fileParams.add(filePath);
                            getItem(mediaItem, i);
                            i++;
                        }
                    } else {
                        Log.e("////", "Error to get media, NULL");
                    }

                    break;
                }
        }
    }

//    private void uploadImg(final String titel, final String owner, final String info, final String price,
//                           final String max_tenant, final String adress, Double lat, Double longi, ArrayList<File> files) {
//        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
//            @Override
//            public void onDataReceived(Object o) {
//                boolean status = ((JSONObject) o).optBoolean("status");
//                if (status) {
//                    JSONArray items = ((JSONObject) o).optJSONArray("items");
//                    ArrayList<Integer> itemList = new Gson().fromJson(items.toString(), new TypeToken<List<Item>>() {
//                    }.getType());
//                    bookRoom(titel, owner, info, price, max_tenant
//                            , adress, location_latitude, location_longitude,itemList);
////                    Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
////                    startActivity(intent);
////                    finish();
////                    Toast.makeText(SignUpActivity.this, R.string.sign_successfully, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AddBookActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new VolleyRequests.ErrorReceiveData() {
//            @Override
//            public void onErrorDataReceived(VolleyError o) {
//                Toast.makeText(AddBookActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
//            }
//        }).uploadImg(AddBookActivity.this, files);
//    }

    private void bookRoom(String titel, String owner, String info, String price,
                          String max_tenant, String adress, Double lat, Double longi/*, ArrayList<Integer> photoId*/) {
        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
            @Override
            public void onDataReceived(Object o) {
                boolean status = ((JSONObject) o).optBoolean("status");
                if (status) {
                    Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(SignUpActivity.this, R.string.sign_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    /******  return to thi point later  and get maage**********/
                    Toast.makeText(AddBookActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyRequests.ErrorReceiveData() {
            @Override
            public void onErrorDataReceived(VolleyError o) {
                Toast.makeText(AddBookActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
            }
        }).bookRoom(AddBookActivity.this, titel, owner, info, price, max_tenant, adress, lat, longi/*, photoId*/);

    }

    private void getItem(MediaItem mediaItem, final int i) {
        final RelativeLayout ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.add_many_img, null);
        SimpleDraweeView img = (SimpleDraweeView) ll.findViewById(R.id.img);
        final ImageView img_close = ll.findViewById(R.id.img_close);
        img_close.setVisibility(View.VISIBLE);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView.removeView((View) view.getParent());
                imgList.remove(i);
                clearImages();
                for (int j = 0; j < imgList.size(); j++) {
                    getItem(imgList.get(j), j);
                }
            }
        });
        if (mediaItem.getUriCropped() == null) {
            img.setImageURI(mediaItem.getUriOrigin());
        } else {
            img.setImageURI(mediaItem.getUriCropped());
        }
        addView.addView(ll);
    }

    private void clearImages() {
        addView.removeAllViews();
    }

    protected final void askForPermissions(String[] permissions, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(AddBookActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(AddBookActivity.this,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]), requestCode);
        } else selectImage();
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                new AlertDialog.Builder(
                        AddBookActivity.this)
                        .setTitle("Permission Needed ! ")
                        .setMessage("Must have permission to continue...")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                askForPermissions(new String[]{
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSIONS);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else selectImage();
        }
    }


    private void selectImage() {
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = builder.canSelectMultiPhoto(true)
                .setMediaListSelected(mMediaSelectedList).build();
        MediaPickerActivity.open(AddBookActivity.this, RESULT_LOAD_IMAGE, options);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
