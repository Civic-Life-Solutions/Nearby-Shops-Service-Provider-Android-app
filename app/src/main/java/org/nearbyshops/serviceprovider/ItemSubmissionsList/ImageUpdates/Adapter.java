package org.nearbyshops.serviceprovider.ItemSubmissionsList.ImageUpdates;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.Model.ItemImage;
import org.nearbyshops.serviceprovider.ModelItemSubmission.ItemImageSubmission;
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
    public static final int VIEW_TYPE_IMAGES_ADDED = 2;
//    public static final int VIEW_TYPE_IMAGES_UPDATED = 3;
//    public static final int VIEW_TYPE_ITEM_UPDATES = 4;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;



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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_submission,parent,false);
            return new ViewHolderImageCurrent(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if(viewType == VIEW_TYPE_IMAGES_ADDED)
        {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image_submission,parent,false);
            return new ViewHolderImagesAdded(view);
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

        if(holder instanceof ViewHolderImageCurrent)
        {

            bindImageCurrent((ViewHolderImageCurrent)holder,position);
        }
        else if(holder instanceof ViewHolderImagesAdded)
        {
            bindImagesAdded((ViewHolderImagesAdded)holder,position);
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

            if(fragment instanceof ImageUpdatesFragment)
            {
                itemCount = (((ImageUpdatesFragment) fragment).item_count_image_updates  + 3);
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
        else if (dataset.get(position) instanceof ItemImage)
        {
            return VIEW_TYPE_ITEM_CURRENT;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof ItemImageSubmission)
        {
            return VIEW_TYPE_IMAGES_ADDED;
        }

        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }


    void bindImagesAdded(ViewHolderImagesAdded holder,int position)
    {
        if(dataset.get(position) instanceof ItemImageSubmission)
        {
            ItemImageSubmission itemImageSubmission = (ItemImageSubmission) dataset.get(position);


            holder.caption.setText(itemImageSubmission.getItemImage().getCaptionTitle() + "\n"
                    + itemImageSubmission.getItemImage().getCaption());


            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/" + "three_hundred_"+ itemImageSubmission.getItemImage().getImageFilename() + ".jpg";

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.profileImage);


        }
    }




    class ViewHolderImagesAdded extends RecyclerView.ViewHolder{



        @Bind(R.id.profile_image) ImageView profileImage;
        @Bind(R.id.caption) TextView caption;



        public ViewHolderImagesAdded(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends








    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @Bind(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends





    void bindImageCurrent(ViewHolderImageCurrent holder,int position)
    {


        if(dataset.get(position) instanceof ItemImage)
        {
            ItemImage itemImage = (ItemImage) dataset.get(position);

            holder.caption.setText(itemImage.getCaptionTitle() + "\n"
                    + itemImage.getCaption());


            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemImage/Image/" + "three_hundred_"+ itemImage.getImageFilename() + ".jpg";

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.profileImage);
        }


    }



    class ViewHolderImageCurrent extends RecyclerView.ViewHolder implements View.OnClickListener {



        @Bind(R.id.profile_image) ImageView profileImage;
        @Bind(R.id.caption) TextView caption;


//        @Bind(R.id.updates_count) TextView updatesCount;
//        @Bind(R.id.latest_update) TextView latestUpdate;



        public ViewHolderImageCurrent(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            notificationReceiver.currentImageClick((ItemImage) dataset.get(getLayoutPosition()),getLayoutPosition());
        }
    }// ViewHolder Class declaration ends


    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category

        void currentImageClick(ItemImage itemImage, int position);
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