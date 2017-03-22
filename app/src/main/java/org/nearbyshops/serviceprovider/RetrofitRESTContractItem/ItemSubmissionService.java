package org.nearbyshops.serviceprovider.RetrofitRESTContractItem;


import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemSubmissionEndPoint;

import javax.annotation.security.RolesAllowed;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 17/3/17.
 */

public interface ItemSubmissionService {


    @POST("/api/v1/ItemSubmission")
    Call<Item> insertItem(@Header("Authorization") String headers,
                          @Body Item item);



    @PUT("/api/v1/ItemSubmission/RejectSubmission/{ItemSubmissionID}")
    Call<ResponseBody> rejectSubmission(@Header("Authorization") String headers,
                                        @Path("ItemSubmissionID")int itemSubmissionID);



    @PUT("/api/v1/ItemSubmission/ApproveItemInsert/{ItemSubmissionID}")
    Call<ResponseBody> approveInsert(@Header("Authorization") String headers,
                                     @Path("ItemSubmissionID")int itemSubmissionID);



    @PUT ("/api/v1/ItemSubmission/ApproveItemUpdate/{ItemSubmissionID}")
    Call<ResponseBody> approveUpdate(@Header("Authorization") String headers,
                                     @Path("ItemSubmissionID")int itemSubmissionID);




    @GET("/api/v1/ItemSubmission")
    Call<ItemSubmissionEndPoint> getSubmissions(
                    @Query("ShopAdminID")Integer shopAdminID,
                    @Query("Status")Integer statusID,
                    @Query("ItemID")Integer itemID,
                    @Query("ItemIDNull")Boolean itemIDNULL,
                    @Query("SortBy") String sortBy,
                    @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                    @Query("GetRowCount")boolean getRowCount
    );




    @GET("/api/v1/ItemSubmission/ItemsUpdated")
    Call<ItemEndPoint> getItemsUpdated(
            @Query("UserID")Integer userID,
            @Query("Status")Integer statusID,
            @Query("ItemIDNull")Boolean itemIDNULL,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("GetRowCount")boolean getRowCount);


}
