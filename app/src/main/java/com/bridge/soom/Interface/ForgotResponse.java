package com.bridge.soom.Interface;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface ForgotResponse {
    public void forgotResponseSuccess(String message);
    public void forgotResponseFailed(String message);
    public void failedtoConnect();

}
