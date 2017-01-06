package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.Model.Shop;
import org.nearbyshops.serviceprovider.ModelRoles.Admin;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sumeet on 12/3/16.
 */
public interface AdminService {

    @GET("/api/v1/Admin/Login")
    Observable<Admin> getAdmin(@Header("Authorization")String headers);

    @PUT("/api/v1/Admin/{id}")
    Call<ResponseBody> putShop(@Header("Authorization")String headers,
                               @Body Admin admin);




//    @POST("/api/v1/Admin")
//    Call<Admin> postAdmin(@Body Admin admin);


//    @DELETE("/api/v1/Admin/{id}")
//    Call<ResponseBody> deleteShop(@Path("id") int id);

}
