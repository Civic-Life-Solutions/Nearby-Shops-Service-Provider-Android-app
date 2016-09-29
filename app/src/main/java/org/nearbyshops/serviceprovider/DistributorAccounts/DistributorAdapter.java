package org.nearbyshops.serviceprovider.DistributorAccounts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
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
import org.nearbyshops.serviceprovider.ModelRoles.Distributor;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 29/9/16.
 */

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder>{


    private ArrayList<Distributor> dataset;
    private Context context;


    public DistributorAdapter(Context context, ArrayList<Distributor> dataset) {
        this.dataset = dataset;
        this.context = context;
    }


    @Override
    public DistributorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_distributor_account,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DistributorAdapter.ViewHolder holder, int position) {

        Distributor distributor = dataset.get(position);

        holder.distributorTitle.setText(distributor.getDistributorName());
        holder.aboutDistributor.setText(distributor.getAbout());


        String imagePath = UtilityGeneral.getImageEndpointURL(context)
                + dataset.get(position).getProfileImageURL();

        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @Bind(R.id.distributor_title)
        TextView distributorTitle;

        @Bind(R.id.profile_picture)
        ImageView profilePicture;

        @Bind(R.id.about_distributor)
        TextView aboutDistributor;

        @Bind(R.id.more_vert)
        ImageView more_options;

        @Bind(R.id.list_item_distributor)
        ConstraintLayout listItem;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.list_item_distributor)
        void listItemClick(View view)
        {
            Toast.makeText(context,"List Item Click", Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.more_vert)
        void moreOptionsClick(View view)
        {
            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.distributor_account_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.action_remove:

                    Toast.makeText(context,"Remove", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.action_edit:

                    Toast.makeText(context,"Edit", Toast.LENGTH_SHORT).show();

                    break;

                default:
                    break;
            }


            return false;
        }
    }

}
