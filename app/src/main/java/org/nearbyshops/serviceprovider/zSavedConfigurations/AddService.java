package org.nearbyshops.serviceprovider.zSavedConfigurations;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import org.nearbyshops.serviceprovider.ModelSettings.ServiceConfigurationLocal;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.serviceprovider.Utility.ConfigImageCalls;
import org.nearbyshops.serviceprovider.Utility.ImageCropUtility;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddService extends AppCompatActivity implements Callback<Image>, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    @Inject
    ServiceConfigurationService serviceConfigurationService;

    boolean isImageAdded = false;


//    @Bind(R.id.itemName)
//    EditText itemName;
//    @Bind(R.id.itemDescription)
//
//    EditText itemDescription;
//    @Bind(R.id.itemDescriptionLong)
//    EditText itemDescriptionLong;
//    @Bind(R.id.quantityUnit)
//    EditText quantityUnit;
//
//    @Bind(R.id.addItemButton)
//    Button addItemButton;


    @Bind(R.id.nickname)
    EditText nickname;

    @Bind(R.id.service_name)
    EditText service_name;

    @Bind(R.id.service_url)
    EditText service_url;

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


    String[] validWords = new String[]{};


    public AddService() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_service);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            // delete previous file in the cache - This will prevent accidently uploading the previous image
            File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
            file.delete();
        }



        ArrayList<String> spinnerList = new ArrayList<>();
        ArrayList<String> spinnerListLanguages = new ArrayList<>();


        String[] locales = Locale.getISOCountries();



        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            System.out.println("Country Code = " + obj.getCountry()
                    + ", Country Name = " + obj.getDisplayCountry());

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

    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }


    void addNewService(String imagePath) {

        ServiceConfigurationLocal serviceConfigurationForAdd = new ServiceConfigurationLocal();

        serviceConfigurationForAdd.setLogoImagePath(imagePath);
//        serviceConfigurationForAdd.setConfigurationNickname(nickname.getText().toString());

        serviceConfigurationForAdd.setServiceName(service_name.getText().toString());
//        serviceConfigurationForAdd.setServiceURL(service_url.getText().toString());
        serviceConfigurationForAdd.setHelplineNumber(helpline_number.getText().toString());

        serviceConfigurationForAdd.setServiceType(spinnerServiceType.getSelectedItemPosition()+ 1);
        serviceConfigurationForAdd.setServiceLevel(spinnerServiceLevel.getSelectedItemPosition()+ 1);

        serviceConfigurationForAdd.setAddress(address.getText().toString());
        serviceConfigurationForAdd.setCity(city.getText().toString());

        if(!pincode.getText().toString().equals(""))
        {
            serviceConfigurationForAdd.setPincode(Long.parseLong(pincode.getText().toString()));
        }


        serviceConfigurationForAdd.setLandmark(landmark.getText().toString());
        serviceConfigurationForAdd.setState(state.getText().toString());
        serviceConfigurationForAdd.setISOCountryCode(countryCodeList.get(spinnerCountry.getSelectedItemPosition()));

        Locale locale = new Locale("", serviceConfigurationForAdd.getISOCountryCode());
        serviceConfigurationForAdd.setCountry(locale.getDisplayCountry());

        serviceConfigurationForAdd.setISOLanguageCode(autoComplete.getText().toString());

        if(!latitude.getText().toString().equals("")&&!longitude.getText().toString().equals(""))
        {
            serviceConfigurationForAdd.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            serviceConfigurationForAdd.setLonCenter(Double.parseDouble(longitude.getText().toString()));
        }

        if(!serviceConverage.getText().toString().equals(""))
        {
            serviceConfigurationForAdd.setServiceRange(Integer.parseInt(serviceConverage.getText().toString()));
        }



    }



    @OnClick(R.id.addItemButton)
    void addService() {

        if (isImageAdded) {

            ConfigImageCalls.getInstance().uploadPickedImage(this, REQUEST_CODE_READ_EXTERNAL_STORAGE, this);

        } else {

            addNewService(null);
        }

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



    void loadImage(String imagePath) {

        Picasso.with(this).load(UtilityGeneral.getConfigImageEndpointURL(this) + imagePath).into(resultView);

    }



    // code for changing / picking image and saving it in the cache folder


    @OnClick(R.id.removePicture)
    void removeImage() {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        // reset the flag to reflect the status of image addition
        isImageAdded = false;
    }



    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

        resultView.setImageDrawable(null);

        ImageCropUtility.showFileChooser(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if (requestCode == ImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            if (filePath != null) {

                ImageCropUtility.startCropActivity(result.getData(), this);
            }
        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageAdded = true;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }// request crop
    }




    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;

    Image image = null;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                //uploadPickedImage();

                addService();

                break;
        }
    }


    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {

        image = response.body();


        if (image != null) {

            loadImage(image.getPath());

            addNewService(image.getPath());

        } else {

            addNewService(null);

            showToastMessage("Image upload failed !");
        }

    }

    @Override
    public void onFailure(Call<Image> call, Throwable t) {


        showToastMessage("Image upload failed !");

        addNewService(null);
    }


    void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String languageCode = "";
        String countryCode = "";

        switch (parent.getId())
        {

            case R.id.spinner_country:

                Log.d("applog","Selected Country : " + countryCodeList.get(position));

                countryCode = countryCodeList.get(position);

                Log.d("applog", "Index of : " + countryCodeList.indexOf("IN") + "Position : " + position);

                Log.d("applog","Contains : IN : " + countryCodeList.contains("IN"));

//                autoComplete.performValidation();

                break;


            default:
                break;
        }


        Locale locale = new Locale(languageCode,countryCode);


        Currency currency = null;

        try{

            currency = Currency.getInstance(locale);
        }
        catch (Exception ex)
        {


        }



        if(currency!=null)
        {
            Log.d("applog","Currency : " + currency.getSymbol() +  " : " + currency.getDisplayName());

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    class Validator implements AutoCompleteTextView.Validator {

        @Override
        public boolean isValid(CharSequence text) {

            Log.v("Test", "Checking if valid: "+ text);

            Arrays.sort(validWords);
            if (Arrays.binarySearch(validWords, text.toString()) > 0) {
                return true;
            }

            Log.d("applog","Contains test : " + languageCodeList.contains(text.toString()));


            return languageCodeList.contains(text.toString());
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            Log.v("Test", "Returning fixed text");


            /* I'm just returning an empty string here, so the field will be blanked,
             * but you could put any kind of action here, like popping up a dialog?
             *
             * Whatever value you return here must be in the list of valid words.
             */


            return "";
        }
    }




}
