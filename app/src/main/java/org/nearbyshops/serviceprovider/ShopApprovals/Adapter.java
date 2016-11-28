package org.nearbyshops.serviceprovider.ShopApprovals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.Model.Shop;
import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<Shop> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;
    private Context context;

    Adapter(List<Shop> dataset, NotifyConfirmOrder notifyConfirmOrder, Context context) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.context = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_approvals,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

        Shop shop = dataset.get(position);


        holder.shopID.setText("Shop ID : " + String.valueOf(shop.getShopID()));
        holder.address.setText(shop.getShopAddress() + "\n" + shop.getCity() + " - " + shop.getPincode());
        holder.shopName.setText(shop.getShopName());

        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Shop/Image/" + "three_hundred_"+ shop.getLogoImagePath() + ".jpg";

        System.out.println(imagePath);


        Picasso.with(context)
                .load(imagePath)
                .placeholder(drawable)
                .into(holder.shopLogo);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Bind(R.id.shop_id) TextView shopID;
        @Bind(R.id.shop_name) TextView shopName;
        @Bind(R.id.address) TextView address;
        @Bind(R.id.shop_logo) ImageView shopLogo;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick({R.id.edit_icon,R.id.edit_text})
        void editClick()
        {
            notifyConfirmOrder.notifyEditClick(dataset.get(getLayoutPosition()));
        }


        @Override
        public void onClick(View v) {
            notifyConfirmOrder.notifyListItemClick(dataset.get(getLayoutPosition()));
        }
    }




    interface NotifyConfirmOrder{
        void notifyEditClick(Shop shop);
        void notifyListItemClick(Shop shop);
    }

}
