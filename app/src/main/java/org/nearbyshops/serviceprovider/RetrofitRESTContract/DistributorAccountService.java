package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.ModelRoles.Distributor;
import org.nearbyshops.serviceprovider.ModelRoles.DistributorEndPoint;
import org.nearbyshops.serviceprovider.ModelRoles.Staff;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface DistributorAccountService {

    @GET("/api/v1/Distributor/Private")
    Call<DistributorEndPoint> getDistributor(@Header("Authorization") String headers,
                                             @Query("DistributorID")Integer distributorID,
                                             @Query("IsEnabled") Boolean isEnabled,
                                             @Query("IsWaitlisted") Boolean isWaitlisted,
                                             @Query("SortBy") String sortBy,
                                             @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                             @Query("metadata_only")Boolean metaonly);


    @POST("/api/v1/Distributor")
    Call<Distributor> postDistributor(@Body Distributor distributor);

    @PUT("/api/v1/Distributor/{id}")
    Call<ResponseBody> putDistributor(@Header("Authorization") String headers,
                                      @Body Distributor distributor, @Path("id") int id);

    @DELETE("/api/v1/Distributor/{id}")
    Call<ResponseBody> deleteStaff(@Header("Authorization") String headers,
                                   @Path("id") int id);

}
