package com.grupomedios.desclub.desclubandroid.home.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.andreabaccega.widget.FormEditText;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.card.activity.SaludActivity;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountListActivity;
import com.grupomedios.desclub.desclubandroid.home.adapter.CategoryAdapter;
import com.grupomedios.desclub.desclubandroid.home.util.FakeCategoryUtil;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubutil.security.UserHelper;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Main {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnKeyListener {

    @Inject
    UserHelper userHelper;

    public static final String LIST_HOTEL_OPTION = "com.beepquest.beepquestandroid.home.fragment.MainFragment";
    private final String TAG = "MainFragment";

    private GridView categoryGridView;
    private List<FakeCategoryRepresentation> categories;

    public MainFragment() {
        // Required empty public constructor
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            FakeCategoryRepresentation fakeCategoryRepresentation = categories.get(position);
            //if not red medica => show category listing
            if (!fakeCategoryRepresentation.get_id().equals("1")) {
                Intent intent = new Intent(getActivity(), DiscountListActivity.class);
                intent.putExtra(DiscountListActivity.CURRENT_CATEGORY_PARAM, fakeCategoryRepresentation);
                startActivity(intent);
            } else {

                Intent intent = new Intent(getActivity(), SaludActivity.class);
                startActivity(intent);

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DesclubApplication) getActivity().getApplication()).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_main, container, false);

        FormEditText searchEdit = (FormEditText) mainView.findViewById(R.id.search_input_edit);
        searchEdit.setOnKeyListener(this);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return mainView;

    }


    @Override
    public void onStart() {
        super.onStart();
        categoryGridView = (GridView) getView().findViewById(R.id.categories_gridView);
        categoryGridView.setOnItemClickListener(itemClickListener);

        setUpCategories();

    }

    @Override
    public void onPause() {
        super.onPause();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setUpCategories() {

        categories = getFakeCategories();
        BaseAdapter adapter = new CategoryAdapter(getActivity(), categories);

        AnimationAdapter animAdapter = new AlphaInAnimationAdapter(adapter);

        animAdapter.setAbsListView(categoryGridView);
        categoryGridView.setAdapter(animAdapter);

    }

    private List<FakeCategoryRepresentation> getFakeCategories() {

        List<FakeCategoryRepresentation> list = new ArrayList<FakeCategoryRepresentation>();


        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.alimentos_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.belleza_salud_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.educacion_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.entretenimiento_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.moda_hogar_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.servicios_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), getResources().getString(R.string.turismo_id)));
        list.add(FakeCategoryUtil.buildCategory(getActivity(), "0"));
        if (userHelper.isLoggedIn() && userHelper.isSalud()) {
            //red medica
            list.add(new FakeCategoryRepresentation("1", getResources().getString(R.string.red_medica), R.drawable.red_medica, R.color.desclub_blue));
        }

        return list;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {


            search();

        }
        return false;
    }

    private void search() {

        FormEditText searchEdit = (FormEditText) getView().findViewById(R.id.search_input_edit);
        Log.d(TAG, searchEdit.getText().toString());

        Intent intent = new Intent(getActivity(), DiscountListActivity.class);
        intent.putExtra(DiscountListActivity.CURRENT_CATEGORY_PARAM, FakeCategoryUtil.buildCategory(getActivity(), "0"));
        intent.putExtra(DiscountListActivity.SEARCH_PARAM, searchEdit.getText().toString());
        startActivity(intent);

    }
}