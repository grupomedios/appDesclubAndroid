package com.grupomedios.desclub.desclubandroid.map.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.VolleySingleton;
import com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity;
import com.grupomedios.desclub.desclubandroid.home.util.FakeCategoryUtil;
import com.grupomedios.desclub.desclubapi.facade.DiscountFacade;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountWithFakeCategoryRepresentation;
import com.grupomedios.desclub.desclubutil.exception.SilentErrorListener;
import com.grupomedios.desclub.desclubutil.gps.GPSService;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.ui.dialog.DialogUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Main {@link Fragment} subclass.
 */
public class DesclubMapFragment extends Fragment implements OnMapReadyCallback {

    private final String TAG = "DesclubMapFragment";

    public static final String SEARCH_PARAM = "com.grupomedios.desclub.desclubandroid.map.fragment.DesclubMapFragment.SearchParam";
    public static final String CENTER_IN_CURRENT_LOCATION = "com.grupomedios.desclub.desclubandroid.map.fragment.DesclubMapFragment.CenterInCurrentLocation";

    private GPSService gpsService;
    private GoogleMap map;

    private List<NearByDiscountRepresentation> discountList;
    private RequestQueue requestQueue;
    private MaterialDialog progressDialog;
    private HashMap<Marker, NearByDiscountWithFakeCategoryRepresentation> markersHashMap;
    private boolean doNotReloadMarkers = true;
    MapFragment mapFragment;

    private boolean centerInCurrentLocation = true;
    private boolean mapAlreadyCentered;

    private String searchString;

    @Inject
    DiscountFacade discountFacade;

    public DesclubMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DesclubApplication) getActivity().getApplication()).inject(this);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        gpsService = new GPSService(getActivity());
        gpsService.getLocation();
        progressDialog = DialogUtil.createProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = MapFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        Bundle arguments = getArguments();
        searchString = arguments.getString(SEARCH_PARAM);

        centerInCurrentLocation = arguments.getBoolean(CENTER_IN_CURRENT_LOCATION);

        return mainView;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setupMap();
    }

    private void setupMap() {

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        if(centerInCurrentLocation){
            // Move the camera instantly to gps position with a zoom of 21.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(gpsService.getLatitude(),
                            gpsService.getLongitude()), 21));
        }

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        loadPageInMultipleSegments(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                doNotReloadMarkers = true;
                marker.showInfoWindow();
                return false;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                NearByDiscountWithFakeCategoryRepresentation discount = markersHashMap.get(marker);
                Intent intent;
                intent = new Intent(getActivity(), DiscountActivity.class);
                intent.putExtra(DiscountActivity.CURRENT_DISCOUNT_PARAM, discount.getNearByDiscount().getDiscount());
                intent.putExtra(DiscountActivity.CURRENT_CATEGORY_PARAM, discount.getCategory());
                startActivity(intent);
            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                if (!doNotReloadMarkers) {
//                    if (discountList != null) {
//                        discountList.clear();
//                    }
                loadPageInMultipleSegments(cameraPosition.target.latitude, cameraPosition.target.longitude);
//                } else {
//                    doNotReloadMarkers = false;
//                }
            }
        });
    }

    private void buildMarkers(List<NearByDiscountRepresentation> newDiscounts) {

        if (newDiscounts != null) {
            for (final NearByDiscountRepresentation discount : newDiscounts) {

                LatLng latLng = new LatLng(discount.getDiscount().getLocation().getCoordinates()[1],
                        discount.getDiscount().getLocation().getCoordinates()[0]);

                FakeCategoryRepresentation fakeCategory = FakeCategoryUtil.buildCategory(
                        getActivity(),
                        discount.getDiscount().getCategory().get_id());
                Marker currentMarker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(
                                        fakeCategory.getImage())));

                markersHashMap.put(currentMarker, new NearByDiscountWithFakeCategoryRepresentation(discount, fakeCategory));

            }

            MarkerInfoWindowAdapter adapter = new MarkerInfoWindowAdapter(getActivity(), markersHashMap);
            map.setInfoWindowAdapter(adapter);
        }

    }

    public void loadPageInMultipleSegments(double latitude, double longitude) {

        LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;

        //top right point
        double topRightLatitude = (latLngBounds.northeast.latitude + latitude) / 2;
        double topRightLongitude = (latLngBounds.northeast.longitude + longitude) / 2;

        //top left point
        double topLeftLatitude = (latLngBounds.northeast.latitude + latitude) / 2;
        double topLeftLongitude = (latLngBounds.southwest.longitude + longitude) / 2;

        //bottom left point
        double bottomLeftLatitude = (latLngBounds.southwest.latitude + latitude) / 2;
        double bottomLeftLongitude = (latLngBounds.southwest.longitude + longitude) / 2;

        //bottom right point
        double bottomRightLatitude = (latLngBounds.southwest.latitude + latitude) / 2;
        double bottomRightLongitude = (latLngBounds.northeast.longitude + longitude) / 2;


        //load all
        loadPage(latitude, longitude);
        loadPage(topRightLatitude, topRightLongitude);
        loadPage(topLeftLatitude, topLeftLongitude);
        loadPage(bottomLeftLatitude, bottomLeftLongitude);
        loadPage(bottomRightLatitude, bottomRightLongitude);

    }


    public void loadPage(double latitude, double longitude) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        int currentResultsSize = 0;
        if (discountList != null) {
            currentResultsSize = discountList.size();
        }

        if(discountList != null && discountList.size() > 0 && !mapAlreadyCentered && !centerInCurrentLocation){
            // Move the camera instantly to gps position with a zoom of 21.
            Float[] coordinates = discountList.get(0).getDiscount().getLocation().getCoordinates();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(coordinates[1],
                            coordinates[0]), 18));

            mapAlreadyCentered = true;
        }

        params.add(new BasicNameValuePair("limit", "30"));
        params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));

        if (searchString != null && !searchString.isEmpty()) {
            params.add(new BasicNameValuePair("branchName", searchString));
        }

        if (discountList == null || discountList.size() == 0) {
            discountList = new ArrayList<NearByDiscountRepresentation>();
            markersHashMap = new HashMap<Marker, NearByDiscountWithFakeCategoryRepresentation>();
            map.clear();
        }

//        progressDialog.show();


        GsonRequest<NearByDiscountRepresentation[]> guestAndLoginRequest = discountFacade.getAllDiscounts(getActivity(), new Response.Listener<NearByDiscountRepresentation[]>() {
            @Override
            public void onResponse(NearByDiscountRepresentation[] discountsRepresentations) {
                Log.d(TAG, String.valueOf(discountsRepresentations.length));
                progressDialog.dismiss();
                List<NearByDiscountRepresentation> newDiscounts = Arrays.asList(discountsRepresentations);
                discountList.addAll(newDiscounts);
                buildMarkers(newDiscounts);
            }
        }, new SilentErrorListener(TAG, getActivity(), progressDialog), params);

        requestQueue.add(guestAndLoginRequest);
    }

}