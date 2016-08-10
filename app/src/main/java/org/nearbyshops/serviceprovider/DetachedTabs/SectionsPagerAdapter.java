package org.nearbyshops.serviceprovider.DetachedTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.serviceprovider.DetachedTabs.ItemCategories.DetachedItemCatFragment;
import org.nearbyshops.serviceprovider.DetachedTabs.Items.DetachedItemFragment;

/**
 * Created by sumeet on 27/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

//    DetachedTabs activity;

    public SectionsPagerAdapter(FragmentManager fm, DetachedTabs activity) {
        super(fm);

//        this.activity = activity;
    }


    DetachedItemCatFragment detachedItemCatFragment;

    DetachedItemFragment detachedItemFragment;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).



        if(position == 0)
        {
            detachedItemCatFragment = new DetachedItemCatFragment();

//            activity.setNotificationReceiver(detachedItemCatFragment);

            return detachedItemCatFragment;
        }
        else if (position == 1)
        {

            detachedItemFragment = new DetachedItemFragment();

            return detachedItemFragment;
        }


        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleDetachedItemCategories;
            case 1:
                return titleDetachedItems;
            case 2:
                return titleDetachedItemCategories;
            case 3:
                return titleDetachedItems;
        }
        return null;
    }



    String titleCategories = "Sub-Categories (0)";
    String titleItems = "Items (0)";
    String titleDetachedItemCategories = "Detached Item-Categories (0/0)";
    String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){


            titleDetachedItemCategories = title;

        }
        else if (tabPosition == 1)
        {
            titleDetachedItems = title;


        }else if(tabPosition == 2)
        {
            titleCategories = title;


        }else if(tabPosition == 3)
        {
            titleItems = title;

        }


        notifyDataSetChanged();
    }




}