package org.nearbyshops.serviceprovider.Settings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 26/9/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {


//    public final String FRAGMENT_TAG_ACCOUNT_SETTINGS = "account_settings";



    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
    private APIKeysSettingsFragment apiKeysSettingsFragment = new APIKeysSettingsFragment();

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentShopAdmins (defined as a static inner class below).

        if(position == 0)
        {

            return accountSettingsFragment;
        }
        else if(position == 1)
        {
            return apiKeysSettingsFragment;
        }
        else
        {
            return PlaceholderFragment.newInstance(position + 1);
        }
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
                return "User Account";
            case 1:
                return "API Keys";
            case 2:
                return "LoginScreen API Keys";
        }
        return null;
    }
}
