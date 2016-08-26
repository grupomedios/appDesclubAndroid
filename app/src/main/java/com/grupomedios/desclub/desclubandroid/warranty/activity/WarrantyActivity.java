package com.grupomedios.desclub.desclubandroid.warranty.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;

public class WarrantyActivity extends DesclubGeneralActivity {

    private final static String TAG = "WarrantyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        findViewById(R.id.warranty_btn_request).setOnClickListener(mOnClickOnRequestWarranty);
        findViewById(R.id.warranty_btn_close).setOnClickListener(mOnClickOnClose);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_warranty;
    }

    @Override
    public String getScreenName() {
        return getString(R.string.analytics_screen_warranty);
    }

    private View.OnClickListener mOnClickOnRequestWarranty = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(WarrantyActivity.this, RequestWarrantyActivity.class));
            finish();
        }
    };

    private View.OnClickListener mOnClickOnClose = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
}
