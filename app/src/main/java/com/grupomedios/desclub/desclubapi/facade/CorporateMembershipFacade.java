package com.grupomedios.desclub.desclubapi.facade;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.grupomedios.desclub.desclubandroid.BuildConfig;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipAlreadyUsedRepresentation;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipInputRepresentation;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipRepresentation;
import com.grupomedios.desclub.desclubutil.network.json.GsonUtil;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 1/06/15.
 */
public class CorporateMembershipFacade {

    private final String TAG = "CorporateMembershipFacade";

    @Inject
    UserHelper userHelper;

    @Inject
    public CorporateMembershipFacade() {
    }


    /**
     * @param context
     * @param successListener
     * @param errorListener
     * @param corporateMembershipInputRepresentation
     * @return
     */
    public GsonRequest<CorporateMembershipRepresentation> createCorporateMembership(Context context, Response.Listener<CorporateMembershipRepresentation> successListener,
                                                                                    Response.ErrorListener errorListener,
                                                                                    CorporateMembershipInputRepresentation corporateMembershipInputRepresentation) {
        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_corporateMembership);

        GsonRequest<CorporateMembershipRepresentation> jsObjRequest = new GsonRequest<CorporateMembershipRepresentation>(
                userHelper, Request.Method.POST, url, GsonUtil.convertToJson(corporateMembershipInputRepresentation),
                CorporateMembershipRepresentation.class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

    /**
     * @param context
     * @param successListener
     * @param errorListener
     * @param number
     * @return
     */
    public GsonRequest<CorporateMembershipRepresentation> getCorporateMembershipByNumber(Context context,
                                                                                         Response.Listener<CorporateMembershipRepresentation> successListener,
                                                                                         Response.ErrorListener errorListener,
                                                                                         String number) {


        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_corporateMembershipsByNumber, number);
        GsonRequest<CorporateMembershipRepresentation> jsObjRequest = new GsonRequest<CorporateMembershipRepresentation>(
                userHelper, Request.Method.GET, url, null,
                CorporateMembershipRepresentation.class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

    /**
     *
     * @param context
     * @param successListener
     * @param errorListener
     * @param number
     * @param email
     * @return
     */
    public GsonRequest<CorporateMembershipRepresentation> updateCorporateMembershipByNumber(Context context,
                                                                                            Response.Listener<CorporateMembershipRepresentation> successListener,
                                                                                            Response.ErrorListener errorListener,
                                                                                            String number,
                                                                                            String email) {


        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_corporateMembershipsByNumber, number);
        GsonRequest<CorporateMembershipRepresentation> jsObjRequest = new GsonRequest<CorporateMembershipRepresentation>(
                userHelper, Request.Method.PUT, url, GsonUtil.convertToJson(new CorporateMembershipRepresentation(email)),
                CorporateMembershipRepresentation.class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

    /**
     * @param context
     * @param successListener
     * @param errorListener
     * @param membershipId
     * @param corporateMembershipAlreadyUsedRepresentation
     * @return
     */
    public GsonRequest<CorporateMembershipRepresentation> changeCorporateMembershipStatus(Context context, Response.Listener<CorporateMembershipRepresentation> successListener,
                                                                                          Response.ErrorListener errorListener,
                                                                                          String membershipId,
                                                                                          CorporateMembershipAlreadyUsedRepresentation corporateMembershipAlreadyUsedRepresentation) {
        String url = BuildConfig.BASE_URL + context.getString(R.string.endpoint_corporateMemberships, membershipId);

        GsonRequest<CorporateMembershipRepresentation> jsObjRequest = new GsonRequest<CorporateMembershipRepresentation>(
                userHelper, Request.Method.PUT, url, GsonUtil.convertToJson(corporateMembershipAlreadyUsedRepresentation),
                CorporateMembershipRepresentation.class,
                successListener,
                errorListener);

        return jsObjRequest;
    }

}
