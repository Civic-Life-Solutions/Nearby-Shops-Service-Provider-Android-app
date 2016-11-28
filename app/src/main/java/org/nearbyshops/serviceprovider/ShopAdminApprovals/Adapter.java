package org.nearbyshops.serviceprovider.ShopAdminApprovals;

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

import org.nearbyshops.serviceprovider.ModelRoles.ShopAdmin;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<ShopAdmin> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;
    private Context context;

    Adapter(List<ShopAdmin> dataset, NotifyConfirmOrder notifyConfirmOrder, Context context) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.context = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shop_admin,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

        ShopAdmin shopAdmin = dataset.get(position);

        holder.shopAdminID.setText("ID : "  + String.valueOf(shopAdmin.getShopAdminID()));
        holder.shopAdminName.setText(shopAdmin.getName());
        holder.designation.setText(shopAdmin.getDesignation());
        holder.about.setText(shopAdmin.getAbout());
        holder.phoneNumber.setText(shopAdmin.getPhoneNumber());

        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/ShopAdmin/Image/" + "three_hundred_"+ shopAdmin.getProfileImageURL() + ".jpg";

        Picasso.with(context)
                .load(imagePath)
                .placeholder(drawable)
                .into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        @Bind(R.id.shop_admin_id)
        TextView shopAdminID;

        @Bind(R.id.shop_admin_name)
        TextView shopAdminName;

        @Bind(R.id.phone_number)
        TextView phoneNumber;

        @Bind(R.id.designation)
        TextView designation;


        @Bind(R.id.about)
        TextView about;

        @Bind(R.id.shop_admin_profile_image)
        ImageView profileImage;



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
        void notifyEditClick(ShopAdmin shopAdmin);
        void notifyListItemClick(ShopAdmin shopAdmin);
    }

}
