package com.bridge.soom.Helper;

import android.content.Context;
import android.util.Log;

import com.bridge.soom.Interface.ForgotResponse;
import com.bridge.soom.Interface.HomeResponse;
import com.bridge.soom.Interface.LoginResponse;
import com.bridge.soom.Interface.RegistrationProviderResponse;
import com.bridge.soom.Interface.RegistrationResponse;
import com.bridge.soom.Interface.VerificationResponse;
import com.bridge.soom.Model.ProviderBasic;
import com.bridge.soom.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bridge.soom.Helper.Constants.ACCESS_TOCKEN;
import static com.bridge.soom.Helper.Constants.DEVICE_ID;
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

    public void RegistrationProciderGetCategoryListResponseParser(RegistrationProviderResponse regrsponse, String jsonStr, Context context) {
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

    public void RegistrationProciderGetSUBCategoryListResponseParser(RegistrationProviderResponse regrsponse, String jsonStr, Context context) {

        Log.i("Reg2_submit"," parser"+jsonStr);

//        {
//            "success": true,
//                "categoryFiltters": [
//            {
//                "filterId": 6,
//                    "categoryId": 2,
//                    "filterName": " Drivers                                          "
//            }
//                                                                ]
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
                            subcatid.add(c.getString("filterId"));
                            subcatname.add(c.getString("filterName"));

                        }

                        regrsponse.GetSubCategoryList(subcatid,subcatname);


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

    public void RegistrationProciderGetStateListResponseParser(RegistrationProviderResponse regrsponse, String jsonStr, Context context) {
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

    public void RegistrationProciderGetCITYListResponseParser(RegistrationProviderResponse regrsponse, String jsonStr, Context context) {

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
                    Log.i("Reg2_submit"," parser succcess");

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

                        regrsponse.GetCityeCategoryList(subcatid,subcatname,lat,lng);


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
        Log.i("Reg2_submit"," parser"+jsonStr);

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
                            providerbasic.setUserFirstName(c.getString("userFirstName"));
                            providerbasic.setUserLastName(c.getString("userLastName"));
                            providerbasic.setUserGender(c.getString("userGender"));
                            providerbasic.setCurrentLocation(c.getString("currentLocation"));
                            providerbasic.setLocationLat(c.getString("locationLat"));
                            providerbasic.setLocationLong(c.getString("locationLong"));
                            providerbasic.setUserAddress(c.getString("userAddress"));
                            providerbasic.setUserDesignation(c.getString("userDesignation"));
                            providerbasic.setUserWagesHour(c.getString("userWagesHour"));
                            providerbasic.setProfileImageUrl(c.getString("profileImageUrl"));
                            providerbasic.setUserEmail(c.getString("userEmail"));
                            providerbasic.setUserMobile(c.getString("userMobile"));
                            providerbasic.setCategoryName(c.getString("categoryName"));

                            Log.i("Reg2_submit"," exception "+c.getString("locationLat"));
                            Log.i("Reg2_submit"," exception "+c.getString("currentLocation"));


                            providers.add(providerbasic);

                        }

                        regrsponse.GetProviderList(providers);


                    }
                }
                else {

                    String msg ="Get Category Failed";
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
}
