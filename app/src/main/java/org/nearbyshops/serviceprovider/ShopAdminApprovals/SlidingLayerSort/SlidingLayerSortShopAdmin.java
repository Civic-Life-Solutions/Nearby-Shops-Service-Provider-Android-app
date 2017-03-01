package org.nearbyshops.serviceprovider.ShopAdminApprovals.SlidingLayerSort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces.NotifySort;
import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;
import org.nearbyshops.serviceprovider.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortShopAdmin extends Fragment {

    @Bind(R.id.sort_registration_date)
    TextView sort_by_registration_date;

    @Bind(R.id.sort_by_name)
    TextView sort_by_name;

//    @Bind(R.id.popularity)
//    TextView sort_by_popularity;

//    @Bind(R.id.available)
//    TextView sort_by_items_available;

//    @Bind(R.id.item_price)
//    TextView sort_by_item_price;



    @Bind(R.id.sort_ascending)
    TextView sort_ascending;

    @Bind(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_REGISTRATION_DATE;
    String currentAscending = SORT_ASCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


//    public static String SORT_BY_REGISTRATION_DATE = "distance";
//    public static String SORT_BY_A_TO_Z = "avg_rating";
//    public static String SORT_BY_POPULARITY = "popularity";


    public static String SORT_BY_REGISTRATION_DATE = ShopAdmin.SHOP_ADMIN_ID;
    public static String SORT_BY_A_TO_Z = ShopAdmin.NAME;
//    public static String SORT_BY_POPULARITY = "popularity";

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_shop_admin,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    void loadDefaultSort() {
//        String[] sort_options = UtilitySortShopItem.getSort(getActivity());

        currentSort = UtilitySortShopAdmin.getSort(getActivity());
        currentAscending = UtilitySortShopAdmin.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_REGISTRATION_DATE))
        {
            sort_by_registration_date.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_registration_date.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_A_TO_Z))
        {
            sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }

//        else if(currentSort.equals(SORT_BY_POPULARITY))
//        {
//            sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//            sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
//
//        }


        if(currentAscending.equals(SORT_ASCENDING))
        {
            sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
        else if(currentAscending.equals(SORT_DESCENDING))
        {
            sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
    }



    @OnClick(R.id.sort_registration_date)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_registration_date.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_registration_date.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortShopAdmin.saveSort(getActivity(), SORT_BY_REGISTRATION_DATE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_by_name)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortShopAdmin.saveSort(getActivity(), SORT_BY_A_TO_Z);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




//    @OnClick(R.id.popularity)
//    void sortByShopCount(View view)
//    {
//        clearSelectionSort();
////        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
////        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
//
////        UtilitySortShopAdmin.saveSort(getActivity(),SORT_BY_POPULARITY);
//
//        if(getActivity() instanceof NotifySort)
//        {
//            ((NotifySort)getActivity()).notifySortChanged();
//        }
//    }







    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortShopAdmin.saveAscending(getActivity(),SORT_ASCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortShopAdmin.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_registration_date.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
//        sort_by_popularity.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_registration_date.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
//        sort_by_popularity.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}
