package org.nearbyshops.serviceprovider.ItemCategoriesTabs;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemCategoriesTabs extends AppCompatActivity implements FragmentsNotificationReceiver, NotifyPagerAdapter {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ReceiveNotificationFromTabsForItemCat notificationReceiver;
    private ReceiveNotificationFromTabsForItems tabsNotificationReceiver;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.tablayoutPager)
    TabLayout tabLayoutPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_tabs);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayoutPager.setupWithViewPager(mViewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_categories_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        mSectionsPagerAdapter.setTitle(title,tabPosition);
    }






    @Override
    public void onBackPressed() {



        if(notificationReceiver!=null)
        {
            if(notificationReceiver.backPressed())
            {
                super.onBackPressed();
            }else
            {
                mViewPager.setCurrentItem(0,true);
            }
        }
        else
        {
            super.onBackPressed();



        }
    }








    public ReceiveNotificationFromTabsForItemCat getNotificationReceiver() {
        return notificationReceiver;
    }

    public void setNotificationReceiver(ReceiveNotificationFromTabsForItemCat notificationReceiver) {
        this.notificationReceiver = notificationReceiver;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }





    @Bind(R.id.appbar)
    AppBarLayout appBar;


    @Override
    public void itemCategoryChanged(ItemCategory currentCategory) {

        Log.d("applog","Item Category Changed : " + currentCategory.getCategoryName() + " : " + String.valueOf(currentCategory.getItemCategoryID()));


        if(tabsNotificationReceiver!=null)
        {
            tabsNotificationReceiver.itemCategoryChanged(currentCategory);
        }
    }


    @Override
    public void showAppBar() {

        appBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAppBar() {


        appBar.setVisibility(View.GONE);
    }

    @Override
    public void insertTab(String categoryName) {

        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText("" + categoryName + " : : "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);
    }

    @Override
    public void removeLastTab() {


        tabLayout.removeTabAt(tabLayout.getTabCount()-1);
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

        if(tabLayout.getTabCount()==0)
        {
            tabLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void notifySwipeToright() {

        mViewPager.setCurrentItem(1);
    }



    public interface ReceiveNotificationFromTabsForItems {

        void itemCategoryChanged(ItemCategory currentCategory);
    }



    public interface ReceiveNotificationFromTabsForItemCat {

        boolean backPressed();
    }



    public ReceiveNotificationFromTabsForItems getTabsNotificationReceiver() {
        return tabsNotificationReceiver;
    }

    public void setTabsNotificationReceiver(ReceiveNotificationFromTabsForItems tabsNotificationReceiver) {
        this.tabsNotificationReceiver = tabsNotificationReceiver;
    }
}
