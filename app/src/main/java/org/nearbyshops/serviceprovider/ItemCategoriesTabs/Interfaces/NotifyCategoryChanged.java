package org.nearbyshops.serviceprovider.ItemCategoriesTabs.Interfaces;

import org.nearbyshops.serviceprovider.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
