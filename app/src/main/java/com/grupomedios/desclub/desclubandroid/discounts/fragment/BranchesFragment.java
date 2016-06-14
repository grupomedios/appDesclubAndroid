package com.grupomedios.desclub.desclubandroid.discounts.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.grupomedios.desclub.desclubandroid.discounts.adapter.DiscountAdapter;
import com.grupomedios.desclub.desclubandroid.home.util.FakeCategoryUtil;
import com.grupomedios.desclub.desclubapi.facade.DiscountFacade;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountRepresentation;
import com.grupomedios.desclub.desclubutil.exception.SilentListErrorListener;
import com.grupomedios.desclub.desclubutil.gps.GPSService;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.ui.dialog.DialogUtil;
import com.grupomedios.desclub.desclubutil.ui.list.PaginableActivity;
import com.grupomedios.desclub.desclubutil.ui.scroll.EndlessScrollListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class BranchesFragment extends Fragment implements PaginableActivity, View.OnKeyListener {

    private final String TAG = "BranchesFragment";

    public static final String CURRENT_BRAND = "com.grupomedios.desclub.desclubandroid.discounts.fragment.BranchesFragment.CURRENT_BRAND";

    BaseAdapter adapter = null;
    AnimationAdapter animAdapter;

    private FakeCategoryRepresentation currentCategory;
    private ListView branchesListView;
    private List<NearByDiscountRepresentation> branchesList;

    private View loadingFooter;
    private RequestQueue requestQueue;
    private MaterialDialog progressDialog;
    private PullToRefreshView pullToRefreshView;
    private GPSService gpsService;

    private FloatingActionMenu searchFloatingMenu;

    private String currentBrand;

    @Inject
    DiscountFacade discountFacade;

    /**
     * Click listener for the discount list
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            if (searchFloatingMenu != null) {
                searchFloatingMenu.close(true);
            }

            Intent intent;
            intent = new Intent(getActivity(), DiscountActivity.class);
            intent.putExtra(DiscountActivity.CURRENT_DISCOUNT_PARAM, branchesList.get(position).getDiscount());
            intent.putExtra(DiscountActivity.CURRENT_CATEGORY_PARAM, currentCategory);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "----------- onCreate");

        super.onCreate(savedInstanceState);
        ((DesclubApplication) getActivity().getApplication()).inject(this);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        gpsService = new GPSService(getActivity());
        gpsService.getLocation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branches, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Bundle arguments = getArguments();
        currentBrand = arguments.getString(CURRENT_BRAND);

        currentCategory = FakeCategoryUtil.buildCategory(getActivity(), "0");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            pullToRefreshView = (PullToRefreshView) getView().findViewById(R.id.branches_list_pull_to_refresh);
            pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    resetSearch();

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDiscounts();
    }

    private void resetSearch() {
        //reload list
        if (branchesList != null) {
            branchesList.clear();
        }
        loadDiscounts();
    }

    public void loadDiscounts() {

        Log.d(TAG, " ----------------------------- loadDiscounts");


        if (branchesList != null) {
            branchesList.clear();
            branchesList = new ArrayList<>();
            branchesList = null;
            if (animAdapter != null) {
                animAdapter.notifyDataSetChanged();
            }
        }

        branchesListView = (ListView) getView().findViewById(R.id.branches_listView);
        branchesListView.setOnItemClickListener(itemClickListener);
        branchesListView.setOnScrollListener(new EndlessScrollListener(this));

        loadingFooter = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.part_loading_footer, null, false);

        loadPage();
    }

    @TargetApi(21)
    private void changeStatusBarColor(FakeCategoryRepresentation category) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getActivity().getResources().getColor(category.getColor()));
        }
    }

    @Override
    public void loadPage() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if (branchesListView.getFooterViewsCount() == 0) {
            //show loading
            branchesListView.addFooterView(loadingFooter);
        }

        int currentResultsSize = 0;
        if (branchesList != null) {
            currentResultsSize = branchesList.size();
        }


        params.add(new BasicNameValuePair("limit", "40"));
        params.add(new BasicNameValuePair("latitude", String.valueOf(gpsService.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(gpsService.getLongitude())));
        if (branchesList != null && branchesList.size() > 0) {
            params.add(new BasicNameValuePair("minDistance", String.valueOf(branchesList.get(branchesList.size() - 1).getDis() + 0.00000001)));
        }

        if (currentBrand != null && !currentBrand.isEmpty()) {
            params.add(new BasicNameValuePair("brand", currentBrand));
        }


        if (branchesList == null) {
            branchesList = new ArrayList<NearByDiscountRepresentation>();
            adapter = new DiscountAdapter(getActivity(), branchesList, currentCategory);

            animAdapter = new AlphaInAnimationAdapter(adapter);

            animAdapter.setAbsListView(branchesListView);
            branchesListView.setAdapter(animAdapter);
        }

        progressDialog = DialogUtil.createProgressDialog(getActivity());

        if (pullToRefreshView != null) {
            pullToRefreshView.setRefreshing(true);
        }

        GsonRequest<NearByDiscountRepresentation[]> guestAndLoginRequest = discountFacade.getAllDiscounts(getActivity(), new Response.Listener<NearByDiscountRepresentation[]>() {
            @Override
            public void onResponse(NearByDiscountRepresentation[] hotelModuleRepresentations) {
                Log.d(TAG, String.valueOf(hotelModuleRepresentations.length));

                if (pullToRefreshView != null) {
                    pullToRefreshView.setRefreshing(false);
                }

                //hide loading
                branchesListView.removeFooterView(loadingFooter);

                branchesList.addAll(Arrays.asList(hotelModuleRepresentations));
                animAdapter.notifyDataSetChanged();

            }
        }, new SilentListErrorListener(TAG, getActivity(), progressDialog, pullToRefreshView), params);

        requestQueue.add(guestAndLoginRequest);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {


            resetSearch();

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
        return false;
    }
}