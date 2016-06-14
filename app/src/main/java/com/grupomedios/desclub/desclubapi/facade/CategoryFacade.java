package com.grupomedios.desclub.desclubapi.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.BuildConfig;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.CategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.SubcategoryRepresentation;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 1/06/15.
 */
public class CategoryFacade {

    private final String TAG = "CategoryFacade";

    @Inject
    UserHelper userHelper;

    @Inject
    public CategoryFacade() {
    }

    /**
     *
     * @param context
     * @param successListener
     * @param errorListener
     * @return
     */
    public GsonRequest<CategoryRepresentation[]> getAllCategories(Context context,
                                                                        Response.Listener<CategoryRepresentation[]> successListener,
                                                                        Response.ErrorListener errorListener) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("limit", "50"));

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_categories);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<CategoryRepresentation[]> jsObjRequest = new GsonRequest<CategoryRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                CategoryRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

    /**
     *
     * @param context
     * @param successListener
     * @param errorListener
     * @param categoryId
     * @return
     */
    public GsonRequest<SubcategoryRepresentation[]> getAllSubcategories(Context context,
                                                                        Response.Listener<SubcategoryRepresentation[]> successListener,
                                                                        Response.ErrorListener errorListener,
                                                                        String categoryId) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("limit", "50"));

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_subcategories, categoryId);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<SubcategoryRepresentation[]> jsObjRequest = new GsonRequest<SubcategoryRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                SubcategoryRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

}
