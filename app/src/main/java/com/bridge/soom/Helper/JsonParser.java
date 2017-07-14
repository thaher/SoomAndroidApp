package com.bridge.soom.Helper;

import android.content.Context;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.DEVICE_ID;
import static com.bridge.soom.Helper.Constants.IMAGEPREFIX;
import static com.bridge.soom.Helper.Constants.USER_EMAIL;
import static com.bridge.soom.Helper.Constants.USER_FIRST_NAME;
import static com.bridge.soom.Helper.Constants.USER_IMAGE_URL;
import static com.bridge.soom.Helper.Constants.USER_LAST_NAME;
import static com.bridge.soom.Helper.Constants.USER_STATUS_LEVEL;
import static com.bridge.soom.Helper.Constants.USER_TYPE;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public class JsonParser {

    public JsonParser() {
    }


    public void RegistrationResponseParser(RegistrationResponse regrsponse, String jsonStr, Context context) {

//        {
//            "success": true,
//                "signupResponse": {
//                    "UserId": 1,
//                    "accessToken": "sample string 1"
//        },
//            "error": {
//            "errorCode": "sample string 1",
//                    "errorTitle": "sample string 2",
//                    "errorDetail": "sample string 3"
//        }
//        }

//new resp
//        {
//            "success": true,
//                "signupResponse": {
//            "userId": 4442,
//                    "accessToken": "100087d2-175f-4db1-abf1-b97c05d64789",
//                    "userEmail": "thdgh@gh.com                                      ",
//                    "userType": "USR       ",
//                    "userFirstName": "tsp",
//                    "userLastName": "does",
//                    "userStatusLevel": 1
//        }
//        }
//
        SharedPreferencesManager.init(context);


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.i("Reg_submit", "ons succscess parsing" );

                if(jsonObj.getBoolean("success"))
               {
//                   SharedPreferencesManager.write(USER_ID,String.valueOf(jsonObj.getJSONObject("signupResponse").getInt("userId")));

                   SharedPreferencesManager.write(ACCESS_TOCKEN,String.valueOf(jsonObj.getJSONObject("signupResponse").getString("accessToken")));
                   SharedPreferencesManager.write(USER_EMAIL,String.valueOf(jsonObj.getJSONObject("signupResponse").getString("userEmail")));
                   SharedPreferencesManager.write(USER_TYPE,String.valueOf(jsonObj.getJSONObject("signupResponse").getString("userType")));
                   SharedPreferencesManager.write(USER_FIRST_NAME,String.valueOf(jsonObj.getJSONObject("signupResponse").getString("userFirstName")));
                   SharedPreferencesManager.write(USER_LAST_NAME,String.valueOf(jsonObj.getJSONObject("signupResponse").getString("userLastName")));
                   SharedPreferencesManager.write(USER_STATUS_LEVEL,String.valueOf(jsonObj.getJSONObject("signupResponse").getInt("userStatusLevel")));

                   regrsponse.registrationResponseSuccess("success");
                   Log.i("Reg_submit", "ons succscess parsing2" );

               }
               else {
                   String msg ="Registration Failed";
                   if(jsonObj.has("error"))
                   {
                       JSONObject error = jsonObj.getJSONObject("error");
                       msg = error.getString("errorDetail");
                   }


                   regrsponse.registrationResponseFailed(msg);

               }

            } catch (JSONException e) {
                Log.i("Reg_submit", "exceptrion"+e.getMessage() );

                e.printStackTrace();
            }
        }



    }

    public void VerificationResponseParser(VerificationResponse verrsponse, String jsonStr ) {


////            {
//        "isVerified": true,
//                "error": {
//            "errorCode": "sample string 1",
//                    "errorTitle": "sample string 2",
//                    "errorDetail": "sample string 3"
//        }
//    }



        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("isVerified"))
                {
                    verrsponse.verResponseSuccess("success");

                }
                else {

                    String msg ="Registration Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    verrsponse.verResponseFailed(msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void RegistrationForgotParser(ForgotResponse regrsponse, String jsonStr) {

//        {
//            "success": true,
//                "error": {
//            "errorCode": "sample string 1",
//                    "errorTitle": "sample string 2",
//                    "errorDetail": "sample string 3"
//        }
//        }

        Log.i("Forgot_submit"," parser");


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Forgot_submit"," parser succcess");

                    regrsponse.forgotResponseSuccess("success");

                }
                else {

                    String msg ="Registration Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }



                    regrsponse.forgotResponseFailed(msg);
                    Log.i("Forgot_submit"," parser failed");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void AttemptLoginParser(LoginResponse logrsponse, String jsonStr, Context context) {


        Log.i("Attempt_login"," parser");

////
////        {
////
//        "success": true,
//                "user": [
//        {
//            "userId": 4398,
//                "userEmail": "thaher.m@bridge-india.in                          ",
//                "userMobile": "9847303069                                        ",
//                "userStatus": false,
//                "userType": "PDR       ",
//                "userDetailsId": 0,
//                "userFirstName": "tahr",
//                "userLastName": "mj",
//                "userGender": "",
//                "currentLocation": "",
//                "userAddress": "",
//                "userEducation": "",
//                "userDesignation": "",
//                "userExperience": "",
//                "userWagesHour": 0.0,
//                "userAdditionalSkill": "",
//                "profileImageUrl": "http://172.16.16.254:81/Api/uploads/",
//                "countryId": 0,
//                "countryName": "",
//                "stateName": "",
//                "timeZone": "GMT+05:30",
//                "cultureinfo": "en",
//                "accessToken": "1314d4aa-2143-41a6-abc9-5e33f0881fed",
//                "categoryName": "",
//                "userEmailVerified": false,
//                "userMobileVerified": false,
//                "locationLat": "",
//                "locationLong": "",
//                "preLocation1": "",
//                "preLocation1Lat": "",
//                "preLocation1Long": "",
//                "preLocation2": "",
//                "preLocation2Lat": "",
//                "preLocation2Long": "",
//                "preLocation3": "",
//                "preLocation3Lat": "",
//                "preLocation3Long": ""
//        }
//       ]
//    }
//

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Attempt_login"," parser succcess");

                    if(jsonObj.has("user"))
                    {
                        JSONArray userArray= jsonObj.getJSONArray("user");
                        JSONObject userObj = userArray.getJSONObject(0);
                        UserModel userModel = new UserModel();
//                        userModel.setUserId(userObj.getInt("userId"));
                        userModel.setUserEmail(userObj.getString("userEmail"));//
//                        userModel.setUserMobile(userObj.getString("userMobile"));
                        userModel.setUserStatus(userObj.getBoolean("userStatus"));//
                        userModel.setUserType(userObj.getString("userType"));//
//                        userModel.setUserDetailsId(userObj.getInt("userDetailsId"));
                        userModel.setUserFirstName(userObj.getString("userFirstName"));//
                        userModel.setUserLastName(userObj.getString("userLastName"));//
//                        userModel.setUserGender(userObj.getString("userGender"));
//                        userModel.setCurrentLocation(userObj.getString("currentLocation"));
//                        userModel.setUserAddress(userObj.getString("userAddress"));
//                        userModel.setUserEducation(userObj.getString("userEducation"));
//                        userModel.setUserDesignation(userObj.getString("userDesignation"));
//                        userModel.setUserExperience(userObj.getString("userExperience"));
//                        userModel.setUserWagesHour(userObj.getDouble("userWagesHour"));
//                        userModel.setUserAdditionalSkill(userObj.getString("userAdditionalSkill"));
                        userModel.setProfileImageUrl(userObj.getString("profileImageUrl"));//
//                        userModel.setCountryId(userObj.getInt( "countryId"));
//                        userModel.setCountryName(userObj.getString("countryName"));
//                        userModel.setStateName(userObj.getString("stateName"));
//                        userModel.setTimeZone(userObj.getString("timeZone"));
//                        userModel.setCultureinfo(userObj.getString("cultureinfo"));
                        userModel.setAccessToken(userObj.getString("accessToken"));//
//                        userModel.setCategoryName(userObj.getString("categoryName"));
                        userModel.setUserEmailVerified(userObj.getBoolean("userEmailVerified"));//
                        userModel.setUserMobileVerified(userObj.getBoolean("userMobileVerified"));//
                        userModel.setLocationLat(userObj.getString("locationLat"));//
                        userModel.setLocationLong(userObj.getString("locationLong"));//
//                        userModel.setPreLocation1(userObj.getString("preLocation1"));
//                        userModel.setPreLocation1Lat(userObj.getString("preLocation1Lat"));
//                        userModel.setPreLocation1Long(userObj.getString("preLocation1Long"));
//                        userModel.setPreLocation2(userObj.getString("preLocation2"));
//                        userModel.setPreLocation2Lat(userObj.getString("preLocation2Lat"));
//                        userModel.setPreLocation2Long(userObj.getString("preLocation2Long"));
//                        userModel.setPreLocation3(userObj.getString("preLocation3"));
//                        userModel.setPreLocation3Lat(userObj.getString("preLocation3Lat"));
//                        userModel.setPreLocation3Long(userObj.getString("preLocation3Long"));
                        userModel.setUserStatusLevel(userObj.getInt("userStatusLevel"));

                        SharedPreferencesManager.init(context);
                        SharedPreferencesManager.write(ACCESS_TOCKEN,userModel.getAccessToken());
                        SharedPreferencesManager.write(USER_EMAIL,userModel.getUserEmail());
                        SharedPreferencesManager.write(USER_TYPE,userModel.getUserType());
                        SharedPreferencesManager.write(USER_FIRST_NAME,userModel.getUserFirstName());
                        SharedPreferencesManager.write(USER_LAST_NAME,userModel.getUserLastName());
                        SharedPreferencesManager.write(USER_STATUS_LEVEL,String.valueOf(userModel.getUserStatusLevel()));
                        SharedPreferencesManager.write(USER_IMAGE_URL,userModel.getProfileImageUrl().trim());

                        logrsponse.loginResponseSuccess("success",userModel);



                    }
                }
                else {

                    String msg ="Login Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    logrsponse.loginResponseFailed(msg);
                    Log.i("Attempt_login"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Attempt_login"," parser Exception"+e.getMessage());

            }
        }

    }

    public void RegistrationProciderGetCategoryListResponseParser(GetCatDatas regrsponse, String jsonStr, Context context) {
        Log.i("Reg2_submit"," parser");


//        {
//            "success": true,
//                "categorys": [
//            {
//                "categoryId": 1,
//                    "categoryName": "Electrician                                       "
//            },
//            {
//                "categoryId": 2,
//                    "categoryName": " Drivers                                          "
//            },
//            {
//                "categoryId": 3,
//                    "categoryName": " Plumbers                                         "
//            },
//            {
//                "categoryId": 4,
//                    "categoryName": "Carpenters                                        "
//            },
//            {
//                "categoryId": 5,
//                    "categoryName": "Automobile works                                  "
//            }
//  ]
//        }

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("categorys"))
                    {
                        List<String> catname = new ArrayList<>();
                        List<String> catid = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("categorys");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            catid.add(c.getString("categoryId"));
                           catname.add(c.getString("categoryName"));

                        }

                        regrsponse.GetCategoryList(catid,catname);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetCategoryListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void RegistrationProciderGetSUBCategoryListResponseParser(GetCatDatas regrsponse, String jsonStr, Context context) {

        Log.i("Reg2_submit"," parser"+jsonStr);
//        {
//            "highestWage": "5992",
//                "success": true,
//                "categoryFiltters": [
//            {
//                "filterId": 1,
//                    "categoryId": 1,
//                    "filterName": "High Voltage                                      "
//            },
//            {
//                "filterId": 2,
//                    "categoryId": 1,
//                    "filterName": "LowVoltage                                        "
//            }
//                                                              ]
//        }
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("categoryFiltters"))
                    {
                        List<String> subcatname = new ArrayList<>();
                        List<String> subcatid = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("categoryFiltters");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            subcatid.add(c.getString("filterId").trim());
                            subcatname.add(c.getString("filterName").trim());

                        }
                        String  highestWage = "";
                            if(jsonObj.has("highestWage"))
                            {
                               highestWage = jsonObj.getString("highestWage");
                            }
                        regrsponse.GetSubCategoryList(subcatid,subcatname ,highestWage);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetSubCategoryListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    public void RegistrationProciderGetCountryListResponseParser(GetCatDatas regrsponse, String jsonStr, Context context) {
        Log.i("Reg2_submit"," parser"+jsonStr);
//        {
//            "success": true,
//                "country": [
//            {
//                "countryId": 1,
//                    "countryName": "INDIA                                             ",
//                    "iso": "IN",
//                    "isO3": "IND",
//                    "phoneCode": "+91"
//            },
//            {
//                "countryId": 2,
//                    "countryName": "SAUDI ARABIA",
//                    "iso": "SA",
//                    "isO3": "SAU",
//                    "phoneCode": "+966"
//            }
//                                                              ]
//        }


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("country"))
                    {
                        List<String> subcatname = new ArrayList<>();
                        List<String> subcatid = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("country");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            subcatid.add(c.getString("countryId"));
                            subcatname.add(c.getString("countryName"));

                        }

                        regrsponse.GetCountryCategoryList(subcatid,subcatname);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetCountryListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    public void RegistrationProciderGetStateListResponseParser(GetCatDatas regrsponse, String jsonStr, Context context) {
        Log.i("Reg2_submit"," parser"+jsonStr);

//        {
//            "success": true,
//                "state": [
//            {
//                "countryId": 1,
//                    "stateName": "KERALA                                            ",
//                    "stateId": 1
//            }
//                                                              ]
//        }


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("state"))
                    {
                        List<String> subcatname = new ArrayList<>();
                        List<String> subcatid = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("state");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            subcatid.add(c.getString("stateId"));
                            subcatname.add(c.getString("stateName"));

                        }

                        regrsponse.GetStateCategoryList(subcatid,subcatname);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetStateListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public void RegistrationProciderGetCITYListResponseParser(GetCatDatas regrsponse, String jsonStr, Context context) {

        Log.i("Reg2_submit"," parser"+jsonStr);

//        {
//            "success": true,
//                "city": [
//            {
//                "stateId": 1,
//                    "cityName": "Calicut",
//                    "cityId": 1,
//                    "latitude": "11.2587531",
//                    "longitude": "75.78041"
//            },
//            {
//                "stateId": 1,
//                    "cityName": "kochi",
//                    "cityId": 2,
//                    "latitude": "9.9816358",
//                    "longitude": "76.2998842"
//            }
//                                                              ]
//        }


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("CITYXISD"," parser succcess");

                    if(jsonObj.has("city"))
                    {
                        List<String> subcatname = new ArrayList<>();
                        List<String> subcatid = new ArrayList<>();
                        List<String> lat = new ArrayList<>();
                        List<String> lng = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("city");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            subcatid.add(c.getString("cityId"));
                            subcatname.add(c.getString("cityName"));
                            lat.add(c.getString("latitude"));
                            lng.add(c.getString("longitude"));
                        }

                        regrsponse.GetCityCategoryList(subcatid,subcatname,lat,lng);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetCityListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void RegistrationFinalRegResponseParser(RegistrationProviderResponse regrsponse, String jsonStr, Context context) {
        Log.i("Reg2_submit"," parser"+jsonStr);


//        {
//            "success": true,
//                "imageUrl": "http://172.16.16.254:81/uploads/Profile-7xAXmU7A-24032017113844468.jpg",
//                "signUpResponse": {
//            "accessToken": "c1fec8f0-5e72-41b5-b723-57743f7b93f9",
//                    "userEmail": "thaher.m@bridge-india.in                          ",
//                    "userType": "PVR       ",
//                    "userFirstName": "thaher",
//                    "userLastName": "majeed",
//                    "userStatusLevel": 3
//        }
//        }
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");
                    String   imageUrl= jsonObj.getString("imageUrl");

                    if(jsonObj.has("signUpResponse"))
                    {

                        JSONObject signUpResponse= jsonObj.getJSONObject("signUpResponse");
                        // looping through All Contacts

                        String   accessToken= signUpResponse.getString("accessToken");
                        String   userEmail= signUpResponse.getString("userEmail");
                        String   userType= signUpResponse.getString("userType");
                        String   userFirstName= signUpResponse.getString("userFirstName");
                        String   userLastName= signUpResponse.getString("userLastName");
                        Integer   userStatusLevel= signUpResponse.getInt("userStatusLevel");

                        SharedPreferencesManager.init(context);
                        SharedPreferencesManager.write(ACCESS_TOCKEN,accessToken);
                        SharedPreferencesManager.write(USER_EMAIL,userEmail);
                        SharedPreferencesManager.write(USER_TYPE,userType);
                        SharedPreferencesManager.write(USER_FIRST_NAME,userFirstName);
                        SharedPreferencesManager.write(USER_LAST_NAME,userLastName);
                        SharedPreferencesManager.write(USER_STATUS_LEVEL,String.valueOf(userStatusLevel));
                        SharedPreferencesManager.write(USER_IMAGE_URL,imageUrl.trim());


                        regrsponse.GetCityeCategoryList(imageUrl,accessToken,userEmail,userType,userFirstName,userLastName,userStatusLevel);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetCityListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void GetCategoryListResponseParser(HomeResponse regrsponse, String jsonStr, Context context) {
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("categorys"))
                    {
                        List<String> catname = new ArrayList<>();
                        List<String> catid = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("categorys");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                            catid.add(c.getString("categoryId"));
                            catname.add(c.getString("categoryName"));

                        }

                        regrsponse.GetCategoryList(catid,catname);


                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetCategoryListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    public void GetProviderListResponseParser(HomeResponse regrsponse, String jsonStr, Context context) {
        Log.i("Reg2_submitXXXX"," parser"+jsonStr);

//        {
//            "success": true,
//                "providers": [
//            {
//                "accessToken": "ec98916d-9f4f-4609-9d56-00c6f979efef",
//                    "userFirstName": "Aneesh",
//                    "userLastName": "sn",
//                    "userGender": "Male      ",
//                    "currentLocation": "Kanayannur",
//                    "locationLat": "10.006488",
//                    "locationLong": "76.303196",
//                    "userAddress": "Cochin ",
//                    "userDesignation": "iOS",
//                    "userWagesHour": "560.00",
//                    "profileImageUrl": "http://172.16.16.254:81/uploads/Profile-grLZmnFo-08032017130005527.jpg                                                              ",
//                    "userEmail": "aneesh_sn@ymail.com                               ",
//                    "userMobile": "+918137085095                                     ",
//                    "categoryName": "Electrician                                       "
//            },
//


        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("providers"))
                    {
                        List<ProviderBasic> providers = new ArrayList<>();

                        JSONArray categoryArray= jsonObj.getJSONArray("providers");
                        // looping through All Contacts
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject c = categoryArray.getJSONObject(i);
                           ProviderBasic providerbasic = new ProviderBasic();


                          providerbasic.setAccessTocken(c.getString("accessToken"));
                            providerbasic.setUserFirstName(c.getString("userFirstName"));
                            providerbasic.setUserLastName(c.getString("userLastName"));
                            providerbasic.setUserGender(c.getString("userGender"));
                            providerbasic.setCurrentLocation(c.getString("currentLocation"));
                            Log.i("Reg2_submitxxx"," exception "+c.getString("accessToken"));

                            providerbasic.setLocationLat(c.getString("locationLat"));
                            providerbasic.setLocationLong(c.getString("locationLong"));
                            providerbasic.setUserAddress(c.getString("userAddress"));
                            providerbasic.setUserDesignation(c.getString("userDesignation"));
                            providerbasic.setUserWagesHour(c.getString("userWagesHour"));
                            providerbasic.setProfileImageUrl(c.getString("profileImageUrl"));
                            providerbasic.setUserEmail(c.getString("userEmail"));
                            providerbasic.setUserMobile(c.getString("userMobile"));
                            providerbasic.setCategoryName(c.getString("categoryName"));
                            Log.i("Reg2_submitxxx"," exception "+c.getString("accessToken"));
                            Log.i("Reg2_submitxxx"," exception "+c.getString("locationLat"));
                            Log.i("Reg2_submixxxt"," exception "+c.getString("currentLocation"));
                            if(providerbasic.getLocationLat().trim().isEmpty())
                            {
                                providerbasic.setLocationLat("0.0");
                            }
                            if(providerbasic.getLocationLong().trim().isEmpty())
                            {
                                providerbasic.setLocationLong("0.0");
                            }

                            providers.add(providerbasic);

                        }

                        regrsponse.GetProviderList(providers);


                    }
                }
                else {

                    String msg ="Getting Providers Failed!";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");

                    }


                    regrsponse.GetProviderListFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());


            }
        }

    }

    public void GetProviderDetailsResponseParser(ProviderDetailsResponse regrsponse, String jsonStr, Context context) {

        Log.i("Reg2_submit"," parser"+jsonStr);

//        "success": true,
//                "signupResponse": {
//            "userId": 4898,
//                    "accessToken": "2f587076-4f45-42e4-91e0-35954706d5fe",
//                    "userEmail": "cijo.jose@bridge-india.in",
//                    "userType": "PVR",
//                    "userFirstName": "cijo",
//                    "userLastName": "jose",
//                    "userStatusLevel": 3,
//                    "userGender": "Male      ",
//                    "userDob": "02-00-1991",
//                    "currentLocation": "Palarivattom, Kochi, Kerala, India",
//                    "stateId": 1,
//                    "userAddress": "Palarivattom",
//                    "userEducation": "MCA",
//                    "userDesignation": "d",
//                    "userEducationType": "",
//                    "userLanguagesKnown": "MALAYALAM, HINDI, MARATTI",
//                    "userExperience": "4",
//                    "userWagesHour": 2.00,
//                    "userAdditionalSkill": "dd",
//                    "profileImageUrl": "                                                                                                    ",
//                    "categoryDetails": [
//            {
//                "categoryId": 1,
//                    "categoryName": "Electrician                                       "
//            },
//            {
//                "categoryId": 5,
//                    "categoryName": "Automobile works                                  "
//            }
//                                                                  ],
//            "categoryFiltterDetails": [
//            {
//                "filterId": 2,
//                    "categoryId": 1,
//                    "filterName": "LowVoltage                                        "
//            },
//            {
//                "filterId": 3,
//                    "categoryId": 5,
//                    "filterName": "Tire workers                                      "
//            },
//            {
//                "filterId": 5,
//                    "categoryId": 5,
//                    "filterName": "Car Cleaners                                      "
//            }
//                                                                  ],
//            "countryId": 1,
//                    "userMobile": "+918086212281",
//                    "cityId": 2,
//                    "locationLat": "9.998480199999998",
//                    "locationLong": "76.31193639999992",
//                    "preLocation1": "Kottayam, Kerala, India",
//                    "preLocation1Lat": "9.591566799999999",
//                    "preLocation1Long": "76.52215309999997",
//                    "preLocation2": "Palakkad, Kerala, India",
//                    "preLocation2Lat": "10.7867303",
//                    "preLocation2Long": "76.65479319999997",
//                    "preLocation3": "",
//                    "preLocation3Lat": "",
//                    "preLocation3Long": "",
//                    "cityName": "Kochi",
//                    "stateName": "KERALA                                            ",
//                    "countryName": "INDIA                                             ",
//                    "employmentType": ""
//        }
//    }



        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");

                    if(jsonObj.has("signupResponse"))
                    {
                        UserModel providerbasic = new UserModel();

                        JSONObject c= jsonObj.getJSONObject("signupResponse");
                        // looping through All Contacts

                        if(c.has("accessToken"))
                        providerbasic.setAccessToken(c.getString("accessToken"));
                        else
                            providerbasic.setAccessToken("");

                        if(c.has("userEmail"))
                            providerbasic.setUserEmail(c.getString("userEmail"));
                        else
                            providerbasic.setUserEmail("");
                        if(c.has("userType"))
                            providerbasic.setUserType(c.getString("userType"));
                        else
                            providerbasic.setUserType("");
                        if(c.has("userFirstName"))
                            providerbasic.setUserFirstName(c.getString("userFirstName"));
                        else
                            providerbasic.setUserFirstName("");
                        if(c.has("userLastName"))
                            providerbasic.setUserLastName(c.getString("userLastName"));
                        else
                            providerbasic.setUserLastName("");
                        if(c.has("userGender"))
                            providerbasic.setUserGender(c.getString("userGender"));
                        else
                            providerbasic.setUserGender("");

                        if(c.has("userAddress"))
                            providerbasic.setUserAddress(c.getString("userAddress"));
                        else
                            providerbasic.setUserAddress("");
                        if(c.has("userEducation"))
                            providerbasic.setUserEducation(c.getString("userEducation"));
                        else
                            providerbasic.setUserEducation(c.getString("userEducation"));

                        if(c.has("userDesignation"))
                            providerbasic.setUserDesignation(c.getString("userDesignation"));
                        else
                            providerbasic.setUserDesignation("");

                        if(c.has("userLanguagesKnown"))
                            providerbasic.setLanguagesknown(c.getString("userLanguagesKnown"));
                        else
                            providerbasic.setLanguagesknown("");

                        if(c.has("userExperience"))
                            providerbasic.setUserExperience(c.getString("userExperience"));
                        else
                            providerbasic.setUserExperience("");

                        if(c.has("userMobile"))
                            providerbasic.setUserMobile(c.getString("userMobile"));
                        else
                            providerbasic.setUserMobile("");

                        if(c.has("userAdditionalSkill"))
                            providerbasic.setUserAdditionalSkill(c.getString("userAdditionalSkill"));
                        else
                            providerbasic.setUserAdditionalSkill("");

                        if(c.has("profileImageUrl"))
                            providerbasic.setProfileImageUrl(IMAGEPREFIX.trim() + c.getString("profileImageUrl").trim());
                        else
                            providerbasic.setProfileImageUrl("");


                        if(c.has("preLocation1"))
                            providerbasic.setPreLocation1(c.getString("preLocation1"));
                        else
                            providerbasic.setPreLocation1("");

                        if(c.has("preLocation2"))
                            providerbasic.setPreLocation2(c.getString("preLocation2"));
                        else
                            providerbasic.setPreLocation2("");

                        if(c.has("preLocation3"))
                            providerbasic.setPreLocation3(c.getString("preLocation3"));
                        else
                            providerbasic.setPreLocation3("");

                        if(c.has("preLocation1Lat"))
                            providerbasic.setPreLocation1Lat(c.getString("preLocation1Lat"));
                        else
                            providerbasic.setPreLocation1Lat("");

                        if(c.has("preLocation1Long"))
                            providerbasic.setPreLocation1Long(c.getString("preLocation1Long"));
                        else
                            providerbasic.setPreLocation1Long("");

                        if(c.has("preLocation2Lat"))
                            providerbasic.setPreLocation2Lat(c.getString("preLocation2Lat"));
                        else
                            providerbasic.setPreLocation2Lat("");

                        if(c.has("preLocation2Long"))
                            providerbasic.setPreLocation2Long(c.getString("preLocation2Long"));
                        else
                            providerbasic.setPreLocation2Long("");

                        if(c.has("preLocation3Lat"))
                            providerbasic.setPreLocation3Lat(c.getString("preLocation3Lat"));
                        else
                            providerbasic.setPreLocation3Lat("");

                        if(c.has("preLocation3Long"))
                            providerbasic.setPreLocation3Long(c.getString("preLocation3Long"));
                        else
                            providerbasic.setPreLocation3Long("");

                        if(c.has("countryId"))
                            providerbasic.setCountryId(c.getInt("countryId"));
                        else
                            providerbasic.setCountryId(0);

                        if(c.has("countryName"))
                            providerbasic.setCountryName(c.getString("countryName"));
                        else
                            providerbasic.setCountryName("");

                        if(c.has("stateName"))
                            providerbasic.setStateName(c.getString("stateName"));
                        else
                            providerbasic.setStateName("");

                        if(c.has("stateId"))
                            providerbasic.setStateId(c.getInt("stateId"));
                        else
                            providerbasic.setStateId(0);


                        if(c.has("cityName"))
                            providerbasic.setCityName(c.getString("cityName"));
                        else
                            providerbasic.setCityName("");

                        if(c.has("cityId"))
                            providerbasic.setCityId(c.getString("cityId"));
                        else
                            providerbasic.setCityId("");

                        if(c.has("employmentType"))
                            providerbasic.setEmploymentType(c.getString("employmentType"));
                        else
                            providerbasic.setEmploymentType("");


                        if(c.has("userDob"))
                            providerbasic.setDob(c.getString("userDob"));
                        else
                            providerbasic.setDob("");

                        if(c.has("userWagesHour"))
                        {providerbasic.setUserWagesHour(Double.valueOf(c.getString("userWagesHour")));}
                        else
                        {providerbasic.setUserWagesHour(0.0);}

                        Log.i("GETPROFILE"," user 0 :"+providerbasic.getUserMobile());



                        if(c.has("categoryDetails"))
                        { JSONArray catdet = c.getJSONArray("categoryDetails");
                            String[] categoryId = new String[catdet.length()];
                            String[] categoryName = new String[catdet.length()];
                            for (int i = 0; i < catdet.length(); i++) {
                                JSONObject cd = catdet.getJSONObject(i);

                                if(cd.has("categoryId"))
                                categoryId[i] = cd.getString("categoryId");
                                if(cd.has("categoryName"))
                                categoryName[i] = cd.getString("categoryName");
                            }
                         providerbasic.setCategoryId(categoryId);
                            providerbasic.setCategoryName(categoryName);
                        }
                        if(c.has("categoryFiltterDetails"))
                        { JSONArray catfildet = c.getJSONArray("categoryFiltterDetails");
                            String[] filterId = new String[catfildet.length()];
                            String[] categoryId = new String[catfildet.length()];
                            String[] filterName = new String[catfildet.length()];

                            for (int i = 0; i < catfildet.length(); i++) {
                                JSONObject cf = catfildet.getJSONObject(i);

                                if(cf.has("filterId"))
                                filterId[i] = cf.getString("filterId");
                                if(cf.has("categoryId"))
                                categoryId[i] = cf.getString("categoryId");
                                if(cf.has("filterName"))
                                filterName[i] = cf.getString("filterName");


                            }
                            providerbasic.setCategoryforFilterId(categoryId);
                            providerbasic.setFilterName(filterName);
                            providerbasic.setFilterId(filterId);
                        }
                        regrsponse.DetailsResponseSuccess(providerbasic);
                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.DetailsResponseFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }
    }

    public void ChangePasswordParser(ChangePassResponse forrsponse, String jsonStr) {

        Log.i("Reg2_submit"," parser"+jsonStr);

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("Reg2_submit"," parser succcess");
                        forrsponse.changePassResponseSuccess("Success");
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    forrsponse.changePassResponseFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }

    }

    public void UpdateProfileParser(ProfileUpdateListner regrsponse, String jsonStr, Context context) {


//        {
//            "success": true,
//                "signupResponse": {
//            "userId": 5001,
//                    "accessToken": "167047a0-747a-4873-8af0-3e6a188ce90b",
//                    "userEmail": "cijo.jose@bridge-india.in",
//                    "userType": "PVR",
//                    "userFirstName": "Cijo",
//                    "userLastName": "Jose",
//                    "userStatusLevel": 3,
//                    "profileImageUrl": "http://172.16.16.254:81/uploads/Profile-K9iSGAhJ-26052017165134290.jpg                                                              "
//        }
//        }
String profileimg= "";
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {


                    if(jsonObj.has("signupResponse"))
                    {

                        JSONObject c= jsonObj.getJSONObject("signupResponse");
                        // looping through All Contacts

                        if(c.has("profileImageUrl"))
                            profileimg= c.getString("profileImageUrl").trim();

                    }


                    Log.i("Reg2_submit"," parser succcess");
                    regrsponse.ProfileUpdateSuccess("Success",profileimg);
                }
                else {

                    String msg ="Update Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.ProfileUpdateFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }


    }

    public void ImageUploadParese(ImageUploader regrsponse, String jsonStr, Context context) {

        Log.i("ImageUpload"," "+jsonStr);

//        {
//            "success": true,
//                "profileImageUrl": "http://172.16.16.253:81/uploads/Profile-CsD1b4XQ-06072017145956664.jpg"
//        }

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {


                    String  profileimg = "";
                        if(jsonObj.has("profileImageUrl"))
                        {   profileimg= jsonObj.getString("profileImageUrl").trim();}
                        else {

                            String msg ="Upload Failed";
                            if(jsonObj.has("error"))
                            {
                                JSONObject error = jsonObj.getJSONObject("error");
                                msg = error.getString("errorDetail");
                            }


                            regrsponse.UploadFailed(msg);
                            Log.i("Reg2_submit"," parser failed"+msg);

                        }




                    Log.i("Reg2_submit"," parser succcess");
                    regrsponse.UploadSuccess("Success",profileimg);
                }
                else {

                    String msg ="Update Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.UploadFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }

    }

    public void GetServicesListResponseParser(ServiceandLocListner regrsponse, String jsonStr, Context context) {
        Log.i("PROFFFSER"," "+jsonStr);
////     [
//        {
//            "categoryId": 5,
//                "categoryName": "Automobile works",
//                "subCategory": [
//            {
//                "filterId": 4,
//                    "filterName": "Electrical Workers",
//                    "experience": 12.00,
//                    "wages": 12
//            },
//            {
//                "filterId": 5,
//                    "filterName": "Car Cleaners",
//                    "experience": 12.00,
//                    "wages": 12
//            },
//            {
//                "filterId": 15,
//                    "filterName": "Automobile works",
//                    "experience": 8.70,
//                    "wages": 100000
//            },
//            {
//                "filterId": 20,
//                    "filterName": "Hair Saloon",
//                    "experience": 8.70,
//                    "wages": 100000
//            },
//            {
//                "filterId": 3,
//                    "filterName": "Tire workers",
//                    "experience": 12.00,
//                    "wages": 12
//            }
//                                                                ]
//        },


        if (jsonStr != null) {

            try {
                JSONArray jsonArr = new JSONArray(jsonStr);
                List<Services> services = new ArrayList<>();
                Log.i("PROFFFSER", "jsonArr length "+ jsonArr.length());

                for (int i = 0; i < jsonArr.length(); i++) {
                    Log.i("PROFFFSER", "jsonArr i "+ i);

                    JSONObject srvc = jsonArr.getJSONObject(i);
                    Services service = new Services();
                    service.setServiceName(srvc.getString("categoryName"));
                    service.setServiceId(srvc.getString("categoryId"));
                    service.setExperiance(srvc.getString("experience"));
                    service.setWages(srvc.getString("wages"));
                    if (srvc.has("subCategory")) {
                        JSONArray subCategory = srvc.getJSONArray("subCategory");
                        List<String> SubServiceName = new ArrayList<>();
                        List<String> SubServiceId = new ArrayList<>();
                        Log.i("PROFFFSER", "subCategory length "+ subCategory.length());

                        for (int j = 0; j < subCategory.length(); j++) {
                            Log.i("PROFFFSER", "1subCategory j "+ j);

                            JSONObject fltr = subCategory.getJSONObject(j);
                            Log.i("PROFFFSER", "2subCategory j "+ j);

                            SubServiceId.add(String.valueOf(fltr.getInt("filterId")));
                            Log.i("PROFFFSER", "3subCategory j "+ j);

                            SubServiceName.add(fltr.getString("filterName"));
                            Log.i("PROFFFSER", "4subCategory j "+ j);

                        }
                        service.setSubServiceName(SubServiceName);
                        service.setSubServiceId(SubServiceId);

                    }
                    services.add(service);
                    Log.i("PROFFFSER", " parser ading...");
                }
                Log.i("PROFFFSER"," inteface");

                regrsponse.GetServiceList(services);



            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("PROFFFSER"," exception "+e.getMessage());
                regrsponse.GetServiceListFailed("Error");
            }
        }
    }

    public void GetLocationListResponseParser(ServiceandLocListner regrsponse, String jsonStr, Context context) {
        Log.i("PROFFFLOC"," "+jsonStr);
//        {
//            "serviceLocations": [
//            {
//                "id": 35,
//                    "preLocation": "Aluva, Kerala, India",
//                    "preLocationLat": "10.107570199999998",
//                    "preLocationLong": "76.34566199999999",
//                    "zip": 0
//            }
//                                                         ],
//            "success": true
//        }

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("PROFFFLOC","  succcess");


                    if(jsonObj.has("serviceLocations"))
                    { JSONArray catdet = jsonObj.getJSONArray("serviceLocations");
                        List<PlaceLoc> placeLocList = new ArrayList<>();
                        for (int i = 0; i < catdet.length(); i++) {
                            JSONObject cd = catdet.getJSONObject(i);
                            PlaceLoc placeLoc = new PlaceLoc();
                            placeLoc.setAddress(cd.getString("preLocation"));
                            placeLoc.setLatitude(cd.getString("preLocationLat"));
                            placeLoc.setLongitude(cd.getString("preLocationLong"));
                            placeLoc.setId(cd.getString("id"));
                            placeLoc.setZip(cd.getInt("zip"));
                            placeLocList.add(placeLoc);
                            Log.i("PROFFFLOC"," parser ading...");



                        }
                        Log.i("PROFFFLOC"," inteface");

                        regrsponse.GetLocationList(placeLocList);

                    }
                    else {
                        String msg ="Location Fetch Failed";
                        if(jsonObj.has("error"))
                        {
                            JSONObject error = jsonObj.getJSONObject("error");
                            msg = error.getString("errorDetail");
                        }


                        regrsponse.GetServiceListFailed(msg);
                        Log.i("PROFFFLOC"," parser failed"+msg);

                    }


                    Log.i("PROFFFLOC"," parser succcess");
                }
                else {

                    String msg ="Location Fetch Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.GetServiceListFailed(msg);
                    Log.i("PROFFFLOC"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("PROFFFLOC"," exception "+e.getMessage());
                regrsponse.GetServiceListFailed("Error");
            }
        }



    }

    public void AddServiceResponseParser(ServiceandLocListner regrsponse, String jsonStr, Context context) {
        Log.i("PROFFF"," "+jsonStr);
//        {
//            "avoidedInsertionResponses": [],
//            "insertedCount": 1,
//                "success": true
//        }

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {


                    regrsponse.AddServiceSuccess();
                }
                else {

                    String msg ="Update Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.AddServiceFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }




    }

    public void AddLocationResponseParser(ServiceandLocListner regrsponse, String jsonStr, Context context) {

        Log.i("PROFFF"," "+jsonStr);

//        {
//            "serviceLocations": [
//            {
//                "id": 35,
//                    "preLocation": "Aluva, Kerala, India",
//                    "preLocationLat": "10.107570199999998",
//                    "preLocationLong": "76.34566199999999",
//                    "zip": 0
//            }
//                                                         ],
//            "responseMessage": "inserted",
//                "success": true
//        }
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {


                    regrsponse.AddLocationSuccess();
                }
                else {

                    String msg ="Update Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.AddLocationFailed(msg);
                    Log.i("Reg2_submit"," parser failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("Reg2_submit"," exception "+e.getMessage());

            }
        }
    }

    public void InsertPersonalInformationResponseParser(PersonalDetailsResponse regrsponse, String jsonStr, Context context) {
        Log.i("PERSONL"," "+jsonStr);
//        {
//            "success": true,
//                "imageUrl": "http://172.16.16.253:81/uploads/",
//                "signUpResponse": {
//            "userId": 5180,
//                    "accessToken": "6dd7f990-6fa5-4ed8-b2c1-a787a9b0754b",
//                    "userEmail": "thaher.m@bridge-india.in",
//                    "userType": "PVR",
//                    "userFirstName": "hkjdsahkja",
//                    "userLastName": "lkdsalkjdsa",
//                    "userStatusLevel": 4,
//                    "userGender": "Male",
//                    "userDob": "14-07-2017",
//                    "stateId": 1,
//                    "userAddress": "awesss",
//                    "userEducation": "",
//                    "userLanguagesKnown": "",
//                    "profileImageUrl": "http://172.16.16.253:81/uploads/SOOM_Icon.png",
//
//            "countryId": 1,
//                    "userMobile": "78924375290",
//                    "cityId": 2,
//                    "cityName": "Kochi",
//                    "stateName": "KERALA                                            ",
//                    "countryName": "INDIA                                             "
//        }
//        }

        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(jsonObj.getBoolean("success"))
                {
                    Log.i("PERSONL"," parser succcess");

                    if(jsonObj.has("signUpResponse"))
                    {
                        Log.i("PERSONL"," has  signupResponse");

                        UserModel providerbasic = new UserModel();

                        JSONObject c= jsonObj.getJSONObject("signUpResponse");
                        // looping through All Contacts

                        if(c.has("accessToken"))
                            providerbasic.setAccessToken(c.getString("accessToken"));
                        else
                            providerbasic.setAccessToken("");

                        if(c.has("userEmail"))
                            providerbasic.setUserEmail(c.getString("userEmail"));
                        else
                            providerbasic.setUserEmail("");
                        if(c.has("userType"))
                            providerbasic.setUserType(c.getString("userType"));
                        else
                            providerbasic.setUserType("");
                        if(c.has("userFirstName"))
                            providerbasic.setUserFirstName(c.getString("userFirstName"));
                        else
                            providerbasic.setUserFirstName("");
                        if(c.has("userLastName"))
                            providerbasic.setUserLastName(c.getString("userLastName"));
                        else
                            providerbasic.setUserLastName("");
                        if(c.has("userGender"))
                            providerbasic.setUserGender(c.getString("userGender"));
                        else
                            providerbasic.setUserGender("");

                        if(c.has("userAddress"))
                            providerbasic.setUserAddress(c.getString("userAddress"));
                        else
                            providerbasic.setUserAddress("");
                        if(c.has("userStatusLevel"))
                            providerbasic.setUserStatusLevel(c.getInt("userStatusLevel"));
                        else
                            providerbasic.setUserStatusLevel(0);
                        if(c.has("userEducation"))
                            providerbasic.setUserEducation(c.getString("userEducation"));
                        else
                            providerbasic.setUserEducation(c.getString("userEducation"));

                        if(c.has("userDesignation"))
                            providerbasic.setUserDesignation(c.getString("userDesignation"));
                        else
                            providerbasic.setUserDesignation("");

                        if(c.has("userLanguagesKnown"))
                            providerbasic.setLanguagesknown(c.getString("userLanguagesKnown"));
                        else
                            providerbasic.setLanguagesknown("");

                        if(c.has("userExperience"))
                            providerbasic.setUserExperience(c.getString("userExperience"));
                        else
                            providerbasic.setUserExperience("");

                        if(c.has("userMobile"))
                            providerbasic.setUserMobile(c.getString("userMobile"));
                        else
                            providerbasic.setUserMobile("");

                        if(c.has("userAdditionalSkill"))
                            providerbasic.setUserAdditionalSkill(c.getString("userAdditionalSkill"));
                        else
                            providerbasic.setUserAdditionalSkill("");

                        if(c.has("profileImageUrl"))
                            providerbasic.setProfileImageUrl(IMAGEPREFIX.trim() + c.getString("profileImageUrl").trim());
                        else
                            providerbasic.setProfileImageUrl("");


                        if(c.has("preLocation1"))
                            providerbasic.setPreLocation1(c.getString("preLocation1"));
                        else
                            providerbasic.setPreLocation1("");

                        if(c.has("preLocation2"))
                            providerbasic.setPreLocation2(c.getString("preLocation2"));
                        else
                            providerbasic.setPreLocation2("");

                        if(c.has("preLocation3"))
                            providerbasic.setPreLocation3(c.getString("preLocation3"));
                        else
                            providerbasic.setPreLocation3("");

                        if(c.has("preLocation1Lat"))
                            providerbasic.setPreLocation1Lat(c.getString("preLocation1Lat"));
                        else
                            providerbasic.setPreLocation1Lat("");

                        if(c.has("preLocation1Long"))
                            providerbasic.setPreLocation1Long(c.getString("preLocation1Long"));
                        else
                            providerbasic.setPreLocation1Long("");

                        if(c.has("preLocation2Lat"))
                            providerbasic.setPreLocation2Lat(c.getString("preLocation2Lat"));
                        else
                            providerbasic.setPreLocation2Lat("");

                        if(c.has("preLocation2Long"))
                            providerbasic.setPreLocation2Long(c.getString("preLocation2Long"));
                        else
                            providerbasic.setPreLocation2Long("");

                        if(c.has("preLocation3Lat"))
                            providerbasic.setPreLocation3Lat(c.getString("preLocation3Lat"));
                        else
                            providerbasic.setPreLocation3Lat("");

                        if(c.has("preLocation3Long"))
                            providerbasic.setPreLocation3Long(c.getString("preLocation3Long"));
                        else
                            providerbasic.setPreLocation3Long("");

                        if(c.has("countryId"))
                            providerbasic.setCountryId(c.getInt("countryId"));
                        else
                            providerbasic.setCountryId(0);

                        if(c.has("countryName"))
                            providerbasic.setCountryName(c.getString("countryName"));
                        else
                            providerbasic.setCountryName("");

                        if(c.has("stateName"))
                            providerbasic.setStateName(c.getString("stateName"));
                        else
                            providerbasic.setStateName("");

                        if(c.has("stateId"))
                            providerbasic.setStateId(c.getInt("stateId"));
                        else
                            providerbasic.setStateId(0);


                        if(c.has("cityName"))
                            providerbasic.setCityName(c.getString("cityName"));
                        else
                            providerbasic.setCityName("");

                        if(c.has("cityId"))
                            providerbasic.setCityId(c.getString("cityId"));
                        else
                            providerbasic.setCityId("");

                        if(c.has("employmentType"))
                            providerbasic.setEmploymentType(c.getString("employmentType"));
                        else
                            providerbasic.setEmploymentType("");


                        if(c.has("userDob"))
                            providerbasic.setDob(c.getString("userDob"));
                        else
                            providerbasic.setDob("");

                        if(c.has("userWagesHour"))
                        {providerbasic.setUserWagesHour(Double.valueOf(c.getString("userWagesHour")));}
                        else
                        {providerbasic.setUserWagesHour(0.0);}

                        Log.i("PERSONL"," user 0 :"+providerbasic.getUserMobile());


//
//                        if(c.has("categoryDetails"))
//                        { JSONArray catdet = c.getJSONArray("categoryDetails");
//                            String[] categoryId = new String[catdet.length()];
//                            String[] categoryName = new String[catdet.length()];
//                            for (int i = 0; i < catdet.length(); i++) {
//                                JSONObject cd = catdet.getJSONObject(i);
//
//                                if(cd.has("categoryId"))
//                                    categoryId[i] = cd.getString("categoryId");
//                                if(cd.has("categoryName"))
//                                    categoryName[i] = cd.getString("categoryName");
//                            }
//                            providerbasic.setCategoryId(categoryId);
//                            providerbasic.setCategoryName(categoryName);
//                        }
//                        if(c.has("categoryFiltterDetails"))
//                        { JSONArray catfildet = c.getJSONArray("categoryFiltterDetails");
//                            String[] filterId = new String[catfildet.length()];
//                            String[] categoryId = new String[catfildet.length()];
//                            String[] filterName = new String[catfildet.length()];
//
//                            for (int i = 0; i < catfildet.length(); i++) {
//                                JSONObject cf = catfildet.getJSONObject(i);
//
//                                if(cf.has("filterId"))
//                                    filterId[i] = cf.getString("filterId");
//                                if(cf.has("categoryId"))
//                                    categoryId[i] = cf.getString("categoryId");
//                                if(cf.has("filterName"))
//                                    filterName[i] = cf.getString("filterName");
//
//
//                            }
//                            providerbasic.setCategoryforFilterId(categoryId);
//                            providerbasic.setFilterName(filterName);
//                            providerbasic.setFilterId(filterId);
//                        }


                        SharedPreferencesManager.init(context);
                        SharedPreferencesManager.write(ACCESS_TOCKEN,providerbasic.getAccessToken());
                        SharedPreferencesManager.write(USER_EMAIL,providerbasic.getUserEmail());
                        SharedPreferencesManager.write(USER_TYPE,providerbasic.getUserType());
                        SharedPreferencesManager.write(USER_FIRST_NAME,providerbasic.getUserFirstName());
                        SharedPreferencesManager.write(USER_LAST_NAME,providerbasic.getUserLastName());
                        SharedPreferencesManager.write(USER_STATUS_LEVEL,String.valueOf(providerbasic.getUserStatusLevel()));
                        SharedPreferencesManager.write(USER_IMAGE_URL,providerbasic.getProfileImageUrl().trim());

                        regrsponse.ResponseSuccess(providerbasic);
                    }else {
                        Log.i("PERSONL"," has not signupResponse");
                        String msg ="Get Category Failed";
                        if(jsonObj.has("error"))
                        {
                            JSONObject error = jsonObj.getJSONObject("error");
                            msg = error.getString("errorDetail");
                        }


                    regrsponse.ResponseFailed(msg);

                    }
                }
                else {

                    String msg ="Get Category Failed";
                    if(jsonObj.has("error"))
                    {
                        JSONObject error = jsonObj.getJSONObject("error");
                        msg = error.getString("errorDetail");
                    }


                    regrsponse.ResponseFailed(msg);
                    Log.i("PERSONL"," parsering failed"+msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("PERSONL"," exception "+e.getMessage());

            }
        }

    }

    public void DeleteServiceResponseParser(ServiceandLocListner regrsponse, String jsonStr, Context context) {
        Log.i("PROFFFTF"," "+jsonStr);
        if(jsonStr.trim().equals("false"))
        {
            Log.i("PROFFFTF"," "+jsonStr+" false");

        }
        else {
            Log.i("PROFFFTF"," "+jsonStr+" true");

        }


    }
}
