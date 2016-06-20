package org.nearbyshops.serviceprovider.SavedConfigurations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.nearbyshops.serviceprovider.Model.Service;
import org.nearbyshops.serviceprovider.R;

import java.util.List;

/**
 * Created by sumeet on 6/6/16.
 */
public class ServiceConfigurationAdapter extends RecyclerView.Adapter<ServiceConfigurationAdapter.ViewHolder>{


    List<Service> dataset = null;


    Context context;
    NotificationReceiver notificationReceiver;



    public ServiceConfigurationAdapter(List<Service> dataset, Context context, NotificationReceiver notificationReceiver) {

        this.dataset = dataset;
        this.context = context;
        this.notificationReceiver = notificationReceiver;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_service,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Service service = dataset.get(position);

        if(service != null)
        {

        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView editButton;
        TextView removeButton;
        RelativeLayout listItemDeliveryAddress;


        public ViewHolder(View itemView) {
            super(itemView);



//            editButton.setOnClickListener(this);
//            removeButton.setOnClickListener(this);
//            listItemDeliveryAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
//                case R.id.editButton:

//                    editClick();
//
//                    break;
//
//                case R.id.removeButton:
//
//                    removeClick();
//
//                    break;
//
//                case R.id.list_item_delivery_vehicle_self:
//
//                    listItemClick();
//
//                    break;


                default:
                    break;
            }

        }


        public void listItemClick()
        {
            notificationReceiver.notifyListItemClick(dataset.get(getLayoutPosition()));
        }

        public void removeClick()
        {
            notificationReceiver.notifyRemove(dataset.get(getLayoutPosition()));
        }

        public void editClick()
        {
            notificationReceiver.notifyEdit(dataset.get(getLayoutPosition()));
        }

    }



    public interface NotificationReceiver{

        void notifyEdit(Service service);

        void notifyRemove(Service service);

        void notifyListItemClick(Service service);

    }

}
