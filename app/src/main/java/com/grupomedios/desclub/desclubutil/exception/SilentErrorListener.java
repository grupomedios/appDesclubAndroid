package com.grupomedios.desclub.desclubutil.exception;

import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

/**
 * Created by jhon on 8/02/15.
 */
public class SilentErrorListener extends CommonErrorListener {

    public SilentErrorListener(String tag, Context context, MaterialDialog progressDialog) {
        super(tag, context, progressDialog);
    }

    @Override
    public void manageError(VolleyError error) {
        Log.e(tag, error.getMessage(), error);
    }
}
