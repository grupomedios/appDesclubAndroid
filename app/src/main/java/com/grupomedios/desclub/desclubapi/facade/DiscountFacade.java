package com.grupomedios.desclub.desclubapi.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.BuildConfig;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.DiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountRepresentation;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class DiscountFacade {

    private final String TAG = "DiscountFacade";

    @Inject
    UserHelper userHelper;

    @Inject
    public DiscountFacade() {
    }

    /**
     *
     * @param context
     * @param successListener
     * @param errorListener
     * @param params
     * @return
     */
    public GsonRequest<NearByDiscountRepresentation[]> getAllDiscounts(Context context,
                                                                 Response.Listener<NearByDiscountRepresentation[]> successListener,
                                                                 Response.ErrorListener errorListener,
                                                                 List<NameValuePair> params) {

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_discounts_nearby);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<NearByDiscountRepresentation[]> jsObjRequest = new GsonRequest<NearByDiscountRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                NearByDiscountRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

    /**
     *
     * @param context
     * @param successListener
     * @param errorListener
     * @param params
     * @return
     */
    public GsonRequest<DiscountRepresentation[]> getAllRecommendedDiscounts(Context context,
                                                                       Response.Listener<DiscountRepresentation[]> successListener,
                                                                       Response.ErrorListener errorListener,
                                                                       List<NameValuePair> params) {

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_discounts_recommended);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<DiscountRepresentation[]> jsObjRequest = new GsonRequest<DiscountRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                DiscountRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

}
