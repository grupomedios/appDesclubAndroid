package com.grupomedios.desclub.desclubutil.network.json;

import android.location.Location;

import com.google.gson.GsonBuilder;

import java.sql.Timestamp;

/**
 * Created by jhon on 19/02/15.
 */
public class GsonUtil {

    public static GsonBuilder createGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationDeserializer());
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return gsonBuilder;
    }

    public static String convertToJson(Object object) {
        return createGsonBuilder().create().toJson(object);
    }

    public static Object convertFromJson(String json, Class class1) {
        return createGsonBuilder().create().fromJson(json, class1);
    }

}
