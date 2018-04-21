package roome.hackathon.com.roome.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.R;
import roome.hackathon.com.roome.Utils.SharedPrefUtil;
import roome.hackathon.com.roome.Utils.VolleyRequests;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LogInActivity extends AppCompatActivity {
    private EditText edEmail;
    private EditText edPassword;
    private Button buLogin;
    private TextView notHaveAccount;
    private LinearLayout llTop;
    private ImageView icFacebook;
    private LoginButton loginButton;
    private ImageView icTwitter;
    private AVLoadingIndicatorView myProgress;
    private CallbackManager callbackManager;
    private String id;
    private String name;
    private String email;
    private String gender;
    private String birthday;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edPassword = (EditText) findViewById(R.id.ed_password);
        buLogin = (Button) findViewById(R.id.bu_login);
        notHaveAccount = (TextView) findViewById(R.id.not_have_account);
        icFacebook = (ImageView) findViewById(R.id.ic_facebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        icTwitter = (ImageView) findViewById(R.id.ic_twitter);
        myProgress = findViewById(R.id.myProgress);
        callbackManager = CallbackManager.Factory.create();

        /************ login Facebook ******************/
        loginButton = (LoginButton) findViewById(R.id.login_button);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile"/*, "AccessToken", "publish_actions"*/);
        /*** make error **/
        ///"AccessToken"
        loginButton.setReadPermissions(permissionNeeds);

        // If using in a fragment
//        loginButton.setFragment(this);
//        For programmatically logout use this.
//        LoginManager.getInstance().logOut();

        profile = Profile.getCurrentProfile().getCurrentProfile();
        if (profile != null) {
            Log.i("Profile", " user has logged in ");
        } else {
            Log.i("Profile", " user has  not logged in ");
        }
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
//                updateWithToken(newAccessToken);
//            }
//        };

//        Callback registration
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("onSuccess");
                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);
                        getUserDetailsFromFacebook(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.i("LoginActivity", exception.getCause().toString());
                    }
                });

        buLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    startAnim();
                    String email = edEmail.getText().toString();
                    String password = edPassword.getText().toString();
                    checkLogin(email, password);
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
        notHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean validation() {
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError(getString(R.string.email_validation));
            return false;
        } else {
            edEmail.setError(null);
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
        buLogin.setVisibility(View.INVISIBLE);
        myProgress.smoothToShow();
    }

    void stopAnim() {
        myProgress.setVisibility(View.GONE);
        buLogin.setVisibility(View.VISIBLE);
        myProgress.smoothToHide();
    }

    private void checkLogin(String email, String password) {
        new VolleyRequests().setIReceiveData(new VolleyRequests.IReceiveData() {
            @Override
            public void onDataReceived(Object o) {
                boolean status = ((JSONObject) o).optBoolean("status");
                if (status) {
                    String token_type = ((JSONObject) o).optString(AppConstants.TOKEN_TYPE);
                    String expires_in = ((JSONObject) o).optString(AppConstants.EXPIRES_IN);
                    String access_token = ((JSONObject) o).optString(AppConstants.ACCESS_TOKEN);
                    final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(LogInActivity.this);
                    sharedPrefUtil.saveBoolean(AppConstants.HAVE_USER, status);
                    sharedPrefUtil.saveString(AppConstants.TOKEN_TYPE, token_type);
                    sharedPrefUtil.saveString(AppConstants.EXPIRES_IN, expires_in);
                    sharedPrefUtil.saveString(AppConstants.ACCESS_TOKEN, access_token);
                    stopAnim();
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
//                    Toast.makeText(SignUpActivity.this, R.string.sign_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    /******  return to thi point later  and get maage**********/
                    stopAnim();
                    Toast.makeText(LogInActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyRequests.ErrorReceiveData() {
            @Override
            public void onErrorDataReceived(VolleyError o) {
                stopAnim();
                Toast.makeText(LogInActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
            }
        }).loginUser(LogInActivity.this, email, password);
    }

    protected void getUserDetailsFromFacebook(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            json_object.get(getString(R.string.fa_email)).toString();
                            json_object.get(getString(R.string.fa_name)).toString();
                            JSONObject profile_pic_data = new JSONObject(json_object.get(getString(R.string.picture)).toString());
                            JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString(getString(R.string.data)));
                            Uri uri = Uri.parse(profile_pic_url.getString("url"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }
}
