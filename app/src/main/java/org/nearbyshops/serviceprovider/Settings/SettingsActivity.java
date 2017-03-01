package org.nearbyshops.serviceprovider.Settings;

import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.ModelSettings.Settings;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.SettingsService;
import org.nearbyshops.serviceprovider.Settings.Interface.UpdateSettings;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.nearbyshops.serviceprovider.R.id.constraintLayout;

public class SettingsActivity extends AppCompatActivity implements UpdateSettings {


    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Inject
    SettingsService settingsService;

//    @State
    Settings settings;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.button_save)
    TextView buttonSave;


    public SettingsActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fetchSettingsNetworkCall();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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


    void showSnackBarMessage(String message)
    {
        Snackbar.make(buttonSave, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    public Settings getSettings() {

        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }







    void fetchSettingsNetworkCall()
    {
        Call<Settings> call = settingsService.getSetting();

        call.enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {

                if(response.body()!=null)
                {
                    settings = response.body();

                    ((AccountSettingsFragment)mPagerAdapter.getItem(0)).settingsFetched();
                    ((APIKeysSettingsFragment)mPagerAdapter.getItem(1)).settingsFetched();
                }


            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {

                showSnackBarMessage("Network Error. Check Connection !");

            }
        });
    }





    void updateSettingsNetworkCall()
    {
        Call<ResponseBody> call = settingsService.updateSettings(settings);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showSnackBarMessage("Updated !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBarMessage("Network Error. Check Connection !");
            }
        });

    }



    @OnClick(R.id.button_save)
    void buttonSaveClick()
    {
        if(mViewPager.getCurrentItem()==0)
        {
            ((AccountSettingsFragment)mPagerAdapter.getItem(0)).getDataFromViews();
        }
        else if(mViewPager.getCurrentItem()==1)
        {
            ((APIKeysSettingsFragment)mPagerAdapter.getItem(1)).getDataFromViews();
        }

        updateSettingsNetworkCall();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public void notifyUpdateSettings() {

        updateSettingsNetworkCall();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Icepick.restoreInstanceState(this,savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);
    }
}
