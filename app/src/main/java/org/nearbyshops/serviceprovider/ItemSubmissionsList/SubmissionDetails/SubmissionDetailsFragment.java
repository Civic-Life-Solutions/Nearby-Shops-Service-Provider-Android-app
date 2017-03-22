package org.nearbyshops.serviceprovider.ItemSubmissionsList.SubmissionDetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemSubmission;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContractItem.ItemSubmissionService;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmissionDetailsFragment extends Fragment {


    @Inject
    ItemSubmissionService itemSubmissionService;


    @Bind(R.id.item_image) ImageView itemImage;
    @Bind(R.id.copyright_info) TextView copyrightInfo;
    @Bind(R.id.item_name) TextView itemName;
    @Bind(R.id.item_description_short) TextView itemDescriptionShort;
    @Bind(R.id.item_unit) TextView itemUnit;
    @Bind(R.id.list_price) TextView itemPrice;
    @Bind(R.id.item_description_long) TextView itemDescriptionLong;
    @Bind(R.id.barcode_results) TextView barcode;

    @Bind(R.id.accept) TextView accept;
    @Bind(R.id.reject) TextView reject;

    ItemSubmission itemSubmission;
    Item item;

    public static final String ITEM_INTENT_KEY = "intent_key";
    public static final String IS_UPDATE_INTENT_KEY = "is_update_intent_key";
    public static final String IS_CURRENT_INTENT_KEY = "is_current_intent_key";



    public SubmissionDetailsFragment() {
        // Required empty public constructor

        DaggerComponentBuilder.getInstance().getNetComponent()
                .Inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this,rootView);

        String itemJson = getActivity().getIntent().getStringExtra(ITEM_INTENT_KEY);

        Gson gson = new Gson();


        if(getActivity().getIntent().getBooleanExtra(IS_CURRENT_INTENT_KEY,false))
        {
            item = gson.fromJson(itemJson,Item.class);
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);

        }
        else
        {
            itemSubmission = gson.fromJson(itemJson, ItemSubmission.class);
            item = itemSubmission.getItem();

        }

        bindViews();


        return rootView;
    }





    void bindViews()
    {
//        if(itemSubmission!=null && itemSubmission.getItem()!=null) {

        if(item==null)
        {
            return;
        }


            String iamgepath = UtilityGeneral.getServiceURL(getContext()) + "/api/v1/Item/Image/" + item.getItemImageURL();

            Picasso.with(getContext())
                    .load(iamgepath)
                    .into(itemImage);


            copyrightInfo.setText(item.getImageCopyrights());
            itemName.setText(item.getItemName());
            itemDescriptionShort.setText(item.getItemDescription());
            itemDescriptionLong.setText(item.getItemDescriptionLong());
            itemUnit.setText("Item Unit : " + item.getQuantityUnit());
            itemPrice.setText("Price : " + String.valueOf(item.getListPrice()));
            barcode.setText("Barcode : " + item.getBarcode() + "\nBarcode Format : " + item.getBarcodeFormat());

//        barcode.setText(item.getTimestampUpdated().toGMTString());

//        }

    }




    @OnClick(R.id.accept)
    void acceptSubmission()
    {


        boolean isUpdate = getActivity().getIntent().getBooleanExtra(IS_UPDATE_INTENT_KEY,false);

        if(isUpdate)
        {
            makeNetworkCallUpdate();

        }
        else
        {
            makeNetworkCallApproveInsert();
        }
    }





    void makeNetworkCallApproveInsert()
    {
        Call<ResponseBody> call = itemSubmissionService.approveInsert(UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemSubmission.getItemSubmissionID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Successful");
                }
                else
                {
                    showToastMessage("Failed code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Connection failure !");
            }
        });
    }

    void makeNetworkCallUpdate()
    {
        Call<ResponseBody> call = itemSubmissionService.approveUpdate(UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemSubmission.getItemSubmissionID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    showToastMessage("Successful");
                }
                else
                {
                    showToastMessage("Failed code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Connection failure !");
            }
        });

    }




    @OnClick(R.id.reject)
    void rejectSubmission()
    {

        Call<ResponseBody> call = itemSubmissionService.rejectSubmission(UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemSubmission.getItemSubmissionID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code()==200)
                {
                    showToastMessage("Successful");
                }
                else
                {
                    showToastMessage("Failed code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Connection failure !");
            }
        });

    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
