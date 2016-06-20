package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Shop;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {

    @GET("/api/Shop")
    Call<List<Shop>> getShops(@Query("DistributorID") int distributorID);

    @GET("/api/Shop/{id}")
    Call<Shop> getShop(@Path("id") int id);

    @POST("/api/Shop")
    Call<Shop> postShop(@Body Shop shop);

    @PUT("/api/Shop/{id}")
    Call<ResponseBody> putShop(@Body Shop shop, @Path("id") int id);

    @DELETE("/api/Shop/{id}")
    Call<ResponseBody> deleteShop(@Path("id") int id);

}
