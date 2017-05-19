package com.bridge.soom.Interface;

import java.util.List;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface RegistrationProviderResponse {
    public void failedtoConnect();

    public void GetCityListFailed(String msg);

   public  void GetCityeCategoryList(String imageUrl, String accessToken, String userEmail, String userType, String userFirstName, String userLastName, Integer userStatusLevel);
}
