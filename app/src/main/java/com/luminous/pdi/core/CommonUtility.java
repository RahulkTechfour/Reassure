package com.luminous.pdi.core;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.luminous.pdi.BuildConfig;
import com.luminous.pdi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;

public class CommonUtility {

    private static ProgressDialog progressDialog;
    public static final String PROFILE_PIC_NAME = "PROFILE_PIC.jpg";
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isNetworkAvailable(Context context) {

        // old approach
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
                return true;
            else {
                Toast.makeText(context, "No Internet Connection!!", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        //Working on Android 10

     /*   if(context == null)  return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }

            else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Toast.makeText(context, "No Internet Connection!!", Toast.LENGTH_LONG).show();
        return false;*/

    }

    public static void showUpdateDialog(Context context) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Update");
            builder.setMessage("A New Update is Available");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.luminous.mpartner")));
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ExitActivity.exitApplication(context);
                }
            });

            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
        }
    }

    public static boolean isNewVersionAvailable(Context context) {
        String currentVersion, latestVersion;
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

//            //Check Play Store Version
        VersionChecker versionChecker = new VersionChecker();
        latestVersion = null;
        try {
            latestVersion = versionChecker.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Compare Current Version with Playstore Version
        if (latestVersion != null) {
            //if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            if (Float.parseFloat(currentVersion) < Float.parseFloat(latestVersion) && !currentVersion.equalsIgnoreCase(latestVersion)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


    public static void printLog(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static List<String> appVersionDetails(Context context) {
        List<String> strings = null;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                strings = new ArrayList<String>();
                strings.add(info.versionName);
                strings.add(String.valueOf(info.versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return strings;
    }




    public static String getNetworkType(Context context) {

        try {

            final ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi =
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile =
                    connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                return "wifi";
            } else if (mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                TelephonyManager mTelephonyManager = (TelephonyManager)
                        context.getSystemService(Context.TELEPHONY_SERVICE);
                int networkType = mTelephonyManager.getNetworkType();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2g";
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        /**
                         From this link https://goo.gl/R2HOjR ..NETWORK_TYPE_EVDO_0 & NETWORK_TYPE_EVDO_A
                         EV-DO is an evolution of the CDMA2000 (IS-2000) standard that supports high data rates.

                         Where CDMA2000 https://goo.gl/1y10WI .CDMA2000 is a family of 3G[1] mobile technology standards for sending voice,
                         data, and signaling data between mobile phones and cell sites.
                         */
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        //Log.d("Type", "3g");
                        //For 3g HSDPA , HSPAP(HSPA+) are main  networktype which are under 3g Network
                        //But from other constants also it will 3g like HSPA,HSDPA etc which are in 3g case.
                        //Some cases are added after  testing(real) in device with 3g enable data
                        //and speed also matters to decide 3g network type
                        //http://goo.gl/bhtVT
                        return "3g";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        //No specification for the 4g but from wiki
                        //I found(LTE (Long-Term Evolution, commonly marketed as 4G LTE))
                        //https://goo.gl/9t7yrR
                        return "4g";
                    default:
                        return "Unknown";
                }
            } else {
                return "no_network";
            }
        } catch (Exception e) {
            return "no_network";
        }
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

   /* public static String getDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }*/


   /* @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDeviceId(Context mContext) {

        String deviceIEMI = null;
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //   deviceIEMI = telephonyManager.getImei();
            deviceIEMI = telephonyManager.getDeviceId();
        }

        return deviceIEMI;
    }*/


    public static String getNetworkOperator(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = tManager.getNetworkOperatorName();
        return carrierName;
    }

    public static String getIpAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;
    }

    public static void openSettingsScreen(Context context) {
      //  Toast.makeText(context, context.getString(R.string.permission_msg), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void downloadFile(Context context, String uRl, String name) {

        try {
            File direct = new File(Environment.getExternalStorageDirectory() + "/Luminous");

            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(true).setTitle(name)
                    .setDescription("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            mgr.enqueue(request);
        } catch (Exception ex) {
            ex.getMessage();
        }
        // Open Download Manager to view File progress
//        Toast.makeText(context, "Downloading...", Toast.LENGTH_LONG).show();
//        context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

    }








  /*  public static void process(Document document, JSONObject json) throws JSONException, DocumentException {
        for (String k : json) {
            Object object = json.get(k);
            if (object instanceof JSONArray) {
                JSONArray list = json.getJSONArray(k);
                process(document, list);
            } else if (object instanceof JSONObject) {
                process(document, json.getJSONObject(k));
            } else {
                document.add(new Paragraph(k + " " + json.get(k)));
            }

        }
    }

    public static void process(Document document, JSONArray json) throws JSONException, DocumentException {
        for (int x = 0; x < json.length(); x++) {
            Object object = json.get(x);
            if (object instanceof JSONArray) {
                JSONArray list = json.getJSONArray(x);
                process(document, list);
            } else if (object instanceof JSONObject) {
                process(document, json.getJSONObject(x));
            } else {
                document.add(new Paragraph(json.get(x).toString()));
            }

        }
    }*/


    public static Bitmap getBitmap(Context context, Uri imgUri) {

        String selectedImagePath = getRealPathFromURI(context, imgUri);
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(selectedImagePath, options);

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String profileImgsaveToInternalSorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, PROFILE_PIC_NAME);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return directory.getAbsolutePath();
    }

    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getPastSeventhDate() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -7);
        Date date = today.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static Date convertDate(String sDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(sDate);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static boolean canShowOrDismissDialog(Activity activity) {
        return activity instanceof Activity && !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean canShowOrDismissDialog(Context context) {
        if (context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isFinishing() && !activity.isDestroyed();
        }
        return false;
    }

   /* public static boolean canShowOrDismissDialog(Activity activity) {
        return activity instanceof Activity && !activity.isFinishing() && !activity.isDestroyed();
    }

    public static boolean canShowOrDismissDialog(Context context) {
        if (context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isFinishing() && !activity.isDestroyed();
        }
        return false;
    }*/








    public static void shareDownloadLink(String message, Context context) {

        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(txtIntent, "Share download link"));

    }

    public static void shareImageLink(String message, Context context, Bitmap bitmap) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(android.content.Intent.EXTRA_TEXT, message);
        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
        context.startActivity(Intent.createChooser(i, "Share Image"));

    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }


    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static Spanned stripHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return Html.fromHtml(html);
            }
        }
        return null;
    }


    public static String encodeFromString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    public static String getUniqueIMEIId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            //  String imei_id =  telephonyManager.getMeid();
            // String imei_id_slot =  telephonyManager.getMeid();


            String imei = null;
            try {
                imei = telephonyManager.getImei();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  Log.e("getid","getid"+imei_id_slot);

            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                String deviceID = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                return deviceID;
            }
        }else {
            /*if imie number not null*/

            String imei = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            }
            if (imei != null && !imei.isEmpty()) {
                return imei;
            }else {
                return telephonyManager.getDeviceId();
            }
        }
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //  String imei_id =  telephonyManager.getMeid();
            //  String imei_id_slot =  telephonyManager.getMeid();

            String imei =  telephonyManager.getImei();
            //   Log.e("getid","getid"+imei_id_slot);

            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                String deviceID = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                return deviceID;
            }


        }else {
            String imei = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imei = telephonyManager.getImei();
            }
            if (imei != null && !imei.isEmpty()) {
                return imei;
            }else {
                return telephonyManager.getDeviceId();
            }
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke


    public static String  getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    static String monthString_day,monthString_year,DayYearConct;
    public static String  getCurrentDateMMYY()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        String formattedDate = df.format(c.getTime());

        try {
            Date dateObj = df.parse(formattedDate);
            monthString_day  = (String) android.text.format.DateFormat.format("MMM",  dateObj); // Jun
            monthString_year  = (String) android.text.format.DateFormat.format("yyyy",  dateObj); // Jun
            DayYearConct = monthString_day+"-"+monthString_year;

        } catch (ParseException e) {
            e.printStackTrace();
        }



        return DayYearConct;
    }

}
