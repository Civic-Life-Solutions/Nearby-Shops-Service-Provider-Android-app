package org.nearbyshops.serviceprovider.DistributorAccounts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sumeet on 28/9/16.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private DistributorAccountFragment distributorAccountFragment = new DistributorAccountFragment(0);
    private DistributorAccountFragment distributorAccountFragmentTwo = new DistributorAccountFragment(1);
    private DistributorAccountFragment distributorAccountFragmentThree = new DistributorAccountFragment(2);

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if(position == 0)
        {
            return distributorAccountFragment;
        }
        else if(position == 1)
        {
            return distributorAccountFragmentTwo;
        }
        else if(position == 2)
        {
            return distributorAccountFragmentThree;
        }
        else
        {
            return PlaceholderFragment.newInstance(position + 1);
        }

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return titleDisabled;
            case 1:
                return titleWaitlisted;
            case 2:
                return titleEnabled;
        }
        return null;
    }


    private String titleDisabled = "Disabled (0/0)";
    private String titleWaitlisted = "Waitlisted (0/0)";
    private String titleEnabled = "Enabled (0/0)";

    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleDisabled = title;
        }
        else if (tabPosition == 1)
        {
            titleWaitlisted = title;

        }else if(tabPosition == 2)
        {
            titleEnabled = title;

        }

        notifyDataSetChanged();
    }
}