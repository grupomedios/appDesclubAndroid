package com.grupomedios.desclub.desclubandroid.map.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountRepresentation;
import com.grupomedios.desclub.desclubapi.representations.NearByDiscountWithFakeCategoryRepresentation;
import com.grupomedios.desclub.desclubutil.ui.image.ImageUtil;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.HashMap;

/**
 * Created by jhoncruz on 29/05/15.
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final String TAG = "MarkerInfoWindowAdapter";

    private Context context;
    private LayoutInflater mInflater;
    private HashMap<Marker, NearByDiscountWithFakeCategoryRepresentation> markersHashMap;
    private View convertView;
    private Marker marker;

    public MarkerInfoWindowAdapter(Context context, HashMap<Marker, NearByDiscountWithFakeCategoryRepresentation> markersHashMap) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.markersHashMap = markersHashMap;
    }

    @Override
    public View getInfoWindow(final Marker currentMarker) {

        convertView = mInflater.inflate(R.layout.part_marker, null);

        this.marker = currentMarker;

        ImageView markerIcon = (ImageView) convertView.findViewById(R.id.marker_businessLogo_imageView);
        TextView name = (TextView) convertView.findViewById(R.id.marker_businessName_textView);
        TextView cash = (TextView) convertView.findViewById(R.id.marker_cash_textView);
        TextView card = (TextView) convertView.findViewById(R.id.marker_card_textView);
        RelativeLayout promoLayout = (RelativeLayout) convertView.findViewById(R.id.marker_promo_circle_layout);
        LinearLayout parentLayout = (LinearLayout) convertView.findViewById(R.id.marker_parent_layout);

        NearByDiscountWithFakeCategoryRepresentation nearByDiscountWithFakeCategory = markersHashMap.get(currentMarker);
        FakeCategoryRepresentation category = nearByDiscountWithFakeCategory.getCategory();
        NearByDiscountRepresentation discount = nearByDiscountWithFakeCategory.getNearByDiscount();

        String logoUrl = discount.getDiscount().getBrand().getLogoSmall();
        Log.d(TAG, logoUrl);
        if (logoUrl != null && logoUrl.length() > 3) {
            ImageUtil.displayImage(markerIcon, logoUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    getInfoContents(currentMarker);
                }
            });
        } else {
            markerIcon.setImageResource(R.drawable.logo_small);
            getInfoContents(currentMarker);
        }

        name.setText(discount.getDiscount().getBranch().getName());
        boolean showPromo = false;

        if (discount.getDiscount().getCash() != null && discount.getDiscount().getCash().length() > 0) {
            cash.setTextColor(convertView.getResources().getColor(category.getColor()));
            cash.setText(discount.getDiscount().getCash() + "%");
        } else {
            parentLayout.removeView(cash);
            showPromo = true;
        }

        if (discount.getDiscount().getCard() != null && discount.getDiscount().getCard().length() > 0) {
            card.setTextColor(convertView.getResources().getColor(category.getColor()));
            card.setText(discount.getDiscount().getCard() + "%");
            showPromo = false;
        } else {
            parentLayout.removeView(card);
            showPromo = true;
        }

        if (showPromo) {
            parentLayout.removeView(card);
            parentLayout.removeView(cash);

            //customize background color
            GradientDrawable drawable = (GradientDrawable) convertView.getResources().getDrawable(R.drawable.round_background);
            drawable.setColor(convertView.getResources().getColor(category.getColor()));
            promoLayout.setBackgroundDrawable(drawable);
        } else {
            parentLayout.removeView(promoLayout);
        }

        return convertView;
    }

    @Override
    public View getInfoContents(final Marker marker2) {

        if (this.marker != null &&
                this.marker.isInfoWindowShown()) {
            this.marker.hideInfoWindow();
            this.marker.showInfoWindow();
        }
        return null;
    }
}
