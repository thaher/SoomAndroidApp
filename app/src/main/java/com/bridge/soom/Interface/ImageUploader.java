package com.bridge.soom.Interface;

import java.util.List;

/**
 * Created by Thaher on 06-07-2017.
 */

public interface ImageUploader {
    public void failedtoConnect();

    public void UploadSuccess(String msg, String url);

    public void UploadFailed(String msg);
}
