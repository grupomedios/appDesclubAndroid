package com.grupomedios.desclub.desclubutil.exception;

import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by jhon on 8/02/15.
 */
public class SilentListErrorListener extends CommonErrorListener {

    private PullToRefreshView pullToRefreshView;

    public SilentListErrorListener(String tag, Context context, MaterialDialog progressDialog, PullToRefreshView pullToRefreshView) {
        super(tag, context, progressDialog);
        this.pullToRefreshView = pullToRefreshView;
    }

    @Override
    public void manageError(VolleyError error) {
        Log.e(tag, error.getMessage(), error);
        if (pullToRefreshView != null) {
            pullToRefreshView.setRefreshing(false);
        }
    }
}
