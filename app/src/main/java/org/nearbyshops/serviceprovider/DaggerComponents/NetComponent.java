package org.nearbyshops.serviceprovider.DaggerComponents;


import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParentAdapter;
import org.nearbyshops.serviceprovider.AddItems.ItemCategories.EditItemCategory;
import org.nearbyshops.serviceprovider.AddItems.ItemCategories.ItemCategories;
import org.nearbyshops.serviceprovider.AddItems.ItemCategories.ItemCategoriesAdapter;
import org.nearbyshops.serviceprovider.DaggerModules.AppModule;
import org.nearbyshops.serviceprovider.DaggerModules.NetModule;
import org.nearbyshops.serviceprovider.SavedConfigurations.AddService;
import org.nearbyshops.serviceprovider.SavedConfigurations.EditItem;
import org.nearbyshops.serviceprovider.SavedConfigurations.ServiceConfigurationActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {


    void Inject(ServiceConfigurationActivity serviceConfigurationActivity);

    void Inject(AddService addService);

    void Inject(EditItem editItem);

    void Inject(EditItemCategory editItemCategory);

    void Inject(ItemCategories itemCategories);

    void Inject(ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);
}
