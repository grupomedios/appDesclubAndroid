package com.grupomedios.desclub.desclubandroid;

import com.grupomedios.desclub.desclubandroid.card.activity.SaludActivity;
import com.grupomedios.desclub.desclubandroid.card.fragment.CardFragment;
import com.grupomedios.desclub.desclubandroid.common.activity.BaseActivity;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubandroid.discounts.activity.BranchesActivity;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountListActivity;
import com.grupomedios.desclub.desclubandroid.discounts.fragment.BranchesFragment;
import com.grupomedios.desclub.desclubandroid.discounts.fragment.DiscountListFragment;
import com.grupomedios.desclub.desclubandroid.home.activity.DesclubMainActivity;
import com.grupomedios.desclub.desclubandroid.home.activity.SplashActivity;
import com.grupomedios.desclub.desclubandroid.home.fragment.MainFragment;
import com.grupomedios.desclub.desclubandroid.map.activity.DesclubMapActivity;
import com.grupomedios.desclub.desclubandroid.map.fragment.DesclubMapFragment;
import com.grupomedios.desclub.desclubandroid.recommended.fragment.RecommendedFragment;
import com.grupomedios.desclub.desclubutil.MCXModule;

import dagger.Module;

/**
 * Created by jhon on 22/01/15.
 */
@Module(
        injects = {
                DesclubApplication.class,
                SplashActivity.class,
                DesclubGeneralActivity.class,
                BaseActivity.class,
                DesclubMainActivity.class,
                DiscountListActivity.class,
                DiscountActivity.class,
                SaludActivity.class,
                DesclubMapActivity.class,
                BranchesActivity.class,

                MainFragment.class,
                DesclubMapFragment.class,
                RecommendedFragment.class,
                CardFragment.class,
                DiscountListFragment.class,
                BranchesFragment.class

        },
        includes = {
                MCXModule.class
        },
        overrides = true
)
public class DesclubModule {

}
