package com.grupomedios.desclub.desclubandroid.recommended.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.VolleySingleton;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity;
import com.grupomedios.desclub.desclubandroid.recommended.adapter.RecommendedDiscountAdapter;
import com.grupomedios.desclub.desclubapi.facade.DiscountFacade;
import com.grupomedios.desclub.desclubapi.representations.DiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubutil.exception.SilentListErrorListener;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.ui.dialog.DialogUtil;
import com.grupomedios.desclub.desclubutil.ui.list.PaginableActivity;
import com.grupomedios.desclub.desclubutil.ui.scroll.EndlessScrollListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Main {@link Fragment} subclass.
 */
public class RecommendedFragment extends Fragment implements PaginableActivity {

    private final String TAG = "RecommendedFragment";

    BaseAdapter adapter = null;
    AnimationAdapter animAdapter;

    private ListView discountsListView;
    private List<DiscountRepresentation> discountList;

    private View loadingFooter;
    private RequestQueue requestQueue;
    private MaterialDialog progressDialog;
    private PullToRefreshView pullToRefreshView;

    @Inject
    DiscountFacade discountFacade;

    /**
     * Click listener for the discount list
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent;
            intent = new Intent(getActivity(), DiscountActivity.class);
            intent.putExtra(DiscountActivity.CURRENT_DISCOUNT_PARAM, discountList.get(position));
            intent.putExtra(DiscountActivity.CURRENT_CATEGORY_PARAM, new FakeCategoryRepresentation("0", getResources().getString(R.string.see_all), R.drawable.see_all, R.color.category_all));
            startActivity(intent);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DesclubApplication) getActivity().getApplication()).inject(this);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_recommended, container, false);

        pullToRefreshView = (PullToRefreshView) mainView.findViewById(R.id.discount_list_pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //reload list
                if (discountList != null) {
                    discountList.clear();
                }
                loadDiscounts();
            }
        });

        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();
        loadDiscounts();
    }

    public void loadDiscounts() {

        Log.d(TAG, " ----------------------------- loadDiscounts");

        if (discountList != null) {
            discountList.clear();
            discountList = new ArrayList<>();
            discountList = null;
            if (animAdapter != null) {
                animAdapter.notifyDataSetChanged();
            }
        }

        discountsListView = (ListView) getView().findViewById(R.id.discounts_listView);
        discountsListView.setOnItemClickListener(itemClickListener);
        discountsListView.setOnScrollListener(new EndlessScrollListener(this));

        loadingFooter = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.part_loading_footer, null, false);

        loadPage();
    }


    @Override
    public void loadPage() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if (discountsListView.getFooterViewsCount() == 0) {
            //show loading
            discountsListView.addFooterView(loadingFooter);
        }

        int currentResultsSize = 0;
        if (discountList != null) {
            currentResultsSize = discountList.size();
        }

        params.add(new BasicNameValuePair("offset", String.valueOf(currentResultsSize)));
        params.add(new BasicNameValuePair("limit", "10"));
        params.add(new BasicNameValuePair("orderBy", "created"));
        params.add(new BasicNameValuePair("orderType", "-1"));

        if (discountList == null) {
            discountList = new ArrayList<DiscountRepresentation>();
            adapter = new RecommendedDiscountAdapter(getActivity(), discountList);

            animAdapter = new AlphaInAnimationAdapter(adapter);

            animAdapter.setAbsListView(discountsListView);
            discountsListView.setAdapter(animAdapter);
        }

        progressDialog = DialogUtil.createProgressDialog(getActivity());

        if (pullToRefreshView != null) {
            pullToRefreshView.setRefreshing(true);
        }

        GsonRequest<DiscountRepresentation[]> allRecommendedDiscounts = discountFacade.getAllRecommendedDiscounts(getActivity(), new Response.Listener<DiscountRepresentation[]>() {
            @Override
            public void onResponse(DiscountRepresentation[] discountRepresentations) {
                Log.d(TAG, String.valueOf(discountRepresentations.length));

                pullToRefreshView.setRefreshing(false);

                //hide loading
                discountsListView.removeFooterView(loadingFooter);

                discountList.addAll(Arrays.asList(discountRepresentations));
                animAdapter.notifyDataSetChanged();

            }
        }, new SilentListErrorListener(TAG, getActivity(), progressDialog, pullToRefreshView), params);

        requestQueue.add(allRecommendedDiscounts);
    }
}