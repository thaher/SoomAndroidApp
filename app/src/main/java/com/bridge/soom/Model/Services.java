package com.bridge.soom.Model;

import java.util.List;

/**
 * Created by thaher on 20-06-2017.
 */

public class Services {
    String tableid;
    String ServiceName;
    String ServiceId;
    List<String> SubServiceName;
    List<String> SubServiceId;
    String wages;
    String experiance;

    public Services() {
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public List<String> getSubServiceName() {
        return SubServiceName;
    }

    public void setSubServiceName(List<String> subServiceName) {
        SubServiceName = subServiceName;
    }

    public List<String> getSubServiceId() {
        return SubServiceId;
    }

    public void setSubServiceId(List<String> subServiceId) {
        SubServiceId = subServiceId;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public String getExperiance() {
        return experiance;
    }

    public void setExperiance(String experiance) {
        this.experiance = experiance;
    }
}
