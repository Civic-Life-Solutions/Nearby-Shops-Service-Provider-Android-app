package org.nearbyshops.serviceprovider.ModelSettings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 18/9/16.
 */
public class Settings implements Parcelable{

    // Table Name
    public static final String TABLE_NAME = "SETTINGS";

    // column Names
    public static final String SETTING_CONFIGURATION_ID = "SETTING_CONFIGURATION_ID";
    public static final String END_USER_ENABLED_DEFAULT = "END_USER_ENABLED_DEFAULT";
    public static final String DISTRIBUTOR_ENABLED_DEFAULT = "DISTRIBUTOR_ENABLED_DEFAULT";
//    public static final String STAFF_ACCOUNT_ENABLED_DEFAULT = "STAFF_ACCOUNT_ENABLED_DEFAULT";
    public static final String GOOGLE_MAPS_API_KEY = "GOOGLE_MAPS_API_KEY";


    // Create Table Statement
    public static final String createTableSettingsPostgres
            = "CREATE TABLE IF NOT EXISTS " + Settings.TABLE_NAME + "("
            + " " + Settings.SETTING_CONFIGURATION_ID + " SERIAL PRIMARY KEY,"
            + " " + Settings.END_USER_ENABLED_DEFAULT + " boolean,"
            + " " + Settings.DISTRIBUTOR_ENABLED_DEFAULT + " boolean,"
            + " " + Settings.GOOGLE_MAPS_API_KEY + " text"
            + ")";



    // Instance Variables
    private int settingsID;
    private boolean endUserEnabledByDefault;
    private boolean distributorEnabledByDefault;
//    private String staffEnabledByDefault;
    private String googleMapsAPIKey;

    public Settings() {
    }

    protected Settings(Parcel in) {
        settingsID = in.readInt();
        endUserEnabledByDefault = in.readByte() != 0;
        distributorEnabledByDefault = in.readByte() != 0;
        googleMapsAPIKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(settingsID);
        dest.writeByte((byte) (endUserEnabledByDefault ? 1 : 0));
        dest.writeByte((byte) (distributorEnabledByDefault ? 1 : 0));
        dest.writeString(googleMapsAPIKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    public int getSettingsID() {
        return settingsID;
    }

    public void setSettingsID(int settingsID) {
        this.settingsID = settingsID;
    }

    public Boolean getEndUserEnabledByDefault() {
        return endUserEnabledByDefault;
    }

    public void setEndUserEnabledByDefault(Boolean endUserEnabledByDefault) {
        this.endUserEnabledByDefault = endUserEnabledByDefault;
    }

    public Boolean getDistributorEnabledByDefault() {
        return distributorEnabledByDefault;
    }

    public void setDistributorEnabledByDefault(Boolean distributorEnabledByDefault) {
        this.distributorEnabledByDefault = distributorEnabledByDefault;
    }

    public String getGoogleMapsAPIKey() {
        return googleMapsAPIKey;
    }

    public void setGoogleMapsAPIKey(String googleMapsAPIKey) {
        this.googleMapsAPIKey = googleMapsAPIKey;
    }
}
