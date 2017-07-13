package com.bridge.soom.Model;

import com.google.android.gms.location.places.Place;

/**
 * Created by Thaher on 10-07-2017.
 */

public class PlaceLoc {
    String address;
    String latitude;
    String longitude;
    String id;
    Integer zip;

    public PlaceLoc() {
    }


    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
