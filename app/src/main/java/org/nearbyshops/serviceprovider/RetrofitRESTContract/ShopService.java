package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.Model.Shop;
import org.nearbyshops.serviceprovider.ModelEndPoints.ShopEndPoint;

import java.util.List;

import javax.annotation.security.RolesAllowed;

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
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface ShopService {


    @GET("/api/v1/Shop/QuerySimple")
    Call<ShopEndPoint> getShopListSimple(
            @Query("Enabled")Boolean enabled,
            @Query("Waitlisted") Boolean waitlisted,
            @Query("FilterByVisibility") Boolean filterByVisibility,
            @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") int offset
    );



    @PUT("/api/v1/Shop/UpdateByAdmin/{ShopID}")
    Call<ResponseBody> updateShop(@Header("Authorization") String headers,
                                  @Body Shop shop,
                                  @Path("ShopID")int ShopID);




    @POST("/api/v1/Shop")
    Call<Shop> postShop(@Header("Authorization") String headers,
                        @Body Shop shop);



    // Image Calls

    @POST("/api/v1/Shop/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/Shop/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);





    //--------------------------------------



//    @POST("/api/Shop")
//    Call<Shop> postShop(@Body Shop shop);



    //***********************************

//
//    @GET("/api/Shop")
//    Call<List<Shop>> getShops(@Query("DistributorID") int distributorID);
//
//    @GET("/api/Shop/{id}")
//    Call<Shop> getShop(@Path("id") int id);


//    @PUT("/api/Shop/{id}")
//    Call<ResponseBody> putShop(@Body Shop shop, @Path("id") int id);
//
//    @DELETE("/api/Shop/{id}")
//    Call<ResponseBody> deleteShop(@Path("id") int id);





}
