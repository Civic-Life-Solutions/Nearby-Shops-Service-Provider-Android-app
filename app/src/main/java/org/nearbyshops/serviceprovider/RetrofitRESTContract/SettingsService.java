package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.ModelSettings.Settings;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by sumeet on 12/3/16.
 */
public interface SettingsService {

    @GET("/api/Settings")
    Call<Settings> getSetting();

    @PUT("/api/Settings")
    Call<ResponseBody> updateSettings(@Body Settings settings);

}
