package org.nearbyshops.serviceprovider.DaggerComponents;


import org.nearbyshops.serviceprovider.DetachedTabs.ItemCategories.DetachedItemCatAdapter;
import org.nearbyshops.serviceprovider.DetachedTabs.ItemCategories.DetachedItemCatFragment;
import org.nearbyshops.serviceprovider.DetachedTabs.Items.DetachedItemAdapter;
import org.nearbyshops.serviceprovider.DetachedTabs.Items.DetachedItemFragment;
import org.nearbyshops.serviceprovider.DistributorAccounts.DistributorAccountFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.AddItem;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.EditItem;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemAdapterTwo;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemRemakeAdapter;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemRemakeFragment;
import org.nearbyshops.serviceprovider.Login;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditServiceConfiguration;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParentAdapter;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.EditItemCategory;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.ItemCategoriesAdapter;
import org.nearbyshops.serviceprovider.DaggerModules.AppModule;
import org.nearbyshops.serviceprovider.DaggerModules.NetModule;
import org.nearbyshops.serviceprovider.Settings.SettingsActivity;
import org.nearbyshops.serviceprovider.zSavedConfigurations.AddService;
import org.nearbyshops.serviceprovider.zSavedConfigurations.ServiceConfigurationActivity;

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

    void Inject(EditServiceConfiguration editServiceConfiguration);

    void Inject(EditItemCategory editItemCategory);

    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.ItemCategoriesFragment itemCategoriesFragment);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.EditItemCategory editItemCategory);

    void Inject(ItemRemakeFragment itemRemakeFragment);

    void Inject(ItemRemakeAdapter itemRemakeAdapter);

    void Inject(AddItem addItem);

    void Inject(EditItem editItem);

    void Inject(DetachedItemCatFragment detachedItemCatFragment);

    void Inject(DetachedItemCatAdapter detachedItemCatAdapter);

    void Inject(DetachedItemAdapter detachedItemAdapter);

    void Inject(DetachedItemFragment detachedItemFragment);

    void Inject(ItemAdapterTwo itemAdapterTwo);

    void Inject(Login login);

    void Inject(SettingsActivity settingsActivity);

    void Inject(DistributorAccountFragment distributorAccountFragment);
}
