package com.grupomedios.desclub.desclubandroid.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;

import java.util.List;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FakeCategoryRepresentation> categories;

    public CategoryAdapter(Context context, List<FakeCategoryRepresentation> categoryList) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categories = categoryList;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryViewHolder holder;
        //set params into a holder
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.part_category, parent, false);
            holder = new CategoryViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.category_text);
            holder.image = (ImageView) convertView.findViewById(R.id.category_image);

            convertView.setTag(holder);
        } else {
            holder = (CategoryViewHolder) convertView.getTag();
        }


        FakeCategoryRepresentation categoryRepresentation = categories.get(position);

        //change params in holder
        holder.name.setText(categoryRepresentation.getName());
        holder.image.setImageResource(categoryRepresentation.getImage());

        return convertView;
    }

    /**
     * Category view holder
     */
    private static class CategoryViewHolder {
        public TextView name;
        public ImageView image;
    }

}
