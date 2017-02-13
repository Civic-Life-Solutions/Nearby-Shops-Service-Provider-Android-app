package org.nearbyshops.serviceprovider.ServiceConfiguration;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;


import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.serviceprovider.Utility.ConfigImageCalls;
import org.nearbyshops.serviceprovider.Utility.ConfigImageCropUtility;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditServiceConfiguration extends AppCompatActivity implements Callback<Image>, AdapterView.OnItemSelectedListener {


    @Inject
    ServiceConfigurationService configurationService;

    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;



//    @Bind(R.id.nickname)
//    EditText nickname;


//    @Bind(R.id.service_url)
//    EditText service_url;


    @Bind(R.id.service_name)
    EditText service_name;

    @Bind(R.id.helpline_number)
    EditText helpline_number;

    @Bind(R.id.address)
    EditText address;

    @Bind(R.id.city)
    EditText city;

    @Bind(R.id.pincode)
    EditText pincode;

    @Bind(R.id.landmark)
    EditText landmark;

    @Bind(R.id.state)
    EditText state;

    @Bind(R.id.auto_complete_language)
    AutoCompleteTextView autoComplete;

    @Bind(R.id.latitude)
    EditText latitude;

    @Bind(R.id.longitude)
    EditText longitude;

    @Bind(R.id.service_coverage)
    EditText serviceConverage;

    @Bind(R.id.getlatlon)
    Button getlatlon;

    @Bind(R.id.spinner_country)
    Spinner spinnerCountry;

    @Bind(R.id.spinner_service_level)
    Spinner spinnerServiceLevel;

    @Bind(R.id.spinner_service_type)
    Spinner spinnerServiceType;




    ArrayList<String> countryCodeList = new ArrayList<>();
    ArrayList<String> languageCodeList = new ArrayList<>();



    @Bind(R.id.saveButton)
    Button buttonUpdateItem;


    public static final String SERVICE_CONFIG_INTENT_KEY = "ServiceConfigIntentKey";

    ServiceConfigurationLocal serviceConfigurationForEdit;


    public EditServiceConfiguration() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        ButterKnife.bind(this);

        serviceConfigurationForEdit = getIntent().getParcelableExtra(SERVICE_CONFIG_INTENT_KEY);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // setup spinners

        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<String> spinnerListLanguages = new ArrayList<>();


        String[] locales = Locale.getISOCountries();



        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

//            System.out.println("Country Code = " + obj.getCountry()
//                    + ", Country Name = " + obj.getDisplayCountry());

            spinnerList.add(obj.getCountry() + " : " + obj.getDisplayCountry());

            countryCodeList.add(obj.getCountry());
        }


        for(String string: Locale.getISOLanguages())
        {
            Locale locale = new Locale(string,"");

            spinnerListLanguages.add(locale.getDisplayLanguage());

            languageCodeList.add(locale.getDisplayLanguage());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, spinnerList);

        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setOnItemSelectedListener(this);

        //spinnerCountry.setSelection(countryCodeList.indexOf("IN"));



        ArrayAdapter<String> adapterLanguages = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinnerListLanguages);

        autoComplete.setAdapter(adapterLanguages);
//        autoComplete.setValidator(new Validator());



        // setup spinner ends




        getConfigurationNetworkCall();


        /*if(serviceConfigurationForEdit !=null) {
            bindDataToEditText();
            loadImage(serviceConfigurationForEdit.getImagePath());
        }*/

    }





    void getConfigurationNetworkCall()
    {
        final Call<ServiceConfigurationLocal> serviceConfiguration = configurationService.getServiceConfiguration(null,null);

        serviceConfiguration.enqueue(new Callback<ServiceConfigurationLocal>() {

            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(response.body()!=null)
                {
                    serviceConfigurationForEdit = response.body();
                    bindDataToEditText();
                    loadImage(serviceConfigurationForEdit.getLogoImagePath());
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {
                showToastMessage(getString(R.string.NetworkConnectionError));
            }
        });


    }








    void loadImage(String imagePath) {

        Picasso.with(this).load(UtilityGeneral.getConfigImageEndpointURL(this) + imagePath).into(resultView);
    }




    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            ConfigImageCalls.getInstance()
                    .deleteImage(
                            serviceConfigurationForEdit.getLogoImagePath(),
                            new DeleteImageCallback()
                    );


            if(isImageRemoved)
            {

                serviceConfigurationForEdit.setLogoImagePath("");

                retrofitPUTRequest();

            }else
            {



                ConfigImageCalls
                        .getInstance()
                        .uploadPickedImage(
                                this,
                                REQUEST_CODE_READ_EXTERNAL_STORAGE,
                                this
                        );

            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }




    void bindDataToEditText()
    {
        if(serviceConfigurationForEdit !=null) {

//            nickname.setText(serviceConfigurationForEdit.getConfigurationNickname());
            service_name.setText(serviceConfigurationForEdit.getServiceName());
//            service_url.setText(serviceConfigurationForEdit.getServiceURL());
            helpline_number.setText(serviceConfigurationForEdit.getHelplineNumber());
            spinnerServiceType.setSelection(serviceConfigurationForEdit.getServiceType()-1);
            spinnerServiceLevel.setSelection(serviceConfigurationForEdit.getServiceLevel()-1);
            address.setText(serviceConfigurationForEdit.getAddress());
            city.setText(serviceConfigurationForEdit.getCity());
            pincode.setText(String.valueOf(serviceConfigurationForEdit.getPincode()));
            landmark.setText(serviceConfigurationForEdit.getLandmark());
            state.setText(serviceConfigurationForEdit.getState());
            spinnerCountry.setSelection(countryCodeList.indexOf(serviceConfigurationForEdit.getISOCountryCode()));
            autoComplete.setText(serviceConfigurationForEdit.getISOLanguageCode());
            latitude.setText(String.valueOf(serviceConfigurationForEdit.getLatCenter()));
            longitude.setText(String.valueOf(serviceConfigurationForEdit.getLonCenter()));
            serviceConverage.setText(String.valueOf(serviceConfigurationForEdit.getServiceRange()));

        }
    }


    void getDataFromEditText(ServiceConfigurationLocal serviceConfiguration)
    {
        if(serviceConfiguration !=null)
        {

//            serviceConfigurationForEdit.setConfigurationNickname(nickname.getText().toString());
            serviceConfigurationForEdit.setServiceName(service_name.getText().toString());
//            serviceConfigurationForEdit.setServiceURL(service_url.getText().toString());
            serviceConfigurationForEdit.setHelplineNumber(helpline_number.getText().toString());
            serviceConfigurationForEdit.setServiceType(spinnerServiceType.getSelectedItemPosition() + 1);
            serviceConfigurationForEdit.setServiceLevel(spinnerServiceLevel.getSelectedItemPosition() + 1);
            serviceConfigurationForEdit.setAddress(address.getText().toString());
            serviceConfigurationForEdit.setCity(city.getText().toString());

            if(!pincode.getText().toString().equals(""))
            {
                serviceConfigurationForEdit.setPincode(Long.parseLong(pincode.getText().toString()));
            }


            serviceConfigurationForEdit.setLandmark(landmark.getText().toString());
            serviceConfigurationForEdit.setState(state.getText().toString());
            serviceConfigurationForEdit.setISOCountryCode(countryCodeList.get(spinnerCountry.getSelectedItemPosition()));

            Locale locale = new Locale("", serviceConfigurationForEdit.getISOCountryCode());
            serviceConfigurationForEdit.setCountry(locale.getDisplayCountry());

            serviceConfigurationForEdit.setISOLanguageCode(autoComplete.getText().toString());

            if(!latitude.getText().toString().equals("")&&!longitude.getText().toString().equals(""))
            {
                serviceConfigurationForEdit.setLatCenter(Double.parseDouble(latitude.getText().toString()));
                serviceConfigurationForEdit.setLonCenter(Double.parseDouble(longitude.getText().toString()));
            }

            if(!serviceConverage.getText().toString().equals(""))
            {
                serviceConfigurationForEdit.setServiceRange(Integer.parseInt(serviceConverage.getText().toString()));
            }

        }

    }



    public void retrofitPUTRequest()
    {



        getDataFromEditText(serviceConfigurationForEdit);


        Call<ResponseBody> itemCall = configurationService.putServiceConfiguration(
                UtilityLogin.getAuthorizationHeaders(this)
                ,serviceConfigurationForEdit);

        itemCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200)
                {
                    Toast.makeText(EditServiceConfiguration.this,"Update Successful !",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");

            }
        });
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    /*
        Utility Methods
     */


    @Bind(R.id.uploadImage)
    ImageView resultView;



    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

        ConfigImageCropUtility.showFileChooser(this);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);



        if (requestCode == ConfigImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {
                ConfigImageCropUtility.startCropActivity(result.getData(),this);
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }


    }





    /*

    // Code for Uploading Image

     */



    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                UpdateButtonClick();

                break;
        }
    }

    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {


        Image image = null;


        image = response.body();


        //// TODO: 31/3/16
        // check whether load image call is required. or Not

        loadImage(image.getPath());


        if (response.code() != 201) {

                showToastMessage("Image Upload error at the server !");

        }


        serviceConfigurationForEdit.setLogoImagePath(null);

        if(serviceConfigurationForEdit !=null)
        {
            serviceConfigurationForEdit.setLogoImagePath(image.getPath());
        }

        retrofitPUTRequest();
    }





    @Override
    public void onFailure(Call<Image> call, Throwable t) {


        showToastMessage("Image Upload failed !");

        serviceConfigurationForEdit.setLogoImagePath("");

        retrofitPUTRequest();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class DeleteImageCallback implements Callback<ResponseBody> {


        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.code()==200)
            {
                showToastMessage("Previous Image removed !");
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            showToastMessage("Image remove failed !");

        }
    }
}
