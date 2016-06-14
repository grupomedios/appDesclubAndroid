package com.grupomedios.desclub.desclubandroid.discounts.activity;

import android.os.Bundle;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubandroid.discounts.fragment.DiscountListFragment;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class DiscountListActivity extends DesclubGeneralActivity {

    public static final String CURRENT_CATEGORY_PARAM = "com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountListActivity";
    public static final String SEARCH_PARAM = "com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountListActivity.SearchParam";

    private final static String TAG = "DiscountListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FakeCategoryRepresentation currentCategory = (FakeCategoryRepresentation) getIntent().getSerializableExtra(CURRENT_CATEGORY_PARAM);
        String searchParam = getIntent().getStringExtra(SEARCH_PARAM);

        DiscountListFragment discountListFragment = (DiscountListFragment) getFragmentManager().findFragmentById(R.id.discount_list_fragment);
        discountListFragment.loadDiscounts(currentCategory, searchParam);

        setTitle(currentCategory.getName());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_discount_list;
    }

}
