package org.nearbyshops.serviceprovider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.serviceURLEditText)
    EditText serviceUrlEditText;

    @Bind(R.id.distributorIDEdittext)
    EditText distributorIDEditText;

    @Bind(R.id.loginButton)
    Button loginButton;

    @Bind(R.id.signUpButton)
    Button signUpButton;



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
        //distributorIDEditText = (EditText) findViewById(R.id.distributorIDEdittext);

        serviceUrlEditText.setText(getServiceURL());
        distributorIDEditText.setText(String.valueOf(getDistributorID()));

        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                UtilityGeneral.saveServiceURL(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                UtilityGeneral.saveServiceURL(s.toString());

            }
        });

        distributorIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(new String(""))) {

                    UtilityGeneral.saveDistributorID(Integer.parseInt(s.toString()));
                }
            }
        });

    }




    public String  getServiceURL()
    {
        // Get a handle to the shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
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
        startActivity(new Intent(this,Home.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
