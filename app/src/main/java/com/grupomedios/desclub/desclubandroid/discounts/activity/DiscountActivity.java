package com.grupomedios.desclub.desclubandroid.discounts.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubandroid.VolleySingleton;
import com.grupomedios.desclub.desclubandroid.common.activity.DesclubGeneralActivity;
import com.grupomedios.desclub.desclubandroid.discounts.adapter.DiscountAdapter;
import com.grupomedios.desclub.desclubandroid.map.activity.DesclubMapActivity;
import com.grupomedios.desclub.desclubapi.representations.CorporateMembershipRepresentation;
import com.grupomedios.desclub.desclubapi.representations.DiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubutil.gps.GPSService;
import com.grupomedios.desclub.desclubutil.security.UserHelper;
import com.grupomedios.desclub.desclubutil.ui.image.ImageUtil;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

/**
 * Created by jhoncruz on 29/05/15.
 */
public class DiscountActivity extends DesclubGeneralActivity {

    private final static String TAG = "DiscountActivity";

    public static final String CURRENT_DISCOUNT_PARAM = "com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity.currentDiscount";
    public static final String CURRENT_CATEGORY_PARAM = "com.grupomedios.desclub.desclubandroid.discounts.activity.DiscountActivity.currentCategory";

    private RequestQueue requestQueue;
    private MaterialDialog progressDialog;

    private DiscountRepresentation discount;
    private FakeCategoryRepresentation category;

    private GPSService gpsService;

    private FloatingActionMenu searchFloatingMenu;

    private View directionsIconView;
    private View showInMapIconView;
    private View branchesIconView;

    @Inject
    UserHelper userHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.discount = (DiscountRepresentation) getIntent().getSerializableExtra(CURRENT_DISCOUNT_PARAM);
        this.category = (FakeCategoryRepresentation) getIntent().getSerializableExtra(CURRENT_CATEGORY_PARAM);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        gpsService = new GPSService(this);
        gpsService.getLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //set category color
        this.setActionBarBackgroundColor(getResources().getColor(category.getColor()));
        changeStatusBarColor(category);
        //set title
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.setTitle(discount.getBranch().getName());

        setDiscountData();

    }

    @TargetApi(21)
    private void changeStatusBarColor(FakeCategoryRepresentation category) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(category.getColor()));
        }
    }

    private void setDiscountData() {

        boolean showPromo = false;
        if (discount.getCash() != null && discount.getCash().length() > 0) {
            TextView cashTextView = (TextView) findViewById(R.id.viewDiscount_cash_textView);
            cashTextView.setTextColor(getResources().getColor(category.getColor()));
            cashTextView.setText(discount.getCash() + "%");
        } else {
            showPromo = true;
        }

        if (discount.getCard() != null && discount.getCard().length() > 0) {
            TextView cardTextView = (TextView) findViewById(R.id.viewDiscount_card_textView);
            cardTextView.setTextColor(getResources().getColor(category.getColor()));
            cardTextView.setText(discount.getCard() + "%");
            showPromo = false;
        } else {
            showPromo = true;
        }

        if (showPromo) {
            LinearLayout cashCardLayout = (LinearLayout) findViewById(R.id.viewDiscount_card_cash_layout);
            cashCardLayout.setVisibility(View.GONE);

            RelativeLayout promoLayout = (RelativeLayout) findViewById(R.id.viewDiscount_promo_layout);
            promoLayout.setVisibility(View.VISIBLE);

            TextView promoTextView = (TextView) findViewById(R.id.viewDiscount_promo_textView);
            promoTextView.setTextColor(getResources().getColor(category.getColor()));
            promoTextView.setText(discount.getPromo());

        }
        //logo
        ImageView logoImage = (ImageView) findViewById(R.id.viewDiscount_business_logo_imageView);
        ImageUtil.displayImage(logoImage, discount.getBrand().getLogoSmall(), null);

        //validity
        TextView validity = (TextView) findViewById(R.id.viewDiscount_validity_end_textView);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(discount.getBrand().getValidity_end());
        validity.setText("Vigente hasta: " + formattedDate);

        //name
        TextView businessName = (TextView) findViewById(R.id.viewDiscount_branch_name_textView);
        businessName.setText(discount.getBranch().getName());

        //distance
        if (gpsService.getLocation() != null && discount.getLocation() != null) {
            Location discountLocation = new Location("");
            discountLocation.setLongitude(discount.getLocation().getCoordinates()[0]);
            discountLocation.setLatitude(discount.getLocation().getCoordinates()[1]);
            float distanceInKm = discountLocation.distanceTo(gpsService.getLocation()) / 1000;
            TextView distance = (TextView) findViewById(R.id.viewDiscount_distance_textView);
            distance.setText(DiscountAdapter.calculateDistance(new Double(distanceInKm)));
        }

        //address
        TextView addressName = (TextView) findViewById(R.id.viewDiscount_branch_address_textView);
        String address = discount.getBranch().getStreet() + " " +
                discount.getBranch().getExtNum() + " " +
                discount.getBranch().getIntNum() + " " +
                discount.getBranch().getColony() + ", " +
                discount.getBranch().getZipCode() + ", " +
                discount.getBranch().getCity();
        addressName.setText(address);

        //restrictions
        TextView restrictions = (TextView) findViewById(R.id.viewDiscount_restrictions_textView);
        restrictions.setText(discount.getRestriction());

        //button color
        RelativeLayout useCouponButton = (RelativeLayout) findViewById(R.id.viewDiscount_coupon_layout);
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.round_background);
        drawable.setColor(getResources().getColor(category.getColor()));
        useCouponButton.setBackgroundDrawable(drawable);
        useCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCoupon();
            }
        });

        setupFloatingButton();

    }

    /**
     * @param text
     * @param icon
     * @param listener
     * @return
     */
    private View createFloatingButton(int text, int icon, View.OnClickListener listener) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View floatingButtonCommonView = inflater.inflate(R.layout.part_floating_options_button, null);

        ImageView buttonImage = (ImageView) floatingButtonCommonView.findViewById(R.id.floatingSearch_image_imageView);
        TextView buttonText = (TextView) floatingButtonCommonView.findViewById(R.id.floatingSearch_text_textView);
        RelativeLayout circleLayout = (RelativeLayout) floatingButtonCommonView.findViewById(R.id.floatingSearch_circle_layout);

        buttonImage.setImageDrawable(getResources().getDrawable(icon));
        buttonImage.setOnClickListener(listener);
        buttonText.setText(getResources().getText(text));
        buttonText.setOnClickListener(listener);

        GradientDrawable blueBackgroundDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.round_background);
        blueBackgroundDrawable.setColor(getResources().getColor(R.color.desclub_blue_dark));
        circleLayout.setBackground(blueBackgroundDrawable);

        return floatingButtonCommonView;

    }

    private void setupFloatingButton() {

        // Set up the white button on the lower left corner
        // more or less with default parameter
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.plus));

        GradientDrawable blueBackgroundDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.round_background);
        blueBackgroundDrawable.setColor(getResources().getColor(R.color.desclub_blue_dark));

        final FloatingActionButton leftLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setBackgroundDrawable(blueBackgroundDrawable)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
                .build();

        directionsIconView = createFloatingButton(R.string.directions, R.drawable.directions, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sourceAddress = gpsService.getLatitude() + "," + gpsService.getLongitude();
                String destAddress = discount.getLocation().getCoordinates()[1] + "," + discount.getLocation().getCoordinates()[0];

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+sourceAddress+"&daddr="+destAddress));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

                searchFloatingMenu.close(true);
            }
        });

        showInMapIconView= createFloatingButton(R.string.show_in_map, R.drawable.location, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DiscountActivity.this, DesclubMapActivity.class);
                intent.putExtra(DesclubMapActivity.SEARCH_PARAM, discount.getBranch().getName());
                startActivity(intent);

                searchFloatingMenu.close(true);
            }
        });

        branchesIconView = createFloatingButton(R.string.view_branches, R.drawable.branches, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DiscountActivity.this, BranchesActivity.class);
                intent.putExtra(BranchesActivity.CURRENT_BRAND, discount.getBrand().get_id());
                startActivity(intent);

                searchFloatingMenu.close(true);
            }
        });

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        searchFloatingMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(directionsIconView)
                .addSubActionView(showInMapIconView)
                .addSubActionView(branchesIconView)
                .attachTo(leftLowerButton)
                .setStartAngle(270)
                .setEndAngle(360)
                .build();

        // Listen menu open and close events to animate the button content view
        searchFloatingMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_24dp));
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.plus));
            }
        });
    }

    /**
     * Toggle visibility of the coupon
     */
    private void toggleCoupon() {

        RelativeLayout viewDiscountLayout = (RelativeLayout) findViewById(R.id.viewDiscount_couponView_layout);
        TextView couponLabel = (TextView) findViewById(R.id.viewDiscount_coupon_label);

        if (viewDiscountLayout.getVisibility() == View.GONE) {

            //force landscape
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            couponLabel.setText(getString(R.string.hide_coupon));

            LinearLayout membershipMessage = (LinearLayout) findViewById(R.id.viewDiscount_showMembershipMessage);
            RelativeLayout showCardLayout = (RelativeLayout) findViewById(R.id.viewDiscount_showCardLayout);

            TextView memebershipNumber = (TextView) findViewById(R.id.viewDiscount_membershipNumber);
            TextView name = (TextView) findViewById(R.id.viewDiscount_name);
            TextView validThru = (TextView) findViewById(R.id.viewDiscount_validThru);

            if (userHelper.isLoggedIn() && discount.getLocation() != null) {

                Location discountLocation = new Location("");
                discountLocation.setLongitude(discount.getLocation().getCoordinates()[0]);
                discountLocation.setLatitude(discount.getLocation().getCoordinates()[1]);

                float distanceInMeters = discountLocation.distanceTo(gpsService.getLocation());

                if (distanceInMeters < 2000) {

                    membershipMessage.setVisibility(View.GONE);
                    showCardLayout.setVisibility(View.VISIBLE);

                    CorporateMembershipRepresentation currentUser = userHelper.getCurrentUser();

                    memebershipNumber.setText(userHelper.getCardNumber());
                    name.setText(currentUser.getName().toUpperCase());
                    validThru.setText(userHelper.getValidThru());

                    expand(viewDiscountLayout);
                } else {

                    //force portrait
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    couponLabel.setText(getString(R.string.use_coupon));

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage(getString(R.string.out_of_range));
                    builder.setIcon(R.drawable.logo_small);
                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            } else {
                membershipMessage.setVisibility(View.VISIBLE);
                showCardLayout.setVisibility(View.GONE);

                memebershipNumber.setText("");
                name.setText("");

                expand(viewDiscountLayout);
            }

        } else {
            //force portrait
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            couponLabel.setText(getString(R.string.use_coupon));

            collapse(viewDiscountLayout);
        }

    }

    public static void expand(final View v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_discount;
    }
}
