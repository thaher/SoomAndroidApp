package com.bridge.soom.Interface;

/**
 * Created by thaher on 16-05-2017.
 */

public interface ChangePassResponse {
    public void changePassResponseSuccess(String message);
    public void changePassResponseFailed(String message);
    public void failedtoConnect();
}
