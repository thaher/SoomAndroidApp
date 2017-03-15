package com.bridge.soom.Interface;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface VerificationResponse {
    public void verResponseSuccess(String message);
    public void verResponseFailed(String message);
    public void failedtoConnect();

}
