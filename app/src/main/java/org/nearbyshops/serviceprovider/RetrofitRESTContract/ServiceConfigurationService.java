package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.ModelSettings.ServiceConfiguration;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigurationService {

    /*

    @GET("/api/ServiceConfiguration")
    Call<List<ServiceConfiguration>> getServices(@Query("ServiceLevel") int serviceLevel,
                                    @Query("ServiceType") int serviceType,
                                    @Query("LatCenter") Double latCenter,
                                    @Query("LonCenter") Double lonCenter,
                                    @Query("SortBy") String sortBy,
                                    @Query("Limit") int limit, @Query("Offset") int offset);

*/


    @GET("/api/ServiceConfiguration")
    Call<ServiceConfiguration> getServiceConfiguration();

    @PUT("/api/ServiceConfiguration")
    Call<ResponseBody> putServiceConfiguration(@Body ServiceConfiguration serviceConfiguration);




/*
    @POST("/api/ServiceConfiguration")
    Call<ServiceConfiguration> postService(@Body ServiceConfiguration service);
*/


/*
    @DELETE("/api/ServiceConfiguration/{id}")
    Call<ResponseBody> deleteService(@Path("id") int id);
*/

}
