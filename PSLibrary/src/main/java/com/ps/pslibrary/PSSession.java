package com.ps.pslibrary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Sanam Mijar on 14, May, 2021.
 */

public class PSSession {
    private final SharedPreferences configPref, userPref;
    private final SharedPreferences.Editor confEditor, userEditor;
    private final Context context;
    private final Gson gson;

    private static final String CONF_PREF = "Configuration";
    public static final String KEY_SYNC_STATE = "Sync State";
    public static final String KEY_INIT_WT_STATE = "InitialWalkThroughState";
    public static final String KEY_SERVER_ADDRESS = "IP Address";

    public static final String KEY_LAST_FRAGMENT = "LastFragment";

    private static final String USER_PREF = "AdminLogin";

    private static final String IS_LOGIN = "Login_Status";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_NAME = "user_full_name";
    public static final String KEY_PROPERTY_CODE = "propertyCode";
    public static final String KEY_SYS_THEME = "SystemTheme";
    public static final String KEY_UID = "UserID";
    public static final String KEY_USER_TYPE = "UserType";
    public static final String KEY_USER_GENDER_TITLE = "UserGenderTitle";
    public static final String KEY_SEL_HW_ROW_ID = "SelectedHWRowId";
    public static final String KEY_SEL_HW_ID = "SelectedHWId";
    public static final String KEY_HW_DTL = "SelectedHomeworkInfo";
    public static final String KEY_NOTICE_DTL = "SelectedNoticeInfo";
    public static final String KEY_USER_TOKEN = "userToken";
    public static final String KEY_USER_P_IMG = "userProfileImage";
    public static final String KEY_IS_CHECKED_REMEMBER = "Remember me";
    public static final String KEY_USER_PROFILE_INFO = "UserProfileInformation";
    public static final String KEY_ZOOM_URL = "OnlineClassURL";

    public static final int FA_SIGN_IN = 0;
    public static final int FA_DASH = 1;
    public static final int FA_WT_S = 2;
    private final String ID;

    /**
     * SessionManager: Constructor that accepts Context as parameter.
     **/
    public PSSession(Context context){
        this.ID = Utils.id(context);
        this.context = context;
        configPref = context.getSharedPreferences(CONF_PREF, Context.MODE_PRIVATE);
        userPref = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        confEditor = configPref.edit();
        userEditor = userPref.edit();

        userEditor.apply();
        confEditor.apply();
        gson = new Gson();
    }





    /**
     * setSystemSessionFor: Create System Session on your needs. Params:
     * String sessionKey    = To define the Key to store value on.
     * String sessionValue  = To assign the value on the sessionKey to store.
     **/
    public void setSystemSessionFor(String sessionKey, String sessionValue){
        confEditor.putString(sessionKey, AESUtils.encrypt(sessionValue, ID));
        confEditor.apply();
        confEditor.commit();
    }

    /**
     * getSystemSession: Get System Session as you have stored in above setSystemSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to get the value assigned to the key.
     **/
    public String getSystemSession(String sessionKey){
        final String decryptedSessionValue = configPref.getString(sessionKey, null);
        if (decryptedSessionValue == null){ return null; }
        return AESUtils.decrypt(decryptedSessionValue, ID);
    }





    /**
     * setSystemBooleanSessionFor: Create System Boolean Session on your needs. Params:
     * String sessionKey    = To define the Key to store value on.
     * boolean sessionValue  = To assign the value on the sessionKey to store.
     **/
    public void setSystemBooleanSessionFor(String sessionKey, boolean sessionValue){
        confEditor.putBoolean(sessionKey, sessionValue);
        confEditor.apply();
        confEditor.commit();
    }

    /**
     * getSystemBooleanSession: Get System Boolean Session as you have stored in above setSystemBooleanSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to get the value assigned to the key.
     **/
    public boolean getSystemBooleanSession(String sessionKey){
        return configPref.getBoolean(sessionKey, false);
    }





    /**
     * clearSystemSessionFor: Clear the System Session according to the key you have stored in setSystemSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to clear/remove the value assigned to the key.
     **/
    public void clearSystemSessionFor(String sessionKey){
        confEditor.remove(sessionKey);
        confEditor.apply();
        confEditor.commit();
    }





    /**
     * setUserSessionFor: Create User Session on your needs. Params:
     * String sessionKey    = To define the Key to store value on.
     * String sessionValue  = To assign the value on the sessionKey to store.
     **/
    public void setUserSessionFor(String sessionKey, String sessionValue){
        userEditor.putString(sessionKey, AESUtils.encrypt(sessionValue, ID));
        userEditor.apply();
        userEditor.commit();
    }

    /**
     * getUserSession: Get User Session as you have stored in above setUserSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to get the value assigned to the key.
     **/
    public String getUserSession(String sessionKey){
        final String decryptedUserSessionValue = userPref.getString(sessionKey, null);
        if (decryptedUserSessionValue == null){ return null; }
        return AESUtils.decrypt(decryptedUserSessionValue, ID);
    }




    /**
     * setUserBooleanSessionFor: Create User Boolean Session on your needs. Params:
     * String sessionKey    = To define the Key to store value on.
     * boolean sessionValue  = To assign the value on the sessionKey to store.
     **/
    public void setUserBooleanSessionFor(String sessionKey, boolean sessionValue){
        userEditor.putBoolean(sessionKey, sessionValue);
        userEditor.apply();
        userEditor.commit();
    }


    /**
     * getUserBooleanSession: Get User Boolean Session as you have stored in above setUserBooleanSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to get the value assigned to the key.
     **/
    public boolean getUserBooleanSession(String sessionKey){
        return userPref.getBoolean(sessionKey, false);
    }





    /**
     * setUserIntSessionFor: Create User Integer Session on your needs. Params:
     * String sessionKey    = To define the Key to store value on.
     * int sessionValue  = To assign the integer value on the sessionKey to store.
     **/
    public void setUserIntSessionFor(String sessionKey, int sessionValue){
        userEditor.putInt(sessionKey, sessionValue);
        userEditor.apply();
        userEditor.commit();
    }

    /**
     * getUserIntSession: Get User Integer Session as you have stored in above setUserIntSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to get the value assigned to the key.
     **/
    public int getUserIntSession(String sessionKey){
        return userPref.getInt(sessionKey, 0);
    }





    /**
     * clearUserSessionFor: Clear the System Session according to the key you have stored in setUserSessionFor method. Params:
     * String sessionKey    = Specify the sessionKey to clear/remove the value assigned to the key.
     **/
    public void clearUserSessionFor(String sessionKey){
        userEditor.remove(sessionKey);
        userEditor.apply();
        userEditor.commit();
    }


    /**
     * createConfigurationSession: Method to store Server URL and it sync status bool session.
     **/
    public void createConfigurationSession(String serverURL){
        confEditor.putString(KEY_SERVER_ADDRESS, AESUtils.encrypt(serverURL, ID));
        confEditor.putBoolean(KEY_SYNC_STATE, true);
        confEditor.apply();
        confEditor.commit();
    }





    /**
     * createLoginSession: To store LoggedIn user session. Params
     * String username      = Username of the loggedIn user. (Either passed data or API fetched data)
     * String userID        = UserId of the LoggedIn user. API fetched data
     * String userToken     = UserToken of the LoggedIn user. API fetched data.
     * String userImage     = UserImage of the LoggedIn user. This is the drawable resource file. (User icon according to the gender.)
     * String userTitle     = UserTitle of the LoggedIn user. API fetched data. e.g.: Mr, Mrs, etc...
     * String userFullName  = FullName of the LoggedIn user. API fetched data.
     * String userType      = UserType of LoggedIn user. API fetched data. e.g.: Teacher, Student, Admin, etc...
     **/
    public void createLoginSession(String username, String userID, String userToken, String userImage, String userTitle, String userFullName, String userType){
        userEditor.putBoolean(IS_LOGIN, true);
        userEditor.putString(KEY_USERNAME, username);
        userEditor.putString(KEY_UID, userID);
        userEditor.putString(KEY_USER_TOKEN, userToken);
        userEditor.putString(KEY_USER_P_IMG, userImage);
        userEditor.putString(KEY_USER_GENDER_TITLE, userTitle);
        userEditor.putString(KEY_USER_NAME, userFullName);
        userEditor.putString(KEY_USER_TYPE, userType);
        userEditor.apply();
    }

    /**
     * getUserDetails: Method that returns HashMap value of type <String, String> of LoggedIn User.
     **/
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, userPref.getString(KEY_USERNAME, ""));
        user.put(KEY_USER_GENDER_TITLE, userPref.getString(KEY_USER_GENDER_TITLE, ""));
        user.put(KEY_USER_NAME, userPref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_TYPE, userPref.getString(KEY_USER_TYPE, ""));
        user.put(KEY_UID, userPref.getString(KEY_UID, ""));
        user.put(KEY_USER_TOKEN, userPref.getString(KEY_USER_TOKEN, ""));
        user.put(KEY_USER_P_IMG, userPref.getString(KEY_USER_P_IMG, ""));
        return user;
    }






    /**
     * setModelSession: Method to store session of model class.
     **/
    public <T> void setModelSession(String key, Class<T> modelClassData){
        String modelJson = gson.toJson(modelClassData);
        String encryptedJson = AESUtils.encrypt(modelJson, key);
        userEditor.putString(key,encryptedJson).apply();
    }

    /**
     * getModelSession: Method to get the session of model class which was set in setModelSession method.
     **/
    public <T> Class<T> getModelSession(String key, Class<T> modelClass){
        String encryptedModelData = userPref.getString(key, "");
        if (encryptedModelData != null && encryptedModelData.length() ==0){
            return null;
        }
        String decryptedModelData = AESUtils.decrypt(encryptedModelData, key);
        return new Gson().fromJson(decryptedModelData, (Type) modelClass);
    }






    /**
     * setSelectedHW: To store the selected homework session.
     **/
    public void setSelectedHW(String hwRowId, String homeworkId){
        userEditor.putString(KEY_SEL_HW_ROW_ID, hwRowId);
        userEditor.putString(KEY_SEL_HW_ID, homeworkId);
        userEditor.apply();
    }

    /**
     * getSelectedHW: To get the selected homework session which was stored in setSelectedHW method.
     **/
    public HashMap<String, String> getSelectedHW(){
        HashMap<String, String> homework = new HashMap<>();
        homework.put(KEY_SEL_HW_ROW_ID, userPref.getString(KEY_SEL_HW_ROW_ID, ""));
        homework.put(KEY_SEL_HW_ID, userPref.getString(KEY_SEL_HW_ID, ""));
        return homework;
    }






    /**
     * checkLogin: To check if the user has logged in session.
     * If user is new and the app is opened for the first time then it will return integer value of FA_WT_S.
     * If user is not new and the user have signed out of the app then it will return integer value of FA_SIGN_IN.
     * If user is signed in then it will return integer value of FA_DASH.
     *
     * According to the integer response returned by the method you can use it differently.
     * e.g.: if(checkLogin() == FA_SIGN_IN){
     *         // Do your task for this response. etc,....
     *      }
     **/
    public int checkLogin(){
        int returnValue;
        if (isInitWTComplete()) {
            if (!this.isLoggedIn()) {
                returnValue = FA_SIGN_IN;
            } else {
                returnValue = FA_DASH;
            }
        }else{
            returnValue = FA_WT_S;
        }
        return returnValue;
    }

    public void clearThisSession(String key){ userEditor.putString(key, null); userEditor.apply(); }
    public void logoutUser(){ userEditor.clear(); userEditor.apply(); }
    private boolean isInitWTComplete(){ return configPref.getBoolean(KEY_INIT_WT_STATE, false); }
    private boolean isLoggedIn(){ return userPref.getBoolean(IS_LOGIN, false); }
    public boolean isCheckedRemember(){ return userPref.getBoolean(KEY_IS_CHECKED_REMEMBER, false); }
}