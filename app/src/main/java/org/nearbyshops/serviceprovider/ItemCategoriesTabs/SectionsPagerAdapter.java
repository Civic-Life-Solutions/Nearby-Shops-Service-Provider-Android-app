package org.nearbyshops.serviceprovider.ItemCategoriesTabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemRemakeFragment;

/**
 * Created by sumeet on 27/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

//    DetachedTabs activity;

    public SectionsPagerAdapter(FragmentManager fm, ItemCategoriesTabs activity) {
        super(fm);

//        this.activity = activity;
    }


    ItemCategoriesFragment itemCategoriesFragment;

    ItemRemakeFragment itemRemakeFragment;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).



        if(position == 0)
        {
            itemCategoriesFragment = new ItemCategoriesFragment();

//            activity.setNotificationReceiver(itemCategoriesFragment);

            return itemCategoriesFragment;
        }
        else if (position == 1)
        {

            itemRemakeFragment = new ItemRemakeFragment();

            return itemRemakeFragment;
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
                return titleCategories;
            case 1:
                return titleItems;
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

            titleCategories = title;
        }
        else if (tabPosition == 1)
        {

            titleItems = title;
        }else if(tabPosition == 2)
        {
            titleDetachedItemCategories = title;

        }else if(tabPosition == 3)
        {
            titleDetachedItems = title;
        }


        notifyDataSetChanged();
    }




}