package com.bridge.soom.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by Thaher-Majeed on 28-03-2017.
 */

public class ProviderBasic implements ClusterItem, Serializable {

    String accessTocken;
    String userFirstName;
    String userLastName;
    String userGender;
    String currentLocation;
    String locationLat;
    String locationLong;
    String userAddress;
    String userDesignation;
    String userWagesHour;
    String profileImageUrl;
    String userEmail;
    String userMobile;
    String categoryName;

    public ProviderBasic() {
    }

    public String getAccessTocken() {
        return accessTocken;
    }

    public void setAccessTocken(String accessTocken) {
        this.accessTocken = accessTocken;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserWagesHour() {
        return userWagesHour;
    }

    public void setUserWagesHour(String userWagesHour) {
        this.userWagesHour = userWagesHour;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(getLocationLat()),Double.parseDouble(getLocationLong()));
    }
}
