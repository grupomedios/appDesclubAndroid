package com.grupomedios.desclub.desclubutil.network.volley;

import android.location.Location;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.grupomedios.desclub.desclubutil.network.json.LocationDeserializer;
import com.grupomedios.desclub.desclubutil.network.json.TimestampDeserializer;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

@Singleton
public class GsonRequest<T> extends JsonRequest<T> {

    /**
     * The default socket timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_MS = 4500;
    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 2;
    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    private final String TAG = "GsonRequest";
    private final Gson gson;
    private final Class<T> clazz;
    private final UserHelper userHelper;


    public GsonRequest(UserHelper userHelper, int method, String url, String requestBody, Class<T> responseClass, Listener<T> listener,
                       ErrorListener errorListener) {

        super(method, url, requestBody, listener, errorListener);

        this.userHelper = userHelper;

        clazz = responseClass;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.gson = gsonBuilder.create();
        // Do some generic stuff in here - for example, set your retry policy to
        // longer if you know all your requests are going to take > 2.5 seconds
        // etc etc...
        // Wait 20 seconds and don't retry more than once
        this.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//
//        Map<String, String> headers = super.getHeaders();
//        Map<String, String> params = new HashMap<String, String>();
//
//        params.putAll(headers);
//        params.put("Secure-Token", userHelper.getToken());
//
//        GuestRepresentation currentUser = userHelper.getCurrentUser();
//        if (currentUser != null) {
//            params.put("gq-guest", currentUser.getEmail());
//        }
//
//        Log.d(TAG, params.toString());
//
//        return params;
//    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
