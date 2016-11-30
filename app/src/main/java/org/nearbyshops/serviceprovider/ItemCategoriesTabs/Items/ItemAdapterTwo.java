package org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelStats.ItemStats;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemService;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class ItemAdapterTwo extends RecyclerView.Adapter<ItemAdapterTwo.ViewHolder>{


    Map<Integer,Item> selectedItems = new HashMap<>();


    @Inject
    ItemService itemCategoryService;

    private List<Item> dataset;
    private Context context;
    private ItemRemakeFragment activity;
    private Item requestedChangeParent = null;
    private NotificationReceiver notificationReceiver;


    final String IMAGE_ENDPOINT_URL = "/api/Images";

    ItemAdapterTwo(List<Item> dataset, Context context, ItemRemakeFragment activity, ItemAdapterTwo.NotificationReceiver notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.activity = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<Item>();
        }

    }

    @Override
    public ItemAdapterTwo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ItemAdapterTwo.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getItemName());

        ItemStats itemStats = dataset.get(position).getItemStats();


        holder.priceRange.setText("Price Range : Rs." + itemStats.getMin_price() + " - " + itemStats.getMax_price());
        holder.priceAverage.setText("Price (Average) : " + itemStats.getAvg_price());
        holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        holder.itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
        holder.ratingCount.setText("( " + String.valueOf(itemStats.getRatingCount()) + " Ratings )");



//        holder.categoryDescription
//                .setText(dataset.get(position).getItemDescription()
//                + "\n\nPrice (Average) : " + String.valueOf(dataset.get(position).getItemStats().getAvg_price()) + " Per " + dataset.get(position).getQuantityUnit()
//                + "\nAvailable in " + String.valueOf(dataset.get(position).getItemStats().getShopCount()) + " Shops");

        if(selectedItems.containsKey(dataset.get(position).getItemID()))
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));
        }
        else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }



        String imagePath = UtilityGeneral.getImageEndpointURL(context)
                + dataset.get(position).getItemImageURL();

        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

//        Log.d("applog",imagePath);

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @Bind(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @Bind(R.id.items_list_item) CardView itemCategoryListItem;
        @Bind(R.id.itemImage) ImageView categoryImage;
        @Bind(R.id.price_range) TextView priceRange;
        @Bind(R.id.price_average) TextView priceAverage;
        @Bind(R.id.shop_count) TextView shopCount;
        @Bind(R.id.item_rating) TextView itemRating;
        @Bind(R.id.rating_count) TextView ratingCount;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);


//            categoryImage = (ImageView) itemView.findViewById(R.id.itemImage);
//            categoryName = (TextView) itemView.findViewById(R.id.itemName);
//            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

//            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.items_list_item);
        }



        @OnClick(R.id.items_list_item)
        public void listItemClick()
        {

//            showToastMessage("Long Click !");


            if(selectedItems.containsKey(
                    dataset.get(getLayoutPosition())
                            .getItemID()
            ))
            {
                selectedItems.remove(dataset.get(getLayoutPosition()).getItemID());
            }
            else
            {

                selectedItems.put(dataset.get(getLayoutPosition()).getItemID(),dataset.get(getLayoutPosition()));
                notificationReceiver.notifyItemSelected();
            }


            notifyItemChanged(getLayoutPosition());

//            showToastMessage(String.valueOf(selectedItems.size()));


//            itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.cyan900));


        }




        /*@OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }



//            notificationReceiver.notifyRequestSubCategory(dataset.get(getLayoutPosition()));

//            selectedItems.clear();

        }


*/
        public void deleteItemCategory()
        {


//            Call<ResponseBody> call = itemCategoryService.deleteItemCategory(dataset.get(getLayoutPosition()).getItemCategoryID());

            Call<ResponseBody> call = itemCategoryService.deleteItem(dataset.get(getLayoutPosition()).getItemID());


            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if(response.code()==200)
                    {
                        notifyDelete();

                        showToastMessage("Removed !");

                    }else if(response.code()==304)
                    {
                        showToastMessage("Delete failed !");

                    }else
                    {
                        showToastMessage("Server Error !");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network request failed ! Please check your connection!");
                }
            });
        }


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.action_remove:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete Item Category !")
                            .setMessage("Do you want to delete this Item Category ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteItemCategory();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    showToastMessage("Cancelled !");
                                }
                            })
                            .show();


                    break;

                case R.id.action_edit:

//                    Intent intent = new Intent(context,EditItemCategory.class);
//                    intent.putExtra(EditItemCategory.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));
//                    context.startActivity(intent);

                    Intent intentEdit = new Intent(context,EditItem.class);
                    intentEdit.putExtra(EditItem.ITEM_INTENT_KEY,dataset.get(getLayoutPosition()));
                    context.startActivity(intentEdit);

                    break;



                case R.id.action_detach:


                    showToastMessage("Detach");


                    break;

                case R.id.action_change_parent:


//                    showToastMessage("Change parent !");

                    Intent intentParent = new Intent(context, ItemCategoriesParent.class);

                    requestedChangeParent = dataset.get(getLayoutPosition());

                    // add the selected item category in the exclude list so that it does not get showed up as an option.
                    // This is required to prevent an item category to assign itself or its children as its parent.
                    // This should not happen because it would be erratic.

                    ItemCategoriesParent.clearExcludeList(); // it is a safe to clear the list before adding any items in it.

//                    ItemCategoriesParent.excludeList
//                            .put(requestedChangeParent.getItemID(),requestedChangeParent);


                    activity.startActivityForResult(intentParent,1,null);


                    break;




                default:

                    break;

            }

            return false;
        }



    }// ViewHolder Class declaration ends




    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    public void notifyDelete()
    {
        activity.notifyDelete();

    }


    public interface NotificationReceiver
    {
        // method for notifying the list object to request sub category
//        public void notifyRequestSubCategory(ItemCategory itemCategory);

        public void notifyItemSelected();

    }



    public Item getRequestedChangeParent() {
        return requestedChangeParent;
    }


    public void setRequestedChangeParent(Item requestedChangeParent) {
        this.requestedChangeParent = requestedChangeParent;
    }
}