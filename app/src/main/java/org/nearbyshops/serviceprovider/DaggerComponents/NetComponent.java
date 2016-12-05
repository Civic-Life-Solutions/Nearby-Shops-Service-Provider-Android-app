package org.nearbyshops.serviceprovider.DaggerComponents;


import org.nearbyshops.serviceprovider.DetachedTabs.ItemCategories.DetachedItemCatAdapter;
import org.nearbyshops.serviceprovider.DetachedTabs.ItemCategories.DetachedItemCatFragment;
import org.nearbyshops.serviceprovider.DetachedTabs.Items.DetachedItemAdapter;
import org.nearbyshops.serviceprovider.DetachedTabs.Items.DetachedItemFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.Deprecated.EditItemCategoryOld;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.EditItem.EditItemFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.Deprecated.EditItemOld;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemFragmentTwo;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.serviceprovider.zDistributorAccounts.DistributorAccountFragment;
import org.nearbyshops.serviceprovider.zDistributorAccounts.DistributorDetail.DistributorDetail;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.Deprecated.AddItem;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.ItemAdapterTwo;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.Items.Deprecated.ItemAdapterOld;
import org.nearbyshops.serviceprovider.LoginScreen;
import org.nearbyshops.serviceprovider.ServiceConfiguration.EditServiceConfiguration;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParent;
import org.nearbyshops.serviceprovider.SelectParent.ItemCategoriesParentAdapter;
import org.nearbyshops.serviceprovider.ItemCategoriesTabs.ItemCategories.ItemCategoriesAdapter;
import org.nearbyshops.serviceprovider.DaggerModules.AppModule;
import org.nearbyshops.serviceprovider.DaggerModules.NetModule;
import org.nearbyshops.serviceprovider.Settings.SettingsActivity;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.EditShopAdmin.EditShopAdminFragment;
import org.nearbyshops.serviceprovider.ShopAdminApprovals.FragmentShopAdmins;
import org.nearbyshops.serviceprovider.ShopApprovals.FragmentShopApprovals;
import org.nearbyshops.serviceprovider.StaffAccounts.EditStaff.EditStaffFragment;
import org.nearbyshops.serviceprovider.StaffAccounts.FragmentStaffAccounts;
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

    void Inject(EditItemCategoryOld editItemCategoryOld);

    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.ItemCategoriesFragment itemCategoriesFragment);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(org.nearbyshops.serviceprovider.zAddItems.ItemCategories.EditItemCategory editItemCategory);

    void Inject(ItemFragmentTwo itemFragmentTwo);

    void Inject(ItemAdapterOld itemAdapterOld);

    void Inject(AddItem addItem);

    void Inject(EditItemOld editItemOld);

    void Inject(DetachedItemCatFragment detachedItemCatFragment);

    void Inject(DetachedItemCatAdapter detachedItemCatAdapter);

    void Inject(DetachedItemAdapter detachedItemAdapter);

    void Inject(DetachedItemFragment detachedItemFragment);

    void Inject(ItemAdapterTwo itemAdapterTwo);

    void Inject(LoginScreen loginScreen);

    void Inject(SettingsActivity settingsActivity);

    void Inject(DistributorAccountFragment distributorAccountFragment);

    void Inject(DistributorDetail distributorDetail);

    void Inject(EditShopAdminFragment editShopAdminFragment);

    void Inject(FragmentShopAdmins fragmentShopAdmins);

    void Inject(FragmentShopApprovals fragmentShopApprovals);

    void Inject(EditStaffFragment editStaffFragment);

    void Inject(FragmentStaffAccounts fragmentStaffAccounts);

    void Inject(EditItemFragment editItemFragment);

    void Inject(EditItemCategoryFragment editItemCategoryFragment);

    void Inject(ItemCategoriesFragmentSimple itemCategoriesFragmentSimple);


//    void Inject(LoginDialog loginDialog);
}
