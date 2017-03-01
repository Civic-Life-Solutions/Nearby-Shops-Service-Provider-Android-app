package org.nearbyshops.serviceprovider.ShopAdminApprovals.SlidingLayerSort;

import android.content.Context;
import android.content.SharedPreferences;

import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 29/9/16.
 */

public class UtilitySortShopAdmin {



    public static void saveSort(Context context, String sort_by)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_shop_admin", sort_by);
        editor.apply();
    }


    public static String getSort(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String sort_by = sharedPref.getString("sort_shop_admin", SlidingLayerSortShopAdmin.SORT_BY_REGISTRATION_DATE);

        return sort_by;
    }



    public static void saveAscending(Context context, String descending)
    {

        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("sort_descending_shop_admin",descending);
        editor.apply();
    }



    public static String getAscending(Context context)
    {

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String descending = sharedPref.getString("sort_descending_shop_admin", SlidingLayerSortShopAdmin.SORT_ASCENDING);

        return descending;
    }

}
