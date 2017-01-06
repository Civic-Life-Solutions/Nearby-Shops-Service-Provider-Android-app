package org.nearbyshops.serviceprovider.ServiceConfiguration.EditConfiguration;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.serviceprovider.ModelRoles.Staff;
import org.nearbyshops.serviceprovider.ModelSettings.ServiceConfiguration;
import org.nearbyshops.serviceprovider.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityServiceConfiguration {

    private static final String TAG_PREF_STAFF = "configuration";

    public static void saveConfiguration(ServiceConfiguration configuration, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(configuration);
        prefsEditor.putString(TAG_PREF_STAFF, json);
        prefsEditor.apply();
    }


    public static ServiceConfiguration getStaff(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_STAFF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ServiceConfiguration.class);
        }
    }


}
