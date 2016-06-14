package com.grupomedios.desclub.desclubutil.ui.dialog;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.grupomedios.desclub.desclubandroid.R;

/**
 * Created by jhoncruz on 8/05/15.
 */
public class DialogUtil {

    public static MaterialDialog createProgressDialog(Activity activity, int title, int content){
        return new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .cancelable(false)
                .progress(true, 0).build();
    }

    public static MaterialDialog createProgressDialog(Activity activity){
        return new MaterialDialog.Builder(activity)
                .title(R.string.loading_title)
                .content(R.string.loading)
                .cancelable(false)
                .progress(true, 0).build();
    }

    public static MaterialDialog createErrorDialog(Activity activity, int title, int description){
        return new MaterialDialog.Builder(activity)
                .title(title)
                .content(description)
                .positiveText("OK")
                .build();
    }

}
