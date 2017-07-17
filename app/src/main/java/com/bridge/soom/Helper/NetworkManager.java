package com.bridge.soom.Helper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.bridge.soom.Activity.ProfileActivity;
import com.bridge.soom.Activity.RegistrationPVRDetailesActivity;
import com.bridge.soom.Interface.ChangePassResponse;
import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.Interface.GetCatDatas;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Interface.ImageUploader;
import com.bridge.soom.Interface.LoginResponse;
import com.bridge.soom.Interface.PersonalDetailsResponse;
import com.bridge.soom.Interface.ProfileUpdateListner;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Interface.RegistrationResponse;
import com.bridge.soom.Interface.ServiceandLocListner;
import com.bridge.soom.Interface.VerificationResponse;
import com.bridge.soom.Model.PlaceLoc;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.Services;
import com.bridge.soom.Model.UserModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static com.bridge.soom.Helper.Constants.URLADDLOCATION;
import static com.bridge.soom.Helper.Constants.URLADDSERVICE;
import static com.bridge.soom.Helper.Constants.URLCHNGPWD;
import static com.bridge.soom.Helper.Constants.URLDELETELOCATION;
import static com.bridge.soom.Helper.Constants.URLDELETESERVICE;
import static com.bridge.soom.Helper.Constants.URLFORGOT;
import static com.bridge.soom.Helper.Constants.URLGETCATLIST;
import static com.bridge.soom.Helper.Constants.URLGETCITYLIST;
import static com.bridge.soom.Helper.Constants.URLGETCOUNTRYLIST;
import static com.bridge.soom.Helper.Constants.URLGETPROVIDERDETAILS;
import static com.bridge.soom.Helper.Constants.URLGETPROVIDERLIST;
import static com.bridge.soom.Helper.Constants.URLGETSELECTEDLOCATIONS;
import static com.bridge.soom.Helper.Constants.URLGETSELECTEDSERVICES;
import static com.bridge.soom.Helper.Constants.URLGETSTATELIST;
import static com.bridge.soom.Helper.Constants.URLGETSUBCATLIST;
import static com.bridge.soom.Helper.Constants.URLHOST;
import static com.bridge.soom.Helper.Constants.URLIMAGEUPLOAD;
import static com.bridge.soom.Helper.Constants.URLLOGIN;
import static com.bridge.soom.Helper.Constants.URLPERSONALREG;
import static com.bridge.soom.Helper.Constants.URLSIGNUP;
import static com.bridge.soom.Helper.Constants.URLUPDATEPROFILE;
import static com.bridge.soom.Helper.Constants.URLUPLOADFINALREG;
import static com.bridge.soom.Helper.Constants.URLVERIFICATION;

/**
 * Created by Thaher-Majeed on 10-03-2017.
 */

public class NetworkManager {

    private AsyncHttpClient client;
    private Context context;
    private JsonParser jsonParser;


    public NetworkManager(Context context) {
        this.context = context;
        this.client = new SyncHttpClient();
        this.client.addHeader("www-request-type", "SOOM2WAPP07459842");
        this.client.addHeader("www-request-api-version", "1.0");
        this.client.addHeader("Content-Type", "application/json");
        jsonParser = new JsonParser();


    }


    //#1 User Registration
    public class RetrieveRegistrationTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String LastName, FirstName, MobileNumber, EmailId, Password, DevideID, UserType, Timexone, cultureInfo;

        private RegistrationResponse regrsponse;

        public RetrieveRegistrationTask(RegistrationResponse regrspons, String lastName, String firstName, String mobileNumber, String emailId, String password, String devideID, String userType, String timexone, String cultureInfo) {
            super();
            regrsponse = regrspons;
            this.LastName = lastName;
            this.FirstName = firstName;
            this.MobileNumber = mobileNumber;
            this.EmailId = emailId;
            this.Password = password;
            this.DevideID = devideID;
            this.UserType = userType;
            this.Timexone = timexone;
            this.cultureInfo = cultureInfo;
            Log.i("Reg_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg_submit", " try");

                    jsonParams.put("UserEmail", EmailId);
                    jsonParams.put("UserMobil", MobileNumber);
                    jsonParams.put("UserFirstName", FirstName);
                    jsonParams.put("UserLastName", LastName);
                    jsonParams.put("UserPassword", Password);
                    jsonParams.put("DeviceUID", DevideID);
                    jsonParams.put("UserType", UserType);
                    jsonParams.put("TimeZone", Timexone);
                    jsonParams.put("cultureinfo", cultureInfo);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLSIGNUP, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#2 User Verification
    public class RetrieveVerficationTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String EmailId, AccessTocken, Timexone, Code;
        private VerificationResponse verrsponse;

        public RetrieveVerficationTask(VerificationResponse verificationActivty, String code, String emailId, String AccessTocken, String timexone) {
            super();
            this.EmailId = emailId;
            this.AccessTocken = AccessTocken;
            this.Timexone = timexone;
            this.Code = code;
            verrsponse = verificationActivty;
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Verification", "try" + EmailId + " " + AccessTocken + " " + Timexone + " " + Code);
                    jsonParams.put("email", EmailId);
                    jsonParams.put("accessToken", AccessTocken);
                    jsonParams.put("timeZone", Timexone);
                    jsonParams.put("verificationCode", Code);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                client.post(context, URLHOST + URLVERIFICATION, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Verification", "ons failed" + responseString);
                        verrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Verification", "ons succscess" + responseString);

                        jsonParser.VerificationResponseParser(verrsponse, responseString);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#3 ForgotPassword
    public class RetrieveForgotTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String EmailId, Timexone, cultureInfo;

        private ForgotResponse forrsponse;

        public RetrieveForgotTask(ForgotResponse regrspons, String emailId, String timexone, String cultureInfo) {
            super();
            forrsponse = regrspons;
            this.EmailId = emailId;
            this.Timexone = timexone;
            this.cultureInfo = cultureInfo;
            Log.i("Forgot_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Forgot_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Forgot_submit", " try");

                    jsonParams.put("email", EmailId);
                    jsonParams.put("timeZone", Timexone);
                    jsonParams.put("cultureinfo", cultureInfo);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Forgot_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLFORGOT, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Forgot_submit", "ons failed" + responseString);

                        forrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Forgot_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationForgotParser(forrsponse, responseString);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Forgot_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#3 Attemp to Login
    public class AttemptLoginTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String deviceUid, number, password;

        private LoginResponse logrsponse;

        public AttemptLoginTask(LoginResponse logrsponse, String deviceUid, String number, String password) {
            super();
            this.logrsponse = logrsponse;
            this.deviceUid = deviceUid;
            this.number = number;
            this.password = password;
            Log.i("Attempt_login", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Attempt_login", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Attempt_login", " try" + number + " " + password + " " + deviceUid);

                    jsonParams.put("email", number);
                    jsonParams.put("password", password);
                    jsonParams.put("deviceUid", deviceUid);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Attempt_login", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLLOGIN, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Attempt_login", "ons failed" + responseString);

                        logrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Attempt_login", "ons succscess" + responseString);

                        jsonParser.AttemptLoginParser(logrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Attempt_login", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#4 Get Category List
    public class RetrieveGetCategoryListTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String LastName, FirstName, MobileNumber, EmailId, Password, DevideID, UserType, Timexone, cultureInfo;

        private GetCatDatas regrsponse;

        public RetrieveGetCategoryListTask(GetCatDatas regrspons) {
            super();
            regrsponse = regrspons;
            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try");

                    jsonParams.put("MainCategoryId", "2");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetCategoryListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#5 Get SUBCategory List
    public class RetrieveGetSubCategoryListTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String CatID;

        private GetCatDatas regrsponse;

        public RetrieveGetSubCategoryListTask(GetCatDatas regrspons, String catID) {
            super();
            regrsponse = regrspons;
            CatID = catID;
            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try" + CatID);

                    jsonParams.put("CategoryId", CatID);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETSUBCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed sub" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetSUBCategoryListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#6 Get State List
    public class RetrieveGetStateListTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String LastName, FirstName, MobileNumber, EmailId, Password, DevideID, UserType, Timexone, cultureInfo;
        private String CatID;

        private GetCatDatas regrsponse;

        public RetrieveGetStateListTask(GetCatDatas regrspons, String CatID  ) {
            super();
            regrsponse = regrspons;
            this.CatID = CatID;

            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try");

                    jsonParams.put("CountryId", CatID);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETSTATELIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetStateListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#7 Get State List
    public class RetrieveGetCityListTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String CatID;

        private GetCatDatas regrsponse;

        public RetrieveGetCityListTask(GetCatDatas regrspons, String catID) {
            super();
            regrsponse = regrspons;
            CatID = catID;
            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try" + CatID);

                    jsonParams.put("stateId", CatID);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETCITYLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed sub" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetCITYListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#8 FINAL REG -- FORM DATA REQUESTR
    public class RetrieveUploadFinalRegTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private RegistrationProviderResponse regrsponse;
        RequestParams params;
        private AsyncHttpClient clientx;


        public RetrieveUploadFinalRegTask(RegistrationPVRDetailesActivity regrspons, String userType, String userGender,
                                          String userDob, String currentLocation, String locationLat, String locationLong,
                                          String preLocation1, String preLocation1Lat, String preLocation1Long,
                                          String preLocation2, String preLocation2Lat, String preLocation2Long,
                                          String preLocation3, String preLocation3Lat, String preLocation3Long,
                                          Integer cityId, String userAddress, String userEducation, String userDesignation,
                                          String userExperience, String userWagesHour, String userAddidtionSkil, String categorys,
                                          String categorysFiltters, String cultureInfo, String accessToken,
                                          String timeZone, String employmentType, String languages, File profileImage) {
            clientx = new SyncHttpClient();
            clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
            clientx.addHeader("www-request-api-version", "1.0");
            clientx.addHeader("enctype", "multipart/form-data");


            regrsponse = regrspons;
            params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("UserType", userType);
            params.put("UserGender", userGender);
            params.put("UserDob", userDob);
            params.put("CurrentLocation", currentLocation);
            params.put("LocationLat", locationLat);
            params.put("LocationLong", locationLong);
            params.put("PreLocation1", preLocation1);
            params.put("PreLocation1Lat", preLocation1Lat);
            params.put("PreLocation1Long", preLocation1Long);
            params.put("PreLocation2", preLocation2);
            params.put("PreLocation2Lat", preLocation2Lat);
            params.put("PreLocation2Long", preLocation2Long);
            params.put("PreLocation3", preLocation3);
            params.put("PreLocation3Lat", preLocation3Lat);
            params.put("PreLocation3Long", preLocation3Long);
            params.put("CityId", cityId);
            params.put("UserAddress", userAddress);
            params.put("UserEducation", userEducation);
            params.put("UserDesignation", userDesignation);
            params.put("UserExperience", userExperience);
            params.put("UserWagesHour", userWagesHour);
            params.put("UserAdditionalSkill", userAddidtionSkil);

            try {
                params.put("ProfileImage", profileImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("Reg2_submit", " img" + e.getMessage());
            }
            params.put("Categorys", categorys);
            params.put("CategorysFiltters", categorysFiltters);
            params.put("cultureInfo", cultureInfo);
            params.put("accessToken", accessToken);
            params.put("timeZone", timeZone);
            params.put("EmploymentType", employmentType);
            params.put("languages", languages);


            Log.i("Reg2_submit", " constreuctor");

        }

        //        E36517F6-7FF1-4C9D-AB69-3E1ABC3DF81A
        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");
                clientx.post(URLHOST + URLUPLOADFINALREG, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseStringx = new String(responseBody);
                        Log.i("Reg2_submit", "ons succscess" + responseStringx + " " + Arrays.toString(headers) + " " + statusCode);
                        jsonParser.RegistrationFinalRegResponseParser(regrsponse, responseStringx, context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


//                        String responseStringx = new String(responseBody);
//                        Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
                        regrsponse.failedtoConnect();
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exceptionxxx" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#9 Get Category List - Home
    public class RetrieveGetCategoryListHomeTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private Context context;

        private HomeResponse regrsponse;

        public RetrieveGetCategoryListHomeTask(HomeResponse regrspons, Context context) {
            super();
            regrsponse = regrspons;
            this.context = context;
            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try");

                    jsonParams.put("MainCategoryId", "2");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.GetCategoryListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#10 Get Provider  List - Home
    public class RetrieveGetProviderListHomeTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private Context context;
        private String accessTocken, Category, Latitude, Logitude, timeZone, cultureinfo, Range,PriceRange,RatingRange,Filter;

        private HomeResponse regrsponse;

        public RetrieveGetProviderListHomeTask(HomeResponse regrspons, Context context, String accessTocken, String Category,
                                               String Latitude, String Logitude, String timeZone, String cultureinfo, String Range,
                                               String PriceRange, String RatingRange, String Filter) {

            super();
            regrsponse = regrspons;
            this.context = context;
            Log.i("Reg2_submit", " constreuctor");
            this.accessTocken = accessTocken.trim();
            this.Category = Category.trim();
            this.Latitude = Latitude;
            this.Logitude = Logitude;
            this.timeZone = timeZone;
            this.cultureinfo = cultureinfo;
            this.Range = Range;
            this.PriceRange = PriceRange;
            this.RatingRange = RatingRange;
            this.Filter = Filter;
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {

                    Log.i("Reg2_submitXXXX", " try"+ accessTocken+"- ");
                    Log.i("Reg2_submitXXXX", " try"+ Category+"-");
                    Log.i("Reg2_submitXXXX", " try"+ Latitude+"-");
                    Log.i("Reg2_submitXXXX", " try"+ Logitude+"-");
                    Log.i("Reg2_submitXXXX", " try"+ timeZone+"-");
                    Log.i("Reg2_submitXXXX", " try"+ cultureinfo+"-");
                    Log.i("Reg2_submitXXXX", " try"+ Range+"-");
                    Log.i("Reg2_submitXXXX", " try"+ PriceRange+"-");
                    Log.i("Reg2_submitXXXX", " try"+ RatingRange+"-");
                    Log.i("Reg2_submitXXXX", " try"+ Filter+"-");




                    jsonParams.put("accessToken", accessTocken);
                    jsonParams.put("Category", Category);
                    jsonParams.put("Latitude", Latitude);
                    jsonParams.put("Logitude", Logitude);
                    jsonParams.put("timeZone", timeZone);
                    jsonParams.put("cultureinfo", cultureinfo);
                    jsonParams.put("Range", Range);
//                    jsonParams.put("PriceRange", PriceRange);
//                    jsonParams.put("StarRating", RatingRange);
//                    jsonParams.put("subCategory", Filter);
                    jsonParams.put("PriceRange", PriceRange);
                    jsonParams.put("StarRating", RatingRange);
                    jsonParams.put("subCategory", Filter);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETPROVIDERLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.GetProviderListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#11 Get Provider  Full Details

    public class GetProviderDetailsTask extends AsyncTask<String, Void, String>  {

        private Exception exception;
        private Context context;
        private String providerBasic;

        private ProviderDetailsResponse regrsponse;

        public GetProviderDetailsTask(ProviderDetailsResponse regrspons, String providerBasic) {
            super();
            regrsponse = regrspons;
            this.providerBasic = providerBasic;

        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try : " + providerBasic);
                    jsonParams.put("accessToken", providerBasic);
                    //    jsonParams.put("accessToken","EC98916D-9F4F-4609-9D56-00C6F979EFEF");
                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());
                }

                client.post(context, URLHOST + URLGETPROVIDERDETAILS, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "xxxxons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "xxxxons succscess" + responseString);
                        jsonParser.GetProviderDetailsResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "xxxxxexception" + e.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    //#12 ForgotPassword
    public class SetNewPasswordTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String tocken, old, newp, devid;

        private ChangePassResponse forrsponse;

        public SetNewPasswordTask(ChangePassResponse regrspons, String tocken, String old, String newp, String devid) {
            super();
            forrsponse = regrspons;
            this.tocken = tocken;
            this.old = old;
            this.newp = newp;
            this.devid = devid;
            Log.i("SetNewPasswordTask", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("SetNewPasswordTask", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("SetNewPasswordTask", " try");

                    jsonParams.put("accessToken", tocken);
                    jsonParams.put("oldPassword", old);
                    jsonParams.put("UserPassword", newp);
                    jsonParams.put("DeviceUID", devid);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("SetNewPasswordTask", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLCHNGPWD, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("SetNewPasswordTask", "ons failed" + responseString);

                        forrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("SetNewPasswordTask", "ons succscess" + responseString);

                        jsonParser.ChangePasswordParser(forrsponse, responseString);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("SetNewPasswordTask", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#13 Get Country List
    public class RetrieveGetCountryListTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String LastName, FirstName, MobileNumber, EmailId, Password, DevideID, UserType, Timexone, cultureInfo;

        private GetCatDatas regrsponse;

        public RetrieveGetCountryListTask(GetCatDatas regrspons) {
            super();
            regrsponse = regrspons;
            Log.i("Reg2_submit", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try");

                    jsonParams.put("CountryId", "1");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.get(context, URLHOST + URLGETCOUNTRYLIST, null, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", " COuntryons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "COuntryons succscess" + responseString);

                        jsonParser.RegistrationProciderGetCountryListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }



    //#14 Update Profile data - Form data request
    public class UpdateprofiledataTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private ProfileUpdateListner regrsponse;
        private RequestParams params;
        private AsyncHttpClient clientx;


        public UpdateprofiledataTask(ProfileUpdateListner regrspons, UserModel user, File profileImage) {
            clientx = new SyncHttpClient();
            clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
            clientx.addHeader("www-request-api-version", "1.0");
            clientx.addHeader("enctype", "multipart/form-data");


            regrsponse = regrspons;
            params = new RequestParams();
            params.setForceMultipartEntityContentType(true);


           if( user.getAccessToken()==null)
           {
               user.setAccessToken("");
           }
            params.put("accessToken", user.getAccessToken());
            Log.i("Reg2_submit", " params "+ user.getAccessToken());
            if( user.getUserType()==null)
            {
                user.setUserType("");
            }
            params.put("UserType", user.getUserType());
            Log.i("Reg2_submit", " params "+ user.getUserType());
            if( user.getDob()==null)
            {
                user.setDob("");
            }
            params.put("UserDob", user.getDob());
            Log.i("Reg2_submit", " params "+ user.getDob());
            if( user.getCurrentLocation()==null)
            {
                user.setCurrentLocation("");
            }
            params.put("CurrentLocation", user.getCurrentLocation());
            Log.i("Reg2_submit", " params "+ user.getCurrentLocation());
            if( user.getLocationLat()==null)
            {
                user.setLocationLat("");
            }
            params.put("LocationLat", user.getLocationLat());
            Log.i("Reg2_submit", " params "+ user.getLocationLat());

            if( user.getLocationLong()==null)
            {
                user.setLocationLong("");
            }
            params.put("LocationLong", user.getLocationLong());
            Log.i("Reg2_submit", " params "+ user.getLocationLong());
            if( user.getPreLocation1()==null)
            {
                user.setPreLocation1("");
            }
            params.put("PreLocation1", user.getPreLocation1());
            Log.i("Reg2_submit", " params "+ user.getPreLocation1());
            if( user.getPreLocation1Lat()==null)
            {
                user.setPreLocation1Lat("");
            }
            params.put("PreLocation1Lat", user.getPreLocation1Lat());
            Log.i("Reg2_submit", " params "+ user.getPreLocation1Lat());
            if( user.getPreLocation1Long()==null)
            {
                user.setPreLocation1Long("");
            }
            params.put("PreLocation1Long", user.getPreLocation1Long());
            Log.i("Reg2_submit", " params "+ user.getPreLocation1Long());
            if( user.getPreLocation2()==null)
            {
                user.setPreLocation2("");
            }
            params.put("PreLocation2", user.getPreLocation2());
            Log.i("Reg2_submit", " params "+ user.getPreLocation2());

            if( user.getPreLocation2Lat()==null)
            {
                user.setPreLocation2Lat("");
            }
            params.put("PreLocation2Lat", user.getPreLocation2Lat());
            Log.i("Reg2_submit", " params "+ user.getPreLocation2Lat());
            if( user.getPreLocation2Long()==null)
            {
                user.setPreLocation2Long("");
            }
            params.put("PreLocation2Long",  user.getPreLocation2Long());
            Log.i("Reg2_submit", " params "+ user.getPreLocation2Long());
            if( user.getPreLocation3()==null)
            {
                user.setPreLocation3("");
            }
            params.put("PreLocation3", user.getPreLocation3());
            Log.i("Reg2_submit", " params "+ user.getPreLocation3());
            if( user.getPreLocation3Lat()==null)
            {
                user.setPreLocation3Lat("");
            }
            params.put("PreLocation3Lat",  user.getPreLocation3Lat());
            Log.i("Reg2_submit", " params "+ user.getPreLocation3Lat());
            if( user.getPreLocation3Long()==null)
            {
                user.setPreLocation3Long("");
            }
            params.put("PreLocation3Long",  user.getPreLocation3Long());
            Log.i("Reg2_submit", " params "+ user.getPreLocation3Long());

            if( user.getCityId()==null)
            {
                user.setCityId("");
            }
            params.put("CityId", user.getCityId());
            Log.i("Reg2_submit", " params "+ user.getCityId());
            if( user.getUserAddress()==null)
            {
                user.setUserAddress("");
            }
            params.put("UserAddress", user.getUserAddress());
            Log.i("Reg2_submit", " params "+ user.getUserAddress());
            if( user.getUserEducation()==null)
            {
                user.setUserEducation("");
            }
            params.put("UserEducation", user.getUserEducation());
            Log.i("Reg2_submit", " params "+ user.getUserEducation());
            if( user.getUserDesignation()==null)
            {
                user.setUserDesignation("");
            }
            params.put("UserDesignation", user.getUserDesignation());
            Log.i("Reg2_submit", " params "+ user.getUserDesignation());
            if( user.getUserExperience()==null)
            {
                user.setUserExperience("");
            }
            params.put("UserExperience", user.getUserExperience());
            Log.i("Reg2_submit", " params "+ user.getUserExperience());

            if( user.getUserWagesHour()==null)
            {

                user.setUserWagesHour(0.0);

            }
            else {
                if( user.getUserWagesHour().toString().isEmpty()) {
                    user.setUserWagesHour(0.0);
                }
            }
            params.put("UserWagesHour", user.getUserWagesHour().toString());
            Log.i("Reg2_submit", " params "+ user.getUserWagesHour());
            if( user.getUserAdditionalSkill()==null)
            {
                user.setUserAdditionalSkill("");
            }
            params.put("UserAdditionalSkill", user.getUserAdditionalSkill());
            Log.i("Reg2_submit", " params "+ user.getUserAdditionalSkill());


//            try {
//                params.put("ProfileImage", profileImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Log.i("Reg2_submit", " img" + e.getMessage());
//            }
            if(profileImage!=null)
            { try {
                params.put("file", profileImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("Reg2_submit", " img" + e.getMessage());
                Log.i("SAVINGIGNG"," image :"+e.getMessage());

            }}

            if( user.getCategoryName()==null)
            {
                params.put("Categorys","");

            }
            else
            params.put("Categorys", user.getCategoryName()[0]);

            Log.i("Reg2_submit", " params "+ user.getCategoryName()[0]);
            if( user.getFilterName()==null)
            {
                params.put("CategoryFiltters","");
            }
            else
            params.put("CategoryFiltters", user.getFilterName()[0]);

            Log.i("Reg2_submit", " params "+ user.getFilterName()[0]);

            if( user.getCultureinfo()==null)
            {
                user.setCultureinfo("");
            }
            params.put("cultureInfo", user.getCultureinfo());
            Log.i("Reg2_submit", " params "+ user.getCultureinfo());
            if( user.getTimeZone()==null)
            {
                user.setTimeZone("");
            }
            params.put("timeZone", user.getTimeZone());
            Log.i("Reg2_submit", " params "+ user.getTimeZone());
            if( user.getEmploymentType()==null)
            {
                user.setEmploymentType("");
            }
            params.put("UserEmploymentType", user.getEmploymentType());
            Log.i("Reg2_submit", " params "+ user.getEmploymentType());
            if( user.getLanguagesknown()==null)
            {
                user.setLanguagesknown("");
            }
            params.put("UserLanguagesKnown", user.getLanguagesknown());
            Log.i("Reg2_submit", " params "+ user.getLanguagesknown());

            if( user.getUserGender()==null)
            {
                user.setUserGender("");
            }
            params.put("UserGender", user.getUserGender());
            Log.i("Reg2_submit", " params "+ user.getUserGender());
            if( user.getUserEmail()==null)
            {
                user.setUserEmail("");
            }
            params.put("UserEmail", user.getUserEmail());
            Log.i("Reg2_submit", " params "+ user.getUserEmail());
            if( user.getUserMobile()==null)
            {
                user.setUserMobile("");
            }
            params.put("UserMobil", user.getUserMobile());
            Log.i("Reg2_submit", " params "+ user.getUserMobile());
            if( user.getUserFirstName()==null)
            {
                user.setUserFirstName("");
            }
            params.put("UserFirstName", user.getUserFirstName());
            Log.i("Reg2_submit", " params "+ user.getUserFirstName());
            if( user.getUserType()==null)
            {
                user.setUserLastName("");
            }
            params.put("UserLastName", user.getUserLastName());
            Log.i("Reg2_submit", " params "+ user.getUserLastName());

            params.put("DeviceId","");
            params.put("ProfileImage","");
            params.put("UserEducationType","");

            Log.i("Reg2_submit", " params "+ user.getAccessToken());



            Log.i("Reg2_submit", " constreuctor");

            if(params==null)
            {
                Log.i("Reg2_submit", " params null");

            }
            else {
                Log.i("Reg2_submit", " not nulll");

            }
        }

        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg "+ params.toString());
                clientx.post(URLHOST + URLUPDATEPROFILE, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseStringx = new String(responseBody);
                        Log.i("Reg2_submitXY", "ons succscess" + responseStringx + " " + Arrays.toString(headers) + " " + statusCode);

                        jsonParser.UpdateProfileParser(regrsponse, responseStringx, context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


//                        String responseStringx = new String(responseBody);
//                        Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
                        regrsponse.failedtoConnect();
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception in call" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#15 Upload Image - Form data request
    public class SaveProfileImage extends AsyncTask<String, Void, String> {

        private Exception exception;
        private ImageUploader regrsponse;
        private RequestParams params;
        private AsyncHttpClient clientx;


        public SaveProfileImage(ImageUploader regrspons,  File profileImage,String Accesstocken) {
            clientx = new SyncHttpClient();
            clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
            clientx.addHeader("www-request-api-version", "1.0");
            clientx.addHeader("enctype", "multipart/form-data");


            regrsponse = regrspons;
            params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
//            params.put("accessToken", Accesstocken);
            params.put("accessToken", "8F432A95-21BF-4F27-8DAB-1F81BD95E233");
            Log.i("Reg2_submit", " params "+ Accesstocken);


            if(profileImage!=null)
            { try {
                params.put("file", profileImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("Reg2_submit", " img" + e.getMessage());
                Log.i("SAVINGIGNG"," image :"+e.getMessage());

            }}


        }

        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg "+ params.toString());
                clientx.post(URLHOST + URLIMAGEUPLOAD, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseStringx = new String(responseBody);
                        Log.i("Reg2_submitXY", "ons succscess" + responseStringx + " " + Arrays.toString(headers) + " " + statusCode);

                        jsonParser.ImageUploadParese(regrsponse, responseStringx, context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


//                        String responseStringx = new String(responseBody);
//                        Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
                        regrsponse.failedtoConnect();
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "exception in call" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#16 Get Loc and Servicces Selected List
    public class RetrieveSelectionServiceTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;

        private ServiceandLocListner regrsponse;

        public RetrieveSelectionServiceTask(ServiceandLocListner regrspons,String Accesstocken) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            Log.i("PROFFF", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFF", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFF", " try");

                    jsonParams.put("accessToken", Accesstocken);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFF", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETSELECTEDSERVICES, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFF", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFF", "ons succscess" + responseString);

                        jsonParser.GetServicesListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFF", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

 //#17 Get Loc and Servicces Selected List
    public class RetrieveLocationTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;

        private ServiceandLocListner regrsponse;

        public RetrieveLocationTask(ServiceandLocListner regrspons,String Accesstocken) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            Log.i("PROFFF", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFF", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFF", " try");

                    jsonParams.put("accessToken", Accesstocken);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFF", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLGETSELECTEDLOCATIONS, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFF", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFF", "ons succscess" + responseString);

                        jsonParser.GetLocationListResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFF", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }
 //#18 Add Servics List
    public class AddServiceTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;
     private Services newService;

        private ServiceandLocListner regrsponse;

        public AddServiceTask(ServiceandLocListner regrspons,String Accesstocken,Services newService) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            this.newService = newService;
            Log.i("PROFFF", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFF", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFF", " try");
//                    jsonParams.put("UserId", 0);
                    jsonParams.put("accessToken", Accesstocken);
                    jsonParams.put("ServiceId", newService.getServiceId());
                    jsonParams.put("FilterId", TextUtils.join(", ", newService.getSubServiceId()));
                    jsonParams.put("Wages", Integer.valueOf(newService.getWages().trim()));
                    jsonParams.put("Experience", Float.valueOf(newService.getExperiance().trim()));

                    Log.i("PROFFF", " jsonParams :  "+jsonParams.toString());

                    entity = new StringEntity(jsonParams.toString());
                    Log.i("PROFFF", " entity :  "+entity);


                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFF", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLADDSERVICE, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFF", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFF", "ons succscess" + responseString);

                        jsonParser.AddServiceResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFF", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

 //#19 Add location List
    public class AddLocationTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;
     private PlaceLoc newService;

        private ServiceandLocListner regrsponse;

        public AddLocationTask(ServiceandLocListner regrspons,String Accesstocken,PlaceLoc newService) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            this.newService = newService;
            Log.i("PROFFF", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFF", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFF", " try");
//                    jsonParams.put("Id", 0);
                    jsonParams.put("accessToken", Accesstocken);
                    jsonParams.put("PreLocation", newService.getAddress());
                    jsonParams.put("PreLocationLat",String.valueOf(newService.getLatitude()));
                    jsonParams.put("PreLocationLong",String.valueOf(newService.getLongitude()));
                    jsonParams.put("ZIP",0);

                    Log.i("PROFFF", " jsonParams :  "+jsonParams.toString());

                    entity = new StringEntity(jsonParams.toString());
                    Log.i("PROFFF", " entity :  "+entity);


                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFF", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLADDLOCATION, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFF", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFF", "ons succscess" + responseString);

                        jsonParser.AddLocationResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFF", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#20 Personal Details -- FORM DATA REQUESTR
    public class SubmitPersonalDetailsTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private PersonalDetailsResponse regrsponse;
        RequestParams params;
        private AsyncHttpClient clientx;


        public SubmitPersonalDetailsTask(PersonalDetailsResponse regrspons, String AccessTocken,String gendertext,String countrytext,String statetext,
                                         String  citytext,String edutext,String dobtext, String addresstext,String langugetext,String ziptext,String photurl) {
            clientx = new SyncHttpClient();
            clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
            clientx.addHeader("www-request-api-version", "1.0");
            clientx.addHeader("enctype", "multipart/form-data");


            regrsponse = regrspons;
            params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("accessToken", AccessTocken);
            params.put("UserGender", gendertext);
            params.put("UserDob", dobtext);
            params.put("UserLanguagesKnown", langugetext);
            params.put("UserEducation", edutext);
            params.put("UserAddress", addresstext);
            params.put("CountryId", countrytext);
            params.put("StateId", statetext);
            params.put("CityId", citytext);
            params.put("ZIP", ziptext);
            params.put("ProfileImage", photurl);



            Log.i("PERSONL", " constreuctor" +params.toString());

        }

        //        E36517F6-7FF1-4C9D-AB69-3E1ABC3DF81A
        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PERSONL", " doin bg");
                clientx.post(URLHOST + URLPERSONALREG, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseStringx = new String(responseBody);
                        Log.i("PERSONL", "ons succscess" + responseStringx + " " + Arrays.toString(headers) + " " + statusCode);
                        jsonParser.InsertPersonalInformationResponseParser(regrsponse, responseStringx, context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


//                        String responseStringx = new String(responseBody);
//                        Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
                        regrsponse.failedtoConnect();
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PERSONL", "exceptionxxx" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

    //#21 Delete Service
    public class DeleteServiceTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;
        private String Sericid;

        private ServiceandLocListner regrsponse;

        public DeleteServiceTask(ServiceandLocListner regrspons,String Accesstocken,String Sericid) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            this.Sericid = Sericid;
            Log.i("PROFFFDEL", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFFDEL", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFFDEL", " try");
//                    jsonParams.put("Id", 0);
                    jsonParams.put("accessToken", Accesstocken);
                    jsonParams.put("ServiceId",Sericid);
                    Log.i("PROFFFDEL", " jsonParams :  "+jsonParams.toString());

                    entity = new StringEntity(jsonParams.toString());
                    Log.i("PROFFFDEL", " entity :  "+entity);


                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFFDEL", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLDELETESERVICE, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFFDEL", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFFDEL", "ons succscess" + responseString);

                        jsonParser.DeleteServiceResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFFDEL", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }
    //#22 Delete location
    public class DeleteLocationTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private String Accesstocken;
        private String Sericid;

        private ServiceandLocListner regrsponse;

        public DeleteLocationTask(ServiceandLocListner regrspons,String Accesstocken,String Sericid) {
            super();
            regrsponse = regrspons;
            this.Accesstocken = Accesstocken;
            this.Sericid = Sericid;
            Log.i("PROFFFDEL", " constreuctor");
        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("PROFFFDEL", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("PROFFFDEL", " try");
//                    jsonParams.put("Id", 0);
//                    jsonParams.put("accessToken", Accesstocken);
                    jsonParams.put("locationId",Sericid);
                    Log.i("PROFFFDEL", " jsonParams :  "+jsonParams.toString());

                    entity = new StringEntity(jsonParams.toString());
                    Log.i("PROFFFDEL", " entity :  "+entity);


                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("PROFFFDEL", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST + URLDELETELOCATION, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("PROFFFDEL", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("PROFFFDEL", "ons succscess" + responseString);

                        jsonParser.DeleteLocationResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("PROFFFDEL", "exception" + e.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }


    //#23 Get Account Full Details

    public class GetProfileDataTask extends AsyncTask<String, Void, String>  {

        private Exception exception;
        private Context context;
        private String providerBasic;

        private ProviderDetailsResponse regrsponse;

        public GetProfileDataTask(ProviderDetailsResponse regrspons, String providerBasic) {
            super();
            regrsponse = regrspons;
            this.providerBasic = providerBasic;

        }


        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit", " doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit", " try : " + providerBasic);
                    jsonParams.put("accessToken", providerBasic);
                    //    jsonParams.put("accessToken","EC98916D-9F4F-4609-9D56-00C6F979EFEF");
                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());
                }

                client.post(context, URLHOST + URLGETPROVIDERDETAILS, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "xxxxons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "xxxxons succscess" + responseString);
                        jsonParser.GetProviderDetailsResponseParser(regrsponse, responseString, context);
                    }
                });
                return null;

            } catch (Exception e) {
                this.exception = e;
                Log.i("Reg2_submit", "xxxxxexception" + e.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


}
