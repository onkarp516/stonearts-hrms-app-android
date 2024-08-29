package in.truethic.hrmsapp.Utils;

import java.util.ArrayList;
import java.util.List;

import in.truethic.hrmsapp.Model.SiteDataModel;
import in.truethic.hrmsapp.Model.TeamMembersModel;

public class Globals {
    public static String tokent = "token";
    public static String isLoggedIn = "isLoggedIn";
    public static String emp_username="";
    public static String username="";
    public static String checkInStatus_G="";
    public static String checkOutStatus_G="";
    public static boolean onclick_or_not=false;
    public static boolean mDownTime=false;
    public static boolean canHePunchIn=false;
    public static String mobileNumber="";
    public static boolean isGpsActive=false;
    public static boolean isGpsClicked=false;
    public static String emp_name="";
    public static String userId="";
    public static String emp_designation="";
    public static String emp_email="";
    public static String emp_phone="";
    public static String DOJ="";
    public static String address="";
    public static int radioSelected=0;
    public static int radioSelectedAttendance=0;
    public static int leaveCategory=0;
    public static String leave_name="";
    //public static String attendanceDetail_responses="";

    public static ArrayList<SiteDataModel> siteData=new ArrayList<>();
    public static String teamlong="";
    public static String teamlat="";

    public static String siteRadius="";
    public static List<TeamMembersModel> finalList=new ArrayList<>();

    public static boolean isSelectAll=false;

    public static String branchId="";
    public static String teamId ="";
    public static Boolean att_type;


    public static String token_branchId="";
    public static String token_teamId ="";


}
