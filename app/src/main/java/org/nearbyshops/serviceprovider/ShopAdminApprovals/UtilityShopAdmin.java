package org.nearbyshops.serviceprovider.ShopAdminApprovals;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;


import org.nearbyshops.serviceprovider.ModelRoles.Distributor;
import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;
import org.nearbyshops.serviceprovider.MyApplication;
import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityShopAdmin {



    public static void saveShopAdmin(ShopAdmin shopAdmin, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shopAdmin);
        prefsEditor.putString("shopAdmin", json);
        prefsEditor.apply();
    }


    public static ShopAdmin getShopAdmin(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("shopAdmin", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ShopAdmin.class);
        }
    }


}
