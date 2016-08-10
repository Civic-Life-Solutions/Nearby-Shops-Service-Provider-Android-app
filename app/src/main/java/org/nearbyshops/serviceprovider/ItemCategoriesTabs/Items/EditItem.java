package org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.Image;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemService;
import org.nearbyshops.serviceprovider.Utility.ImageCalls;
import org.nearbyshops.serviceprovider.Utility.ImageCropUtility;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditItem extends AppCompatActivity implements Callback<Image> {


    @Inject
    ItemService itemService;

    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    @Bind(R.id.itemID) EditText itemID;
    @Bind(R.id.itemName) EditText itemName;
    @Bind(R.id.itemDescription) EditText itemDescription;
    @Bind(R.id.itemDescriptionLong) EditText itemDescriptionLong;
    @Bind(R.id.quantityUnit) EditText quantityUnit;

    @Bind(R.id.saveButton) Button buttonUpdateItem;


//    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";
    public static final String ITEM_INTENT_KEY = "itemIntentKey";

    Item itemForEdit;
    //ItemCategory itemCategory;




    public EditItem() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        ButterKnife.bind(this);

        itemForEdit = getIntent().getParcelableExtra(ITEM_INTENT_KEY);
//        itemCategory = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);



        if(itemForEdit!=null) {

            bindDataToEditText();

            loadImage(itemForEdit.getItemImageURL());
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    void loadImage(String imagePath) {

        Picasso.with(this).load(UtilityGeneral.getImageEndpointURL(this) + imagePath).into(resultView);
    }




    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );


            if(isImageRemoved)
            {

                itemForEdit.setItemImageURL("");

                retrofitPUTRequest();

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

            retrofitPUTRequest();
        }
    }




    void bindDataToEditText()
    {
        if(itemForEdit!=null) {

            itemID.setText(String.valueOf(itemForEdit.getItemID()));
            itemName.setText(itemForEdit.getItemName());
            itemDescription.setText(itemForEdit.getItemDescription());

            quantityUnit.setText(itemForEdit.getQuantityUnit());
            itemDescriptionLong.setText(itemForEdit.getItemDescriptionLong());
        }
    }


    void getDataFromEditText(Item item)
    {
        if(item!=null)
        {

            item.setItemName(itemName.getText().toString());
            item.setItemDescription(itemDescription.getText().toString());

            item.setItemDescriptionLong(itemDescriptionLong.getText().toString());
            item.setQuantityUnit(quantityUnit.getText().toString());
        }

    }



    public void retrofitPUTRequest()
    {



        getDataFromEditText(itemForEdit);


        Call<ResponseBody> itemCall = itemService.updateItem(
                                        itemForEdit,
                                        itemForEdit.getItemID());



        itemCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200)
                {
                    Toast.makeText(EditItem.this,"Update Successful !",Toast.LENGTH_SHORT).show();
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


        itemForEdit.setItemImageURL(null);

        if(itemForEdit!=null)
        {
            itemForEdit.setItemImageURL(image.getPath());
        }

        retrofitPUTRequest();
    }





    @Override
    public void onFailure(Call<Image> call, Throwable t) {


        showToastMessage("Image Upload failed !");

        itemForEdit.setItemImageURL("");

        retrofitPUTRequest();

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
