package org.nearbyshops.serviceprovider.ItemSubmissionsList.ImageUpdates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.HeaderTitle;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails.SubmissionDetails;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails.SubmissionDetailsFragment;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.Model.ItemImage;
import org.nearbyshops.serviceprovider.ModelItemSpecification.EndPoints.ItemImageEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemImageSubmissionEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemSubmissionEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemImageSubmission;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemSubmission;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContractItem.ItemImageSubmissionService;
import org.nearbyshops.serviceprovider.RetrofitRESTContractItem.ItemSubmissionService;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 21/3/17.
 */

public class ImageUpdatesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotificationsFromAdapter{


    boolean isDestroyed = false;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;


    @Inject ItemImageSubmissionService imageSubmissionService;



    GridLayoutManager layoutManager;
    Adapter listAdapter;



    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemImageSubmission> datasetItemsUpdated = new ArrayList<>();
    ItemImage currentImage = new ItemImage();


    // flags

    boolean clearDataset = false;

    boolean getRowCountImageUpdates = false;
    boolean resetOffsetImageUpdates = false;


    private int limit_image_updates = 10;
    int offset_image_updates = 0;
    public int item_count_image_updates = 0;


    public static final String CURRENT_IMAGE_INTENT_KEY = "current_image_intent_key";




    public ImageUpdatesFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_image_updates, container, false);
        ButterKnife.bind(rootView);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);




        setupSwipeContainer();
        setupRecyclerView();





        String itemJson = getActivity().getIntent().getStringExtra(CURRENT_IMAGE_INTENT_KEY);

        Gson gson = new Gson();
        currentImage = gson.fromJson(itemJson, ItemImage.class);


//        dataset.add(new HeaderTitle("Current Item"));
//        dataset.add(currentItem);
//
//        listAdapter.notifyDataSetChanged();




        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


        return rootView;
    }




    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }




    void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this,this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(dataset!=null && dataset.size()>0 && position < dataset.size())
                {
                    if(dataset.get(position) instanceof ItemImageSubmission)
                    {
                        return 3;
                    }
                    else if(dataset.get(position) instanceof ItemImage)
                    {
                        return 3;
                    }
                    else
                    {
                        return 6;
                    }
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page




                    // trigger fetch next page




                    if((offset_image_updates+limit_image_updates)<=item_count_image_updates)
                    {
                        offset_image_updates = offset_image_updates + limit_image_updates;
                        makeRequestImagesAdded();
                    }

//                    else if((offset_images_updated+limit_images_updated)<=item_count_images_updated)
//                    {
//                        offset_images_updated = offset_images_updated + limit_images_updated;
//
//                        makeRequestImagesUpdated();
//                    }
//
//                    else if((offset_item_updates+limit_item_updates)<=item_count_item_updates)
//                    {
//                        offset_item_updates = offset_item_updates + limit_item_updates;
//
//                        makeRequestItemUpdates();
//                    }


                }
            }



        });
    }





    void makeRequestImagesAdded()
    {
        if(resetOffsetImageUpdates)
        {
            offset_image_updates = 0;
            resetOffsetImageUpdates = false;
        }


        Call<ItemImageSubmissionEndPoint> call = imageSubmissionService.getImagesAdded(
          null,1,null,currentImage.getImageID(),null,null,limit_image_updates,offset_image_updates,getRowCountImageUpdates
        );



        call.enqueue(new Callback<ItemImageSubmissionEndPoint>() {
            @Override
            public void onResponse(Call<ItemImageSubmissionEndPoint> call, Response<ItemImageSubmissionEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null)
                {

                    if(clearDataset)
                    {
                        dataset.clear();
                        clearDataset = false;

                        dataset.add(new HeaderTitle("Current Image"));
                        dataset.add(currentImage);
                    }


                    if(response.code()==200 && response.body()!=null)
                    {
                        if(getRowCountImageUpdates)
                        {
                            item_count_image_updates = response.body().getItemCount();
                            getRowCountImageUpdates = false;

                            dataset.add(new HeaderTitle("Image Updates"));
                        }


                        dataset.addAll(response.body().getResults());
                        listAdapter.notifyDataSetChanged();
                    }




//                    if(offset_images_added + limit_images_added > item_count_images_added)
//                    {
//                        makeRequestImagesUpdated();
//                    }

                    swipeContainer.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<ItemImageSubmissionEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });
    }



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {


        clearDataset = true;

        getRowCountImageUpdates = true;
        resetOffsetImageUpdates = true;


        makeRequestImagesAdded();
//        makeRequestItemUpdates();
    }


    void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });

    }



    @Override
    public void currentImageClick(ItemImage itemImage, int position) {


        Gson gson = new Gson();
        String json = gson.toJson(itemImage);

//        Intent intent = new Intent(getActivity(), SubmissionDetails.class);
//        intent.putExtra(SubmissionDetailsFragment.ITEM_INTENT_KEY,json);
//        intent.putExtra(SubmissionDetailsFragment.IS_CURRENT_INTENT_KEY,true);
//        startActivity(intent);
    }

    @Override
    public void itemUpdateClick(ItemSubmission itemSubmission, int position) {


        Gson gson = new Gson();
        String json = gson.toJson(itemSubmission);

//        Intent intent = new Intent(getActivity(), SubmissionDetails.class);
//        intent.putExtra(SubmissionDetailsFragment.ITEM_INTENT_KEY,json);
//        intent.putExtra(SubmissionDetailsFragment.IS_UPDATE_INTENT_KEY,true);
//        startActivity(intent);

    }



}
