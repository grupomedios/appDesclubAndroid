package com.grupomedios.desclub.desclubapi.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.BuildConfig;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.ZoneRepresentation;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 1/06/15.
 */
public class ZoneFacade {

    private final String TAG = "ZoneFacade";

    @Inject
    UserHelper userHelper;

    @Inject
    public ZoneFacade() {
    }


    public GsonRequest<ZoneRepresentation[]> getAllZones(Context context,
                                                         Response.Listener<ZoneRepresentation[]> successListener,
                                                         Response.ErrorListener errorListener,
                                                         List<NameValuePair> params) {

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_zones);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<ZoneRepresentation[]> jsObjRequest = new GsonRequest<ZoneRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                ZoneRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

}
