package com.grupomedios.desclub.desclubandroid;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.grupomedios.desclub.desclubutil.MCXApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jhoncruz on 27/05/15.
 */
public class DesclubApplication extends MCXApplication {

    private static DesclubApplication mInstance;
    private static Context mAppContext;
    public final String TAG = "DesclubApplication";

    public static DesclubApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        this.injectSelf();

        mInstance = this;
        this.setAppContext(getApplicationContext());

        ImageLoader.getInstance()
                .init(ImageLoaderConfiguration.createDefault(this));

    }

    @Override
    public void buildDaggerModules(List<Object> modules) {
        modules.add(new DesclubModule());
    }
}
