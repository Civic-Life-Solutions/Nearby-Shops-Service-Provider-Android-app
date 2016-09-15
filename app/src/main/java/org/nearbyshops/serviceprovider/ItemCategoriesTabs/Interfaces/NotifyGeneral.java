package org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces;

import org.nearbyshops.serviceprovider.Model.ItemCategory;

/**
 * Created by sumeet on 27/6/16.
 */
public interface NotifyGeneral {

        void insertTab(String categoryName);
        void removeLastTab();
        void notifySwipeToright();
}
