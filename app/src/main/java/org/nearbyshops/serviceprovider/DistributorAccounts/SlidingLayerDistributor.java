package org.nearbyshops.serviceprovider.DistributorAccounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.serviceprovider.DistributorAccounts.Interfaces.NotifySort;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilitySortDistributor;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerDistributor extends Fragment{

    @Bind(R.id.sort_by_name)
    TextView sort_by_name;

    @Bind(R.id.sort_by_username)
    TextView sort_by_username;

    @Bind(R.id.sort_by_created)
    TextView sort_by_created;

    @Bind(R.id.sort_by_updated)
    TextView sort_by_updated;

    @Bind(R.id.sort_ascending)
    TextView sort_ascending;

    @Bind(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_CREATED;
    String currentAscending = SORT_ASCENDING;

    int colorSelected = R.color.colorPrimary;
    int colorSelectedAscending = R.color.colorAccent;


    public static String SORT_BY_NAME = "DISTRIBUTOR_NAME";
    public static String SORT_BY_USERNAME = "USERNAME";
    public static String SORT_BY_CREATED = "CREATED";
    public static String SORT_BY_UPDATED = "UPDATED";

    public static String SORT_DESCENDING = "DESC";
    public static String SORT_ASCENDING = "ASC";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_distributor,container,false);
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
//        String[] sort_options = UtilitySortDistributor.getSort(getActivity());

        currentSort = UtilitySortDistributor.getSort(getActivity());
        currentAscending = UtilitySortDistributor.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_NAME))
        {
            sort_by_name.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_USERNAME))
        {
            sort_by_username.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_username.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if (currentSort.equals(SORT_BY_CREATED))
        {
            sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_UPDATED))
        {
            sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }


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



    @OnClick(R.id.sort_by_name)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortDistributor.saveSort(getActivity(),SORT_BY_NAME);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.sort_by_username)
    void sortByUsername(View view)
    {
        clearSelectionSort();
        sort_by_username.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_username.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortDistributor.saveSort(getActivity(),SORT_BY_USERNAME);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.sort_by_created)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortDistributor.saveSort(getActivity(),SORT_BY_CREATED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.sort_by_updated)
    void sortByUpdated(View view)
    {
        clearSelectionSort();
        sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortDistributor.saveSort(getActivity(),SORT_BY_UPDATED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortDistributor.saveAscending(getActivity(),SORT_ASCENDING);

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


        UtilitySortDistributor.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_username.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_username.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}
