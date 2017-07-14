package com.bridge.soom.Interface;

import com.bridge.soom.Model.UserModel;

/**
 * Created by thaher on 16-05-2017.
 */

public interface PersonalDetailsResponse {
    public void ResponseSuccess(UserModel userModel);
    public void ResponseFailed(String message);
    public void failedtoConnect();
}
