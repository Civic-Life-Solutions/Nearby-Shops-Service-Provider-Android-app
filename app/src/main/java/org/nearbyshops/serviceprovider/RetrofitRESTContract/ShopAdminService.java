package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.ModelRoles.Endpoints.ShopAdminEndPoint;
import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;

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
public interface ShopAdminService {

    @POST("/api/ShopAdmin")
    Call<ShopAdmin> postShopAdmin(@Header("Authorization") String headers,
                                  @Body ShopAdmin shopAdmin);


    @PUT("/api/ShopAdmin/{id}")
    Call<ResponseBody> putShopAdmin(@Header("Authorization") String headers,
                                    @Body ShopAdmin shopAdmin, @Path("id") int id);

    @DELETE("/api/ShopAdmin/{id}")
    Call<ResponseBody> deleteShopAdmin(@Header("Authorization") String headers,
                                       @Path("id") int id);

    @GET("/api/ShopAdmin")
    Call<ShopAdminEndPoint> getShopAdmin(@Header("Authorization") String headers,
                                         @Query("Enabled") Boolean enabled,
                                         @Query("Waitlisted") Boolean waitlisted,
                                         @Query("SearchString") String searchString,
                                         @Query("SortBy") String sortBy,
                                         @Query("Limit") Integer limit, @Query("Offset") Integer offset,
                                         @Query("metadata_only") Boolean metaonly);


    @GET("/api/ShopAdmin/{id}")
    Call<ShopAdmin> getShopAdmin(@Header("Authorization") String headers,
                                 @Path("id") int id);



    @GET("/api/ShopAdmin/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username") String username);



    @GET("/api/ShopAdmin/Login")
    Call<ShopAdmin> getShopAdminLogin(@Header("Authorization") String headers);




    // Image Calls

    @POST("/api/ShopAdmin/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/ShopAdmin/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);


}
