package org.nearbyshops.serviceprovider.ItemSubmissionsList;

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
import org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemUpdates.ItemUpdates;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemUpdates.ItemUpdatesFragment;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails.SubmissionDetails;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails.SubmissionDetailsFragment;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelEndPoints.ItemEndPoint;
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
 * Created by sumeet on 20/3/17.
 */

public class ItemReviewFragment extends Fragment implements Adapter.NotificationsFromAdapter, SwipeRefreshLayout.OnRefreshListener {

    boolean isDestroyed = false;

    private int limit_item_added = 10;
    int offset_item_added = 0;
    public int item_count_item_added = 0;


    private int limit_item_updated = 10;
    int offset_item_updated = 0;
    public int item_count_item_updated = 10;

    // flags
    boolean getRowCountItemsAdded = false;
    boolean resetOffsetItemsAdded = false;
    boolean clearDatasetItemsAdded = false;

    boolean getRowCountItemsUpdated = false;
    boolean resetOffsetItemsUpdated = false;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;


    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemSubmission> datasetItemsAdded = new ArrayList<>();
    ArrayList<Item> datasetItemsUpdated = new ArrayList<>();


    GridLayoutManager layoutManager;
    Adapter listAdapter;


    @Inject ItemSubmissionService itemSubmissionService;


    public ItemReviewFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_review, container, false);
        ButterKnife.bind(rootView);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        setupRecyclerView();
        setupSwipeContainer();



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



//                    if(offset_item_added + limit_item_added > layoutManager.findLastVisibleItemPosition())
//                    {
//                        return;
//                    }
//                    else if(item_count_item_added + offset_item_updated + limit_item_updated > layoutManager.findLastVisibleItemPosition())
//                    {
//                        return;
//                    }



                    // trigger fetch next page

                    if((offset_item_added+limit_item_added)<=item_count_item_added)
                    {
                        offset_item_added = offset_item_added + limit_item_added;
                        makeRequestItemsAdded();

                    }
                    else if((offset_item_updated+limit_item_updated)<=item_count_item_updated)
                    {

                        offset_item_updated = offset_item_updated + limit_item_updated;
                        makeRequestItemsUpdated();
                    }


                }
            }



        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        isDestroyed = true;
    }

    @Override
    public void notifyDeleteItem(Item item, int position) {

    }

    @Override
    public void itemAddedClick(ItemSubmission itemSubmission, int position) {

        Gson gson = new Gson();
        String json = gson.toJson(itemSubmission);

        Intent intent = new Intent(getActivity(), SubmissionDetails.class);
        intent.putExtra(SubmissionDetailsFragment.ITEM_INTENT_KEY,json);
        startActivity(intent);

    }



    @Override
    public void itemUpdatedClick(Item item, int position) {


        Gson gson = new Gson();
        String json = gson.toJson(item);

        Intent intent = new Intent(getActivity(), ItemUpdates.class);
        intent.putExtra(ItemUpdatesFragment.CURRENT_ITEM_INTENT_KEY,json);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

        getRowCountItemsAdded = true;
        clearDatasetItemsAdded = true;
        resetOffsetItemsAdded = true;

        getRowCountItemsUpdated = true;
        resetOffsetItemsUpdated = true;

        makeRequestItemsAdded();
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







    void makeRequestItemsAdded()
    {

        if(resetOffsetItemsAdded)
        {
            offset_item_added = 0;
            resetOffsetItemsAdded = false;
        }


        Call<ItemSubmissionEndPoint> call = itemSubmissionService.getSubmissions(
                null,1,null,true,null,limit_item_added,offset_item_added,getRowCountItemsAdded
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

                    if(clearDatasetItemsAdded)
                    {
                        dataset.clear();
                        clearDatasetItemsAdded = false;


                    }

                    if(getRowCountItemsAdded)
                    {
                        item_count_item_added = response.body().getItemCount();
                        getRowCountItemsAdded = false;

                        dataset.add(new HeaderTitle("Items Added"));
                    }


                    dataset.addAll(response.body().getResults());

                    listAdapter.notifyDataSetChanged();
                }




                if(offset_item_added+limit_item_added > item_count_item_added)
                {
                    makeRequestItemsUpdated();
                }





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





    void makeRequestItemsUpdated()
    {

        if(resetOffsetItemsUpdated)
        {
            offset_item_updated = 0;
            resetOffsetItemsUpdated = false;
        }

        Call<ItemEndPoint> call = itemSubmissionService.getItemsUpdated(
                null,1,null,"time_latest",limit_item_updated,offset_item_updated,getRowCountItemsUpdated
        );


        call.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null)
                {

                    if(getRowCountItemsUpdated)
                    {
                        item_count_item_updated = response.body().getItemCount();
                        getRowCountItemsUpdated = false;
                        dataset.add(new HeaderTitle("Items Updated"));
                    }


                    dataset.addAll(response.body().getResults());

                    listAdapter.notifyDataSetChanged();
                }


                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                swipeContainer.setRefreshing(false);
            }
        });
    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }









}
