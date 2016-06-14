package com.grupomedios.desclub.desclubandroid.home.util;

import android.app.Activity;

import com.grupomedios.desclub.desclubandroid.R;
import com.grupomedios.desclub.desclubapi.representations.FakeCategoryRepresentation;

/**
 * Created by jhoncruz on 30/05/15.
 */
public class FakeCategoryUtil {

    public static FakeCategoryRepresentation buildCategory(Activity activity, String categoryId) {

        if (categoryId != null) {
            if (categoryId.equals(activity.getResources().getString(R.string.alimentos_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.alimentos), R.drawable.alimentos, R.color.category_alimentos);
            } else if (categoryId.equals(activity.getResources().getString(R.string.belleza_salud_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.belleza_salud), R.drawable.belleza_salud, R.color.category_belleza_salud);
            } else if (categoryId.equals(activity.getResources().getString(R.string.educacion_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.educacion), R.drawable.educacion, R.color.category_educacion);
            } else if (categoryId.equals(activity.getResources().getString(R.string.entretenimiento_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.entretenimiento), R.drawable.entretenimiento, R.color.category_entretenimiento);
            } else if (categoryId.equals(activity.getResources().getString(R.string.moda_hogar_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.moda_hogar), R.drawable.moda_hogar, R.color.category_moda);
            } else if (categoryId.equals(activity.getResources().getString(R.string.servicios_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.servicios), R.drawable.servicios, R.color.category_servicios);
            } else if (categoryId.equals(activity.getResources().getString(R.string.turismo_id))) {
                return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.turismo), R.drawable.turismo, R.color.category_turismo);
            }
        }

        return new FakeCategoryRepresentation(categoryId, activity.getResources().getString(R.string.see_all), R.drawable.see_all, R.color.category_all);
    }

}
