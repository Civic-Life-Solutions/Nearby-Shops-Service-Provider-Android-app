package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.ModelEndPoints.ItemCategoryEndPoint;

import java.util.List;

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
 * Created by sumeet on 2/4/16.
 */

public interface ItemCategoryService {



    @POST("/api/v1/ItemCategory")
    Call<ItemCategory> insertItemCategory(@Header("Authorization") String headers,
                                          @Body ItemCategory itemCategory);


    @PUT("/api/v1/ItemCategory/ChangeParent/{id}")
    Call<ResponseBody> changeParent(@Header("Authorization") String headers,
                                          @Body ItemCategory itemCategory,
                                          @Path("id") int id);

    @PUT("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> updateItemCategory(@Header("Authorization") String headers,
                                          @Body ItemCategory itemCategory,
                                          @Path("id") int id);

    @DELETE("/api/v1/ItemCategory/{id}")
    Call<ResponseBody> deleteItemCategory(@Header("Authorization") String headers,
                                          @Path("id") int id);


    @PUT("/api/v1/ItemCategory/")
    Call<ResponseBody> updateItemCategoryBulk(@Header("Authorization") String headers,
                                              @Body List<ItemCategory> itemCategoryList);


    @GET("api/v1/ItemCategory")
    Call<ItemCategoryEndPoint> getItemCategories(
            @Query("ShopID")Integer shopID,
            @Query("ParentID")Integer parentID,@Query("IsDetached")Boolean parentIsNull,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly
    );



    @GET("/api/v1/ItemCategory/{id}")
    Call<ItemCategory> getItemCategory(@Path("id") int ItemCategoryID);



    // Image Calls

    @POST("/api/v1/ItemCategory/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);


    @DELETE("/api/v1/ItemCategory/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);




    // Deprecated Calls


    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(@Query("ParentID") int parentID, @Query("ShopID") int shopID);

    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(@Query("ParentID") int parentID);


    @GET("/api/v1/ItemCategory/Deprecated")
    Call<List<ItemCategory>> getItemCategories(
            @Query("ShopID")Integer shopID,
            @Query("ParentID")Integer parentID,@Query("IsDetached")Boolean parentIsNull,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset);


}
