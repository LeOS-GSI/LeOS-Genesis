package com.darkweb.genesissearchengine.appManager.historyManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.darkweb.genesissearchengine.appManager.activityContextManager;
import com.darkweb.genesissearchengine.appManager.databaseManager.databaseController;
import com.darkweb.genesissearchengine.appManager.homeManager.homeController;
import com.darkweb.genesissearchengine.constants.enums;
import com.darkweb.genesissearchengine.constants.keys;
import com.darkweb.genesissearchengine.constants.sql;
import com.darkweb.genesissearchengine.constants.status;
import com.darkweb.genesissearchengine.constants.strings;
import com.darkweb.genesissearchengine.dataManager.dataController;
import com.darkweb.genesissearchengine.dataManager.dataEnums;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import com.darkweb.genesissearchengine.helperManager.helperMethod;
import com.darkweb.genesissearchengine.pluginManager.pluginController;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import static com.darkweb.genesissearchengine.appManager.historyManager.historyEnums.eHistoryViewCommands.M_VERTIFY_SELECTION_MENU;

public class historyController extends AppCompatActivity
{
    /*Private Variables*/

    private historyModel mHistoryModel;
    private homeController mHomeController;
    private activityContextManager mContextManager;
    private historyAdapter mHistoryAdapter;

    /*Private Views*/

    private ImageView mEmptyListNotification;
    private EditText mSearchInput;
    private RecyclerView mRecycleView;
    private Button mClearButton;
    private ImageButton mMenuButton;
    private ImageButton mSearchButton;

    private historyViewController mHistoryViewController;
    private boolean isUpdatingRecyclerView = false;

    /*Initializations*/

    @Override
    protected void onCreate(Bundle savedInstanceState){
        pluginController.getInstance().onCreate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_view);
        initializeListModel();
        initializeViews();
        initializeList();
        initCustomListeners();
        initSwipe();
    }

    public void initializeListModel(){
        mHistoryModel = new historyModel();
        mContextManager = activityContextManager.getInstance();
        mHomeController = activityContextManager.getInstance().getHomeController();
        mContextManager.setHistoryController(this);
        activityContextManager.getInstance().setHistoryController(this);
        pluginController.getInstance().logEvent(strings.EVENT_HISTORY_OPENED);
    }
    public void initializeViews(){
        mEmptyListNotification = findViewById(R.id.pEmptyListNotification);
        mSearchInput = findViewById(R.id.pSearchInput);
        mRecycleView = findViewById(R.id.pRecycleView);
        mClearButton = findViewById(R.id.pClearButton);
        mMenuButton = findViewById(R.id.pMenuButton);
        mSearchButton = findViewById(R.id.pSearchButton);

        mHistoryViewController = new historyViewController(mEmptyListNotification, mSearchInput, mRecycleView, mClearButton,this, mMenuButton, mSearchButton);
    }
    public void initializeList(){
        ArrayList<historyRowModel> model = (ArrayList<historyRowModel>) dataController.getInstance().invokeHistory(dataEnums.eHistoryCommands.M_GET_HISTORY ,null);
        mHistoryModel.setList(model);
        historyAdapter adapter = new historyAdapter(mHistoryModel.getList(),new adapterCallback(), this);
        mHistoryAdapter = adapter;
        adapter.invokeFilter(false);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setHasFixedSize(true);

        mRecycleView.setAdapter(adapter);
        mRecycleView.setItemViewCacheSize(100);
        mRecycleView.setDrawingCacheEnabled(true);
        mRecycleView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_UPDATE_LIST_IF_EMPTY, Arrays.asList(mHistoryModel.getList().size(),0));
    }

    public void initCustomListeners(){
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if(mSearchInput.getVisibility()==View.GONE){
                        LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisible = layoutManager.findLastVisibleItemPosition();

                        boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;

                        if (totalItemCount > 0 && endHasBeenReached) {
                            mRecycleView.computeVerticalScrollOffset();
                            onLoadMoreHistory(mRecycleView.computeVerticalScrollOffset()>0);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            }
        });

        mClearButton.requestFocusFromTouch();
        mClearButton.setOnClickListener(v -> pluginController.getInstance().MessageManagerHandler(activityContextManager.getInstance().getHistoryController(), Collections.singletonList(strings.GENERIC_EMPTY_STR), enums.etype.clear_history));

        mSearchInput.setOnEditorActionListener((v, actionId, event) ->{
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                helperMethod.hideKeyboard(this);
                return true;
            }
            return false;
        });

        mSearchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                mSearchInput.clearFocus();
            }else {
                mHistoryAdapter.setFilter(mSearchInput.getText().toString());
                mHistoryAdapter.invokeFilter(true);
            }
        });

        mSearchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                mHistoryAdapter.setFilter(mSearchInput.getText().toString());
                mHistoryAdapter.invokeFilter(true);
            }
        });
    }


    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.ON_CLOSE,Collections.singletonList(position));
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Canvas mCanvas = (Canvas) mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.ON_GENERATE_SWIPABLE_BACKGROUND, Arrays.asList(c, viewHolder, dX, actionState));
                super.onChildDraw(mCanvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);
    }

    /*View Custom Overrides*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onTrimMemory(int level)
    {
        if(status.sSettingIsAppPaused && (level==80 || level==15))
        {
            dataController.getInstance().invokePrefs(dataEnums.ePreferencesCommands.M_SET_BOOL, Arrays.asList(keys.HOME_LOW_MEMORY,true));
            finish();
        }
    }

    @Override
    public void onResume()
    {
        activityContextManager.getInstance().setCurrentActivity(this);
        status.sSettingIsAppPaused = false;
        super.onResume();
    }

    @Override
    public void onPause()
    {
        status.sSettingIsAppPaused = true;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(mSearchInput.getVisibility() == View.VISIBLE){
            onHideSearch(null);
        }else if((Boolean) mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.GET_LONG_SELECTED_STATUS,null)){
            onClearMultipleSelection(null);
        }else {
            onBackPressed(null);
        }
    }

    /*External XML Listeners*/

    public void onBackPressed(View view){
        this.finish();
    }

    public void onHideSearch(View view) {
        mHistoryAdapter.onUpdateSearchStatus((boolean) mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_HIDE_SEARCH, null));
    }

    public void onLongPressMenu(View view) {
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_LONG_PRESS_MENU, Collections.singletonList(view));
    }

    public void onOpenMultipleTabs(View view) {
        ArrayList<String> m_long_selected_urk = (ArrayList<String>) mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.GET_LONG_SELECTED_URL, null);
        for(int m_counter=0;m_counter<m_long_selected_urk.size();m_counter++){
            mHomeController.onOpenLinkNewTab(m_long_selected_urk.get(m_counter));
        }
        onBackPressed(null);
        mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.M_CLEAR_LONG_SELECTED_URL,null);
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_CLOSE_MENU, null);
    }

    public void onShareSelectedURL(View view) {
        String m_joined_url = (String) mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.GET_SELECTED_URL, null);
        helperMethod.shareURL(this, m_joined_url);
        mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.M_CLEAR_LONG_SELECTED_URL,null);
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_CLOSE_MENU, null);
    }

    public void onClearMultipleSelection(View view) {
        mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.M_CLEAR_LONG_SELECTED_URL, null);
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_CLOSE_MENU, null);
        mHistoryViewController.onTrigger(M_VERTIFY_SELECTION_MENU,Collections.singletonList(true));
    }

    public void onDeleteSelected(View view) {
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_CLOSE_MENU, null);
        mHistoryAdapter.onDeleteSelected();
    }

    /*Helper Methods*/

    public void onclearData(){
        mHistoryModel.clearList();
        mHistoryAdapter.invokeFilter(true );
        mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_CLEAR_LIST, null);
        databaseController.getInstance().execSQL(sql.SQL_CLEAR_HISTORY,null);
    }

    private void onLoadMoreHistory(boolean pLoadingEnabled) {
        if(!isUpdatingRecyclerView){
            isUpdatingRecyclerView = true;
            new Thread(){
                public void run(){
                    if(pLoadingEnabled){
                        mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.M_ON_LOADING, null);
                    }
                    dataController.getInstance().invokeHistory(dataEnums.eHistoryCommands.M_LOAD_MORE_HISTORY ,null);
                    try {
                        sleep(500);
                        activityContextManager.getInstance().getHistoryController().runOnUiThread(() -> mHistoryAdapter.onTrigger(historyEnums.eHistoryAdapterCommands.M_LOADING_CLEAR, null));
                        sleep(1000);
                        isUpdatingRecyclerView = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isUpdatingRecyclerView = false;
                    }
                }
            }.start();
        }
    }

    /*Event Observer*/

    public class adapterCallback implements eventObserver.eventListener{
        @Override
        public Object invokeObserver(List<Object> data, Object e_type)
        {
            if(e_type.equals(enums.etype.url_triggered)){
                String url_temp = helperMethod.completeURL(data.get(0).toString());
                pluginController.getInstance().logEvent(strings.EVENT_HISTORY_TRIGGERED);
                mHomeController.onLoadURL(url_temp);
                finish();
            }
            else if(e_type.equals(enums.etype.url_triggered_new_tab)){
                String url_temp = helperMethod.completeURL(data.get(0).toString());
                pluginController.getInstance().logEvent(strings.EVENT_HISTORY_TRIGGERED);
                mHomeController.onOpenLinkNewTab(url_temp);
                finish();
            }
            else if(e_type.equals(enums.etype.url_clear)){
                mHistoryModel.onManualClear((int)data.get(0));
            }
            else if(e_type.equals(enums.etype.url_clear_at)){
                dataController.getInstance().invokeHistory(dataEnums.eHistoryCommands.M_REMOVE_HISTORY ,data);
            }
            else if(e_type.equals(enums.etype.is_empty)){
                mHistoryViewController.onTrigger(historyEnums.eHistoryViewCommands.M_UPDATE_LIST_IF_EMPTY, Arrays.asList(mHistoryModel.getList().size(),300));
            }
            else if(e_type.equals(enums.etype.remove_from_database)){
                databaseController.getInstance().deleteFromList((int)data.get(0),strings.HISTORY_TITLE);
            }
            else if(e_type.equals(enums.etype.on_verify_selected_url_menu)){
                mHistoryViewController.onTrigger(M_VERTIFY_SELECTION_MENU, data);
            }
            return null;
        }
   }
}