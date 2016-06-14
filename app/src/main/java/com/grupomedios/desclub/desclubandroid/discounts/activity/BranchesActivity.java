package com.grupomedios.desclub.desclubandroid.discounts.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubandroid.discounts.fragment.BranchesFragment;
import com.ogaclejapan.smarttablayout.utils.v13.Bundler;

/**
 * Created by jhoncruz on 20/11/15.
 */
public class BranchesActivity extends DesclubGeneralActivity {

    public static final String CURRENT_BRAND = "com.grupomedios.desclub.desclubandroid.discounts.activity.BranchesActivity.CURRENT_BRAND";

    private final static String TAG = "BranchesActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String currentBrand = getIntent().getStringExtra(CURRENT_BRAND);

        //show fragment
        Bundler bundler = new Bundler()
                .putString(BranchesFragment.CURRENT_BRAND, currentBrand);

        BranchesFragment branchesFragment = new BranchesFragment();
        branchesFragment.setArguments(bundler.get());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.branches_fragment, branchesFragment, "branches");
        fragmentTransaction.commit();

        setTitle(getResources().getString(R.string.branches));

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setActionBarBackgroundColor(getResources().getColor(R.color.desclub_blue));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_branches;
    }
}
