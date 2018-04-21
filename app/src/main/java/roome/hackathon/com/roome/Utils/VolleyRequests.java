package roome.hackathon.com.roome.Utils;

import android.app.Activity;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import roome.hackathon.com.roome.Interface.AppConstants;


public class VolleyRequests {

    public interface IReceiveData {
        void onDataReceived(Object o);
    }

    public interface ErrorReceiveData {
        void onErrorDataReceived(VolleyError o);
    }

    private IReceiveData iReceiveData;
    private ErrorReceiveData errorReceiveData;

    public IReceiveData getIReceiveData() {
        return iReceiveData;
    }

    public VolleyRequests setIReceiveData(IReceiveData iReceiveData, ErrorReceiveData errorReceiveData) {
        this.iReceiveData = iReceiveData;
        this.errorReceiveData = errorReceiveData;
        return this;
    }

    public void newUser(final Activity activity, String name, String email, String password, String mobile) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", 2);
        params.put("client_secret", AppConstants.CLIENT_SECRET);
        params.put("scope", "*");
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);
        params.put("mobile", mobile);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL + "api/v1/user",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean status = response.optBoolean("status");
                if (iReceiveData != null) {
                    iReceiveData.onDataReceived(response);
                    Log.i(AppConstants.TAG, "status " + status);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "error get newUser" + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AppConstants.HEADERS_KEY, AppConstants.HEADERS_VALUE);
                return headers;
            }
        };
        UIApplication.getAnInstance().addToRequestQueue(request);
    }

    public void loginUser(final Activity activity, String email, String password) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", 2);
        params.put("client_secret", AppConstants.CLIENT_SECRET);
        params.put("scope", "*");
        params.put("username", email);
        params.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL + "oauth/token",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean status = response.optBoolean("status");
                if (iReceiveData != null) {
                    iReceiveData.onDataReceived(response);
                    Log.i(AppConstants.TAG, "status " + status);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "error get loginUser" + error.getMessage());
            }
        });
        UIApplication.getAnInstance().addToRequestQueue(request);
    }

    //    api/v1/rooms/
    public void roomes(final Activity activity) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                AppConstants.URL + "api/v1/rooms/",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean status = response.optBoolean("status");
                if (iReceiveData != null) {
                    iReceiveData.onDataReceived(response);
                    Log.i(AppConstants.TAG, "status " + status);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "error get newUser" + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(activity);

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AppConstants.HEADERS_KEY, AppConstants.HEADERS_VALUE);
                headers.put(AppConstants.AUTHORIZATION, "Bearer " + sharedPrefUtil.getString(AppConstants.ACCESS_TOKEN));
                return headers;
            }
        };
        UIApplication.getAnInstance().addToRequestQueue(request);
    }

    public void booking(final Activity activity, String date, int room_id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("date", date);
        params.put("room_id", room_id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL + "api/v1/bookings/",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean status = response.optBoolean("status");
                if (iReceiveData != null) {
                    iReceiveData.onDataReceived(status);
                    Log.i(AppConstants.TAG, "status " + status);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(AppConstants.TAG, "error get booking" + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(activity);

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AppConstants.HEADERS_KEY, AppConstants.HEADERS_VALUE);
                headers.put(AppConstants.AUTHORIZATION, "Bearer " + sharedPrefUtil.getString(AppConstants.ACCESS_TOKEN));
                return headers;
            }
        };
        UIApplication.getAnInstance().addToRequestQueue(request);
    }

    public void bookRoom(final Activity activity,
                         String titel, String owner, String info, String price,
                         String max_tenant, String adress, Double lat, Double longi/*, ArrayList<Integer> photoId*/){
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", titel);
        params.put("owner", owner);
        params.put("info", info);
        params.put("price", price);
        params.put("max_tenants", max_tenant);
        params.put("location_address", adress);
        params.put("location_latitude", lat);
        params.put("location_longitude", longi);
//        params.put("photos_id", photoId);
//        params.put("photos_id", room_id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConstants.URL + "api/v1/rooms/",
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean status = response.optBoolean("status");
                if (iReceiveData != null) {
                    iReceiveData.onDataReceived(status);
                    Log.i(AppConstants.TAG, "status " + status);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(AppConstants.TAG, "error get booking" + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(activity);

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AppConstants.HEADERS_KEY, AppConstants.HEADERS_VALUE);
                headers.put(AppConstants.AUTHORIZATION, "Bearer " + sharedPrefUtil.getString(AppConstants.ACCESS_TOKEN));
                return headers;
            }
        };
        UIApplication.getAnInstance().addToRequestQueue(request);
    }
    public void uploadImg(final Activity activity,ArrayList<File> files){
////        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
//        MultipartEntity entity = new MultipartEntity();
//
//        entity.addPart("profile_picture", new FileBody(new File("/storage/emulated/0/Pictures/VSCOCam/2015-07-31 11.55.14 1.jpg")));
//
//
//        }
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("model_name", "Room");
//        params.put("file_key", "photos");
//        params.put("bucket","photos" );
//        params.put("photos",files);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
//                AppConstants.URL + "api/v1/spatie/media/upload",
//                new JSONObject(params), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                boolean status = response.optBoolean("status");
//                if (iReceiveData != null) {
//                    iReceiveData.onDataReceived(response);
//                    Log.i(AppConstants.TAG, "status " + status);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i(AppConstants.TAG, "error get booking" + error.getMessage());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                final SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(activity);
//
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put(AppConstants.HEADERS_KEY, AppConstants.HEADERS_VALUE);
//                headers.put(AppConstants.AUTHORIZATION, "Bearer " + sharedPrefUtil.getString(AppConstants.ACCESS_TOKEN));
//                return headers;
//            }
//        };
//        UIApplication.getAnInstance().addToRequestQueue(request);
    }
}
