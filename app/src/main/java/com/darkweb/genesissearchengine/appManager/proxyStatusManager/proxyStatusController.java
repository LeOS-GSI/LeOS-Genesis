package com.darkweb.genesissearchengine.appManager.proxyStatusManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.darkweb.genesissearchengine.appManager.activityContextManager;
import com.darkweb.genesissearchengine.appManager.helpManager.helpController;
import com.darkweb.genesissearchengine.appManager.orbotLogManager.orbotLogController;
import com.darkweb.genesissearchengine.constants.constants;
import com.darkweb.genesissearchengine.constants.status;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import com.darkweb.genesissearchengine.helperManager.helperMethod;
import com.darkweb.genesissearchengine.pluginManager.pluginController;
import com.example.myapplication.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Arrays;
import java.util.List;

public class proxyStatusController extends AppCompatActivity {

    /* PRIVATE VARIABLES */
    private proxyStatusModel mProxyStatusModel;
    private proxyStatusViewController mProxyStatusViewController;

    /* INITIALIZATIONS */
    private TextView mOrbotStatus;
    private SwitchMaterial mVpnStatus;
    private SwitchMaterial mBridgeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.push_anim_in, R.anim.push_anim_out);

        pluginController.getInstance().onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proxy_status_view);

        viewsInitializations();
    }

    public void viewsInitializations() {
        mOrbotStatus = findViewById(R.id.pOrbotStatus);
        mVpnStatus = findViewById(R.id.pVpnStatus);
        mBridgeStatus = findViewById(R.id.pBridgeStatus);

        activityContextManager.getInstance().onStack(this);
        mProxyStatusViewController = new proxyStatusViewController(this, mOrbotStatus, mVpnStatus, mBridgeStatus);
        mProxyStatusViewController.onTrigger(proxyStatusEnums.eProxyStatusViewCommands.M_INIT_VIEWS, Arrays.asList(pluginController.getInstance().getOrbotStatus(), status.sBridgeVPNStatus,status.sBridgeStatus));
        mProxyStatusModel = new proxyStatusModel(new proxyStatusModelCallback());
    }

    public void orbotLog(View view) {
        helperMethod.openActivity(orbotLogController.class, constants.CONST_LIST_HISTORY, this,true);
    }

    /* LISTENERS */

    public class proxyStatusModelCallback implements eventObserver.eventListener{
        @Override
        public Object invokeObserver(List<Object> data, Object e_type)
        {
            return null;
        }
    }

    public void onClose(View view){
        finish();
        activityContextManager.getInstance().onRemoveStack(this);
        overridePendingTransition(R.anim.push_anim_out_reverse, R.anim.push_anim_in_reverse);
    }

    /* LOCAL OVERRIDES */

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        onClose(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}