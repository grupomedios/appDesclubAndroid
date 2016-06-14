package com.grupomedios.desclub.desclubandroid.common.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.VolleySingleton;
import com.grupomedios.desclub.desclubutil.MCXApplication;

public abstract class BaseActivity extends ActionBarActivity {

    private static final String TAG = "BaseActivity";

    private Toolbar toolbar;

    public RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MCXApplication) getApplication()).inject(this);

        setContentView(getLayoutResource());

        toolbar = (Toolbar) findViewById(R.id.beep_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.d(TAG, "No toolbar " + findViewById(R.id.beep_toolbar));
        }

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    protected abstract int getLayoutResource();

    /**
     *
     * @param iconRes
     */
    protected void setActionBarIcon(int iconRes) {
        if (toolbar != null) {
            toolbar.setNavigationIcon(iconRes);
        }
    }

    /**
     *
     * @param color
     */
    public void setActionBarBackgroundColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }
}