package org.nearbyshops.serviceprovider.RetrofitRESTContractItem;

import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSpecification.EndPoints.ItemImageEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemImageSubmissionEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemSubmissionEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemImageSubmission;

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

public interface ItemImageSubmissionService {



    @GET ("/api/v1/ItemImageSubmission/ItemsUpdated")
    Call<ItemEndPoint> getItemsUpdated(
            @Query("UserID")Integer userID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("GetRowCount")boolean getRowCount);




    @GET ("/api/v1/ItemImageSubmission/ItemImagesUpdated")
    Call<ItemImageEndPoint> getImagesUpdated(
            @Query("UserID") Integer userID,
            @Query("Status")Integer statusID,
            @Query("ItemID")Integer itemID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("GetRowCount")boolean getRowCount);




    @GET("/api/v1/ItemImageSubmission")
    Call<ItemImageSubmissionEndPoint> getImagesAdded(
            @Query("UserID")Integer userID,
            @Query("Status")Integer statusID,
            @Query("ItemIDNull")Boolean itemIDNULL,
            @Query("ItemImageID")Integer itemImageID,
            @Query("ItemID")Integer itemID,
            @Query("SortBy") String sortBy,
            @Query("Limit")Integer limit, @Query("Offset")Integer offset,
            @Query("GetRowCount")boolean getRowCount);




    //***********************************************************************


}
