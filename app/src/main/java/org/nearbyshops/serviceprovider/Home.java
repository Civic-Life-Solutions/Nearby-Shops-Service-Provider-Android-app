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
import android.widget.Toast;

import org.nearbyshops.serviceprovider.DetachedTabs.DetachedTabs;
import org.nearbyshops.serviceprovider.EditProfileAdmin.EditAdminFragment;
import org.nearbyshops.serviceprovider.EditProfileAdmin.EditProfileAdmin;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategoriesTabs;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.ItemCategoriesSimple;
import org.nearbyshops.serviceprovider.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditConfiguration.EditConfiguration;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditConfiguration.EditConfigurationFragment;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditConfiguration.UtilityServiceConfiguration;
import org.nearbyshops.serviceprovider.Settings.SettingsActivity;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.ShopAdminApprovals;
import org.nearbyshops.serviceprovider.ShopApprovals.ShopApprovals;
import org.nearbyshops.serviceprovider.StaffAccounts.StaffAccounts;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

//    DrawerLayout drawer;
//    NavigationView navigationView;

//    @Bind(R.id.option_saved_configuration)
//    RelativeLayout optionSavedConfiguration;

    @Inject
    ServiceConfigurationService configurationService;



    public Home() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_home);
        setContentView(R.layout.app_bar_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

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



//    @OnClick(R.id.option_service_stats)
//    void optionServiceStatsClick()
//    {
//        startActivity(new Intent(this,MapsActivity.class));
//    }


    @OnClick(R.id.detached_items)
    void optionDetachedClick()
    {
        startActivity(new Intent(this, DetachedTabs.class));
    }


    @OnClick(R.id.items_database)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(this, ItemCategoriesSimple.class));
    }





    @Override
    public void onBackPressed() {

        super.onBackPressed();

//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }






    @OnClick(R.id.service_configuration)
    void savedConfigurationClick(View view)
    {


        Call<ServiceConfigurationLocal> call = configurationService.getServiceConfiguration(null,null);

        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(response.code()==200 && response.body()!=null)
                {
                    UtilityServiceConfiguration.saveConfiguration(response.body(),Home.this);

                    Intent intent = new Intent(Home.this, EditConfiguration.class);
                    intent.putExtra(EditConfigurationFragment.EDIT_MODE_INTENT_KEY,EditConfigurationFragment.MODE_UPDATE);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {
                showToastMessage("Failed !");
            }
        });
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




    @OnClick(R.id.settings)
    void settingsClick(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }



    @OnClick(R.id.end_user_approvals)
    void itemCategoriesClick(View view)
    {
        Intent intent = new Intent(this, ItemCategoriesTabs.class);
        startActivity(intent);
    }




    @OnClick(R.id.shop_approvals)
    void optionAdminClick(View view)
    {
        Intent intent = new Intent(this, ShopApprovals.class);
        startActivity(intent);
    }


    @OnClick(R.id.staff_accounts)
    void optionStaffClick(View view)
    {
        Intent intent = new Intent(this, StaffAccounts.class);
        startActivity(intent);
    }



    @OnClick(R.id.shop_admin_approvals)
    void distributorAccountClick(View view)
    {
//        Intent intent = new Intent(this, DistributorAccountsActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(this, ShopAdminApprovals.class);
        startActivity(intent);
    }


    @OnClick(R.id.edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(this, EditProfileAdmin.class);
        intent.putExtra(EditAdminFragment.EDIT_MODE_INTENT_KEY,EditAdminFragment.MODE_UPDATE);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


//    @Bind(R.id.row_three)
//    LinearLayout rowSettins;

//    @Bind(R.id.row_eight)
//    LinearLayout rowAccounts;


//    void setVisibilityByRole() {
//
//        if (UtilityLogin.getRoleID(this) == UtilityLogin.ROLE_STAFF)
//        {
//            rowAccounts.setVisibility(View.GONE);
//            rowSettins.setVisibility(View.GONE);
//
//        }
//        else if(UtilityLogin.getRoleID(this)== UtilityLogin.ROLE_ADMIN)
//        {
//            rowAccounts.setVisibility(View.VISIBLE);
//            rowSettins.setVisibility(View.VISIBLE);
//        }
//    }


}
