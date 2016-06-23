package org.nearbyshops.serviceprovider.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class Service implements Parcelable{

    int serviceID;
    String imagePath;
    String logoImagePath;

    String backdropImagePath;
    String serviceName;
    String helplineNumber;

    String address;
    String city;
    long pincode;

    String landmark;
    String state;
    String country;

    String ISOCountryCode;
    String ISOLanguageCode;
    int serviceType;

    int serviceLevel;
    double latCenter;
    double lonCenter;

    int serviceRange;
    boolean isEthicalServiceProvider;
    boolean isVerified;

    double latMax;
    double lonMax;
    double latMin;

    double lonMin;
    String configurationNickname;
    String serviceURL;

    Timestamp created;
    Timestamp updated;


    // real time variables : the values of these variables are generated in real time.
    Double rt_distance;


    public Service() {
    }


    protected Service(Parcel in) {
        serviceID = in.readInt();
        imagePath = in.readString();
        logoImagePath = in.readString();

        backdropImagePath = in.readString();
        serviceName = in.readString();
        helplineNumber = in.readString();

        address = in.readString();
        city = in.readString();
        pincode = in.readLong();

        landmark = in.readString();
        state = in.readString();
        country = in.readString();

        ISOCountryCode = in.readString();
        ISOLanguageCode = in.readString();
        serviceType = in.readInt();

        serviceLevel = in.readInt();
        latCenter = in.readDouble();
        lonCenter = in.readDouble();

        serviceRange = in.readInt();
        isEthicalServiceProvider = in.readByte() != 0;
        isVerified = in.readByte() != 0;

        latMax = in.readDouble();
        lonMax = in.readDouble();
        latMin = in.readDouble();

        lonMin = in.readDouble();
        configurationNickname = in.readString();
        serviceURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serviceID);
        dest.writeString(imagePath);
        dest.writeString(logoImagePath);
        dest.writeString(backdropImagePath);
        dest.writeString(serviceName);
        dest.writeString(helplineNumber);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeLong(pincode);
        dest.writeString(landmark);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(ISOCountryCode);
        dest.writeString(ISOLanguageCode);
        dest.writeInt(serviceType);
        dest.writeInt(serviceLevel);
        dest.writeDouble(latCenter);
        dest.writeDouble(lonCenter);
        dest.writeInt(serviceRange);
        dest.writeByte((byte) (isEthicalServiceProvider ? 1 : 0));
        dest.writeByte((byte) (isVerified ? 1 : 0));
        dest.writeDouble(latMax);
        dest.writeDouble(lonMax);
        dest.writeDouble(latMin);
        dest.writeDouble(lonMin);
        dest.writeString(configurationNickname);
        dest.writeString(serviceURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLogoImagePath() {
        return logoImagePath;
    }

    public void setLogoImagePath(String logoImagePath) {
        this.logoImagePath = logoImagePath;
    }

    public String getBackdropImagePath() {
        return backdropImagePath;
    }

    public void setBackdropImagePath(String backdropImagePath) {
        this.backdropImagePath = backdropImagePath;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getISOCountryCode() {
        return ISOCountryCode;
    }

    public void setISOCountryCode(String ISOCountryCode) {
        this.ISOCountryCode = ISOCountryCode;
    }

    public String getISOLanguageCode() {
        return ISOLanguageCode;
    }

    public void setISOLanguageCode(String ISOLanguageCode) {
        this.ISOLanguageCode = ISOLanguageCode;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public double getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(double latCenter) {
        this.latCenter = latCenter;
    }

    public double getLonCenter() {
        return lonCenter;
    }

    public void setLonCenter(double lonCenter) {
        this.lonCenter = lonCenter;
    }

    public int getServiceRange() {
        return serviceRange;
    }

    public void setServiceRange(int serviceRange) {
        this.serviceRange = serviceRange;
    }

    public boolean isEthicalServiceProvider() {
        return isEthicalServiceProvider;
    }

    public void setEthicalServiceProvider(boolean ethicalServiceProvider) {
        isEthicalServiceProvider = ethicalServiceProvider;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public double getLatMax() {
        return latMax;
    }

    public void setLatMax(double latMax) {
        this.latMax = latMax;
    }

    public double getLonMax() {
        return lonMax;
    }

    public void setLonMax(double lonMax) {
        this.lonMax = lonMax;
    }

    public double getLatMin() {
        return latMin;
    }

    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }

    public double getLonMin() {
        return lonMin;
    }

    public void setLonMin(double lonMin) {
        this.lonMin = lonMin;
    }

    public String getConfigurationNickname() {
        return configurationNickname;
    }

    public void setConfigurationNickname(String configurationNickname) {
        this.configurationNickname = configurationNickname;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(Double rt_distance) {
        this.rt_distance = rt_distance;
    }
}
