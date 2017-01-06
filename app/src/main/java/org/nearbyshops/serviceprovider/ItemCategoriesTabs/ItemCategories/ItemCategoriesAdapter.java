package org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories;

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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.EditItemCategory;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.UtilityItemCategory;
import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;
import org.nearbyshops.serviceprovider.Utility.UtilityLogin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesAdapter extends RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder>{


    Map<Integer,ItemCategory> selectedItems = new HashMap<>();


    @Inject
    ItemCategoryService itemCategoryService;

    private List<ItemCategory> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;


    public ItemCategoriesAdapter(List<ItemCategory> dataset, Context context, NotificationsFromAdapter notificationReceiver) {


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public ItemCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ItemCategoriesAdapter.ViewHolder holder, final int position) {


        holder.categoryName.setText(String.valueOf(dataset.get(position).getCategoryName()));

        if(selectedItems.containsKey(dataset.get(position).getItemCategoryID()))
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.gplus_color_2));
        }
        else
        {
            holder.itemCategoryListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }



        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                + dataset.get(position).getImagePath() + ".jpg";

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(placeholder)
                .into(holder.categoryImage);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @Bind(R.id.name) TextView categoryName;
        @Bind(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;
        @Bind(R.id.cardview) CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



        @OnLongClick(R.id.itemCategoryListItem)
        boolean listItemLongClick()
        {

//            showToastMessage("Long Click !");


            if(selectedItems.containsKey(
                    dataset.get(getLayoutPosition())
                            .getItemCategoryID()
            )
                    )

            {
                selectedItems.remove(dataset.get(getLayoutPosition()).getItemCategoryID());

            }else
            {

                selectedItems.put(dataset.get(getLayoutPosition()).getItemCategoryID(),dataset.get(getLayoutPosition()));

                notificationReceiver.notifyItemCategorySelected();
            }


            notifyItemChanged(getLayoutPosition());

            return true;
        }



        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }


//            itemCategoryListItem.animate()
//                    .y(100)
//                    .x(100);


            notificationReceiver.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
            selectedItems.clear();
        }



        public void deleteItemCategory()
        {


            Call<ResponseBody> call = itemCategoryService.deleteItemCategory(UtilityLogin.getAuthorizationHeaders(context),
                    dataset.get(getLayoutPosition()).getItemCategoryID());

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

                    UtilityItemCategory.saveItemCategory(dataset.get(getLayoutPosition()),context);

                    Intent intent = new Intent(context,EditItemCategory.class);
                    intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_UPDATE);
                    context.startActivity(intent);



                    break;



                case R.id.action_detach:

//                    showToastMessage("Detach");

                    notificationReceiver.detachItem(dataset.get(getLayoutPosition()));

                    break;

                case R.id.action_change_parent:


                    notificationReceiver.changeParent(dataset.get(getLayoutPosition()));

//                    showToastMessage("Change parent !");

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
        notificationReceiver.notifyDelete();
    }


    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemCategorySelected();
        void detachItem(ItemCategory itemCategory);
        void notifyDelete();
        void changeParent(ItemCategory itemCategory);
    }


}