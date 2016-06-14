package com.grupomedios.desclub.desclubapi.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.BuildConfig;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.StateRepresentation;
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
public class StateFacade {

    private final String TAG = "StateFacade";

    @Inject
    UserHelper userHelper;

    @Inject
    public StateFacade() {
    }


    public GsonRequest<StateRepresentation[]> getAllStates(Context context,
                                                           Response.Listener<StateRepresentation[]> successListener,
                                                           Response.ErrorListener errorListener) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("limit", "50"));

        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_states);
        url = url + "?" + URLEncodedUtils.format(params, "UTF-8");
        Log.d(TAG, url);
        GsonRequest<StateRepresentation[]> jsObjRequest = new GsonRequest<StateRepresentation[]>(
                userHelper, Request.Method.GET, url, null,
                StateRepresentation[].class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

}
