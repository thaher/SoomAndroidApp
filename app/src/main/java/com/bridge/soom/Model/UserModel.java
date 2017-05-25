package com.bridge.soom.Model;

import java.io.Serializable;

/**
 * Created by Thaher-Majeed on 13-03-2017.
 */

public class UserModel implements Serializable {

      private String  userId  ;
      private String  userEmail ;
      private String  userMobile  ;
      private Boolean  userStatus ;
      private String  userType    ;
      private Integer  userDetailsId ;
      private String  userFirstName  ;
      private String  userLastName    ;
      private String  userGender ;
      private String  currentLocation ;
      private String  userAddress   ;
      private String  userEducation    ;
      private String  userDesignation    ;
      private String  userExperience    ;
      private Double  userWagesHour ;
      private String  userAdditionalSkill    ;
      private String  profileImageUrl    ;
      private Integer  countryId  ;
      private String  countryName  ;
      private String  stateName  ;
    private Integer stateId;
    private String cityName;
    private String cityId;
      private String  timeZone   ;
      private String  cultureinfo ;
      private String  accessToken   ;

    private Boolean  userEmailVerified  ;
      private Boolean  userMobileVerified   ;
      private String  locationLat     ;
      private String  locationLong    ;
      private String  preLocation1    ;
      private String  preLocation1Lat   ;
      private String  preLocation1Long    ;
      private String  preLocation2    ;
      private String  preLocation2Lat    ;
      private String  preLocation2Long    ;
      private String  preLocation3     ;
      private String  preLocation3Lat   ;
      private String  preLocation3Long   ;
    private Integer  userStatusLevel   ;
    private String languagesknown;
    private String employmentType;
    private  String dob;
    private String [] categoryId;
    private String [] categoryName;
    private String [] filterId;
    private String [] categoryforFilterId;
    private String [] filterName;


    public UserModel() {
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String[] getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String[] categoryId) {
        this.categoryId = categoryId;
    }

    public String[] getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String[] categoryName) {
        this.categoryName = categoryName;
    }
    public void setCategoryNameindex(String catnam, int i) {
        this.categoryName[i] = catnam;
    }

    public String[] getFilterId() {
        return filterId;
    }

    public void setFilterId(String[] filterId) {
        this.filterId = filterId;
    }

    public String[] getCategoryforFilterId() {
        return categoryforFilterId;
    }

    public void setCategoryforFilterId(String[] categoryforFilterId) {
        this.categoryforFilterId = categoryforFilterId;
    }

    public String[] getFilterName() {
        return filterName;
    }

    public void setFilterName(String[] filterName) {
        this.filterName = filterName;
    }

    public void setFilterNameindex(String catnam, int i) {
        this.filterName[i] = catnam;
    }


    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }



    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLanguagesknown() {
        return languagesknown;
    }

    public void setLanguagesknown(String languagesknown) {
        this.languagesknown = languagesknown;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getUserStatusLevel() {
        return userStatusLevel;
    }

    public void setUserStatusLevel(Integer userStatusLevel) {
        this.userStatusLevel = userStatusLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserDetailsId() {
        return userDetailsId;
    }

    public void setUserDetailsId(Integer userDetailsId) {
        this.userDetailsId = userDetailsId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public String getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(String userExperience) {
        this.userExperience = userExperience;
    }

    public Double getUserWagesHour() {
        return userWagesHour;
    }

    public void setUserWagesHour(Double userWagesHour) {
        this.userWagesHour = userWagesHour;
    }

    public String getUserAdditionalSkill() {
        return userAdditionalSkill;
    }

    public void setUserAdditionalSkill(String userAdditionalSkill) {
        this.userAdditionalSkill = userAdditionalSkill;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCultureinfo() {
        return cultureinfo;
    }

    public void setCultureinfo(String cultureinfo) {
        this.cultureinfo = cultureinfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public Boolean getUserEmailVerified() {
        return userEmailVerified;
    }

    public void setUserEmailVerified(Boolean userEmailVerified) {
        this.userEmailVerified = userEmailVerified;
    }

    public Boolean getUserMobileVerified() {
        return userMobileVerified;
    }

    public void setUserMobileVerified(Boolean userMobileVerified) {
        this.userMobileVerified = userMobileVerified;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getPreLocation1() {
        return preLocation1;
    }

    public void setPreLocation1(String preLocation1) {
        this.preLocation1 = preLocation1;
    }

    public String getPreLocation1Lat() {
        return preLocation1Lat;
    }

    public void setPreLocation1Lat(String preLocation1Lat) {
        this.preLocation1Lat = preLocation1Lat;
    }

    public String getPreLocation1Long() {
        return preLocation1Long;
    }

    public void setPreLocation1Long(String preLocation1Long) {
        this.preLocation1Long = preLocation1Long;
    }

    public String getPreLocation2() {
        return preLocation2;
    }

    public void setPreLocation2(String preLocation2) {
        this.preLocation2 = preLocation2;
    }

    public String getPreLocation2Lat() {
        return preLocation2Lat;
    }

    public void setPreLocation2Lat(String preLocation2Lat) {
        this.preLocation2Lat = preLocation2Lat;
    }

    public String getPreLocation2Long() {
        return preLocation2Long;
    }

    public void setPreLocation2Long(String preLocation2Long) {
        this.preLocation2Long = preLocation2Long;
    }

    public String getPreLocation3() {
        return preLocation3;
    }

    public void setPreLocation3(String preLocation3) {
        this.preLocation3 = preLocation3;
    }

    public String getPreLocation3Lat() {
        return preLocation3Lat;
    }

    public void setPreLocation3Lat(String preLocation3Lat) {
        this.preLocation3Lat = preLocation3Lat;
    }

    public String getPreLocation3Long() {
        return preLocation3Long;
    }

    public void setPreLocation3Long(String preLocation3Long) {
        this.preLocation3Long = preLocation3Long;
    }
}
