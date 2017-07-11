package com.bridge.soom.Model;

import com.google.android.gms.location.places.Place;

/**
 * Created by Thaher on 10-07-2017.
 */

public class PlaceLoc {
    Place place;
    String id;

    public PlaceLoc() {
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
