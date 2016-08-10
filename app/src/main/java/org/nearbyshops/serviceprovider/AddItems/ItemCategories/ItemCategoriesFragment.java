package org.nearbyshops.serviceprovider.AddItems.ItemCategories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategoriesTabs;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoriesFragment extends Fragment
        implements  ItemCategoriesAdapter.NotificationReceiver, SwipeRefreshLayout.OnRefreshListener, ItemCategoriesTabs.ReceiveNotificationFromTabsForItemCat {

    List<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    ItemCategoriesAdapter listAdapter;

    GridLayoutManager layoutManager;

//    @Inject
//    ItemCategoryDataRouter dataRouter;


    boolean show = true;
    boolean isDragged = false;

    @Inject
    ItemCategoryService itemCategoryService;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.options)
    RelativeLayout options;

    @Bind(R.id.appbar)
    AppBarLayout appBar;



    int currentCategoryID = 1; // the ID of root category is always supposed to be 1
    ItemCategory currentCategory = null;




    public ItemCategoriesFragment() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);


    }


    int getMaxChildCount(int spanCount, int heightPixels)
    {
       return (spanCount * (heightPixels/250));
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

        setupRecyclerView();
        setupSwipeContainer();


        return  rootView;

    }



    void setupRecyclerView()
    {


        listAdapter = new ItemCategoriesAdapter(dataset,getActivity(),this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        itemCategoriesList.setLayoutManager(layoutManager);



        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        layoutManager.setSpanCount(metrics.widthPixels/350);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

//                        options.animate().translationX(metrics.widthPixels-10);
//                        options.animate().translationY(200);

                        options.setVisibility(View.GONE);
                        appBar.setVisibility(View.GONE);
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;



                    if(show!=previous)
                    {
                        // changed
//                        options.setVisibility(View.VISIBLE);
//                        options.animate().translationX(0);
                        Log.d("scrolllog","hide");

//                        options.animate().translationY(0);
                        options.setVisibility(View.VISIBLE);
                        appBar.setVisibility(View.VISIBLE);
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
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    public void makeRequestRetrofit()
    {

        Call<List<ItemCategory>> itemCategoryCall = itemCategoryService
                .getItemCategories(currentCategory.getItemCategoryID());


        itemCategoryCall.enqueue(new Callback<List<ItemCategory>>() {


            @Override
            public void onResponse(Call<List<ItemCategory>> call, Response<List<ItemCategory>> response) {



                dataset.clear();

                if(response.body()!=null) {

                    dataset.addAll(response.body());
                }

                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

                swipeContainer.setRefreshing(false);

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
        makeRequestRetrofit();
    }


    @Override
    public void onResume() {
        super.onResume();


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    makeRequestRetrofit();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

//                adapter.notifyDataSetChanged();
            }
        });
    }


    private boolean isRootCategory = true;

    private ArrayList<String> categoryTree = new ArrayList<>();


    private void insertTab(String categoryName)
    {

        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText("" + categoryName + " : : "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

    }

    private void removeLastTab()
    {

        tabLayout.removeTabAt(tabLayout.getTabCount()-1);
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

        if(tabLayout.getTabCount()==0)
        {
            tabLayout.setVisibility(View.GONE);
        }
    }





    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;

        currentCategory = itemCategory;

        currentCategoryID = itemCategory.getItemCategoryID();

        currentCategory.setParentCategory(temp);


        categoryTree.add(currentCategory.getCategoryName());

        insertTab(currentCategory.getCategoryName());



        if(isRootCategory) {

            isRootCategory = false;

        }else
        {
            boolean isFirst = true;
        }

        options.setVisibility(View.VISIBLE);
        appBar.setVisibility(View.VISIBLE);
        makeRequestRetrofit();
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

                    makeUpdateRequest(listAdapter.getRequestedChangeParent());
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

                    makeRequestBulk(tempList);
                }

            }
        }
    }



    void makeUpdateRequest(ItemCategory itemCategory)
    {
        Call<ResponseBody> call = itemCategoryService.updateItemCategory(itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Change Parent Successful !");

                    makeRequestRetrofit();

                }else
                {
                    showToastMessage("Change Parent Failed !");
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


    @OnClick(R.id.changeParentBulk)
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


    void makeRequestBulk(final List<ItemCategory> list)
    {
        Call<ResponseBody> call = itemCategoryService.updateItemCategoryBulk(list);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }



                makeRequestRetrofit();
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

        options.setVisibility(View.VISIBLE);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @OnClick(R.id.addItemCategory)
    void addItemCategoryClick()
    {
        Intent addIntent = new Intent(getActivity(), AddItemCategory.class);

        addIntent.putExtra(AddItemCategory.ADD_ITEM_CATEGORY_INTENT_KEY,currentCategory);

        startActivity(addIntent);
    }


    @Override
    public void onRefresh() {

        makeRequestRetrofit();
    }


    @Override
    public boolean backPressed() {

        // clear the selected items when back button is pressed
        listAdapter.selectedItems.clear();

        if(currentCategory!=null)
        {

            if(categoryTree.size()>0) {

                categoryTree.remove(categoryTree.size() - 1);
                removeLastTab();
            }


            if(currentCategory.getParentCategory()!= null) {

                currentCategory = currentCategory.getParentCategory();

                currentCategoryID = currentCategory.getItemCategoryID();

            }
            else
            {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if(currentCategoryID!=-1)
            {
                options.setVisibility(View.VISIBLE);
                appBar.setVisibility(View.VISIBLE);
                makeRequestRetrofit();
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

}



// commented out sections


            /*@Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== RecyclerView.SCROLL_STATE_SETTLING)
                {
//                    Log.d("scrolllog","Settling");
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                {
//                    Log.d("scrolllog","Idle");
                }



                if(newState== RecyclerView.SCROLL_STATE_DRAGGING && dataset.size() <= getMaxChildCount(layoutManager.getSpanCount(),metrics.heightPixels))
                {


                    Log.d("scrolllog","Child COunt :" + String.valueOf(recyclerView.getChildCount()));

                    Log.d("scrolllog","Max Child COunt : " + getMaxChildCount(layoutManager.getSpanCount(),metrics.heightPixels));

                    Log.d("scrolllog","Dataset size :" + String.valueOf(dataset.size()));

                    Log.d("scrolllog","drag");

                    if(!isDragged) {



//                        options.animate().translationY(200);

                        boolean previous = isDragged;

                        isDragged = true;

                        if(isDragged!=previous)
                        {
//

                        }

                    }else
                    {

//                        options.animate().translationY(0);

                        isDragged = false;

                    }

                }



            }
*/