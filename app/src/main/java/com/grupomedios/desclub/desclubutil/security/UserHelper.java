package com.grupomedios.desclub.desclubutil.security;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andreabaccega.widget.FormEditText;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.home.activity.DesclubMainActivity;
import com.grupomedios.desclub.desclubapi.facade.CorporateMembershipFacade;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipAlreadyUsedRepresentation;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipRepresentation;
import com.grupomedios.desclub.desclubutil.exception.LoginErrorListener;
import com.grupomedios.desclub.desclubutil.network.volley.GsonRequest;
import com.grupomedios.desclub.desclubutil.ui.dialog.DialogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserHelper {

    private static final String TAG = "UserHelper";

    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String TOKEN = "TOKEN";

    public static final String INITIAL_DIALOG_SHOW_TIME = "INITIAL_DIALOG_SHOW_TIME";

    // Sharedpref file name
    private static final String PREFERENCES_NAME = "DesclubPrefs";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    @Inject
    public UserHelper() {
        pref = DesclubApplication.getAppContext().getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void partialLogout() {
        editor.remove(CURRENT_USER);
        editor.remove(TOKEN);
        editor.commit();
    }

    public void logout() {
        partialLogout();
    }

    public CorporateMembershipRepresentation getCurrentUser() {
        String currentUserJson = pref.getString(CURRENT_USER, null);

        CorporateMembershipRepresentation guest = null;

        if (currentUserJson != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            guest = gsonBuilder.create().fromJson(currentUserJson, CorporateMembershipRepresentation.class);
        }

        return guest;
    }

    /**
     * @param currentUser
     */
    public void setCurrentUser(CorporateMembershipRepresentation currentUser) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(currentUser);

        editor.putString(CURRENT_USER, json);

        // commit changes
        editor.commit();

    }

    public String getCardNumber() {
        CorporateMembershipRepresentation currentUser = UserHelper.this.getCurrentUser();
        String cardNumberReturn = "0000 0000 0000 0000";

        if (currentUser.getMembershipNumber() != null) {

            String membershipNumber = currentUser.getMembershipNumber();

            cardNumberReturn = membershipNumber.substring(0, 4) + " " +
                    membershipNumber.substring(4, 8) + " " +
                    membershipNumber.substring(8, 12) + " " +
                    membershipNumber.substring(12, 16);
        }

        return cardNumberReturn;
    }

    public String getSaludNumber() {
        CorporateMembershipRepresentation currentUser = UserHelper.this.getCurrentUser();
        String cardNumberReturn = "0000000";

        if (currentUser.getMembershipNumber() != null) {

            String membershipNumber = currentUser.getMembershipNumber();

            cardNumberReturn = membershipNumber.substring(9, 16);
        }

        return cardNumberReturn;
    }

    public String getValidThru() {

        CorporateMembershipRepresentation currentUser = UserHelper.this.getCurrentUser();

        String validThruReturn = "00-15";

        if (currentUser.getValidThru() != null) {
            validThruReturn = new SimpleDateFormat("MM-yy").format(currentUser.getValidThru());
        } else {
            Calendar cal = Calendar.getInstance();
            if (currentUser.getCreated() != null) {
                cal.setTime(currentUser.getCreated());
                cal.add(Calendar.DATE, 365); //minus number would decrement the days
                validThruReturn = new SimpleDateFormat("MM-yy").format(cal.getTime());
            } else {
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 30); //minus number would decrement the days
                validThruReturn = new SimpleDateFormat("MM-yy").format(cal.getTime());
            }
        }

        return validThruReturn;

    }

    public Long getInitialDialogShowTime() {
        return pref.getLong(INITIAL_DIALOG_SHOW_TIME, 0);
    }

    public void setInitialDialogShowTime(Long time) {
        editor.putLong(INITIAL_DIALOG_SHOW_TIME, time);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, null);
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public boolean isLoggedIn() {
        boolean isLoggedIn = false;
        CorporateMembershipRepresentation currentUser = getCurrentUser();
        if (currentUser != null) {
            isLoggedIn = true;
        }
        return isLoggedIn;
    }

    public boolean isSalud() {
        boolean isLoggedIn = false;
        CorporateMembershipRepresentation currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getMembershipNumber() != null && currentUser.getMembershipNumber().charAt(6) == '1') {
            isLoggedIn = true;
        }
        return isLoggedIn;
    }

    /**
     * @param activity
     * @param requestQueue
     * @param corporateMembershipFacade
     */
    public void showInitialDialog(final Activity activity, final RequestQueue requestQueue, final CorporateMembershipFacade corporateMembershipFacade) {

        if (!this.isLoggedIn()) {

            Long currentTime = System.currentTimeMillis();
            Long lastTime = this.getInitialDialogShowTime();
            Long maxDiffTime = 1000 * 60 * 30 * 1L; // 30 mins

            if (currentTime - lastTime > maxDiffTime) {
                final MaterialDialog initialDialog = new MaterialDialog.Builder(activity)
                        .title(R.string.start)
                        .positiveText(R.string.enter_now)
                        .cancelable(false)
                        .customView(R.layout.part_dialog_initial, false)
                        .build();

                initialDialog.show();

                //set last time
                this.setInitialDialogShowTime(System.currentTimeMillis());

                View customView = initialDialog.getCustomView();
                Button exploreButton = (Button) customView.findViewById(R.id.dialog_initial_explore);
                exploreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initialDialog.dismiss();
                    }
                });

                Button signinButton = (Button) customView.findViewById(R.id.dialog_initial_signin);
                signinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initialDialog.dismiss();
                        showLoginDialog(activity, requestQueue, corporateMembershipFacade);
                    }
                });

            }
        }
    }

    /**
     * @param activity
     * @param requestQueue
     * @param corporateMembershipFacade
     */
    public void showLoginDialog(final Activity activity, final RequestQueue requestQueue, final CorporateMembershipFacade corporateMembershipFacade) {

        MaterialDialog loginDialog = new MaterialDialog.Builder(activity)
                .title(R.string.signin)
                .negativeText(R.string.cancel)
                .positiveText(R.string.access)
                .cancelable(false)
                .autoDismiss(false)
                .customView(R.layout.part_dialog_login, false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(final MaterialDialog dialog) {

                        final FormEditText cardCode = (FormEditText) dialog.getCustomView().findViewById(R.id.login_card_edit);
                        final FormEditText email = (FormEditText) dialog.getCustomView().findViewById(R.id.login_email_edit);
                        final FormEditText lastName1 = (FormEditText) dialog.getCustomView().findViewById(R.id.login_lastName1_edit);

                        MaterialDialog progressDialog = DialogUtil.createProgressDialog(activity);

                        GsonRequest<CorporateMembershipRepresentation> guestAndLoginRequest = corporateMembershipFacade.getCorporateMembershipByNumber(activity, new Response.Listener<CorporateMembershipRepresentation>() {
                            @Override
                            public void onResponse(final CorporateMembershipRepresentation membershipRepresentation) {

                                if (membershipRepresentation != null ) {
                                    successfulMembership(membershipRepresentation, dialog);
                                    updateMembershipEmail(cardCode.getText().toString(), email.getText().toString());
                                } else {
                                    if (membershipRepresentation != null && membershipRepresentation.isAlreadyUsed()) {
                                        showValidationErrorAlreadyUsedLabel(dialog);
                                    } else {
                                        showValidationErrorLabel(dialog);
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                showValidationErrorLabel(dialog);
                            }
                        }, cardCode.getText().toString());

                        requestQueue.add(guestAndLoginRequest);
                    }

                    /**
                     *
                     * @param number
                     * @param email
                     */
                    private void updateMembershipEmail(String number, String email) {

                        if (email != null && !email.isEmpty()) {
                            GsonRequest<CorporateMembershipRepresentation> guestAndLoginRequest = corporateMembershipFacade.updateCorporateMembershipByNumber(activity, new Response.Listener<CorporateMembershipRepresentation>() {
                                @Override
                                public void onResponse(final CorporateMembershipRepresentation membershipRepresentation) {
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            }, number, email);

                            requestQueue.add(guestAndLoginRequest);
                        }

                    }

                    /**
                     *
                     * @param membershipRepresentation
                     * @param dialog
                     */
                    private void successfulMembership(CorporateMembershipRepresentation membershipRepresentation, final MaterialDialog dialog) {
                        //change membership status in backend
                        GsonRequest<CorporateMembershipRepresentation> changeStatusRquest = corporateMembershipFacade.changeCorporateMembershipStatus(activity,
                                new Response.Listener<CorporateMembershipRepresentation>() {
                                    @Override
                                    public void onResponse(CorporateMembershipRepresentation membershipRepresentation) {
                                        //success!! save credentials
                                        UserHelper.this.setCurrentUser(membershipRepresentation);
                                        //close dialog
                                        dialog.dismiss();

                                        //restart activity
                                        Intent intent = new Intent(activity, DesclubMainActivity.class);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    }
                                },
                                new LoginErrorListener(TAG, activity, null),
                                membershipRepresentation.get_id(),
                                new CorporateMembershipAlreadyUsedRepresentation(true));

                        requestQueue.add(changeStatusRquest);
                    }

                    /**
                     *
                     * @param dialog
                     */
                    private void showValidationErrorLabel(MaterialDialog dialog) {
                        View loginCustomView = dialog.getCustomView();
                        TextView errorLabel = (TextView) loginCustomView.findViewById(R.id.login_error_label);
                        errorLabel.setVisibility(View.VISIBLE);

                        //hide the other one
                        TextView alreadyUsedLabel = (TextView) loginCustomView.findViewById(R.id.login_error_already_used_label);
                        alreadyUsedLabel.setVisibility(View.GONE);
                    }

                    /**
                     *
                     * @param dialog
                     */
                    private void showValidationErrorAlreadyUsedLabel(MaterialDialog dialog) {
                        View loginCustomView = dialog.getCustomView();

                        TextView alreadyUsedLabel = (TextView) loginCustomView.findViewById(R.id.login_error_already_used_label);
                        alreadyUsedLabel.setVisibility(View.VISIBLE);

                        //hide the other one
                        TextView errorLabel = (TextView) loginCustomView.findViewById(R.id.login_error_label);
                        errorLabel.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();

        loginDialog.show();

    }
}
