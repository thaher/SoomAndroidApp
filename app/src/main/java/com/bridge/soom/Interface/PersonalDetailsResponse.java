package com.bridge.soom.Interface;

/**
 * Created by thaher on 16-05-2017.
 */

public interface PersonalDetailsResponse {
    public void ResponseSuccess(String message);
    public void ResponseFailed(String message);
    public void failedtoConnect();
}
