package com.bridge.soom.Interface;

import com.bridge.soom.Model.UserModel;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface LoginResponse {
    public void loginResponseSuccess(String message, UserModel userModel);
    public void loginResponseFailed(String message);
    public void failedtoConnect();

}
