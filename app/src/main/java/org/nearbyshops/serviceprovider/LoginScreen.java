package org.nearbyshops.serviceprovider;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.serviceprovider.ModelRoles.Admin;
import org.nearbyshops.serviceprovider.ModelRoles.Staff;
import org.nearbyshops.serviceprovider.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.AdminService;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.StaffService;
import org.nearbyshops.serviceprovider.Services.ServicesActivity;
import org.nearbyshops.serviceprovider.ShopApprovals.UtilityLocation;
import org.nearbyshops.serviceprovider.StaffHome.StaffHome;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;
import org.nearbyshops.serviceprovider.Utility.UtilityServiceConfig;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

//    @Bind(R.id.serviceURL) EditText serviceUrlEditText;
    @Bind(R.id.distributorIDEdittext) EditText username;
    @Bind(R.id.loginButton) Button loginButton;
    @Bind(R.id.password) EditText password;
//    @Bind(R.id.signUpButton) Button signUpButton;
    @Bind(R.id.role_admin) TextView roleAdmin;
    @Bind(R.id.role_staff) TextView roleStaff;


//    @Inject AdminService adminService;
//    @Inject StaffService staffService;

    @Inject Gson gson;


    public LoginScreen() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        //loginButton = (Button) findViewById(R.id.loginButton);
        //signUpButton = (Button) findViewById(R.id.signUpButton);

        //loginButton.setOnClickListener(this);



        //serviceUrlEditText = (EditText) findViewById(R.id.serviceURLEditText);
        //username = (EditText) findViewById(R.id.distributorIDEdittext);

//        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(getApplicationContext()));
        username.setText(UtilityLogin.getUsername(this));
        password.setText(UtilityLogin.getPassword(this));
        setRoleButtons();

//
//        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                UtilityGeneral.saveServiceURL(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                UtilityGeneral.saveServiceURL(s.toString());
//
//            }
//        });

        setupServiceURLEditText();
        setStatusLight();

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginButton:

                startActivity(new Intent(this,Home.class));
                break;
        }

    }


    @OnClick(R.id.loginButton)
    public void login()
    {
        int role = UtilityLogin.getRoleID(this);

        UtilityLogin.saveCredentials(this,username.getText().toString(),password.getText().toString());

        if(role == UtilityLogin.ROLE_ADMIN)
        {
            networkCallLoginAdminSimple();
        }
        else if(role == UtilityLogin.ROLE_STAFF)
        {

            networkCallLoginStaff();

        }
        else if(role == -1)
        {
            showSnackBar("Please select a Role !");
        }

    }


    void networkCallLoginStaff()
    {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        setProgressBar(true);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        StaffService staffService = retrofit.create(StaffService.class);

        Call<Staff> staffCall = staffService.getLogin(UtilityLogin.baseEncoding(username,password));

        staffCall.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {

                Staff staff = response.body();

                if(staff !=null)
                {
//                    Gson gson = new Gson();
//                    Log.d("login", gson.toJson(staff));

                    UtilityLogin.saveStaff(staff,LoginScreen.this);
                    startActivity(new Intent(LoginScreen.this,StaffHome.class));
                }

                if(response.code()==403 || response.code() ==401)
                {
                    showSnackBar("Unable to login. Username or password is incorrect !");
                }

                setProgressBar(false);
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {

                Log.d("login",String.valueOf(t.toString()));
                showSnackBar("LoginScreen failed. Please try again !");
                setProgressBar(false);
            }

        });

    }



    void networkCallLoginAdminSimple()
    {

        String username = this.username.getText().toString();
        String password = this.password.getText().toString();


        setProgressBar(true);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        AdminService adminService = retrofit.create(AdminService.class);

        Call<Admin> call = adminService.getAdminSimple(UtilityLogin.baseEncoding(username,password));

        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {

                if(response.code()==200)
                {

                    UtilityLogin.saveAdmin(response.body(),LoginScreen.this);
                    startActivity(new Intent(LoginScreen.this,Home.class));

                }
                else
                {
                    showSnackBar("Failed Code : " + String.valueOf(response.code()));
                }

            setProgressBar(false);


        }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {

                showSnackBar("Failed Network !");

                setProgressBar(false);
            }
        });
    }


//    void networkCallLoginAdmin()
//    {
//
//        String username = this.username.getText().toString();
//        String password = this.password.getText().toString();
//
//
//        setProgressBar(true);
//
//
//        Observable<Admin> call = adminService.getAdmin(UtilityLogin.baseEncoding(username,password));
//
//        /*call.enqueue(new Callback<Admin>() {
//            @Override
//            public void onResponse(Call<Admin> call, Response<Admin> response) {
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Admin> call, Throwable t) {
//
//            }
//        });*/
//
//        call.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Admin>() {
//                    @Override
//                    public void onCompleted() {
//
//                        setProgressBar(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        if(e instanceof HttpException)
//                        {
//                            HttpException response = (HttpException) e;
//                            Log.d("login",String.valueOf(response.code()));
//
//                            if(response.code()==403 || response.code() ==401)
//                            {
//                                showSnackBar("Unable to login. Username or password is incorrect !");
//                            }
//                            else
//                            {
//                                showSnackBar("LoginScreen failed. Please try again !");
//                            }
//
//                        }
//
//                        setProgressBar(false);
//
//                    }
//
//                    @Override
//                    public void onNext(Admin admin) {
//
//                        if(admin !=null)
//                        {
//                            Gson gson = new Gson();
//                            Log.d("login", gson.toJson(admin));
//
//                            UtilityLogin.saveAdmin(admin,LoginScreen.this);
//
//                            startActivity(new Intent(LoginScreen.this,Home.class));
//
//                        }
//
//                        setProgressBar(false);
//
//                    }
//                });
//
//    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    void showSnackBar(String message)
    {
//        Snackbar.make(loginButton,message, Snackbar.LENGTH_SHORT).show();
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }






    @OnClick(R.id.role_admin)
    void selectRoleAdmin()
    {
        clearSelection();
        roleAdmin.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleAdmin.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_ADMIN);
    }


    @OnClick(R.id.role_staff)
    void selectRoleStaff()
    {
        clearSelection();
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_STAFF);
    }



    void clearSelection()
    {
        roleAdmin.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));

        roleAdmin.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
    }




    void setRoleButtons() {

        if (UtilityLogin.getRoleID(this) == UtilityLogin.ROLE_ADMIN)
        {
            selectRoleAdmin();
        }
        else if(UtilityLogin.getRoleID(this)==UtilityLogin.ROLE_STAFF)
        {
            selectRoleStaff();
        }
    }





    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    void setProgressBar(boolean visible)
    {
        progressBar.setIndeterminate(true);

        if(visible)
        {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

    }


    // Setup Status Light

    @Bind(R.id.text_input_service_url) TextInputLayout textInputServiceURL;
    @Bind(R.id.serviceURL) EditText serviceURL;

    UrlValidator urlValidator;

    void setupServiceURLEditText()
    {
        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);

        serviceURL.setText(UtilityGeneral.getServiceURL(this));

        serviceURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (urlValidator.isValid(s.toString())) {
                    UtilityGeneral.saveServiceURL(s.toString());
                    textInputServiceURL.setError(null);
                    textInputServiceURL.setErrorEnabled(false);
                    updateStatusLight();
                }
                else
                {
//                    serviceURL.setError("URL Invalid");
                    textInputServiceURL.setErrorEnabled(true);
                    textInputServiceURL.setError("Invalid URL");

                    UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }

            }
        });


    }


    void updateStatusLight()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(
                UtilityLocation.getLatitude(this),
                UtilityLocation.getLongitude(this)
        );


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {
                        ServiceConfigurationLocal configurationLocal = response.body();
                        UtilityServiceConfig.saveConfiguration(configurationLocal,LoginScreen.this);


                        if(configurationLocal.getRt_distance()<=configurationLocal.getServiceRange())
                        {
                            UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_GREEN);
                            setStatusLight();
                        }
                        else
                        {
                            UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_YELLOW);
                            setStatusLight();
                        }

                    }
                    else
                    {
                        UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_RED);
                        setStatusLight();
                    }
                }
                else
                {
                    UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                UtilityServiceConfig.saveServiceLightStatus(LoginScreen.this,STATUS_LIGHT_RED);
                setStatusLight();

            }
        });

    }


    @Bind(R.id.status_indicator_one) TextView statusLight;
    public static final int STATUS_LIGHT_GREEN = 1;
    public static final int STATUS_LIGHT_YELLOW = 2;
    public static final int STATUS_LIGHT_RED = 3;


    void setStatusLight()
    {
        int status = UtilityServiceConfig.getServiceLightStatus(this);

        if(status == STATUS_LIGHT_GREEN)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_1));
        }
        else if(status == STATUS_LIGHT_YELLOW)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_2));
        }
        else if(status == STATUS_LIGHT_RED)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.deepOrange900));
        }
    }



    @OnClick(R.id.paste_url_button)
    void pasteURLClick()
    {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.getPrimaryClip()!=null)
        {
            serviceURL.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }


    @OnClick(R.id.discover_services_button)
    void discoverServices()
    {
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
    }




}
