package org.nearbyshops.serviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.nearbyshops.serviceprovider.DetachedTabs.DetachedTabs;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategoriesTabs;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.ItemCategoriesSimple;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditServiceConfiguration;
import org.nearbyshops.serviceprovider.Settings.SettingsActivity;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.ShopAdminApprovals;
import org.nearbyshops.serviceprovider.ShopApprovals.ShopApprovals;
import org.nearbyshops.serviceprovider.StaffAccounts.StaffAccounts;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;

    @Bind(R.id.option_saved_configuration)
    RelativeLayout optionSavedConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setVisibilityByRole();
    }



//    void setupFab()
//    {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }



    @OnClick(R.id.option_service_stats)
    void optionServiceStatsClick()
    {
        startActivity(new Intent(this,MapsActivity.class));
    }


    @OnClick(R.id.option_detached)
    void optionDetachedClick()
    {
        startActivity(new Intent(this, DetachedTabs.class));
    }


    @OnClick(R.id.option_item_category_approval)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(this, ItemCategoriesSimple.class));
    }



    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @OnClick(R.id.option_saved_configuration)
    void savedConfigurationClick(View view)
    {
        Intent intent = new Intent(this, EditServiceConfiguration.class);
        startActivity(intent);
    }


    @OnClick(R.id.option_settings)
    void settingsClick(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



    @OnClick(R.id.option_add_items)
    void itemCategoriesClick(View view)
    {
        Intent intent = new Intent(this, ItemCategoriesTabs.class);
        startActivity(intent);
    }


    @OnClick(R.id.option_admin_accounts)
    void optionAdminClick(View view)
    {
        Intent intent = new Intent(this, ShopApprovals.class);
        startActivity(intent);
    }


    @OnClick(R.id.option_staff_accounts)
    void optionStaffClick(View view)
    {
        Intent intent = new Intent(this, StaffAccounts.class);
        startActivity(intent);
    }



    @OnClick(R.id.option_shop_approvals)
    void distributorAccountClick(View view)
    {
//        Intent intent = new Intent(this, DistributorAccountsActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, ShopAdminApprovals.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @Bind(R.id.row_three)
    LinearLayout rowSettins;

    @Bind(R.id.row_eight)
    LinearLayout rowAccounts;


    void setVisibilityByRole() {

        if (UtilityLogin.getRoleID(this) == UtilityLogin.ROLE_STAFF)
        {
            rowAccounts.setVisibility(View.GONE);
            rowSettins.setVisibility(View.GONE);

        }
        else if(UtilityLogin.getRoleID(this)== UtilityLogin.ROLE_ADMIN)
        {
            rowAccounts.setVisibility(View.VISIBLE);
            rowSettins.setVisibility(View.VISIBLE);
        }
    }


}
