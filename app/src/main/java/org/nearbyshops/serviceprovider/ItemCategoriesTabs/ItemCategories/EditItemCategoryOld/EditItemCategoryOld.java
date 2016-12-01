package org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.EditItemCategoryOld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.serviceprovider.Utility.ImageCalls;
import org.nearbyshops.serviceprovider.Utility.ImageCropUtility;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemCategoryOld extends AppCompatActivity implements Callback<Image> {


    @Inject
    ItemCategoryService itemCategoryService;


    @Bind(R.id.uploadImage)
    ImageView resultView;

    @Bind(R.id.textChangePicture)
    TextView changePicture;

    @Bind(R.id.itemCategoryID) EditText itemCategoryID;
    @Bind(R.id.itemCategoryName) TextInputEditText itemCategoryName;
    @Bind(R.id.itemCategoryDescription) EditText itemCategoryDescription;
    @Bind(R.id.descriptionShort) EditText descriptionShort;
    @Bind(R.id.isAbstractNode) CheckBox isAbstractNode;
    @Bind(R.id.isLeafNode) CheckBox isLeafNode;


    @Bind(R.id.updateItemCategory) Button updateItemCategory;




    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    //public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";
    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";

    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;



    ItemCategory itemCategoryForEdit;



    // recently added


    boolean isLeafExplainationOpen= false;
    boolean isAbstractExplainationOpen = false;

    TextView what_is_leaf_node;
    TextView leaf_node_explaination;
    TextView what_is_abstract_node;
    ScrollView abstract_node_explaination;




    public EditItemCategoryOld() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_category);

        ButterKnife.bind(this);

        itemCategoryForEdit = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);


        what_is_leaf_node = (TextView) findViewById(R.id.whatleaf);
        leaf_node_explaination = (TextView) findViewById(R.id.leaf_node_explaination);
        what_is_abstract_node = (TextView) findViewById(R.id.what_is_abstract_node);
        abstract_node_explaination = (ScrollView) findViewById(R.id.scrollview_abstract_node_explanation);


        if(savedInstanceState==null) {
            // delete previous file in the cache - This will prevent accidently uploading the previous image
            File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
            file.delete();

            //showMessageSnackBar("File delete Status : " + String.valueOf(file.delete()));

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(itemCategoryForEdit!=null) {

            bindDataToEditText();

            loadImage(itemCategoryForEdit.getImagePath());
        }

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




    void loadImage(String imagePath) {

        Picasso.with(this)
                .load(UtilityGeneral.getImageEndpointURL(this) + imagePath)
                .into(resultView);

    }



    void bindDataToEditText()
    {
        if(itemCategoryForEdit!=null) {

            itemCategoryID.setText(String.valueOf(itemCategoryForEdit.getItemCategoryID()));
            itemCategoryName.setText(itemCategoryForEdit.getCategoryName());
            itemCategoryDescription.setText(itemCategoryForEdit.getCategoryDescription());
            descriptionShort.setText(itemCategoryForEdit.getDescriptionShort());

            isLeafNode.setChecked(itemCategoryForEdit.getIsLeafNode());
            isAbstractNode.setChecked(itemCategoryForEdit.getAbstractNode());

        }
    }


    void getDataFromEditText(ItemCategory itemCategory)
    {
        if(itemCategory!=null)
        {

            itemCategory.setCategoryName(itemCategoryName.getText().toString());
            itemCategory.setCategoryDescription(itemCategoryDescription.getText().toString());
            itemCategory.setIsLeafNode(isLeafNode.isChecked());

            itemCategory.setAbstractNode(isAbstractNode.isChecked());
            itemCategory.setDescriptionShort(descriptionShort.getText().toString());
        }

    }



    @OnClick(R.id.updateItemCategory)
    public void updateButtonClick()
    {

        if(itemCategoryForEdit==null)
        {
            return;
        }


        if(isImageChanged)
        {

            // delete previous Image from the Server
            ImageCalls.getInstance()
                    .deleteImage(
                            itemCategoryForEdit.getImagePath(),
                            new DeleteImageCallback()
                    );


            // delete previous image here

            if(isImageRemoved)
            {
                itemCategoryForEdit.setImagePath("");

                updateItemCategory();



            }else
            {

                ImageCalls
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


            updateItemCategory();

        }
    }





    public void updateItemCategory()
    {



//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UtilityGeneral.getServiceURL(this))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);




        getDataFromEditText(itemCategoryForEdit);


        Call<ResponseBody> itemCategoryCall = itemCategoryService
                                                    .updateItemCategory(UtilityLogin.getAuthorizationHeaders(this),
                                                            itemCategoryForEdit,
                                                            itemCategoryForEdit.getItemCategoryID());


        itemCategoryCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.code() == 200)
                {
                    Toast.makeText(EditItemCategoryOld.this,"Update Successful !",Toast.LENGTH_SHORT).show();
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

        ImageCropUtility.showFileChooser(this);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if (requestCode == ImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {
                ImageCropUtility.startCropActivity(result.getData(),this);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                //uploadPickedImage();

                updateButtonClick();

                break;
        }
    }



    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {

        Image image = null;

        image = response.body();

        Log.d("applog", "inside retrofit call !" + String.valueOf(response.code()));
        Log.d("applog", "image Path : " + image.getPath());


        //// TODO: 31/3/16
        // check whether load image call is required. or Not

        loadImage(image.getPath());


        if (response.code() != 201) {

            showToastMessage("Unable to upload image");
        }

        itemCategoryForEdit.setImagePath(null);

        if(image!=null)
        {
            itemCategoryForEdit.setImagePath(image.getPath());
        }

        updateItemCategory();

    }

    @Override
    public void onFailure(Call<Image> call, Throwable t) {

        showToastMessage("Image Upload failed !");

        itemCategoryForEdit.setImagePath("");

        updateItemCategory();

    }




    private class DeleteImageCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.code()==200)
            {
                showToastMessage("Previous Image Removed !");
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            showToastMessage("Image remove failed !");

        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
