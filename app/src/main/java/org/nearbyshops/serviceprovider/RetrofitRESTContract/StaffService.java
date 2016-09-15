package org.nearbyshops.serviceprovider.RetrofitRESTContract;

import org.nearbyshops.serviceprovider.ModelRoles.Admin;
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
import rx.Observable;

/**
 * Created by sumeet on 12/3/16.
 */
public interface StaffService {

    @GET("/api/v1/Staff/Login")
    Call<Staff> loginStaff(@Header("Authorization") String headers);

    @POST("/api/v1/Staff")
    Call<Staff> postStaff(@Body Staff staff);

    @PUT("/api/v1/Staff/{id}")
    Call<ResponseBody> putStaff(@Body Staff staff, @Path("id") int id);

    @DELETE("/api/v1/Staff/{id}")
    Call<ResponseBody> deleteStaff(@Path("id") int id);

}
