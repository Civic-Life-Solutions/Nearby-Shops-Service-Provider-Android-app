package org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemUpdates;

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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.ItemSubmissionsList.HeaderTitle;
import org.nearbyshops.serviceprovider.ItemSubmissionsList.ItemReviewFragment;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemSubmission;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;
    private Fragment fragment;

    public static final int VIEW_TYPE_ITEM_CURRENT = 1;
    public static final int VIEW_TYPE_ITEM_UPDATES = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    private final static int VIEW_TYPE_PROGRESS_BAR = 4;



    public Adapter(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CURRENT)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_submission,parent,false);
            return new ViewHolderItemCurrent(view);
        }
        else if(viewType == VIEW_TYPE_ITEM_UPDATES)
        {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_update,parent,false);
            return new ViewHolderItemSubmissions(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if (viewType == VIEW_TYPE_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }


//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderItemSubmissions)
        {
            bindItemSubissions((ViewHolderItemSubmissions) holder,position);
        }
        else if(holder instanceof ViewHolderItemCurrent)
        {
            bindItem((ViewHolderItemCurrent) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderTitle)
            {
                HeaderTitle header = (HeaderTitle) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }
        else if(holder instanceof LoadingViewHolder)
        {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            int itemCount = 0;

            if(fragment instanceof ItemUpdatesFragment)
            {
                itemCount = (((ItemUpdatesFragment) fragment).item_count_item_updates + 3);
            }


            if(position == 0 || position == itemCount)
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


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_PROGRESS_BAR;
        }
        if(dataset.get(position) instanceof ItemSubmission)
        {
            return VIEW_TYPE_ITEM_UPDATES;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM_CURRENT;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @Bind(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends



    void bindItemSubissions(ViewHolderItemSubmissions holder,int position)
    {

            ItemSubmission itemSubmission = (ItemSubmission) dataset.get(position);
            Item item = ((ItemSubmission)dataset.get(position)).getItem();


            holder.itemName.setText(item.getItemName());
            holder.item_unit.setText(item.getQuantityUnit());
            holder.itemDescription.setText(item.getItemDescription());
            holder.latestUpdate.setText("Submitted :\n" + itemSubmission.getTimestampSubmitted().toLocaleString());



            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/Item/Image/" + "three_hundred_"+ item.getItemImageURL() + ".jpg";

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.itemImage);
    }



    class ViewHolderItemSubmissions extends RecyclerView.ViewHolder{


        @Bind(R.id.description_short) TextView itemDescription;
        @Bind(R.id.quantity_unit) TextView item_unit;
        @Bind(R.id.itemName) TextView itemName;
//        @Bind(R.id.items_list_item) CardView itemCategoryListItem;
        @Bind(R.id.itemImage) ImageView itemImage;
        @Bind(R.id.latest_update) TextView latestUpdate;


        public ViewHolderItemSubmissions(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.items_list_item)
        void listItemClick()
        {
            notificationReceiver.itemUpdateClick((ItemSubmission) dataset.get(getLayoutPosition()),getLayoutPosition());

        }

    }// ViewHolder Class declaration ends




    void bindItem(ViewHolderItemCurrent holder,int position)
    {

        Item item = (Item) dataset.get(position);

        holder.itemName.setText(item.getItemName());
//        holder.latestUpdate.setText("Latest Update :\n" + item.getItemStats().getLatestUpdate().toLocaleString());
//        holder.updatesCount.setText("Updates Count : " + String.valueOf(item.getItemStats().getSubmissionsCount()));



        String imagePath = UtilityGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.itemImage);

    }



    class ViewHolderItemCurrent extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Bind(R.id.itemName) TextView itemName;
//        @Bind(R.id.updates_count) TextView updatesCount;
//        @Bind(R.id.latest_update) TextView latestUpdate;
        @Bind(R.id.itemImage) ImageView itemImage;



        public ViewHolderItemCurrent(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            notificationReceiver.currentItemClick((Item) dataset.get(getLayoutPosition()),getLayoutPosition());
        }
    }// ViewHolder Class declaration ends


    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category

        void currentItemClick(Item item, int position);
        void itemUpdateClick(ItemSubmission itemSubmission, int position);
    }



    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }






}