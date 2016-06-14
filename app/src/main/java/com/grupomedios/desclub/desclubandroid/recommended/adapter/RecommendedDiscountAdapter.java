package com.grupomedios.desclub.desclubandroid.recommended.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.DiscountRepresentation;
import com.grupomedios.desclub.desclubutil.ui.image.ImageUtil;

import java.util.List;

/**
 * Discount adapter list
 *
 * @author jhon
 */
public class RecommendedDiscountAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DiscountRepresentation> discountsList;

    public RecommendedDiscountAdapter(Context context, List<DiscountRepresentation> discountModels) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        discountsList = discountModels;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return discountsList.size();
    }

    @Override
    public Object getItem(int position) {
        return discountsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = mInflater.inflate(R.layout.part_recommended_discount, parent, false);
        holder = new ViewHolder();
        holder.logoImage = (ImageView) convertView.findViewById(R.id.discount_businessLogo_imageView);
        holder.businessName = (TextView) convertView.findViewById(R.id.discount_businessName_textView);
        holder.cash = (TextView) convertView.findViewById(R.id.discount_cash_textView);
        holder.card = (TextView) convertView.findViewById(R.id.discount_card_textView);
        holder.promoLayout = (RelativeLayout) convertView.findViewById(R.id.discount_promo_circle_layout);
        holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.discount_parent_layout);
        convertView.setTag(holder);

        DiscountRepresentation dm = discountsList.get(position);

        ImageUtil.displayImage(holder.logoImage, dm.getBrand().getLogoSmall(), null);
        holder.businessName.setText(dm.getBranch().getName());

        boolean showPromo = false;

        if (dm.getCash() != null && dm.getCash().length() > 0) {
            holder.cash.setText(dm.getCash() + "%");
        } else {
            holder.parentLayout.removeView(holder.cash);
            showPromo = true;
        }

        if (dm.getCard() != null && dm.getCard().length() > 0) {
            holder.card.setText(dm.getCard() + "%");
            showPromo = false;
        } else {
            holder.parentLayout.removeView(holder.card);
            showPromo = true;
        }

        if (showPromo) {
            holder.parentLayout.removeView(holder.card);
            holder.parentLayout.removeView(holder.cash);

        } else {
            holder.parentLayout.removeView(holder.promoLayout);
        }

        return convertView;
    }

    private static class ViewHolder {
        public ImageView logoImage;
        public TextView businessName;
        public TextView cash;
        public TextView card;
        public RelativeLayout promoLayout;
        public LinearLayout parentLayout;
    }
}
