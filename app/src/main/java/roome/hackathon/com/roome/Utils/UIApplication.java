package roome.hackathon.com.roome.Utils;

import android.support.multidex.MultiDexApplication;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import roome.hackathon.com.roome.Interface.AppConstants;
import roome.hackathon.com.roome.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * extends MultiDexApplication  because java.lang.NoClassDefFoundError: uk.co.chrisjenx.calligraphy.R$attr
 */

public class UIApplication extends MultiDexApplication {

    public static UIApplication anInstance = null;
    RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();

        //      /************ CalligraphyConfig **********************/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(AppConstants.SFUI_TEXT_REGULAR)
                .setFontAttrId(R.attr.fontPath)
                .build());


        //      /************ Initial Volley ****************/
        anInstance = this;
        requestQueue = Volley.newRequestQueue(this);
        //      /************ Fresco **********************/
//        Fresco.initialize(this);
        ImagePipelineConfig frescoConfig = ImagePipelineConfig.newBuilder(getApplicationContext()).setDownsampleEnabled(true).build();
        Fresco.initialize(this, frescoConfig);

//        /********* Facebook sdk Initialize ********/
        FacebookSdk.sdkInitialize(this.getApplicationContext());

    }

    public synchronized void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public void cancel(String tag) {
        requestQueue.cancelAll(tag);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static UIApplication getAnInstance() {
        return anInstance;
    }
}
