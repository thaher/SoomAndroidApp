package com.bridge.soom.Interface;

import java.util.List;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface RegistrationProviderResponse {
    public void failedtoConnect();
    public void GetCategoryListFailed(String msg);

   public void GetCategoryList(List<String> catid, List<String> catname);
}
