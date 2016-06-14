package com.grupomedios.desclub.desclubutil.exception;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.grupomedios.desclub.desclubandroid.R;

/**
 * Created by jhon on 8/02/15.
 */
public class SignUpErrorListener extends CommonErrorListener {

    public SignUpErrorListener(String tag, Activity context, MaterialDialog materialDialog) {
        super(tag, context, materialDialog);
    }

    @Override
    public void manageError(VolleyError error) {
        if (error.networkResponse.statusCode == 409) {
            showErrorMessage(R.string.error, R.string.error_user_alreay_exists);
        }
    }
}
