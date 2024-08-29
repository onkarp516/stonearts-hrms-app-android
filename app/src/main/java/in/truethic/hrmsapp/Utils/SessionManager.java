package in.truethic.hrmsapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
    private final SharedPreferences mPrefs;
    private static final String MyPREFERENCES = "Renuka_Eng";
    public static  String checkedIn = "check In";
    public static String checkInTime = "check_in_time";
    public static String checkedOut = "check Out";
    public static String checkedOuTime = "check_out_time";
    public static String Token_expiry_time="Token_expiry_time";
    public static String Refresh_Token="Refresh_Token";
    public static String Employee_Name="e_name";
    public static String Employee_Username="username";
    public static String Employee_Password="password";
    public static String attId="";
    public static String breakId="";
    public static String breakMasterId;
    public static String remark="";
    public static String taskType="";
    public static String siteName="sname";
    public static String siteCode="scode";
    public static String siteLatitude="slat";
    public static String siteLongitude ="slog";
    public static String siteRadius="srad";
    public static String isRange="range";
    public static String teamsiteLatitude="";
    public static String teamsiteLongitude ="";
    public static String token_branchId="";
    public static String token_teamId ="";

    private Context mContext;
    SharedPreferences.Editor mEditor;

    public SessionManager(Context mContext)
    {
        this.mContext = mContext;
        mPrefs =mContext.getSharedPreferences( MyPREFERENCES, mContext.MODE_PRIVATE );
        mEditor = mPrefs.edit();
    }

    public void setIntData(String key, int val)
    {
        mEditor.putInt( key,val );
        mEditor.commit();
    }

    public int getIntData(String key)
    {
        return mPrefs.getInt( key,0 );
    }

    public void setLongData(String key,long val)
    {
        mEditor.putLong(key,val);
        mEditor.commit();
    }

    public long getLongData(String key)
    {
        return  mPrefs.getLong(key,0);
    }

    public void setStringData(String key, String val)
    {
        mEditor.putString( key,val );
        mEditor.commit();
    }

    public String getStringData(String key)
    {
        return mPrefs.getString( key,"" );
    }

    public void setBooleanData(String key,Boolean val)
    {
        mEditor.putBoolean( key,val );
        mEditor.commit();
    }

    public Boolean getBooleanData(String key)
    {
        return mPrefs.getBoolean( key,false );
    }
}
