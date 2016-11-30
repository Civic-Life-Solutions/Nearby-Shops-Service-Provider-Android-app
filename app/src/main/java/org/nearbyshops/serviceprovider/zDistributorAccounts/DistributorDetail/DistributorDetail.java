package org.nearbyshops.serviceprovider.zDistributorAccounts.DistributorDetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.ModelRoles.Distributor;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.DistributorAccountService;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistributorDetail extends AppCompatActivity {


    public static final String DISTRIBUTOR_DETAIL_INTENT_KEY = "distributor_detail_key";

    Distributor distributor;

    @Bind(R.id.date_created)
    TextView dateCreated;

    @Bind(R.id.date_updated)
    TextView dateUpdated;

    @Bind(R.id.about_text)
    TextView aboutText;

    @Bind(R.id.username)
    TextView username;

    @Bind(R.id.distributor_name)
    TextView distributorName;

    @Bind(R.id.profile_image)
    ImageView profileImage;

    @Bind(R.id.button_save)
    TextView buttonSave;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.switch_enabled)
    SwitchCompat switchEnabled;

    @Bind(R.id.switch_waitlisted)
    SwitchCompat switchWaitlisted;


    @Inject
    DistributorAccountService distributorAccountService;


    public DistributorDetail() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        distributor = getIntent().getParcelableExtra(DISTRIBUTOR_DETAIL_INTENT_KEY);

        bindViews();
    }



    void bindViews()
    {
        if(distributor==null)
        {
            return;
        }

        String imagePath = UtilityGeneral.getImageEndpointURL(this)
                + distributor.getProfileImageURL();

        Drawable drawable = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_nature_people_white_48px, getTheme());

        Picasso.with(this).load(imagePath)
                .placeholder(drawable)
                .into(profileImage);

        distributorName.setText(distributor.getDistributorName());
        aboutText.setText(distributor.getAbout());
        dateCreated.setText("Date Created   : " + distributor.getCreated().toLocaleString());


        if(distributor.getUpdated().getTime()==0)
        {
            dateUpdated.setText("Date Updated : Not Available");
        }
        else
        {
            dateUpdated.setText("Date Updated : " + distributor.getUpdated().toLocaleString());
        }


        switchEnabled.setChecked(distributor.isEnabled());
        switchWaitlisted.setChecked(distributor.isWaitlisted());


//        updateSwitchLabels();

        username.setText("Usename : " + distributor.getUsername());
    }


    void updateSwitchLabels_()
    {
        if(switchWaitlisted.isChecked())
        {
            switchWaitlisted.setText("Account waitlisted !");

//            switchWaitlisted.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_2));
        }
        else
        {
            switchWaitlisted.setText("Not Waitlisted !");
//            switchWaitlisted.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray));
        }

        if(switchEnabled.isChecked())
        {
            switchEnabled.setText("Account Enabled !");
//            switchEnabled.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_1));
        }
        else
        {
            switchEnabled.setText("Account Disabled !");
//            switchEnabled.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray));
        }
    }


    @OnCheckedChanged({R.id.switch_enabled,R.id.switch_waitlisted})
    void swtichEnabled()
    {
//        updateSwitchLabels();
    }


    @OnCheckedChanged({R.id.switch_enabled})
    void switchEnabled()
    {
        if(switchEnabled.isChecked())
        {
//            switchWaitlisted.setVisibility(View.INVISIBLE);
        }
        else
        {
//            switchWaitlisted.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }



    @OnClick(R.id.button_save)
    void buttonSaveClick()
    {

        distributor.setEnabled(switchEnabled.isChecked());
        distributor.setWaitlisted(switchWaitlisted.isChecked());

        String headerString = UtilityLogin.baseEncoding(UtilityLogin.getUsername(this),UtilityLogin.getPassword(this));

        Call<ResponseBody> call = distributorAccountService
                .putDistributor(headerString,distributor,distributor.getDistributorID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showSnackbar("Update Successful !");
                }
                else if(response.code()==304)
                {
                    showSnackbar("Not Modified !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showSnackbar("Update Failed. Please check your Internet connection !");

            }
        });


    }



    void showSnackbar(String message)
    {
        Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
    }

}
