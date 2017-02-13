package org.nearbyshops.serviceprovider.ShopApprovals.Fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Interfaces.GetLocation;
import org.nearbyshops.serviceprovider.Interfaces.NotifyLocation;
import org.nearbyshops.serviceprovider.Interfaces.NotifySearch;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifySort;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyTitleChanged;
import org.nearbyshops.serviceprovider.Model.Shop;
import org.nearbyshops.serviceprovider.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ShopService;
import org.nearbyshops.serviceprovider.ShopApprovals.EditShop.EditShop;
import org.nearbyshops.serviceprovider.ShopApprovals.EditShop.EditShopFragment;
import org.nearbyshops.serviceprovider.ShopApprovals.EditShop.UtilityShop;
import org.nearbyshops.serviceprovider.ShopApprovals.SlidingLayerSort.UtilitySortShops;
import org.nearbyshops.serviceprovider.ShopApprovals.UtilityLocation;
import org.nearbyshops.serviceprovider.Utility.DividerItemDecoration;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 24/11/16.
 */


public class FragmentShopApprovals extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotifyByShopAdapter ,NotifySearch, NotifySort,NotifyLocation{

    private static final String ARG_SECTION_NUMBER = "section_number";

    Location location;

    @Inject
    ShopService shopService;

    RecyclerView recyclerView;
    Adapter adapter;

    public List<Shop> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;

    boolean isDestroyed;

    public static final int MODE_ENABLED = 1;
    public static final int MODE_DISABLED = 2;
    public static final int MODE_WAITLISTED = 3;



    public FragmentShopApprovals() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static FragmentShopApprovals newInstance(int sectionNumber) {
        FragmentShopApprovals fragment = new FragmentShopApprovals();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_approvals, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();

        return rootView;
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


    void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this,getContext(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);


//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(1);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);



                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1-1)
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false,false);
                    }
                }

            }
        });
    }



    @Override
    public void onRefresh() {
        makeNetworkCall(true,true);
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




    void makeNetworkCall(final boolean clearDataset,boolean resetOffset)
    {
        if(resetOffset)
        {
            offset = 0;
        }


        Call<ShopEndPoint> call = null;

        String current_sort = "";
        current_sort = UtilitySortShops.getSort(getContext()) + " " + UtilitySortShops.getAscending(getContext());


        Double latitude = null;
        Double longitude = null;


        if(getActivity() instanceof GetLocation)
        {
            this.location = ((GetLocation)getActivity()).getLocation();
        }

        if(location!=null)
        {
            showToastMessage("Location" + String.valueOf(this.location.getLongitude()) + " : " + String.valueOf(this.location.getLatitude()));
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }


//        latitude = UtilityLocationServices.getLatitude(getActivity());
//        longitude = UtilityLocationServices.getLongitude(getActivity());

//        showToastMessage("Latitude : " + UtilityLocationServices.getLatitude(getActivity()) + " : Longitude " + UtilityLocationServices.getLongitude(getActivity()));


        if(getArguments().getInt(ARG_SECTION_NUMBER) == MODE_DISABLED)
        {
            call = shopService.getShopListSimple(
                    false,false,
                    null,
                    latitude,longitude,
                    null,null,null,
                    searchQuery,current_sort,limit,offset);

        }
        else if (getArguments().getInt(ARG_SECTION_NUMBER) == MODE_WAITLISTED)
        {

            call = shopService.getShopListSimple(
                    false,true,
                    null,
                    latitude,longitude,
                    null,null,null,
                    searchQuery,current_sort,limit,offset);

        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ENABLED)
        {

            call = shopService.getShopListSimple(
                    true,null,
                    null,
                    latitude,longitude,
                    null,null, null,
                    searchQuery,current_sort,limit,offset);
        }



        if(call == null)
        {
            showToastMessage("call returned !");
            return;
        }


        call.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!= null)
                {
                    item_count = response.body().getItemCount();

                    if(clearDataset)
                    {
                        dataset.clear();
                    }

                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    notifyTitleChanged();

                }

//                showToastMessage("Status Code : " + String.valueOf(response.code())
//                + "\nDataset Size : " + dataset.size() + " Item Count : " + response.body().getItemCount());

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }


    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_DISABLED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Disabled (" + String.valueOf(dataset.size())
                                        + "/" + String.valueOf(item_count) + ")",0);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_WAITLISTED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Waitlisted (" + String.valueOf(dataset.size())
                                        + "/" + String.valueOf(item_count) + ")",1);

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ENABLED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Enabled (" + String.valueOf(dataset.size())
                                        + "/" + String.valueOf(item_count) + ")",2);

            }

        }
    }


    // Refresh the Confirmed PlaceHolderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }




    @Override
    public void notifyEditClick(Shop shop) {

        UtilityShop.saveShop(shop,getActivity());
        Intent intent = new Intent(getActivity(), EditShop.class);
        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY,EditShopFragment.MODE_UPDATE);
        startActivity(intent);

    }

    @Override
    public void notifyListItemClick(Shop shop) {

    }


    String searchQuery = null;

    @Override
    public void search(String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }


    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }



    @Override
    public void fetchedLocation(Location location) {
//        this.location = location;
        makeRefreshNetworkCall();
    }
}