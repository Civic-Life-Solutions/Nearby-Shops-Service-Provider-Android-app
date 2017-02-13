package org.nearbyshops.serviceprovider.Services;

import android.content.Context;
import android.content.SharedPreferences;


import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */




public class UtilityLocationServices {

    public static String KEY_LAT_CENTER = "key_lat_center";
    public static String KEY_LON_CENTER = "key_lon_center";

    public static String KEY_PROXIMITY = "key_proximity";
    public static String KEY_DELIVERY_RANGE_MAX = "key_delivery_range_max";
    public static String KEY_DELIVERY_RANGE_MIN = "key_delivery_range_min";



    public static void saveLatitude(Float latitude, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context
                .getSharedPreferences(
                        context.getString(R.string.preference_file_name),
                        MODE_PRIVATE
                );


        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(latitude == null)
        {
            prefsEditor.putFloat(KEY_LAT_CENTER, -1);
        }
        else
        {
            prefsEditor.putFloat(KEY_LAT_CENTER, latitude);
        }

        prefsEditor.apply();
    }


    public static Double getLatitude(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Double latitude = (double) sharedPref.getFloat(KEY_LAT_CENTER, -1);

        if( latitude == -1)
        {
            return null;
        }
        else
        {
            return latitude;
        }
    }


    // saving longitude

    public static void saveLongitude(Float longitude, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context
                .getSharedPreferences(
                        context.getString(R.string.preference_file_name),
                        MODE_PRIVATE
                );


        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(longitude == null)
        {
            prefsEditor.putFloat(KEY_LON_CENTER, -1);
        }
        else
        {
            prefsEditor.putFloat(KEY_LON_CENTER, longitude);
        }

        prefsEditor.apply();
    }


    public static Double getLongitude(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Double longitude = (double) sharedPref.getFloat(KEY_LON_CENTER, -1);

        if( longitude == -1)
        {
            return null;
        }
        else
        {
            return longitude;
        }
    }

}
