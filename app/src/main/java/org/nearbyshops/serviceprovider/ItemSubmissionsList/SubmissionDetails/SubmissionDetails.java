package org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nearbyshops.serviceprovider.R;

public class SubmissionDetails extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "tag_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SubmissionDetailsFragment())
                    .commit();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
