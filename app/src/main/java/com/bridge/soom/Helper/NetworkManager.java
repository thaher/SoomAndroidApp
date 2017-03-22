package com.bridge.soom.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.Interface.LoginResponse;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Interface.RegistrationResponse;
import com.bridge.soom.Interface.VerificationResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static com.bridge.soom.Helper.Constants.URLFORGOT;
import static com.bridge.soom.Helper.Constants.URLGETCATLIST;
import static com.bridge.soom.Helper.Constants.URLGETSTATELIST;
import static com.bridge.soom.Helper.Constants.URLGETSUBCATLIST;
import static com.bridge.soom.Helper.Constants.URLHOST;
import static com.bridge.soom.Helper.Constants.URLLOGIN;
import static com.bridge.soom.Helper.Constants.URLSIGNUP;
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

    //#4 Get State List
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


}
