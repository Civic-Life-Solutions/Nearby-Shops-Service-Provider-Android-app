package org.nearbyshops.serviceprovider.DistributorAccounts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;

import org.nearbyshops.serviceprovider.DistributorAccounts.Interfaces.NotifyTitleChanged;
import org.nearbyshops.serviceprovider.DistributorAccounts.Interfaces.ToggleFab;
import org.nearbyshops.serviceprovider.ModelRoles.Distributor;
import org.nearbyshops.serviceprovider.ModelRoles.DistributorEndPoint;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.DistributorAccountService;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;
import org.nearbyshops.serviceprovider.Utility.UtilitySort;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 28/9/16.
 */


public class DistributorAccountFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Inject
    DistributorAccountService distributorAccountService;

    @State
    ArrayList<Distributor> dataset = new ArrayList<>();

    @Bind(R.id.recyclerView)
    RecyclerView itemCategoriesList;

    DistributorAdapter listAdapter;
    GridLayoutManager layoutManager;

    @State
    boolean show = true;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


    // scroll variables
    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;

    @State
    int layoutPosition = 0;

    @State
    boolean isSaved;

    public DistributorAccountFragment() {

        DaggerComponentBuilder.getInstance().getNetComponent()
                .Inject(this);
    }


    @SuppressLint("ValidFragment")
    public DistributorAccountFragment(int layoutPosition) {
        this.layoutPosition = layoutPosition;

        DaggerComponentBuilder.getInstance().getNetComponent()
                .Inject(this);
    }



    public static DistributorAccountFragment newInstance(int sectionNumber) {
        DistributorAccountFragment fragment = new DistributorAccountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_distributor_accounts_, container, false);
        ButterKnife.bind(this,rootView);

        setupRecyclerView();
        setupSwipeContainer();

        // the function is called because we figured out the view state was not getting restored at this point
        onViewStateRestored(savedInstanceState);

        Log.d("saved",String.valueOf(isSaved));

        if(!isSaved)
        {
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);
                    dataset.clear();
                    networkCallGetDistributors();
                }
            });
        }
        else
        {
            onViewStateRestored(savedInstanceState);
            notifyTitleChanged();
            listAdapter.notifyDataSetChanged();
        }


        isSaved = true;
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if(isSaved)
        {
            Log.d("saved","resumed !");
            notifyTitleChanged();
            listAdapter.notifyDataSetChanged();


            setupRecyclerView();
            setupSwipeContainer();
        }
    }

    void setupRecyclerView()
    {


        listAdapter = new DistributorAdapter(getActivity(),dataset);
        itemCategoriesList.setAdapter(listAdapter);
        layoutManager = new GridLayoutManager(getActivity(),1);
        itemCategoriesList.setLayoutManager(layoutManager);

        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        layoutManager.setSpanCount(metrics.widthPixels/350);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition() == dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        networkCallGetDistributors();
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

                        if(getActivity() instanceof ToggleFab){
                            ((ToggleFab)getActivity()).hideFab();
                        }

                    }

                }else if(dy < -20)
                {

                    boolean previous = show;
                    show = true;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","hide");
                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).showFab();
                        }
                    }
                }
            }

        });

    }


    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }


    void networkCallGetDistributors()
    {

        Boolean isEnabled = false;
        Boolean isWaitlisted = false;

        String current_sort = "";
//        String[] sort_options;

        if(layoutPosition == 1)
        {
            isWaitlisted = true;
        }
        else if(layoutPosition == 2)
        {
            isEnabled = true;
            isWaitlisted = null;
        }


//        sort_options = UtilitySort.getSort(getActivity());
        current_sort = UtilitySort.getSort(getContext()) + " " + UtilitySort.getAscending(getContext());

        Call<DistributorEndPoint> call = distributorAccountService
                .getDistributor(
                    UtilityLogin.baseEncoding(UtilityLogin.getUsername(getActivity()),UtilityLogin.getPassword(getActivity())),
                        null,isEnabled,isWaitlisted,current_sort,limit,offset,null);


        call.enqueue(new Callback<DistributorEndPoint>() {

            @Override
            public void onResponse(Call<DistributorEndPoint> call, Response<DistributorEndPoint> response) {

                if(response.body()!=null)
                {
                    DistributorEndPoint endPoint = response.body();
                    dataset.addAll(endPoint.getResults());
                    item_count = endPoint.getItemCount();
                }


                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();
                notifyTitleChanged();
            }

            @Override
            public void onFailure(Call<DistributorEndPoint> call, Throwable t) {

                swipeContainer.setRefreshing(false);
                showToastMessage("Network request failed. Please check your connection !");
            }
        });
    }


    public void notifySort()
    {
        onRefresh();
    }


    void notifyTitleChanged() {

        String title = "";

        if (layoutPosition == 0) {
            title = "Disabled (" + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count) + ")";
        }
        else if (layoutPosition == 1) {

            title = "Waitlisted (" + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count) + ")";
        }
        else if (layoutPosition == 2)
        {
            title = "Enabled (" + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count) + ")";
        }


        if(getActivity() instanceof NotifyTitleChanged)
        {

            ((NotifyTitleChanged)getActivity()).NotifyTitleChanged(title,layoutPosition);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0 ; // reset the offset
        networkCallGetDistributors();
    }


    void onRefreshSwipeContainer()
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);
//        outState.putParcelableArrayList("dataset",dataset);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Icepick.restoreInstanceState(this,savedInstanceState);
/*
        if(isSaved)
        {
            ArrayList<Distributor> tempCat = savedInstanceState.getParcelableArrayList("dataset");
            dataset.clear();
            dataset.addAll(tempCat);

            notifyTitleChanged();
            listAdapter.notifyDataSetChanged();
        }*/
    }



    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }

}
