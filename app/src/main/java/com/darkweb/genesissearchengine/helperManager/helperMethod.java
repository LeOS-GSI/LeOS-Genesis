package com.darkweb.genesissearchengine.helperManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

import com.darkweb.genesissearchengine.constants.keys;
import com.darkweb.genesissearchengine.dataManager.dataController;
import com.darkweb.genesissearchengine.dataManager.dataEnums;
import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class helperMethod
{
    /*Helper Methods General*/

    public static String completeURL(String pURL){
        if(pURL.equals("about:blank")){
            return pURL;
        }
        URL weburl = null;
        try
        {
            weburl = new URL(pURL);
            URLConnection result = weburl.openConnection();

            if (result instanceof HttpsURLConnection) {

            }
            else if (result instanceof HttpURLConnection) {
                // http
            }
            else {
                // null or something bad happened
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if(!pURL.startsWith("www.")&& !pURL.startsWith("http://")&& !pURL.startsWith("https://")){
            pURL = ""+pURL;
        }
        if(!pURL.startsWith("http://")&&!pURL.startsWith("https://")){
            pURL = "http://"+pURL;
        }
        return pURL;
    }

    public static SpannableString urlDesigner(String url, Context pContext){

        if (url.contains("https://"))
        {
            SpannableString ss = new SpannableString(url);
            ss.setSpan(new ForegroundColorSpan(Color.argb(255, 0, 123, 43)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        } else if (url.contains("http://"))
        {
            SpannableString ss = new SpannableString(url);
            ss.setSpan(new ForegroundColorSpan(Color.argb(255, 0, 128, 43)), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(Color.GRAY), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        } else
        {
            SpannableString ss = new SpannableString(url);
            ss.setSpan(new ForegroundColorSpan(pContext.getResources().getColor(R.color.c_text_v1)), 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
    }

    public static void sendRateEmail(Context context){
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "gamesolstudios@gmail.com", null));
        i.putExtra(Intent.EXTRA_SUBJECT, "Issue Report");
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            if (i.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(i);
            }
        } catch (android.content.ActivityNotFoundException ignored) {
        }
    }

    public static void sendBridgeEmail(Context context){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = { "bridges@torproject.org"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "get transport");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "get transport");
        context.startActivity(emailIntent);
    }

    public static void hideKeyboard(AppCompatActivity context) {
        View view = context.findViewById(android.R.id.content);
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void rateApp(AppCompatActivity context){
        dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_BOOL, Arrays.asList(keys.PROXY_IS_APP_RATED,true));
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.darkweb.genesissearchengine")));
    }

    public static void shareApp(AppCompatActivity context) {
        ShareCompat.IntentBuilder.from(context)
                .setType("text/plain")
                .setChooserTitle("Hi! Check out this Awesome App")
                .setSubject("Hi! Check out this Awesome App")
                .setText("Genesis | Onion Search | http://play.google.com/store/apps/details?id=" + context.getPackageName())
                .startChooser();
    }

    public static void shareApp(AppCompatActivity context, String p_share, String p_title) {
        ShareCompat.IntentBuilder.from(context)
                .setType("text/plain")
                .setChooserTitle("Hi! Check out this Awesome URL | " + p_title)
                .setSubject("Hi! Check out this Awesome URL | " + p_title)
                .setText("Website URL | " + p_share)
                .startChooser();
    }

    public static void vibrate(AppCompatActivity context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);
    }

    public static void shareURL(AppCompatActivity context, String p_share) {
        ShareCompat.IntentBuilder.from(context)
                .setType("text/plain")
                .setChooserTitle("Hi! Check out these Awesome URLS")
                .setSubject("Hi! Check out these Awesome URL")
                .setText("Website URL | " + p_share)
                .startChooser();
    }

    public static void openDownloadFolder(AppCompatActivity context)
    {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        if(intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);
        }else {
            helperMethod.showToastMessage("Download Folder Not Found",context);
        }
    }

    public static void openLocaleSettings(Context context){
        Intent i = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
        context.startActivity(i);
    }

    static String getHost(String link){
        URL url;
        try
        {
            url = new URL(link);
            return url.getHost();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return "";
        }

    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String removeLastSlash(String url){
        if(url.length()>2){
            if(url.charAt(url.length()-1)=='/'){
                return url.substring(0,url.length()-1);
            }
        }
        return url;
    }

    public static String urlWithoutPrefix(String url){
        try{
            url = url.substring(url.indexOf(getHost(url)),url.length()).replace("www.","").replace("m.","");
            return url;
        }catch (Exception ex){
            return url;
        }
    }

    public static void openActivity( Class<?> cls,int type,AppCompatActivity context,boolean animation){
        Intent myIntent = new Intent(context, cls);
        myIntent.putExtra(keys.PROXY_LIST_TYPE, type);
        if(!animation){
            myIntent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
        }
        context.startActivity(myIntent);
    }

    public static void restartActivity( Intent pIntent, AppCompatActivity pContext){
        pContext.finish();
        pContext.startActivity(pIntent);
    }

    public static void onMinimizeApp(AppCompatActivity context){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
    }

    public static int screenWidth()
    {
        return (Resources.getSystem().getDisplayMetrics().widthPixels);
    }

    public static RotateAnimation getRotationAnimation(){
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        return rotate;
    }

    public static void openNotification(AppCompatActivity pContext)
    {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", pContext.getPackageName());
        pContext.startActivity(intent);
    }

    public static String ellipsize(String input, int maxLength) {
        String ellip = "...";
        if (input == null || input.length() <= maxLength
                || input.length() < ellip.length()) {
            return input;
        }
        return input.substring(0, maxLength - ellip.length()).concat(ellip);
    }

    public static String getDomainName(String url)
    {
        try{
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        }catch (Exception ex){
            return url;
        }
    }

    public static ViewGroup.MarginLayoutParams getCenterScreenPoint(ViewGroup.LayoutParams itemLayoutParams) {
        double heightloader = Resources.getSystem().getDisplayMetrics().heightPixels*0.78;
        ViewGroup.MarginLayoutParams params_loading = (ViewGroup.MarginLayoutParams) itemLayoutParams;
        params_loading.topMargin = (int)(heightloader);

        return params_loading;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isBuildValid (){
        return BuildConfig.FLAVOR.equals("aarch64") && Build.SUPPORTED_ABIS[0].equals("arm64-v8a") || BuildConfig.FLAVOR.equals("arm") && Build.SUPPORTED_ABIS[0].equals("armeabi-v7a") || BuildConfig.FLAVOR.equals("x86") && Build.SUPPORTED_ABIS[0].equals("x86") || BuildConfig.FLAVOR.equals("x86_64") && Build.SUPPORTED_ABIS[0].equals("x86_64");
    }

    public static void openPlayStore(String packageName,AppCompatActivity context)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id="+packageName));

        if(intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);
        }else {
            helperMethod.showToastMessage("Playstore Not Found",context);
        }

    }

    public static int dpFromPx(final Context context, final float px) {
        return (int)(px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(int dp){
        return   (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(AppCompatActivity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size.y;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void copyURL(String url,Context context){

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("link", url);
        clipboard.setPrimaryClip(clip);

        showToastMessage("Copied to Clipboard",context);

    }

    public static void showToastMessage(String message,Context context){
        Toast toast=Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static boolean checkPermissions(AppCompatActivity context) {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    public static void clearAppData(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        sdf.applyPattern("E | MMM dd,yyyy");
        return sdf.format(date);
    }

    public static String getCurrentTime(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);

        System.out.printf("%d-%02d-%02d %02d:%02d:%02d.%03d", year, month, day, hour, minute, second, millis);
        return month + "/" + year + " | " + hour + ":" + minute + ":" + second;
    }

    public static PopupWindow onCreateMenu(View p_view, int p_layout) {
        PopupWindow popupWindow = null;
        if(popupWindow!=null){
            popupWindow.dismiss();
        }

        LayoutInflater layoutInflater
                = (LayoutInflater) p_view.getContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View popupView = layoutInflater.inflate(p_layout, null);


        popupWindow = new PopupWindow(
                popupView,
                ActionMenuView.LayoutParams.WRAP_CONTENT,
                ActionMenuView.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.showAtLocation(p_view, Gravity.TOP|Gravity.END,0,0);
        popupWindow.setElevation(7);

        return popupWindow;
    }

}