package org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyBackPressed;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyFabClick_ItemCategories;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifySort;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyTitleChanged;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.ToggleFab;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.EditItemCategory.EditItemCategory;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategoriesTabs;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifyGeneral;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.inject.Inject;
import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoriesFragment extends Fragment
        implements ItemCategoriesAdapter.ReceiveNotificationsFromAdapter, SwipeRefreshLayout.OnRefreshListener,
        NotifyBackPressed,
        NotifyFabClick_ItemCategories , NotifySort{



    @State
    ArrayList<ItemCategory> dataset = new ArrayList<>();

    RecyclerView itemCategoriesList;
    ItemCategoriesAdapter listAdapter;
    GridLayoutManager layoutManager;

    @State boolean show = true;

    @Inject
    ItemCategoryService itemCategoryService;

    NotifyGeneral notificationReceiverFragment;

    @State
    ItemCategory currentCategory = null;


    public ItemCategoriesFragment() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_item_categories, container, false);

        ButterKnife.bind(this,rootView);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemCategoriesList = (RecyclerView)rootView.findViewById(R.id.recyclerViewItemCategories);




        if(getActivity() instanceof ItemCategoriesTabs)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();
//            Log.d("applog","DetachedItemFragment: Fragment Recreated");
            activity.notifyFabClickItemCategories = this;
            activity.notifyBackPressed = this;
        }


        if(getActivity() instanceof NotifyGeneral)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();
            this.notificationReceiverFragment = (NotifyGeneral) activity;
        }



        if(savedInstanceState==null)
        {
            // make request to the network only for the first time and not the second time or when the context is changed.

            // reset the offset before making request


            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {


                        // make a network call
                        offset = 0;
                        dataset.clear();
                        makeRequestRetrofit(false,false);


                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });


        }
        else
        {
            onViewStateRestored(savedInstanceState);
        }



        setupRecyclerView();
        setupSwipeContainer();


        return  rootView;

    }



    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;



    void setupRecyclerView()
    {

        listAdapter = new ItemCategoriesAdapter(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);
        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);


        // Code for Staggered Grid Layout
        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        */


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeRequestRetrofit(false,false);
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

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).hideFab();
                        }
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;

                    if(show!=previous)
                    {
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




    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


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



    public void makeRequestRetrofit(final boolean notifyItemCategoryChanged, final boolean backPressed)
    {

        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategories(
                null,currentCategory.getItemCategoryID(),
                null,null,null,null,null,null,"id",limit,offset,false);

        Log.d("applog","DetachedTabs: Network call made !");


        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(response.body()!=null)
                {
                    ItemCategoryEndPoint endPoint = response.body();
                    item_count = endPoint.getItemCount();
                    dataset.addAll(endPoint.getResults());
                    listAdapter.notifyDataSetChanged();

                    if(notifyItemCategoryChanged)
                    {
                        if(currentCategory!=null)
                        {

                            if(getActivity() instanceof NotifyCategoryChanged)
                            {
                                ((NotifyCategoryChanged)getActivity()).itemCategoryChanged(currentCategory,backPressed);
                            }
                            // throws null pointer exception
                        }
                    }

                    notifyTitleChanged();


                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");
                if(swipeContainer!=null)
                {
                    swipeContainer.setRefreshing(false);
                }
            }
        });

    }




    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }



    void notifyDelete()
    {
        dataset.clear();
        offset = 0 ; // reset the offset
        makeRequestRetrofit(false,false);
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    listAdapter.getRequestedChangeParent().setParentCategoryID(parentCategory.getItemCategoryID());

                    makeRequestUpdate(listAdapter.getRequestedChangeParent());

                }
            }
        }

        if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    List<ItemCategory> tempList = new ArrayList<>();

                    for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItems.entrySet())
                    {
                        entry.getValue().setParentCategoryID(parentCategory.getItemCategoryID());
                        tempList.add(entry.getValue());
                    }

                    makeRequestUpdateBulk(tempList);
                }

            }
        }
    }



    void makeRequestUpdate(ItemCategory itemCategory)
    {
        Call<ResponseBody> call = itemCategoryService.changeParent(UtilityLogin.getAuthorizationHeaders(getContext()),
                itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Successful !");

                    dataset.clear();
                    offset = 0 ; // reset the offset
                    makeRequestRetrofit(false,false);

                }else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }

                listAdapter.setRequestedChangeParent(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

                listAdapter.setRequestedChangeParent(null);
            }
        });
    }




    void makeRequestUpdateBulk(final List<ItemCategory> list)
    {
        Call<ResponseBody> call = itemCategoryService.changeParentBulk(UtilityLogin.getAuthorizationHeaders(getActivity()),
                list);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check for data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }


                /*dataset.clear();
                offset = 0 ; // reset the offset
                makeRequestRetrofit(false);*/
                onRefresh();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }


    void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }






    @Override
    public void notifyItemCategorySelected() {

        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @Override
    public void onRefresh() {

        // reset the offset and make a network call
        offset = 0;
        dataset.clear();
        makeRequestRetrofit(false,false);
    }


//    private boolean isRootCategory = true;
//
//    private ArrayList<String> categoryTree = new ArrayList<>();



    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);


        if(notificationReceiverFragment!=null)
        {
            notificationReceiverFragment.insertTab(currentCategory.getCategoryName());
        }


        dataset.clear();
        offset = 0 ; // reset the offset
        makeRequestRetrofit(true,false);

/*
        if(!currentCategory.getAbstractNode())
        {
            notificationReceiverFragment.notifySwipeToright();
        }*/

    }


    @Override
    public boolean backPressed() {

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear the selected items when back button is pressed
        listAdapter.selectedItems.clear();

        if(currentCategory!=null) {

            if (notificationReceiverFragment != null) {
                notificationReceiverFragment.removeLastTab();
            }

            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
//                options.setVisibility(View.VISIBLE);
//                appBar.setVisibility(View.VISIBLE);
//                notificationReceiverFragment.showAppBar();

//                exitFullscreen();

                dataset.clear();
                offset =0; // reset the offset
                makeRequestRetrofit(true,true);
            }

        }

        if(currentCategoryID == -1)
        {
//            super.onBackPressed();

            return  true;
        }else
        {
            return  false;
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
//        outState.putParcelableArrayList("dataset",dataset);
//        outState.putParcelable("currentCat",currentCategory);
    }



    void notifyTitleChanged()
    {

        /*String name = "";

        if(currentCategory.getCategoryName()!=null)
        {
            name = currentCategory.getCategoryName();
        }*/

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged) getActivity())
                    .NotifyTitleChanged(currentCategory.getCategoryName()
                             + " Subcategories ("
                            + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count )+ ")",0
                    );
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Icepick.restoreInstanceState(this, savedInstanceState);
        notifyTitleChanged();

        /*
        if (savedInstanceState != null) {

            ArrayList<ItemCategory> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);



            listAdapter.notifyDataSetChanged();
//
//            currentCategory = savedInstanceState.getParcelable("currentCat");
        }*/

    }


    @Override
    public void detachItem(final ItemCategory itemCategory) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Are you sure you want to detach selected category ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemCategory.setParentCategoryID(-1);
                        makeRequestUpdate(itemCategory);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }



    void detachedSelectedDialog()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Detach Item Categories !")
                .setMessage("Do you want to remove / detach parent for the selected Categories ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        detachSelected();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }




    void detachSelected()
    {

        /*if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }*/

        List<ItemCategory> tempList = new ArrayList<>();

        for(Map.Entry<Integer,ItemCategory> entry : listAdapter.selectedItems.entrySet())
        {
            entry.getValue().setParentCategoryID(-1);
            tempList.add(entry.getValue());
        }

        makeRequestUpdateBulk(tempList);
    }



    void addItemCategoryClick()
    {
//        Intent addIntent = new Intent(getActivity(), AddItemCategory.class);
//        addIntent.putExtra(AddItemCategory.ADD_ITEM_CATEGORY_INTENT_KEY,currentCategory);
//        startActivity(addIntent);

        Intent intent = new Intent(getActivity(),EditItemCategory.class);
        intent.putExtra(EditItemCategoryFragment.ITEM_CATEGORY_INTENT_KEY,currentCategory);
        intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_ADD);
        startActivity(intent);
    }



    void changeParentBulk()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        // make an exclude list. Put selected items to an exclude list. This is done to preven a category to make itself or its
        // children its parent. This is logically incorrect and should not happen.

        ItemCategoriesParent.clearExcludeList();
        ItemCategoriesParent.excludeList.putAll(listAdapter.selectedItems);

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);
        startActivityForResult(intentParent,2,null);
    }



    @Override
    public void detachSelectedClick() {
        detachedSelectedDialog();
    }

    @Override
    public void changeParentForSelected() {
        changeParentBulk();
    }

    @Override
    public void addItemCategory() {
        addItemCategoryClick();
    }

    @Override
    public void addfromGlobal() {

    }

    @Override
    public void notifySortChanged() {
        onRefresh();
    }
}