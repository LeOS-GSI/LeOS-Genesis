package com.darkweb.genesissearchengine.pluginManager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import com.darkweb.genesissearchengine.constants.status;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import java.util.List;
import java.util.Locale;

class langManager {

    /*Private Variables*/

    private AppCompatActivity mAppContext;
    private eventObserver.eventListener mEvent;
    private Locale mLanguage;

    /*Initializations*/

    langManager(AppCompatActivity pAppContext, eventObserver.eventListener pEvent, Locale pLanguage) {
        this.mAppContext = pAppContext;
        this.mEvent = pEvent;
        this.mLanguage = pLanguage;

        onInitLanguage(pAppContext);
    }

    private void onInitLanguage(AppCompatActivity pAppContext) {
        if(status.sSettingLanguage.equals("default")){
            if(mLanguage==null || !mLanguage.getLanguage().equals(Resources.getSystem().getConfiguration().locale.getLanguage()) || !mLanguage.getCountry().equals(Resources.getSystem().getConfiguration().locale.getCountry()))
            {
                Locale mSystemLocale = Resources.getSystem().getConfiguration().locale;
                String mSystemLangugage = mSystemLocale.toString();
                if(mSystemLangugage.equals("en_US") || mSystemLangugage.equals("de_DE") || mSystemLangugage.equals("ur_UR") || mSystemLangugage.equals("ca_ES") || mSystemLangugage.equals("zh_CN") || mSystemLangugage.equals("ch_CZ") || mSystemLangugage.equals("nl_NL") || mSystemLangugage.equals("fr_FR") || mSystemLangugage.equals("el_GR") || mSystemLangugage.equals("hu_HU") || mSystemLangugage.equals("in_ID") || mSystemLangugage.equals("it_IT") || mSystemLangugage.equals("ja_JP") || mSystemLangugage.equals("ko_KR") || mSystemLangugage.equals("pt_PT") || mSystemLangugage.equals("ro_RO") || mSystemLangugage.equals("ru_RU") || mSystemLangugage.equals("th_TH") || mSystemLangugage.equals("tr_TR") || mSystemLangugage.equals("uk_UA") || mSystemLangugage.equals("vi_VN")){
                    mLanguage = new Locale(mSystemLocale.getLanguage(), mSystemLocale.getCountry());
                }else {
                    mLanguage = new Locale("en", "Us");
                }
            }else {
                return;
            }
        }else {
            mLanguage = new Locale(status.sSettingLanguage, status.sSettingLanguageRegion);
        }

        Locale.setDefault(mLanguage);
        Resources resources = pAppContext.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(mLanguage);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /*Helper Methods*/

    private void onCreate(AppCompatActivity pActivity) {
        onInitLanguage(pActivity);
    }

    private void onResume(AppCompatActivity pActivity) {
        onInitLanguage(pActivity);
    }

    private String getSupportedSystemLanguageInfo() {
        if(status.sSettingLanguage.equals("default")){
            Locale mSystemLocale = Resources.getSystem().getConfiguration().locale;
            String mSystemLangugage = mSystemLocale.toString();
            if(mSystemLangugage.equals("en_US") || mSystemLangugage.equals("de_DE") || mSystemLangugage.equals("ca_ES") || mSystemLangugage.equals("zh_CN") || mSystemLangugage.equals("ch_CZ") || mSystemLangugage.equals("nl_NL") || mSystemLangugage.equals("fr_FR") || mSystemLangugage.equals("el_GR") || mSystemLangugage.equals("hu_HU") || mSystemLangugage.equals("in_ID") || mSystemLangugage.equals("it_IT") || mSystemLangugage.equals("ja_JP") || mSystemLangugage.equals("ko_KR") || mSystemLangugage.equals("pt_PT") || mSystemLangugage.equals("ro_RO") || mSystemLangugage.equals("ru_RU") || mSystemLangugage.equals("th_TH") || mSystemLangugage.equals("tr_TR") || mSystemLangugage.equals("uk_UA") || mSystemLangugage.equals("vi_VN")){
                return "Default | " + mSystemLocale.getDisplayName();
            }else {
                return mSystemLocale.getDisplayName() + " | is unsupported";
            }
        }else {
            return mLanguage.getDisplayName();
        }
    }

    /*External Triggers*/

    Object onTrigger(List<Object> pData, pluginEnums.eLangManager pEventType) {
        if(pEventType.equals(pluginEnums.eLangManager.M_ACTIVITY_CREATED))
        {
            onCreate((AppCompatActivity) pData.get(0));
        }
        else if(pEventType.equals(pluginEnums.eLangManager.M_RESUME))
        {
            onResume((AppCompatActivity) pData.get(0));
        }
        else if(pEventType.equals(pluginEnums.eLangManager.M_SET_LANGUAGE))
        {
            onInitLanguage((AppCompatActivity) pData.get(0));
        }
        else if(pEventType.equals(pluginEnums.eLangManager.M_SUPPORTED_SYSTEM_LANGUAGE_INFO))
        {
            return getSupportedSystemLanguageInfo();
        }
        return null;
    }

}