package org.nearbyshops.serviceprovider.zAddItems.ItemCategories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.MyApplication;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.serviceprovider.Utility.ImageCalls;
import org.nearbyshops.serviceprovider.Utility.ImageCropUtility;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddItemCategory extends AppCompatActivity implements Callback<Image>, View.OnClickListener {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.uploadImage) ImageView resultView;

    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;
    Image image = null;
    boolean isImageAdded = false;
    private int PICK_IMAGE_REQUEST = 21;


    @Bind(R.id.itemCategoryName) EditText itemCategoryName;
    @Bind(R.id.itemCategoryDescription) EditText itemCategoryDescription;
    @Bind(R.id.addItemCategory) Button addItemCategory;



    final String IMAGES_END_POINT_URL = "/api/Images";


    public static final String ADD_ITEM_CATEGORY_INTENT_KEY = "add_category_intent_key";


    ItemCategory parentCategory;

    @Bind(R.id.isLeafNode) CheckBox isLeafNode;


    ItemCategory itemCategory = new ItemCategory();


    // recently added

    @Bind(R.id.descriptionShort)
    EditText descriptionShort;

    @Bind(R.id.isAbstractNode)
    CheckBox isAbstractNode;

    boolean isLeafExplainationOpen= false;
    boolean isAbstractExplainationOpen = false;

    TextView what_is_leaf_node;
    TextView leaf_node_explaination;
    TextView what_is_abstract_node;
    ScrollView abstract_node_explaination;






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item_category);
        ButterKnife.bind(this);

        what_is_leaf_node = (TextView) findViewById(R.id.whatleaf);
        leaf_node_explaination = (TextView) findViewById(R.id.leaf_node_explaination);
        what_is_abstract_node = (TextView) findViewById(R.id.what_is_abstract_node);
        abstract_node_explaination = (ScrollView) findViewById(R.id.scrollview_abstract_node_explanation);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parentCategory = getIntent().getParcelableExtra(ADD_ITEM_CATEGORY_INTENT_KEY);

    }




    @OnClick(R.id.whatleaf)
    void whatLeafNodeClick()
    {
        if(!isLeafExplainationOpen)
        {
            leaf_node_explaination.setVisibility(View.VISIBLE);

            isLeafExplainationOpen = true;
        }
        else
        {
            leaf_node_explaination.setVisibility(View.GONE);

            isLeafExplainationOpen = false;
        }
    }

    @OnClick(R.id.what_is_abstract_node)
    void whatAbstractNodeClick() {

        if (!isAbstractExplainationOpen) {

            abstract_node_explaination.setVisibility(View.VISIBLE);

            isAbstractExplainationOpen = true;
        }
        else
        {
            abstract_node_explaination.setVisibility(View.GONE);
            isAbstractExplainationOpen = false;

        }
    }




    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

        }

    }




    void loadImage(String imagePath) {

        Picasso.with(this).load(UtilityGeneral.getServiceURL(null) + IMAGES_END_POINT_URL + imagePath).into(resultView);
    }


    void getDatafromEditText()
    {
        itemCategory.setParentCategoryID(parentCategory.getItemCategoryID());

        itemCategory.setCategoryName(itemCategoryName.getText().toString());
        itemCategory.setCategoryDescription(itemCategoryDescription.getText().toString());
        itemCategory.setIsLeafNode(isLeafNode.isChecked());

        itemCategory.setisAbstractNode(isAbstractNode.isChecked());
        itemCategory.setDescriptionShort(descriptionShort.getText().toString());
    }


    void makeRetrofitRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);

        getDatafromEditText();

        Call<ItemCategory> itemCategoryCall = itemCategoryService.insertItemCategory(UtilityLogin.getAuthorizationHeaders(this),
                itemCategory);

        itemCategoryCall.enqueue(new Callback<ItemCategory>() {

            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                ItemCategory responseCategory = response.body();

//                displayResult(responseCategory);

                if(response.code() == 201)
                {

                    showToastMessage("Added Successfully !");
                }

            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                showToastMessage("Network request failed !");
            }
        });

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


//    void displayResult(ItemCategory itemCategory)
//    {
//        if(itemCategory!=null)
//        {
//            result.setText("Result : " + "\n"
//                    + itemCategory.getItemCategoryID() + "\n"
//                    + itemCategory.getCategoryName() + "\n"
//                    + itemCategory.getCategoryDescription() + "\n"
//                    + itemCategory.getImagePath() + "\n"
//                    + itemCategory.getParentCategoryID() + "\n");
//        }
//    }






    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }





    /*
        Utility Methods
     */




    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();

    }




    /*

        // Code for Changeing / picking image and saving it in the cache folder


     */


    // code for changing / picking image and saving it in the cache folder


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);
        isImageAdded = false;
    }




    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";
    private Uri mDestinationUri;

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


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {

            Uri filePath = result.getData();

            if (filePath != null) {
                ImageCropUtility.startCropActivity(result.getData(),this);
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageAdded = true;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }


    }


    /*

    // Code for Uploading Image

     */



    @OnClick(R.id.addItemCategory)
    public void addItemCategory()
    {

        if(isImageAdded)
        {

            ImageCalls.getInstance()
                    .uploadPickedImage(
                            this,
                            REQUEST_CODE_READ_EXTERNAL_STORAGE,
                            this
                    );

        }
        else
        {

            itemCategory.setImagePath(null);
            makeRetrofitRequest();

        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                addItemCategory();

                break;
        }
    }


    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {

        // image upload successful

        if(response.code()==201)
        {
            itemCategory.setImagePath(response.body().getPath());

        }else
        {
            itemCategory.setImagePath(null);
        }

        makeRetrofitRequest();
    }


    @Override
    public void onFailure(Call<Image> call, Throwable t) {

        itemCategory.setImagePath(null);
        makeRetrofitRequest();
    }


}
