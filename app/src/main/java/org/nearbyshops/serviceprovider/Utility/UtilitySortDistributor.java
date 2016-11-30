package org.nearbyshops.serviceprovider.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.serviceprovider.zDistributorAccounts.SlidingLayerDistributor;
import org.nearbyshops.serviceprovider.MyApplication;
import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 29/9/16.
 */

public class UtilitySortDistributor {



    public static void saveSort(Context context, String sort_by)
    {

        if(context==null)
        {
            context = MyApplication.getAppContext();
        }

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_distributor", sort_by);
        editor.apply();
    }


    public static String getSort(Context context)
    {

        if(context==null)
        {
            context = MyApplication.getAppContext();
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String sort_by = sharedPref.getString("sort_distributor", SlidingLayerDistributor.SORT_BY_CREATED);

        return sort_by;
    }



    public static void saveAscending(Context context, String descending)
    {

        if(context==null)
        {
            context = MyApplication.getAppContext();
        }


        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_descending_distributor",descending);
        editor.apply();
    }



    public static String getAscending(Context context)
    {

        if(context==null)
        {
            context = MyApplication.getAppContext();
        }

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String descending = sharedPref.getString("sort_descending_distributor", SlidingLayerDistributor.SORT_DESCENDING);

        return descending;
    }

}
