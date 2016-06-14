package com.grupomedios.desclub.desclubutil.exception;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.grupomedios.desclub.desclubandroid.R;

/**
 * Created by jhon on 8/02/15.
 */
public class    LoginErrorListener extends CommonErrorListener {

    public LoginErrorListener(String tag, Activity context, MaterialDialog materialDialog) {
        super(tag, context, materialDialog);
    }

    @Override
    public void manageError(VolleyError error) {
        if (error.networkResponse.statusCode == 404) {
            showErrorMessage(R.string.error_login_title, R.string.error_connection);
        }
    }
}
