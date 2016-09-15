package org.nearbyshops.serviceprovider.ItemCategoriesTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nearbyshops.serviceprovider.R;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_layer,container,false);
        return view;
    }
}
