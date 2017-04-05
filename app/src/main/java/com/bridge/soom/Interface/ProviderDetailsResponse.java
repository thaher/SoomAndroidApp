package com.bridge.soom.Interface;

import com.bridge.soom.Model.UserModel;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface ProviderDetailsResponse {
    public void DetailsResponseSuccess( UserModel userModel);
    public void DetailsResponseFailed(String message);
    public void failedtoConnect();

}
