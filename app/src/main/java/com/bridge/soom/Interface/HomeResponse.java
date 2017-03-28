package com.bridge.soom.Interface;

import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;

import java.util.List;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface HomeResponse {

    public void failedtoConnect();

    public void GetCategoryList(List<String> catid, List<String> catname);

    public void GetCategoryListFailed(String msg);

    public void GetProviderListFailed(String msg);

    public void GetProviderList(List<ProviderBasic> providers);
}
