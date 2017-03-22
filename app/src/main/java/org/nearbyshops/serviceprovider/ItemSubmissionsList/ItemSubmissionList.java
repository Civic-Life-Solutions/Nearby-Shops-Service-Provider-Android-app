package org.nearbyshops.serviceprovider.ItemSubmissionsList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.nearbyshops.serviceprovider.R;

public class ItemSubmissionList extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "tag_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_submission_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ItemReviewFragment(),TAG_FRAGMENT)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
