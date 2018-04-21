package roome.hackathon.com.roome.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.R;
import roome.hackathon.com.roome.Utils.FragmentsUtil;
import roome.hackathon.com.roome.Utils.SharedPrefUtil;
import roome.hackathon.com.roome.Utils.VolleyRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    private EditText edName;
    private EditText edEmail;
    private EditText edMobile;
    private EditText edPassword;
    private Button buSignUp;
    private AVLoadingIndicatorView myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edName = (EditText) findViewById(R.id.ed_name);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edMobile = (EditText) findViewById(R.id.ed_mobile);
        edPassword = (EditText) findViewById(R.id.ed_password);
        buSignUp = (Button) findViewById(R.id.bu_signUp);
        myProgress = findViewById(R.id.myProgress);

        buSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    startAnim();
                    String name = edName.getText().toString();
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();
                    String mobile = edMobile.getText().toString();
                    checkSignUp(name, email, password, mobile);
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkSignUp(String name, String email, String password, String mobile) {
        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
            @Override
            public void onDataReceived(Object o) {
                boolean status = ((JSONObject) o).optBoolean("status");
                if (status) {
                    stopAnim();
                    final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(SignUpActivity.this);
                    sharedPrefUtil.saveBoolean(AppConstants.HAVE_USER, status);
                    String access_token = ((JSONObject) o).optString(AppConstants.ACCESS_TOKEN);
                    String token_type = ((JSONObject) o).optString(AppConstants.TOKEN_TYPE);
                    String expires_in = ((JSONObject) o).optString(AppConstants.EXPIRES_IN);

                    sharedPrefUtil.saveString(AppConstants.ACCESS_TOKEN, access_token);
                    sharedPrefUtil.saveString(AppConstants.TOKEN_TYPE, token_type);
                    sharedPrefUtil.saveString(AppConstants.EXPIRES_IN, expires_in);
                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(SignUpActivity.this, R.string.sign_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    /******  return to thi point later  and get maage**********/
                    stopAnim();
                    Toast.makeText(SignUpActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyRequests.ErrorReceiveData() {
            @Override
            public void onErrorDataReceived(VolleyError o) {
                stopAnim();
                Toast.makeText(SignUpActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
            }
        }).newUser(SignUpActivity.this, name, email, password, mobile);
    }

    private boolean validation() {
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        String mobile = edMobile.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            edName.setError(getString(R.string.name_validation));
            return false;
        } else {
            edName.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError(getString(R.string.email_validation));
            return false;
        } else {
            edEmail.setError(null);
        }
        if (mobile.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edMobile.setError(getString(R.string.mobile_validation));
            return false;
        } else {
            edMobile.setError(null);
        }
        if (password.isEmpty() || password.length() < 6) {
            edPassword.setError(getString(R.string.pass_validation));
            return false;
        } else {
            edPassword.setError(null);
        }
        return true;
    }

    void startAnim() {
        myProgress.setVisibility(View.VISIBLE);
        buSignUp.setVisibility(View.INVISIBLE);
        myProgress.smoothToShow();
    }

    void stopAnim() {
        myProgress.setVisibility(View.GONE);
        buSignUp.setVisibility(View.VISIBLE);
        myProgress.smoothToHide();
    }
}
