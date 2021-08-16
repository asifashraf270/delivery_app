package com.example.khushbakht.grocerjin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by khush on 12/7/2017.
 */

public class RememberCredentials {

    public  static   String SharedPreferenceName = "Login.Credentials.Remember.Custom.MOWDelivery";
    private Context context;
    private  String flagName = "LoginCredentialFlag";
    private  String UserName = "LoginCredentialUsername";
    private  String PassName = "LoginCredentialPassword";

    private SharedPreferences sharedPreferences;

    public RememberCredentials(Context context)
    {
        this.context = context;

    }

    /*****************************************************************************/

    /**
     * These methods are used to get/set flag that either the
     * user checked the Checkbox or not ?
     * if checked then  returns "true"
     * else "false"
     * @return flag from Shared Preferences
     */
    public boolean getFlag() {

        sharedPreferences = context.getSharedPreferences(SharedPreferenceName,context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(flagName,false);
    }
    public void setFlag(boolean flag) {

        sharedPreferences = context.getSharedPreferences(SharedPreferenceName,context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(flagName,flag);
        editor.commit();
    }

    /*****************************************************************************/

    /**
     * These methods are used to get/set username
     * if username exists then  returns "username"
     * else "null"
     * @return UserName from Shared Preferences
     */
    public String getUsername() {

        sharedPreferences = context.getSharedPreferences(SharedPreferenceName, context.MODE_PRIVATE);
        return sharedPreferences.getString(UserName,null);
    }

    public void setUserName(String userName){

        sharedPreferences = context.getSharedPreferences(SharedPreferenceName,context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UserName,userName);
        editor.commit();

    }

    /*****************************************************************************/

    /**
     * These methods are used to get/set password
     * if password exists then  returns "password"
     * else "null"
     * @return Password from Shared Preferences
     */
    public String getPassword()
    {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceName,context.MODE_PRIVATE);
        return sharedPreferences.getString(PassName,null);
    }

    public void setPassword(String password)
    {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceName,context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PassName,password);
        editor.commit();

    }

}

