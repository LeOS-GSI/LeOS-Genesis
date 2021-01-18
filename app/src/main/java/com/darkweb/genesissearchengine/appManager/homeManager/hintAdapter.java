package com.darkweb.genesissearchengine.appManager.homeManager;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.darkweb.genesissearchengine.appManager.historyManager.historyRowModel;
import com.darkweb.genesissearchengine.appManager.tabManager.tabEnums;
import com.darkweb.genesissearchengine.constants.enums;
import com.darkweb.genesissearchengine.constants.strings;
import com.darkweb.genesissearchengine.helperManager.eventObserver;
import com.darkweb.genesissearchengine.helperManager.helperMethod;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class hintAdapter extends RecyclerView.Adapter<hintAdapter.listViewHolder>
{
    /*Private Variables*/

    private ArrayList<historyRowModel> mHintList;
    private AppCompatActivity mContext;
    private eventObserver.eventListener mEvent;
    ImageView[] mWebIcon = new ImageView[15];

    boolean mCounters=false;
    hintAdapter(ArrayList<historyRowModel> pHintList, eventObserver.eventListener pEvent, AppCompatActivity pContext) {
        this.mHintList = new ArrayList();
        int maxCounter=5;
        if(pHintList.size()<maxCounter){
            maxCounter = pHintList.size();
        }

        this.mHintList.addAll(pHintList.subList(0,maxCounter));
        this.mContext = pContext;
        this.mEvent = pEvent;
    }


    public void onUpdateAdapter(ArrayList<historyRowModel> pHintList){
        mHintList.clear();
        mHintList.addAll(pHintList);
        notifyDataSetChanged();
    }

    /*Initializations*/

    @NonNull @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hint_view, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull hintAdapter.listViewHolder holder, int position)
    {
        holder.bindListView(mHintList.get(position));
    }

    @Override
    public int getItemCount() {
        return mHintList.size();
    }

    /*Listeners*/

    /*View Holder Extensions*/
    class listViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener
    {
        TextView mHeader;
        TextView mHeaderSingle;
        TextView mURL;
        ImageButton mMoveURL;
        ImageView mHindTypeIcon;
        ImageView mHintWebIcon = null;
        LinearLayout mpHintListener;
        ImageView mHindTypeIconTemp;

        listViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        void bindListView(historyRowModel model) {
            mHeader = itemView.findViewById(R.id.pHeader);
            mHeaderSingle = itemView.findViewById(R.id.pHeaderSingle);
            mURL = itemView.findViewById(R.id.pURL);
            mHindTypeIcon = itemView.findViewById(R.id.pHindTypeIcon);
            mpHintListener = itemView.findViewById(R.id.pHintListener);
            mMoveURL = itemView.findViewById(R.id.pMoveURL);
            mHintWebIcon = itemView.findViewById(R.id.pHintWebIcon);
            mHindTypeIconTemp = new ImageView(mContext);

            if(mWebIcon[getLayoutPosition()]!=null){
                mHintWebIcon = itemView.findViewById(R.id.pHintWebIcon);
                mHintWebIcon.setImageDrawable(mWebIcon[getLayoutPosition()].getDrawable());
                mHindTypeIconTemp.setImageDrawable(mWebIcon[getLayoutPosition()].getDrawable());
            }
            mWebIcon[getLayoutPosition()] = mHintWebIcon;

            if(model.getDescription().equals(strings.GENERIC_EMPTY_STR)){
                mHeaderSingle.setText(model.getHeader());
                mHeaderSingle.setVisibility(View.VISIBLE);
                mHeader.setVisibility(View.GONE);
                mURL.setVisibility(View.GONE);
                mHintWebIcon.setVisibility(View.GONE);
                mHindTypeIcon.setVisibility(View.VISIBLE);
            }else {
                mHeaderSingle.setVisibility(View.GONE);
                mHeader.setVisibility(View.VISIBLE);
                mURL.setVisibility(View.VISIBLE);
                mHintWebIcon.setVisibility(View.VISIBLE);
                mHindTypeIcon.setVisibility(View.GONE);
            }

            mHeader.setText(model.getHeader());
            mMoveURL.setTag(model.getDescription());
            mURL.setText(model.getDescription());
            Drawable mDrawable = null;
            Resources res = itemView.getContext().getResources();
            try {
                if(model.getDescription().equals(strings.GENERIC_EMPTY_STR)){
                    mDrawable = Drawable.createFromXml(res, res.getXml(R.xml.ic_baseline_search));
                    mMoveURL.setVisibility(View.GONE);
                }else {
                    mDrawable = Drawable.createFromXml(res, res.getXml(R.xml.ic_baseline_browser));
                    mMoveURL.setVisibility(View.VISIBLE);
                    mMoveURL.setOnTouchListener(listViewHolder.this);
                }
            } catch (Exception ignored) {
            }

            if(model.getDescription().equals(strings.GENERIC_EMPTY_STR)){
                mHindTypeIcon.setImageDrawable(mDrawable);
            }

            mpHintListener.setOnTouchListener(listViewHolder.this);

            new Thread(){
                public void run(){
                    try {
                        mHindTypeIconTemp.setImageDrawable(null);
                        mEvent.invokeObserver(Arrays.asList(mHindTypeIconTemp, "https://" + model.getDescription()), enums.etype.fetch_favicon);
                        while (true){
                            int mCounter=0;
                            if(mHindTypeIconTemp.isAttachedToWindow() || mHindTypeIconTemp.getDrawable()==null){
                                sleep(50);
                                mCounter+=1;
                            }else {
                                Log.i("BREAK","");
                                break;
                            }
                            if(mCounter>6){
                                break;
                            }
                        }
                        mContext.runOnUiThread(() -> mHintWebIcon.setImageDrawable(mHindTypeIconTemp.getDrawable()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
       }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(v.getId() == mpHintListener.getId() || v.getId() == mMoveURL.getId()){
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    helperMethod.hideKeyboard(mContext);
                }
            }
            return false;
        }
    }

    public Object onTrigger(tabEnums.eTabAdapterCommands pCommands, List<Object> pData){
        return null;
    }

}

