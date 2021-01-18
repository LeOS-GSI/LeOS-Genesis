package com.darkweb.genesissearchengine.appManager.settingManager.notificationManager;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.darkweb.genesissearchengine.appManager.activityContextManager;
import com.darkweb.genesissearchengine.appManager.helpManager.helpController;
import com.darkweb.genesissearchengine.constants.constants;
import com.darkweb.genesissearchengine.constants.keys;
import com.darkweb.genesissearchengine.constants.status;
import com.darkweb.genesissearchengine.dataManager.dataController;
import com.darkweb.genesissearchengine.dataManager.dataEnums;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import com.darkweb.genesissearchengine.helperManager.helperMethod;
import com.darkweb.genesissearchengine.pluginManager.pluginController;
import com.example.myapplication.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class settingNotificationController extends AppCompatActivity {

    /* PRIVATE VARIABLES */
    private SwitchMaterial mNotificationManual;
    private settingNotificationModel mSettingNotificationModel;
    private settingNotificationViewController mSettingNotificationViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pluginController.getInstance().onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notification_view);

        viewsInitializations();
    }

    public void viewsInitializations() {
        activityContextManager.getInstance().onStack(this);
        mNotificationManual = findViewById(R.id.pNotificationManual);
        mSettingNotificationViewController = new settingNotificationViewController(this, new settingNotificationViewCallback(), mNotificationManual);

        mSettingNotificationModel = new settingNotificationModel(new settingNotificationModelCallback());
    }

    /* LISTENERS */
    public class settingNotificationViewCallback implements eventObserver.eventListener{

        @Override
        public Object invokeObserver(List<Object> data, Object e_type)
        {
            return null;
        }
    }


    public class settingNotificationModelCallback implements eventObserver.eventListener{

        @Override
        public Object invokeObserver(List<Object> data, Object e_type)
        {
            return null;
        }
    }

    /* LOCAL OVERRIDES */

    @Override
    public void onResume()
    {
        activityContextManager.getInstance().setCurrentActivity(this);
        super.onResume();

        int notificationStatus = pluginController.getInstance().getNotificationStatus();
        if(notificationStatus==0){
            pluginController.getInstance().disableTorNotification();
        } else if(notificationStatus==1){
            pluginController.getInstance().enableTorNotification();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        activityContextManager.getInstance().onRemoveStack(this);
    }

    /*UI Redirection*/

    public void onOpenInfo(View view) {
        helperMethod.openActivity(helpController.class, constants.CONST_LIST_HISTORY, this,true);
    }

    public void onClose(View view){
        finish();
    }

    public void onOpenNotificationSettings(View view){
        helperMethod.openNotification(this);
    }

    public void onSaveLocalNotificationSettings(View view){
        mSettingNotificationModel.onTrigger(settingNotificationEnums.eNotificationModel.M_UPDATE_LOCAL_NOTIFICATION, Collections.singletonList(!mNotificationManual.isChecked()));
        mNotificationManual.toggle();
        dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_INT, Arrays.asList(keys.SETTING_NOTIFICATION_STATUS, status.sBridgeNotificationManual));
    }
}