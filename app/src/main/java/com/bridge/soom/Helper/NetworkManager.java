package com.bridge.soom.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.bridge.soom.Activity.RegistrationPVRDetailesActivity;
import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Interface.LoginResponse;
import com.bridge.soom.Interface.ProviderDetailsResponse;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Interface.RegistrationResponse;
import com.bridge.soom.Interface.VerificationResponse;
import com.bridge.soom.Model.ProviderBasic;
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

import static com.bridge.soom.Helper.Constants.URLFORGOT;
import static com.bridge.soom.Helper.Constants.URLGETCATLIST;
import static com.bridge.soom.Helper.Constants.URLGETCITYLIST;
import static com.bridge.soom.Helper.Constants.URLGETPROVIDERDETAILS;
import static com.bridge.soom.Helper.Constants.URLGETPROVIDERLIST;
import static com.bridge.soom.Helper.Constants.URLGETSTATELIST;
import static com.bridge.soom.Helper.Constants.URLGETSUBCATLIST;
import static com.bridge.soom.Helper.Constants.URLHOST;
import static com.bridge.soom.Helper.Constants.URLLOGIN;
import static com.bridge.soom.Helper.Constants.URLSIGNUP;
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
    private String  LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo;

    private RegistrationResponse regrsponse ;
            public RetrieveRegistrationTask(RegistrationResponse regrspons, String lastName, String firstName, String mobileNumber, String emailId, String password, String devideID, String userType, String timexone, String cultureInfo) {
                super();
                regrsponse= regrspons;
                this.LastName =lastName;
                        this.FirstName=firstName;
                this.MobileNumber=mobileNumber;
                        this.EmailId=emailId;
                this.Password=password;
                        this.DevideID=devideID;
                this.UserType=userType;
                        this.Timexone=timexone;
                this.cultureInfo=cultureInfo;
                Log.i("Reg_submit"," constreuctor");
            }



            protected String doInBackground(String... urls) {
                try {

                    //check if needs this header or I can take off this and leave just the url+token2
                    Log.i("Reg_submit"," doin bg");

                    JSONObject jsonParams = new JSONObject();
                    StringEntity entity = null;
                    try {
                        Log.i("Reg_submit"," try");

                        jsonParams.put("UserEmail",EmailId);
                        jsonParams.put("UserMobil", MobileNumber);
                        jsonParams.put("UserFirstName", FirstName);
                        jsonParams.put("UserLastName",LastName);
                        jsonParams.put("UserPassword",Password);
                        jsonParams.put("DeviceUID", DevideID);
                        jsonParams.put("UserType", UserType);
                        jsonParams.put("TimeZone",Timexone);
                        jsonParams.put("cultureinfo", cultureInfo);

                        entity = new StringEntity(jsonParams.toString());

                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.i("Reg_submit", "exception1" + e.getMessage());

                    }

                    client.post(context, URLHOST+URLSIGNUP, entity, "application/json", new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.i("Reg_submit", "ons failed" + responseString);
                            regrsponse.failedtoConnect();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.i("Reg_submit", "ons succscess" + responseString);

                          jsonParser.RegistrationResponseParser(regrsponse,responseString,context);
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
        private String EmailId,AccessTocken,Timexone,Code;
        private VerificationResponse verrsponse ;

        public RetrieveVerficationTask(VerificationResponse verificationActivty, String code, String emailId, String AccessTocken, String timexone) {
            super();
            this.EmailId=emailId;
            this.AccessTocken=AccessTocken;
            this.Timexone=timexone;
            this.Code=code;
            verrsponse = verificationActivty;
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Verification", "try" + EmailId+" "+AccessTocken+" "+Timexone+" "+Code);
                    jsonParams.put("email",EmailId);
                    jsonParams.put("accessToken", AccessTocken);
                    jsonParams.put("timeZone", Timexone);
                    jsonParams.put("verificationCode",Code);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                client.post(context, URLHOST+URLVERIFICATION, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Verification", "ons failed" + responseString);
                        verrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Verification", "ons succscess" + responseString);

                        jsonParser.VerificationResponseParser(verrsponse,responseString);
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
        private String EmailId,Timexone,cultureInfo;

        private ForgotResponse forrsponse ;
        public RetrieveForgotTask(ForgotResponse regrspons,  String emailId,  String timexone, String cultureInfo) {
            super();
            forrsponse= regrspons;
            this.EmailId=emailId;
            this.Timexone=timexone;
            this.cultureInfo=cultureInfo;
            Log.i("Forgot_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Forgot_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Forgot_submit"," try");

                    jsonParams.put("email",EmailId);
                    jsonParams.put("timeZone",Timexone);
                    jsonParams.put("cultureinfo", cultureInfo);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Forgot_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLFORGOT, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Forgot_submit", "ons failed" + responseString);

                        forrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Forgot_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationForgotParser(forrsponse,responseString);
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
        private String deviceUid,number,password;

        private LoginResponse logrsponse ;
        public AttemptLoginTask(LoginResponse logrsponse, String deviceUid, String number, String password) {
            super();
            this.logrsponse= logrsponse;
            this.deviceUid=deviceUid;
            this.number=number;
            this.password=password;
            Log.i("Attempt_login"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Attempt_login"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Attempt_login"," try"+number+" "+password+" "+deviceUid);

                    jsonParams.put("email",number);
                    jsonParams.put("password",password);
                    jsonParams.put("deviceUid", deviceUid);

                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Attempt_login", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLLOGIN, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Attempt_login", "ons failed" + responseString);

                        logrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Attempt_login", "ons succscess" + responseString);

                        jsonParser.AttemptLoginParser(logrsponse,responseString,context);
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
        private String  LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo;

        private RegistrationProviderResponse regrsponse ;
        public RetrieveGetCategoryListTask(RegistrationProviderResponse regrspons) {
            super();
            regrsponse= regrspons;
            Log.i("Reg2_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try");

                    jsonParams.put("MainCategoryId","2");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetCategoryListResponseParser(regrsponse,responseString,context);
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
        private String  CatID;

        private RegistrationProviderResponse regrsponse ;
        public RetrieveGetSubCategoryListTask(RegistrationProviderResponse regrspons,String catID) {
            super();
            regrsponse= regrspons;
            CatID = catID;
            Log.i("Reg2_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try"+CatID);

                    jsonParams.put("CategoryId",CatID);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETSUBCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed sub" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetSUBCategoryListResponseParser(regrsponse,responseString,context);
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
        private String  LastName,FirstName,MobileNumber,EmailId,Password,DevideID,UserType,Timexone,cultureInfo;

        private RegistrationProviderResponse regrsponse ;
        public RetrieveGetStateListTask(RegistrationProviderResponse regrspons) {
            super();
            regrsponse= regrspons;
            Log.i("Reg2_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try");

                    jsonParams.put("CountryId","1");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETSTATELIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetStateListResponseParser(regrsponse,responseString,context);
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
        private String  CatID;

        private RegistrationProviderResponse regrsponse ;
        public RetrieveGetCityListTask(RegistrationProviderResponse regrspons,String catID) {
            super();
            regrsponse= regrspons;
            CatID = catID;
            Log.i("Reg2_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try"+CatID);

                    jsonParams.put("stateId",CatID);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETCITYLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed sub" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.RegistrationProciderGetCITYListResponseParser(regrsponse,responseString,context);
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
        private RegistrationProviderResponse regrsponse ;
        RequestParams params ;
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
            clientx =  new SyncHttpClient();
          clientx.addHeader("www-request-type", "SOOM2WAPP07459842");
           clientx.addHeader("www-request-api-version", "1.0");
            clientx.addHeader("enctype", "multipart/form-data");


            regrsponse= regrspons;
            params = new RequestParams();
            params.setForceMultipartEntityContentType(true);
            params.put("UserType",userType);
            params.put("UserGender",userGender);
            params.put("UserDob",userDob);
            params.put("CurrentLocation",currentLocation);
            params.put("LocationLat",locationLat);
            params.put("LocationLong",locationLong);
            params.put("PreLocation1",preLocation1);
            params.put("PreLocation1Lat",preLocation1Lat);
            params.put("PreLocation1Long",preLocation1Long);
            params.put("PreLocation2",preLocation2);
            params.put("PreLocation2Lat",preLocation2Lat);
            params.put("PreLocation2Long",preLocation2Long);
            params.put("PreLocation3",preLocation3);
            params.put("PreLocation3Lat",preLocation3Lat);
            params.put("PreLocation3Long",preLocation3Long);
            params.put("CityId",cityId);
            params.put("UserAddress",userAddress);
            params.put("UserEducation",userEducation);
            params.put("UserDesignation",userDesignation);
            params.put("UserExperience",userExperience);
            params.put("UserWagesHour",userWagesHour);
            params.put("UserAdditionalSkill",userAddidtionSkil);

            try {
                params.put("ProfileImage",profileImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," img"+e.getMessage());
            }
            params.put("Categorys",categorys);
            params.put("CategorysFiltters",categorysFiltters);
            params.put("cultureInfo",cultureInfo);
            params.put("accessToken",accessToken);
            params.put("timeZone",timeZone);
            params.put("EmploymentType",employmentType);
            params.put("languages",languages);


            Log.i("Reg2_submit"," constreuctor");

        }

//        E36517F6-7FF1-4C9D-AB69-3E1ABC3DF81A
        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");
                clientx.post(URLHOST + URLUPLOADFINALREG, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseStringx = new String(responseBody);
                        Log.i("Reg2_submit", "ons succscess" + responseStringx + " " + Arrays.toString(headers)+" "+statusCode);
                        jsonParser.RegistrationFinalRegResponseParser(regrsponse, responseStringx, context);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                        String responseStringx = new String(responseBody);
                        Log.i("Reg2_submit", "ons failed sub" + responseStringx + " " + Arrays.toString(headers));
                        regrsponse.failedtoConnect();
                    }
                } );
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



    //#9 Get Category List - Home
    public class RetrieveGetCategoryListHomeTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private Context context;

        private HomeResponse regrsponse ;
        public RetrieveGetCategoryListHomeTask(HomeResponse regrspons,Context context) {
            super();
            regrsponse= regrspons;
            this.context = context;
            Log.i("Reg2_submit"," constreuctor");
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try");

                    jsonParams.put("MainCategoryId","2");


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETCATLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.GetCategoryListResponseParser(regrsponse,responseString,context);
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
        private String accessTocken,Category,Latitude,Logitude,timeZone,cultureinfo,Range;

        private HomeResponse regrsponse ;
        public RetrieveGetProviderListHomeTask(HomeResponse regrspons,Context context,String accessTocken,String Category,
                                               String Latitude,String Logitude,String timeZone,String cultureinfo,String Range) {
            super();
            regrsponse= regrspons;
            this.context = context;
            Log.i("Reg2_submit"," constreuctor");
            this.accessTocken = accessTocken;
            this.Category = Category;
            this.Latitude= Latitude;
            this.Logitude = Logitude;
            this.timeZone = timeZone;
            this.cultureinfo = cultureinfo;
            this.Range = Range;
        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try");

                    jsonParams.put("accessToken","EC98916D-9F4F-4609-9D56-00C6F979EFEF");
                    jsonParams.put("Category",Category);
                    jsonParams.put("Latitude",Latitude);
                    jsonParams.put("Logitude",Logitude);
                    jsonParams.put("timeZone",timeZone);
                    jsonParams.put("cultureinfo",cultureinfo);
                    jsonParams.put("Range",Range);


                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());

                }

                client.post(context, URLHOST+URLGETPROVIDERLIST, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "ons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "ons succscess" + responseString);

                        jsonParser.GetProviderListResponseParser(regrsponse,responseString,context);
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

    public class GetProviderDetailsTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private Context context;
        private ProviderBasic providerBasic;

        private ProviderDetailsResponse regrsponse ;
        public GetProviderDetailsTask(ProviderDetailsResponse regrspons, ProviderBasic providerBasic) {
            super();
            regrsponse= regrspons;
            this.providerBasic = providerBasic;

        }



        protected String doInBackground(String... urls) {
            try {

                //check if needs this header or I can take off this and leave just the url+token2
                Log.i("Reg2_submit"," doin bg");

                JSONObject jsonParams = new JSONObject();
                StringEntity entity = null;
                try {
                    Log.i("Reg2_submit"," try");
                    jsonParams.put("accessToken",providerBasic.getAccessTocken());
                //    jsonParams.put("accessToken","EC98916D-9F4F-4609-9D56-00C6F979EFEF");
                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.i("Reg2_submit", "exception1" + e.getMessage());
                }

                client.post(context, URLHOST+URLGETPROVIDERDETAILS, entity, "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("Reg2_submit", "xxxxons failed" + responseString);
                        regrsponse.failedtoConnect();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("Reg2_submit", "xxxxons succscess" + responseString);
                        jsonParser.GetProviderDetailsResponseParser(regrsponse,responseString,context);
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
