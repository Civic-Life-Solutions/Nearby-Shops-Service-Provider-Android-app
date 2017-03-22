package org.nearbyshops.serviceprovider.ItemSubmissionsMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemSubmissionList;
import org.nearbyshops.serviceprovider.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemSubmissionMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_submission_menu);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }





    @OnClick(R.id.item_submissions)
    void itemSubmissionsList()
    {
        startActivity(new Intent(this, ItemSubmissionList.class));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
