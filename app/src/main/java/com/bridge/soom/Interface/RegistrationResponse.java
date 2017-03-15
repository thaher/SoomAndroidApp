package com.bridge.soom.Interface;


/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public interface RegistrationResponse {
    public void registrationResponseSuccess(String message);
    public void registrationResponseFailed(String message);
    public void failedtoConnect();

}
