package com.bridge.soom.Interface;

import com.bridge.soom.Model.PlaceLoc;
import com.bridge.soom.Model.Services;

import java.util.List;

/**
 * Created by Thaher on 11-07-2017.
 */

public interface ServiceandLocListner {
    public void failedtoConnect();
    public void GetServiceListFailed(String msg);

    public void GetServiceList(List<Services> servicesList);

    public void GetLocationList(List<PlaceLoc> placeLocList);

    public void GetLocationListFailed(String msg);

    public void AddLocationSuccess();

    public void AddLocationFailed(String msg);
    public void DeleteLocationSuccess();

    public void DeleteLocationFailed(String msg);
    public void AddServiceSuccess();

    public void AddServiceFailed(String msg);

    public void DeleteServiceSuccess();

    public void DeleteServiceFailed(String msg);



}
