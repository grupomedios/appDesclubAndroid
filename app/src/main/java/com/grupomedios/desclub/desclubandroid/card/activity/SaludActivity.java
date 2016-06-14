package com.grupomedios.desclub.desclubandroid.card.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class SaludActivity extends DesclubGeneralActivity {

    private final static String TAG = "SaludActivity";

    @Inject
    UserHelper userHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView memebershipNumber = (TextView) findViewById(R.id.salud_membershipNumber);
        TextView name = (TextView) findViewById(R.id.salud_name);
        TextView validThru = (TextView) findViewById(R.id.salud_validThru);

        memebershipNumber.setText(userHelper.getSaludNumber());
        name.setText(userHelper.getCurrentUser().getName().toUpperCase());
        validThru.setText("VENCE " + userHelper.getValidThru());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_salud;
    }

}
