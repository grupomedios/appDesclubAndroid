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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andreabaccega.widget.FormEditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.VolleySingleton;
import com.grupomedios.desclub.desclubandroid.common.activity.BaseActivity;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity;
import com.grupomedios.desclub.desclubandroid.discounts.adapter.DiscountAdapter;
import com.grupomedios.desclub.desclubapi.facade.CategoryFacade;
import com.grupomedios.desclub.desclubapi.facade.DiscountFacade;
import com.grupomedios.desclub.desclubapi.facade.StateFacade;
import com.grupomedios.desclub.desclubapi.facade.ZoneFacade;
import com.grupomedios.desclub.desclubapi.representations.CategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.StateRepresentation;
import com.grupomedios.desclub.desclubapi.representations.SubcategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.ZoneRepresentation;
import com.grupomedios.desclub.desclubutil.exception.SilentErrorListener;
import com.grupomedios.desclub.desclubutil.exception.SilentListErrorListener;
import com.grupomedios.desclub.desclubutil.gps.GPSService;
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
 * Created by jhoncruz on 28/05/15.
 */
public class DiscountListFragment extends Fragment implements PaginableActivity, View.OnKeyListener {

    private final String TAG = "DiscountListFragment";

    BaseAdapter adapter = null;
    AnimationAdapter animAdapter;

    private FakeCategoryRepresentation currentCategory;
    private ListView discountsListView;
    private List<NearByDiscountRepresentation> discountList;

    private View loadingFooter;
    private RequestQueue requestQueue;
    private MaterialDialog progressDialog;
    private PullToRefreshView pullToRefreshView;
    private GPSService gpsService;

    private MaterialDialog statesDialog;
    private MaterialDialog zonesDialog;
    private MaterialDialog categoryDialog;
    private MaterialDialog subcategoryDialog;

    private StateRepresentation selectedState;
    private ZoneRepresentation selectedZone;
    private CategoryRepresentation selectedCategory;
    private SubcategoryRepresentation selectedSubcategory;

    private List<StateRepresentation> allStates;
    private List<ZoneRepresentation> allZones;
    private List<CategoryRepresentation> allCategories;
    private List<SubcategoryRepresentation> allSubcategories;

    private FormEditText searchText;
    private Button statesButton;
    private Button categoriesButton;
    private Button subcategoriesButton;
    private Button zonesButton;

    @Inject
    DiscountFacade discountFacade;

    @Inject
    StateFacade stateFacade;

    @Inject
    CategoryFacade categoryFacade;

    @Inject
    ZoneFacade zoneFacade;

    /**
     * Click listener for the discount list
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            Intent intent;
            intent = new Intent(getActivity(), DiscountActivity.class);
            intent.putExtra(DiscountActivity.CURRENT_DISCOUNT_PARAM, discountList.get(position).getDiscount());
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
        View view = inflater.inflate(R.layout.fragment_discounts_list, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        searchText = (FormEditText) view.findViewById(R.id.discount_list_search_input);
        statesButton = (Button) view.findViewById(R.id.discount_list_state_button);
        categoriesButton = (Button) view.findViewById(R.id.discount_list_category_button);
        subcategoriesButton = (Button) view.findViewById(R.id.discount_list_subcategory_button);
        zonesButton = (Button) view.findViewById(R.id.discount_list_zone_button);

        searchText.setOnKeyListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            pullToRefreshView = (PullToRefreshView) getView().findViewById(R.id.discount_list_pull_to_refresh);
            pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    resetSearch();

                }
            });
        }

        setupSearchFilters();
        initDialogs();
    }

    private void resetSearch() {
        //reload list
        if (discountList != null) {
            discountList.clear();
        }
        loadDiscounts(currentCategory, searchText.getText().toString());
    }

    private void setupSearchFilters() {

        statesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statesDialog.show();
            }
        });


        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog.show();
            }
        });


        subcategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subcategoryDialog.show();
            }
        });

        subcategoriesButton.setVisibility(View.GONE);

        zonesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zonesDialog.show();
            }
        });

        zonesButton.setVisibility(View.GONE);
    }

    private void initDialogs() {

        initStatesDialog();
        initZonesDialog();
        initCategoryDialog();

        if (!currentCategory.get_id().equals("0")) {
            initSubcategoryDialog();
            subcategoriesButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     *
     */
    private void initCategoryDialog() {

        categoryDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.select_category)
                .positiveText(R.string.close)
                .cancelable(true)
                .customView(R.layout.part_dialog_categories, false)
                .build();

        GsonRequest<CategoryRepresentation[]> subcategoriesRequest = categoryFacade.getAllCategories(getActivity(), new Response.Listener<CategoryRepresentation[]>() {
            @Override
            public void onResponse(final CategoryRepresentation[] categories) {

                View categoriesCustomView = categoryDialog.getCustomView();

                Spinner categoriesSpinner = (Spinner) categoriesCustomView.findViewById(R.id.categories_spinner);

                allCategories = new ArrayList<CategoryRepresentation>();
                allCategories.add(new CategoryRepresentation("", "Seleccione un giro", 0));
                allCategories.addAll(Arrays.asList(categories));

                ArrayAdapter<CategoryRepresentation> dataAdapter = new ArrayAdapter<CategoryRepresentation>
                        (getActivity(), R.layout.part_spinner_item, allCategories);

                categoriesSpinner.setAdapter(dataAdapter);
                categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        selectedCategory = allCategories.get(position);
                        if (position > 0) {
                            categoriesButton.setText(selectedCategory.getName());
                            selectedSubcategory = null;
                            initSubcategoryDialog();
                            subcategoriesButton.setVisibility(View.VISIBLE);
                            subcategoriesButton.setText(getResources().getString(R.string.subcategory));
                        } else {
                            categoriesButton.setText(getResources().getString(R.string.category));
                            subcategoriesButton.setVisibility(View.GONE);
                        }

                        resetSearch();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                });

                //set selected category into spinner
                if (selectedCategory != null) {
                    for (int i = 0; i < allCategories.size(); i++) {
                        CategoryRepresentation category = allCategories.get(i);
                        if (category.get_id().equals(selectedCategory.get_id())) {
                            categoriesSpinner.setSelection(i, true);
                        }
                    }
                }
            }
        }, new SilentErrorListener(TAG, getActivity(), progressDialog));

        requestQueue.add(subcategoriesRequest);
    }

    /**
     *
     */
    private void initSubcategoryDialog() {

        subcategoryDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.select_subcategory)
                .positiveText(R.string.close)
                .cancelable(true)
                .customView(R.layout.part_dialog_subcategories, false)
                .build();

        GsonRequest<SubcategoryRepresentation[]> subcategoriesRequest = categoryFacade.getAllSubcategories(getActivity(), new Response.Listener<SubcategoryRepresentation[]>() {
            @Override
            public void onResponse(final SubcategoryRepresentation[] subcategories) {

                View subcategoriesCustomView = subcategoryDialog.getCustomView();

                Spinner subcategoriesSpinner = (Spinner) subcategoriesCustomView.findViewById(R.id.subcategories_spinner);

                allSubcategories = new ArrayList<SubcategoryRepresentation>();
                allSubcategories.add(new SubcategoryRepresentation("", 0, "Seleccione un subgiro", ""));
                allSubcategories.addAll(Arrays.asList(subcategories));

                ArrayAdapter<SubcategoryRepresentation> dataAdapter = new ArrayAdapter<SubcategoryRepresentation>
                        (getActivity(), R.layout.part_spinner_item, allSubcategories);

                subcategoriesSpinner.setAdapter(dataAdapter);
                subcategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        selectedSubcategory = allSubcategories.get(position);
                        if (position > 0) {
                            subcategoriesButton.setText(selectedSubcategory.getName());
                        } else {
                            subcategoriesButton.setText(getResources().getString(R.string.subcategory));
                        }
                        resetSearch();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new SilentErrorListener(TAG, getActivity(), progressDialog), selectedCategory.get_id());

        requestQueue.add(subcategoriesRequest);
    }

    /**
     *
     */
    private void initZonesDialog() {

        zonesDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.select_zone)
                .positiveText(R.string.close)
                .cancelable(true)
                .customView(R.layout.part_dialog_zones, false)
                .build();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("limit", "50"));

        if (selectedState != null) {
            params.add(new BasicNameValuePair("stateId", selectedState.getStateId().toString()));
        }

        GsonRequest<ZoneRepresentation[]> zonesRequest = zoneFacade.getAllZones(getActivity(), new Response.Listener<ZoneRepresentation[]>() {
            @Override
            public void onResponse(final ZoneRepresentation[] zones) {

                View zonesCustomView = zonesDialog.getCustomView();

                Spinner zonesSpinner = (Spinner) zonesCustomView.findViewById(R.id.zones_spinner);

                allZones = new ArrayList<ZoneRepresentation>();
                allZones.add(new ZoneRepresentation("", 0, "Seleccione una zona"));
                allZones.addAll(Arrays.asList(zones));

                ArrayAdapter<ZoneRepresentation> dataAdapter = new ArrayAdapter<ZoneRepresentation>
                        (getActivity(), R.layout.part_spinner_item, allZones);

                zonesSpinner.setAdapter(dataAdapter);
                zonesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        selectedZone = allZones.get(position);
                        if (position > 0) {
                            zonesButton.setText(selectedZone.getName());
                        } else {
                            zonesButton.setText(getResources().getString(R.string.zone));
                        }
                        resetSearch();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new SilentErrorListener(TAG, getActivity(), progressDialog), params);

        requestQueue.add(zonesRequest);
    }

    /**
     *
     */
    private void initStatesDialog() {

        statesDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.select_state)
                .positiveText(R.string.close)
                .cancelable(true)
                .customView(R.layout.part_dialog_states, false)
                .build();

        GsonRequest<StateRepresentation[]> guestAndLoginRequest = stateFacade.getAllStates(getActivity(), new Response.Listener<StateRepresentation[]>() {
            @Override
            public void onResponse(final StateRepresentation[] states) {

                View statesCustomView = statesDialog.getCustomView();

                Spinner statesSpinner = (Spinner) statesCustomView.findViewById(R.id.states_spinner);

                allStates = new ArrayList<StateRepresentation>();
                allStates.add(new StateRepresentation("", 0, "Seleccione un estado"));
                allStates.addAll(Arrays.asList(states));

                ArrayAdapter<StateRepresentation> dataAdapter = new ArrayAdapter<StateRepresentation>
                        (getActivity(), R.layout.part_spinner_item, allStates);

                statesSpinner.setAdapter(dataAdapter);
                statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        selectedState = allStates.get(position);
                        if (position > 0) {
                            statesButton.setText(selectedState.getName());

                            if (selectedState.getStateId().equals(new Integer(9)) || selectedState.getStateId().equals(new Integer(15))) {
                                zonesButton.setText(getResources().getString(R.string.zone));
                                zonesButton.setVisibility(View.VISIBLE);
                                initZonesDialog();
                            } else {
                                selectedZone = null;
                                zonesButton.setText(getResources().getString(R.string.zone));
                                zonesButton.setVisibility(View.GONE);
                            }

                        } else {
                            statesButton.setText(getResources().getString(R.string.state));
                        }

                        resetSearch();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getActivity(), "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new SilentErrorListener(TAG, getActivity(), progressDialog));

        requestQueue.add(guestAndLoginRequest);

    }

    public void loadDiscounts(FakeCategoryRepresentation category, String searchParam) {

        Log.d(TAG, " ----------------------------- loadDiscounts");

        this.currentCategory = category;

        searchText.setText(searchParam);

        if (selectedCategory == null) {
            selectedCategory = currentCategory;
        }
        if (!selectedCategory.get_id().equals("0")) {
            categoriesButton.setText(selectedCategory.getName());
        }

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

        //set category color
        ((BaseActivity) getActivity()).setActionBarBackgroundColor(getView().getResources().getColor(category.getColor()));
        changeStatusBarColor(category);

        LinearLayout parentLayout = (LinearLayout) getView().findViewById(R.id.discount_list_parent_layout);
        parentLayout.setBackgroundColor(getView().getResources().getColor(category.getColor()));

        //set category icon
        ImageView categoryImage = (ImageView) getView().findViewById(R.id.toolbar_category_image);
        if (categoryImage != null) {
            categoryImage.setImageResource(category.getImage());
        }


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

        if (discountsListView.getFooterViewsCount() == 0) {
            //show loading
            discountsListView.addFooterView(loadingFooter);
        }

        int currentResultsSize = 0;
        if (discountList != null) {
            currentResultsSize = discountList.size();
        }


        params.add(new BasicNameValuePair("limit", "40"));
        params.add(new BasicNameValuePair("latitude", String.valueOf(gpsService.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(gpsService.getLongitude())));
        if (discountList != null && discountList.size() > 0) {
            params.add(new BasicNameValuePair("minDistance", String.valueOf(discountList.get(discountList.size() - 1).getDis() + 0.00000001)));
        }

        String searchString = searchText.getText().toString();
        if (!searchString.isEmpty()) {
            params.add(new BasicNameValuePair("branchName", searchString));
        }

        if (selectedCategory != null && !selectedCategory.get_id().equals("0")) {
            params.add(new BasicNameValuePair("category", selectedCategory.get_id()));
        }

        if (selectedState != null) {
            params.add(new BasicNameValuePair("state", selectedState.get_id()));
        }

        if (selectedZone != null) {
            params.add(new BasicNameValuePair("zone", selectedZone.get_id()));
        }

        if (selectedSubcategory != null) {
            params.add(new BasicNameValuePair("subcategory", selectedSubcategory.get_id()));
        }

        if (discountList == null) {
            discountList = new ArrayList<NearByDiscountRepresentation>();
            adapter = new DiscountAdapter(getActivity(), discountList, currentCategory);

            animAdapter = new AlphaInAnimationAdapter(adapter);

            animAdapter.setAbsListView(discountsListView);
            discountsListView.setAdapter(animAdapter);
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
                discountsListView.removeFooterView(loadingFooter);

                discountList.addAll(Arrays.asList(hotelModuleRepresentations));
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