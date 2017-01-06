package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.ModelSettings.ServiceConfiguration;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigurationService {

    @GET("/api/ServiceConfiguration")
    Call<ServiceConfiguration> getServiceConfiguration();

    @PUT("/api/ServiceConfiguration")
    Call<ResponseBody> putServiceConfiguration( @Header("Authorization") String headers,
                                                @Body ServiceConfiguration serviceConfiguration);



    // Image Calls

    @POST("/api/ServiceConfiguration/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    @DELETE("/api/ServiceConfiguration/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);
}
