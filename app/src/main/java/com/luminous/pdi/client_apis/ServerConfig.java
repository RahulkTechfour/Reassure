package com.luminous.pdi.client_apis;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.luminous.pdi.core.CommonUtility;
import com.luminous.pdi.core.SharedPreferenceKeys;
import com.luminous.pdi.core.SharedPrefsManager;

import java.util.List;
import java.util.Locale;

public class ServerConfig {


    public static String TEST_URL = "http://dev-reassureapi.innov.co.in/";
    public static String PRODUCTION_URL = "http://166.62.100.102/Api/MpartnerApi2/";
    public static String TEST_OTP_URL = "https://mpartnerv2.luminousindia.com/nonsapservices/api/nonsapservice/";
    public static String PRIMARY_REPORT_URL = "https://mpartnerv2.luminousindia.com/nonsapservices/api/sapservice/";



    private static final String TAG = "ServerConfig";


    public static String getURL() {
            return TEST_URL;
    }
    public static String reassurecreateOTPUrl() {
        return getURL() + "mobile/api/v1/user/login/otp/";
    }

    public static String ReassureVerifyOTPUrl() {
        return getURL() + "/mobile/api/v1/user/login/otpverification/";
    }

    public static String reAssureUploadImage(){

        return getURL()+"/mobile/api/v1/document/upload/";
    }

    public static String createOTPUrl() {
        return getURL() + "logincreateotp?user_id=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M&fcm_token=";
    }

    public static String calenderFragmentURL() {
        return getURL() + "PendingVisitDetails?user_id=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M&fcm_token=";
    }

    public static String newUrl(String url, Context context, String callerClass) {

        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(context);
        String userId = sharedPrefsManager.getString(SharedPreferenceKeys.USER_ID);

        //user Id
        if (url.contains("?"))
            url += "&user_id=" + userId;
        else
            url += "?user_id=" + userId;
//


        //Token
        if (!TextUtils.isEmpty(sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN)))
            url += "&token=" + sharedPrefsManager.getString(SharedPreferenceKeys.TOKEN);
//
//        if (url.contains("?")) {
//            url += "&user_id=9900000002&token=e26aafe885163d9b565fe4b9dcad10f1";
//        } else {
//            url += "?user_id=9900000002&token=e26aafe885163d9b565fe4b9dcad10f1";
//        }

        //app version name and code
        String appVersionName = "";
        String appVersionCode = "";
        final List<String> versionList = CommonUtility.appVersionDetails(context);
        if (versionList != null && versionList.size() == 2) {
            appVersionName = versionList.get(0);
            appVersionCode = versionList.get(1);
        }
        url += "&app_version=" + appVersionName;
        url += "&appversion_code=" + appVersionCode;

        //device_id
        url += "&device_id=" + CommonUtility.getDeviceId(context);

        //device_name
        url += "&device_name=" + CommonUtility.getDeviceName();

        //os_type
        url += "&os_type=Android";

        //os_version_code and os_version_name
        if (!url.toLowerCase().contains("os_v_code") && !url.toLowerCase().contains("os_v_code")) {
            try {
                url += "&os_version_code=" + Build.VERSION.SDK_INT;
                url += "&os_version_name=" + Build.VERSION.RELEASE;
            } catch (Exception e) {
                // intentionally left blank
            }
        }

        //ip_address
        url += "&ip_address=" + CommonUtility.getIpAddress(context);

        //language=EN
        url += "&language=" + "EN";

        //screen_name=A.class
        if (callerClass != null) {
            url += "&screen_name=" + callerClass;
        }

        //network_type=2G
        url += "&network_type=" + CommonUtility.getNetworkType(context);

        //network_operator=Airtel
        url += "&network_operator=" + CommonUtility.getNetworkOperator(context);

        //time_captured
        url += "&time_captured=" + System.currentTimeMillis();

        //channel=M
        url += "&channel=M";

        url += "&fcm_token=" + "";

        url = url.replaceAll(" ", "%20");

        Log.d(TAG, "newUrl: " + url);

        return url;

    }

    public static String verifyOtpUrl() {
        return getURL() + "OTPAuthentication?user_id=%s&otp=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getSideBottomNavigationUrl() {
        return "http://166.62.100.102:6262/Api/PDI_Api/" + "SideAndBottomMenu?user_id=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getHomePageUrl() {
        return getURL() +"mobile/api/v1/case/list/";
    }
    public static String getHitDataeUrl() {
        return getURL() + "Calander_Pending_Visit?user_id=%s&pending_date=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getFaultUrl() {
        return getURL() + "GetFaultAndMaterialList?user_id=%s&DivisionID=%s&DistributorID=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getMaterialUrl() {
        return getURL() + "GetScanProductData?user_id=%s&serialNo=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getVisitDetailPageUrl() {
        return getURL() + "Get_Visit_Status_Details?user_id=%s&statusflag=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getDraftVisitDetailPageUrl() {
        return getURL() + "Get_Visit_Status_Details?user_id=%s&statusflag=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getSearchPageUrl() {
        return getURL() + "SearchRecordStatus_ByEngineerVisit?user_id=%s&serialNo=%s&MaterialName=%s&ClaimStatus=%s&Request_No=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getDraftDeleteUrl() {
        return getURL() + "Delete_DraftRecord_ByEngineerVisit?user_id=%s&DistributorID=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String saveFanDetailUrl() {
        return getURL() + "SaveFanDetails?user_id=%s&DistributorID=%s&Request_No=%s&StatusID=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getMaterialLightUrl() {

        return getURL() + "GetFaultAndMaterialList?user_id=%s&DivisionID=%s&DistributorID=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getInvoiceQntyListUrl() {
        return getURL() + "Get_InvoiceQtySold_By_MaterialName?user_id=%s&Materialname=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getSaveStatusListUrl() {
        return getURL() + "GetStatusList?user_id=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String saveLightDetailUrl() {
        return getURL() + "SaveLightandWDDetails?user_id=%s&DistributorID=%s&Request_No=%s&StatusID=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getSavedDraftFanDetailUrl() {
        return getURL() + "DraftFanDetails?user_id=%s&Request_No=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getproductWarrantyStatusUrl() {
        return getURL() + "FAN_WarrantyStatus?user_id=%s&serialNo=%s&InspectionDate=%s&DOP=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getproductLightWarrantyStatusUrl() {
        return getURL() + "LightAndWD_WarrantyStatus?user_id=%s&ProductID=%s&MFGDate=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }

    public static String getSavedDraftLightDetailUrl() {
        return getURL() + "DraftLightAndWDDetails?user_id=%s&Request_No=%s&token=%s&app_version=%s&appversion_code=%s&device_id=%s&device_name=%s&os_type=%s&os_version_code=%s&os_version_name=%s&ip_address=%s&language=EN&screen_name=%s&network_type=%s&network_operator=%s&time_captured=%s&channel=M";
    }
}
