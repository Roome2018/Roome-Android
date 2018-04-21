package roome.hackathon.com.roome.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Simple util class for easing the use of shared preference.
 */
public class SharedPrefUtil {
    /**
     * Name of the preference file
     */
    public static String PREFS = "KwtAds";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SharedPrefUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void saveString(String key, String value) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void saveInt(String key, int value) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void saveBoolean(String key, boolean value) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
    public String getString(String key) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key,"");
    }

    public String getString(String key, String defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Clears the shared preference file
     */
    public void clear() {
        mSharedPreferences = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().apply();
    }
}
