package com.bridge.soom.Interface;

import java.util.List;

/**
 * Created by thaher on 19-05-2017.
 */

public interface GetCatDatas {

    public void failedtoConnect();
    public void GetCategoryListFailed(String msg);

    public void GetCategoryList(List<String> catid, List<String> catname);

    public void GetSubCategoryList(List<String> subcatid, List<String> subcatname);

    public void GetSubCategoryListFailed(String msg);

    public void GetStateCategoryList(List<String> subcatid, List<String> subcatname);

    public void GetStateListFailed(String msg);

    public void GetCityCategoryList(List<String> subcatid, List<String> subcatname, List<String> lat, List<String> lng);

    public void GetCityListFailed(String msg);

    public void GetCountryCategoryList(List<String> subcatid, List<String> subcatname);

    public void GetCountryListFailed(String msg);
}
