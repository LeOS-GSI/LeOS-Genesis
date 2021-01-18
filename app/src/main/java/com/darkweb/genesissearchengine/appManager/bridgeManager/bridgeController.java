package com.darkweb.genesissearchengine.appManager.bridgeManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;
import com.darkweb.genesissearchengine.appManager.activityContextManager;
import com.darkweb.genesissearchengine.appManager.helpManager.helpController;
import com.darkweb.genesissearchengine.constants.constants;
import com.darkweb.genesissearchengine.constants.enums;
import com.darkweb.genesissearchengine.constants.keys;
import com.darkweb.genesissearchengine.constants.status;
import com.darkweb.genesissearchengine.dataManager.dataController;
import com.darkweb.genesissearchengine.dataManager.dataEnums;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import com.darkweb.genesissearchengine.helperManager.helperMethod;
import com.darkweb.genesissearchengine.pluginManager.pluginController;
import com.darkweb.genesissearchengine.pluginManager.pluginEnums;
import com.example.myapplication.R;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class bridgeController extends AppCompatActivity {


    /*Private Variables*/
    private bridgeModel mBridgeModel;
    private bridgeViewController mBridgeViewController;

    private RadioButton mBridgeObfs;
    private RadioButton mBridgeChina;
    private RadioButton mBridgeCustom;
    private EditText mCustomPort;
    private Button mBridgeButton;
    private ImageView mCustomBridgeBlocker;

    /*Initializations*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pluginController.getInstance().onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bridge_settings_view);

        initializeAppModel();
        initializeConnections();
        initializeLocalEventHandlers();
    }

    public void initializeAppModel()
    {
        activityContextManager.getInstance().onStack(this);
        mBridgeViewController = new bridgeViewController();
    }

    public void initializeConnections()
    {
        mBridgeObfs = findViewById(R.id.pBridgeObfs);
        mBridgeChina = findViewById(R.id.pBridgeChina);
        mCustomPort = findViewById(R.id.pCustomPort);
        mBridgeButton = findViewById(R.id.pBridgeButton);
        mBridgeCustom = findViewById(R.id.pBridgeCustom);
        mCustomBridgeBlocker = findViewById(R.id.pCustomBridgeBlocker);

        mBridgeViewController.initialization(mCustomPort, mBridgeButton,this, mBridgeObfs, mBridgeChina, mBridgeCustom, mCustomBridgeBlocker);
        mBridgeViewController.onTrigger(bridgeEnums.eBridgeViewCommands.M_INIT_VIEWS, Arrays.asList(status.sBridgeCustomBridge,0));
        mBridgeModel = new bridgeModel(new bridgeController.bridgeModelCallback(), this);
    }

    private void initializeLocalEventHandlers()
    {
        mCustomPort.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                helperMethod.hideKeyboard(this);
            }
        });

        mCustomPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                status.sBridgeCustomBridge = mCustomPort.getText().toString();
            }
        });
    }

    public void onOpenInfo(View view) {
        helperMethod.openActivity(helpController.class, constants.CONST_LIST_HISTORY, this,true);
    }

    /* LISTENERS */

    public class bridgeModelCallback implements eventObserver.eventListener{
        @Override
        public Object invokeObserver(List<Object> data, Object e_type)
        {
            return null;
        }
    }

    /* LOCAL OVERRIDES */

    @Override
    protected void onPause()
    {
        super.onPause();
        if(dataController.getInstance()!=null){
            dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_STRING, Arrays.asList(keys.BRIDGE_CUSTOM_BRIDGE_1,status.sBridgeCustomBridge));
            dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_BOOL, Arrays.asList(keys.SETTING_GATEWAY_AUTO,status.sBridgeGatewayAuto));
            dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_BOOL, Arrays.asList(keys.SETTING_GATEWAY_MANUAL,status.sBridgeGatewayManual));
        }
    }

    @Override
    public void onResume()
    {
        activityContextManager.getInstance().setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        activityContextManager.getInstance().onRemoveStack(this);
        finish();
    }

    /*Helper Method*/

    public void onClose(View view){
        finish();
    }

    public void requestBridges(View view){
        mBridgeModel.onTrigger(bridgeEnums.eBridgeModelCommands.M_REQUEST_BRIDGE, null);
        pluginController.getInstance().MessageManagerHandler(this, Collections.singletonList(constants.CONST_BACKEND_GOOGLE_URL), enums.eMessageEnums.M_BRIDGE_MAIL);
    }

    public void onCustomChecked(View view){
        mBridgeModel.onTrigger(bridgeEnums.eBridgeModelCommands.M_CUSTOM_BRIDGE, null);
        mBridgeViewController.onTrigger(bridgeEnums.eBridgeViewCommands.M_INIT_VIEWS, Arrays.asList(status.sBridgeCustomBridge,250));

    }
    public void onMeekChecked(View view){
        mBridgeModel.onTrigger(bridgeEnums.eBridgeModelCommands.M_MEEK_BRIDGE, null);
        mBridgeViewController.onTrigger(bridgeEnums.eBridgeViewCommands.M_INIT_VIEWS, Arrays.asList(status.sBridgeCustomBridge,250));
    }
    public void onObfsChecked(View view){
        mBridgeModel.onTrigger(bridgeEnums.eBridgeModelCommands.M_OBFS_CHECK, null);
        mBridgeViewController.onTrigger(bridgeEnums.eBridgeViewCommands.M_INIT_VIEWS, Arrays.asList(status.sBridgeCustomBridge,250));
    }
}