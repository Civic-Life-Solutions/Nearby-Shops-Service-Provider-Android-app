package org.nearbyshops.serviceprovider.ShopApprovals.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.Model.Shop;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Shop> dataset = null;
    private NotifyByShopAdapter notifyByShopAdapter;
    private Context context;


    public static final int VIEW_TYPE_SHOP_APPROVAL = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;
    private Fragment fragment;

    Adapter(List<Shop> dataset, NotifyByShopAdapter notifyByShopAdapter, Context context, Fragment fragment) {
        this.dataset = dataset;
        this.notifyByShopAdapter = notifyByShopAdapter;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_shop_approvals,parent,false);

        View view = null;

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_shop,parent,false);

        if(viewType== VIEW_TYPE_SHOP_APPROVAL)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop,parent,false);

            return new ViewHolderShopApproval(view);
        }
        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }


//        return new ViewHolder(view);

        return null;
    }



    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_SHOP_APPROVAL;
        }

//        return -1;
    }






    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {



        if(holderVH instanceof ViewHolderShopApproval) {
            ViewHolderShopApproval holder = (ViewHolderShopApproval) holderVH;

            Shop shop = dataset.get(position);



//        holder.shopID.setText("Shop ID : " + String.valueOf(shop.getShopID()));
//        holder.address.setText(shop.getShopAddress() + "\n" + shop.getCity() + " - " + shop.getPincode());
//        holder.shopName.setText(shop.getShopName());


            if(shop!=null)
            {

                holder.shopName.setText(shop.getShopName());

                if(shop.getShopAddress()!=null)
                {
                    holder.shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
                }

//                String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                        + shop.getLogoImagePath();

                String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Shop/Image/three_hundred_"
                        + shop.getLogoImagePath() + ".jpg";

                Drawable placeholder = VectorDrawableCompat
                        .create(context.getResources(),
                                R.drawable.ic_nature_people_white_48px, context.getTheme());

                Picasso.with(context)
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.shopLogo);

                holder.delivery.setText("Delivery : Rs " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
                holder.distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


                if(shop.getRt_rating_count()==0)
                {
                    holder.rating.setText("N/A");
                    holder.rating_count.setText("( Not Yet Rated )");

                }
                else
                {
                    holder.rating.setText(String.valueOf(shop.getRt_rating_avg()));
                    holder.rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
                }


                if(shop.getShortDescription()!=null)
                {
                    holder.description.setText(shop.getShortDescription());
                }

            }

            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Shop/Image/" + "three_hundred_"+ shop.getLogoImagePath() + ".jpg";

            System.out.println(imagePath);


            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.shopLogo);
        }
        else if(holderVH instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof FragmentShopApprovals)
            {
                int items_count = ((FragmentShopApprovals) fragment).item_count;

                if(dataset.size() == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }



    }

    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



    public class ViewHolderShopApproval extends RecyclerView.ViewHolder implements View.OnClickListener {


//        @Bind(R.id.shop_id) TextView shopID;
//        @Bind(R.id.shop_name) TextView shopName;
//        @Bind(R.id.address) TextView address;
//        @Bind(R.id.shop_logo) ImageView shopLogo;



        @Bind(R.id.shop_name) TextView shopName;
        @Bind(R.id.shop_address) TextView shopAddress;
        @Bind(R.id.shop_logo) ImageView shopLogo;
        @Bind(R.id.delivery) TextView delivery;
        @Bind(R.id.distance) TextView distance;
        @Bind(R.id.rating) TextView rating;
        @Bind(R.id.rating_count) TextView rating_count;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.shop_info_card) CardView list_item;



        public ViewHolderShopApproval(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick({R.id.edit_icon,R.id.edit_text})
        void editClick()
        {
            notifyByShopAdapter.notifyEditClick(dataset.get(getLayoutPosition()));
        }


        @Override
        public void onClick(View v) {
            notifyByShopAdapter.notifyListItemClick(dataset.get(getLayoutPosition()));
        }
    }




    interface NotifyByShopAdapter {
        void notifyEditClick(Shop shop);
        void notifyListItemClick(Shop shop);
    }

}
