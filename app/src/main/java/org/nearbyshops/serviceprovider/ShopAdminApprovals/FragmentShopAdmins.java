package org.nearbyshops.serviceprovider.ShopAdminApprovals;

import android.content.Intent;
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
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyTitleChanged;
import org.nearbyshops.serviceprovider.ModelRoles.Endpoints.ShopAdminEndPoint;
import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ShopAdminService;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.EditShopAdmin.EditShopAdmin;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.EditShopAdmin.EditShopAdminFragment;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 22/11/16.
 */

public class FragmentShopAdmins extends Fragment implements Adapter.NotifyConfirmOrder, SwipeRefreshLayout.OnRefreshListener {


    @Inject
    ShopAdminService shopAdminService;

    RecyclerView recyclerView;
    Adapter adapter;

    public List<ShopAdmin> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;

    boolean isDestroyed;


//    private static final String ARG_ACCOUNTS_MODE = "arg_accounts_mode";
    public static final int MODE_ACCOUNTS_ENABLED = 1;
    public static final int MODE_ACCOUNTS_DISABLED = 2;
    public static final int MODE_ACCOUNTS_WAITLISTED = 3;



    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentShopAdmins() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    public static FragmentShopAdmins newInstance(int sectionNumber) {
        FragmentShopAdmins fragment = new FragmentShopAdmins();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_admin_approvals, container, false);


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

        adapter = new Adapter(dataset,this,getContext());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(1);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
                    {
                        return;
                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }


    int previous_position = -1;


    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
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

    void makeNetworkCall(final boolean clearDataset)
    {

        Call<ShopAdminEndPoint> call = null;


        if(getArguments().getInt(ARG_SECTION_NUMBER) == MODE_ACCOUNTS_DISABLED)
        {
            call = shopAdminService
                    .getShopAdmin(UtilityLogin.getAuthorizationHeaders(getContext()),
                            false,false,
                            null,null,limit,offset,null);


        }
        else if (getArguments().getInt(ARG_SECTION_NUMBER) == MODE_ACCOUNTS_WAITLISTED)
        {
            call = shopAdminService
                    .getShopAdmin(UtilityLogin.getAuthorizationHeaders(getContext()),
                            false,true,
                            null,null,limit,offset,null);


        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ACCOUNTS_ENABLED)
        {
            call = shopAdminService
                    .getShopAdmin(UtilityLogin.getAuthorizationHeaders(getContext()),
                            true,null,
                            null,null,limit,offset,null);

        }



        if(call == null)
        {
            showToastMessage("call returned !");
            return;
        }


        call.enqueue(new Callback<ShopAdminEndPoint>() {
            @Override
            public void onResponse(Call<ShopAdminEndPoint> call, Response<ShopAdminEndPoint> response) {


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
            public void onFailure(Call<ShopAdminEndPoint> call, Throwable t) {

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

            if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ACCOUNTS_DISABLED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Disabled (" + String.valueOf(dataset.size())
                                        + "/" + String.valueOf(item_count) + ")",0);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ACCOUNTS_WAITLISTED)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Waitlisted (" + String.valueOf(dataset.size())
                                        + "/" + String.valueOf(item_count) + ")",1);

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==MODE_ACCOUNTS_ENABLED)
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
    public void notifyEditClick(ShopAdmin shopAdmin) {

        UtilityShopAdmin.saveShopAdmin(shopAdmin,getContext());

        Intent intent = new Intent(getContext(),EditShopAdmin.class);
        intent.putExtra(EditShopAdminFragment.EDIT_MODE_INTENT_KEY,EditShopAdminFragment.MODE_UPDATE);
        startActivity(intent);
    }

    @Override
    public void notifyListItemClick(ShopAdmin shopAdmin) {

    }
}