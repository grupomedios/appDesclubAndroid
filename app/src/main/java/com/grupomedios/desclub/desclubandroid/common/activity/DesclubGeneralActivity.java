package com.grupomedios.desclub.desclubandroid.common.activity;

import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by jhoncruz on 5/03/15.
 */
public abstract class DesclubGeneralActivity extends BaseActivity {

    private final static String TAG = "DesclubGeneralActivity";

    private MaterialDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
