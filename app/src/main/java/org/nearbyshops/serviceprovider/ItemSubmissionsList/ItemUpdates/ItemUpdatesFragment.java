package org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemUpdates;

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
import org.nearbyshops.serviceprovider.ModelItemSubmission.Endpoints.ItemSubmissionEndPoint;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemSubmission;
import org.nearbyshops.serviceprovider.R;
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

public class ItemUpdatesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotificationsFromAdapter{


    boolean isDestroyed = false;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;


    @Inject ItemSubmissionService itemSubmissionService;

    GridLayoutManager layoutManager;
    Adapter listAdapter;



    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemSubmission> datasetItemsUpdated = new ArrayList<>();
    Item currentItem = new Item();


    // flags
    boolean getRowCountItemUpdates = false;
    boolean resetOffsetItemUpdates = false;
    boolean clearDatasetItemUpdates = false;


    private int limit_item_updates = 10;
    int offset_item_updates = 0;
    public int item_count_item_updates = 0;


    public static final String CURRENT_ITEM_INTENT_KEY = "current_item_intent_key";



    public ItemUpdatesFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_review, container, false);
        ButterKnife.bind(rootView);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);




        setupSwipeContainer();
        setupRecyclerView();





        String itemJson = getActivity().getIntent().getStringExtra(CURRENT_ITEM_INTENT_KEY);

        Gson gson = new Gson();
        currentItem = gson.fromJson(itemJson, Item.class);


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

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


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

                    if((offset_item_updates+limit_item_updates)<=item_count_item_updates)
                    {
                        offset_item_updates = offset_item_updates + limit_item_updates;

                    }


                }
            }



        });

    }






    void makeRequestItemUpdates()
    {

        if(resetOffsetItemUpdates)
        {
            offset_item_updates = 0;
            resetOffsetItemUpdates = false;
        }


        Call<ItemSubmissionEndPoint> call = itemSubmissionService.getSubmissions(
                null,1,currentItem.getItemID(),null,ItemSubmission.TIMESTAMP_SUBMITTED + " desc ",limit_item_updates,offset_item_updates,getRowCountItemUpdates
        );


        call.enqueue(new Callback<ItemSubmissionEndPoint>() {
            @Override
            public void onResponse(Call<ItemSubmissionEndPoint> call, Response<ItemSubmissionEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null)
                {

                    if(clearDatasetItemUpdates)
                    {
                        dataset.clear();
                        clearDatasetItemUpdates = false;

                        dataset.add(new HeaderTitle("Current Item"));
                        dataset.add(currentItem);
                    }

                    if(getRowCountItemUpdates)
                    {
                        item_count_item_updates = response.body().getItemCount();
                        getRowCountItemUpdates = false;

                        dataset.add(new HeaderTitle("Item Updates"));


                    }

//                    showToastMessage("Current ItemID : " + String.valueOf(currentItem.getItemID())
//                    + "\nDatasetSize : " + String.valueOf(response.body().getResults().size()));

                    dataset.addAll(response.body().getResults());

                    listAdapter.notifyDataSetChanged();
                }




//                if(offset_item_updates+limit_item_updates > item_count_item_updates)
//                {
//                    makeRequestItemsUpdated();
//                }
//

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemSubmissionEndPoint> call, Throwable t) {


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

        getRowCountItemUpdates = true;
        clearDatasetItemUpdates = true;
        resetOffsetItemUpdates = true;

        makeRequestItemUpdates();
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
    public void currentItemClick(Item item, int position) {


        Gson gson = new Gson();
        String json = gson.toJson(item);

        Intent intent = new Intent(getActivity(), SubmissionDetails.class);
        intent.putExtra(SubmissionDetailsFragment.ITEM_INTENT_KEY,json);
        intent.putExtra(SubmissionDetailsFragment.IS_CURRENT_INTENT_KEY,true);
        startActivity(intent);

    }

    @Override
    public void itemUpdateClick(ItemSubmission itemSubmission, int position) {


        Gson gson = new Gson();
        String json = gson.toJson(itemSubmission);

        Intent intent = new Intent(getActivity(), SubmissionDetails.class);
        intent.putExtra(SubmissionDetailsFragment.ITEM_INTENT_KEY,json);
        intent.putExtra(SubmissionDetailsFragment.IS_UPDATE_INTENT_KEY,true);
        startActivity(intent);
    }



}
