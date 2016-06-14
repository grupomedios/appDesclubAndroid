package com.grupomedios.desclub.desclubandroid.map.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubandroid.map.fragment.DesclubMapFragment;
import com.ogaclejapan.smarttablayout.utils.v13.Bundler;

/**
 * Created by jhoncruz on 20/11/15.
 */
public class DesclubMapActivity extends DesclubGeneralActivity {

    public static final String SEARCH_PARAM = "com.grupomedios.desclub.desclubandroid.map.activity.DesclubMapActivity.SearchParam";

    private final static String TAG = "DesclubMapActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String searchParam = getIntent().getStringExtra(SEARCH_PARAM);

        //show fragment
        Bundler bundler = new Bundler()
                .putString(DesclubMapFragment.SEARCH_PARAM, searchParam)
                .putBoolean(DesclubMapFragment.CENTER_IN_CURRENT_LOCATION, false);

        DesclubMapFragment desclubMapFragment = new DesclubMapFragment();
        desclubMapFragment.setArguments(bundler.get());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment, desclubMapFragment, "map");
        fragmentTransaction.commit();

        setTitle(getResources().getString(R.string.map));

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setActionBarBackgroundColor(getResources().getColor(R.color.desclub_blue));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }
}
