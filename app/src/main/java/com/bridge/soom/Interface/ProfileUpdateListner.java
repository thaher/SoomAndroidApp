package com.bridge.soom.Interface;

import com.bridge.soom.Model.UserModel;

/**
 * Created by thaher on 24-05-2017.
 */

public interface ProfileUpdateListner {

    public void ProfileUpdateSuccess(String message, String userModel);
    public void ProfileUpdateFailed(String message);
    public void failedtoConnect();
}
