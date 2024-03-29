package me.vinhdo.effortless.english;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import me.vinhdo.effortless.english.Database.LocalStorage;
import me.vinhdo.effortless.english.Database.StorageUnavailableException;

/**
 * Created by Vinh on 1/9/15.
 */
public class EffortlessApplication extends Application {

    public static Context mAppContext;
    private LocalStorage localStorage;
    public static SharedPreferences mPref;
    private Handler handler;

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        EffortlessApplication.mAppContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EffortlessApplication.setAppContext(getApplicationContext());
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        handler = new Handler();
        try {
            localStorage = LocalStorage.create(this);
        } catch (StorageUnavailableException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void recreateLocalStorage() {
        if (localStorage == null) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        localStorage = LocalStorage
                                .create(EffortlessApplication.this);
                    } catch (StorageUnavailableException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public final LocalStorage getLocalStorage() {
        return localStorage;
    }

    public static LocalStorage getLocalStorage(Context context) {
        return ((EffortlessApplication) context.getApplicationContext())
                .getLocalStorage();
    }

    public String getLastPlay(){
        return mPref.getString("lastplay","");
    }

    public void setLastPlay(String last){
        mPref.edit().putString("lastplay", last).commit();
    }

    public boolean getCurrentVerRun(){
        int cur =  mPref.getInt("123dsfow2321s1",0);
        if(!isShared()){
            if(cur < 3)
                cur = cur + 1;
            else {
                cur = 0;
                setCurrentVerRun(cur);
                return true;
            }
        }else{
            if(cur < 5)
                cur = cur + 1;
            else {
                cur = 0;
                setCurrentVerRun(cur);
                return true;
            }
        }
        setCurrentVerRun(cur);
        return false;
    }

    public void setCurrentVerRun(int num){
        mPref.edit().putInt("123dsfow2321s1",num).commit();
    }

    public boolean isShared(){
        return mPref.getBoolean("Adw2312",false);
    }

    public void setIsShare(boolean isShared){
        mPref.edit().putBoolean("Adw2312",isShared).commit();
    }

}
