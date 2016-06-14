package com.grupomedios.desclub.desclubutil.exception;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.facebook.LoginActivity;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.home.activity.DesclubMainActivity;
import com.grupomedios.desclub.desclubutil.security.UserHelper;
import com.grupomedios.desclub.desclubutil.ui.dialog.DialogUtil;

/**
 * Created by jhon on 7/02/15.
 */
public abstract class CommonErrorListener implements Response.ErrorListener {

    private final String TAG = "CommonErrorListener";

    protected String tag;
    private Activity context;
    protected MaterialDialog progressDialog;

    public CommonErrorListener(String tag, Activity context, MaterialDialog progressDialog) {
        this.tag = tag;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public CommonErrorListener(String tag, Context context, MaterialDialog progressDialog) {
        this.tag = tag;
        this.context = null;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e(tag, volleyError.getMessage(), volleyError);
        Crashlytics.logException(volleyError);
        //handle common errors
        manageCommonError(volleyError);
        //handle specific errors
        manageError(volleyError);
    }

    public abstract void manageError(VolleyError error);

    /**
     * @param error
     */
    private void manageCommonError(VolleyError error) {

        Log.d(tag, error.toString());
        Log.d(tag, "=============================>>>>>>");

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (error.networkResponse != null) {
            Log.d(tag, error.networkResponse.toString());
            Log.d(tag, String.valueOf(error.networkResponse.statusCode));

            if (error.networkResponse.statusCode == 401) {
                Context appContext = DesclubApplication.getAppContext();

                Intent intent = new Intent(appContext, DesclubMainActivity.class);
                intent.setAction(LoginActivity.class.getName());
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                appContext.startActivity(intent);

                UserHelper userHelper = new UserHelper();
                userHelper.partialLogout();

                return;
            }
        }

        if (error instanceof TimeoutError || (error instanceof NoConnectionError && error.networkResponse != null && error.networkResponse.statusCode != 401)) {
            showErrorMessage(R.string.error_connection, R.string.error_connection_detail);
        }
    }

    /**
     * @param title
     * @param message
     */
    public void showErrorMessage(int title, int message) {

        if (context != null) {
            MaterialDialog materialDialog = DialogUtil.createErrorDialog(context, title, message);
            materialDialog.show();
        } else {
            Log.d(TAG, "Trying to show error message but context is null. Error: " + title + ' ' + message);
        }

    }
}
