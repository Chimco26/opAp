package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.department.MachineLineResponse;
import com.example.common.department.MachinesLineDetail;
import com.operatorsapp.R;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by alex on 07/01/2019.
 */

public class TechCallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<TechCallInfo> mTechList;
    private final Context mContext;
    private final TechCallItemListener mListener;
    private final boolean isManageServiceCall;
//    private final String mMachineName;
    private final MachineLineResponse mMachineLine;

    public TechCallAdapter(Context context, ArrayList<TechCallInfo> mTechList, boolean isManageServiceCall, MachineLineResponse machineLine, TechCallItemListener listener) {
        mContext = context;
        this.mTechList = mTechList;
//        mMachineName = machineName;
        this.isManageServiceCall = isManageServiceCall;
        mListener = listener;
        mMachineLine = machineLine;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tech_call_recycler_item, parent, false);
        return new TechViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TechViewHolder techViewHolder = (TechViewHolder) holder;

        if (isManageServiceCall){
            techViewHolder.mManageCallFl.setVisibility(View.VISIBLE);
            techViewHolder.mManageCallIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wrench));
            techViewHolder.mManageCallTv.setText(mContext.getResources().getString(R.string.start_service));
        }else {
            techViewHolder.mManageCallFl.setVisibility(View.GONE);
        }
        techViewHolder.mManageCallFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onManageCall(mTechList.get(position));
            }
        });

        techViewHolder.mMoreInfoTv.setText(mTechList.get(position).getmAdditionalText());
        techViewHolder.mTextTv.setText(mTechList.get(position).getmName());
        techViewHolder.mTimeTv.setText(TimeUtils.getDate(mTechList.get(position).getmCallTime(), TimeUtils.COMMON_DATE_FORMAT));
        techViewHolder.mRemoveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemoveCallPressed(mTechList.get(position));
            }
        });

        int icon = R.drawable.technician_blue_svg;
        String txt = mContext.getResources().getString(R.string.waiting_for_replay);
        switch (mTechList.get(position).getmResponseType()){

            case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                icon = R.drawable.call_recieved;
                txt = mContext.getResources().getString(R.string.waiting_for_replay);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                icon = R.drawable.call_sent_blue;
                txt = mContext.getResources().getString(R.string.call_approved);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                icon = R.drawable.call_declined;
                txt = mContext.getResources().getString(R.string.call_declined);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                icon = R.drawable.cancel_blue;
                txt = mContext.getResources().getString(R.string.service_call_was_canceled);
                techViewHolder.mManageCallFl.setVisibility(View.INVISIBLE);
                techViewHolder.mRemoveIv.setVisibility(View.INVISIBLE);
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                icon = R.drawable.at_work_blue;
                txt = mContext.getResources().getString(R.string.at_work);
                techViewHolder.mManageCallIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_all));
                techViewHolder.mManageCallTv.setText(mContext.getResources().getString(R.string.finish_service));
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                icon = R.drawable.service_done;
                txt = mContext.getResources().getString(R.string.service_completed);
                techViewHolder.mManageCallFl.setVisibility(View.INVISIBLE);
                break;
        }
        techViewHolder.mStatusIv.setImageResource(icon);
        techViewHolder.mSubTextTv.setText(txt);
        if (mMachineLine != null && mMachineLine.getLineID() > 0) {
            for (MachinesLineDetail machine : mMachineLine.getMachinesData()) {
                if (machine.getMachineID() == mTechList.get(position).getmMachineId()){
                    techViewHolder.mMachineNameTv.setText(machine.getMachineName());
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTechList.size();
    }

    public class TechViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mMoreInfoLil;
        private TextView mMachineNameTv;
        private TextView mSubTextTv;
        private TextView mMoreInfoTv;
        private TextView mTextTv;
        private TextView mTimeTv;
        private ImageView mRemoveIv;
        private ImageView mStatusIv;
        private ImageView mManageCallIv;
        private TextView mManageCallTv;
        private FrameLayout mManageCallFl;
        private boolean isMoreInfoOpen = false;

        public TechViewHolder(View v) {
            super(v);
            mRemoveIv = itemView.findViewById(R.id.tech_call_item_remove_iv);
            mMoreInfoLil = itemView.findViewById(R.id.tech_call_item_more_info_lil);
            mMoreInfoTv = itemView.findViewById(R.id.tech_call_item_more_info_tv);
            mManageCallIv = itemView.findViewById(R.id.tech_call_item_manage_call_iv);
            mManageCallTv = itemView.findViewById(R.id.tech_call_item_manage_call_tv);
            mManageCallFl = itemView.findViewById(R.id.tech_call_item_manage_call_fl);
            mTextTv = itemView.findViewById(R.id.tech_call_item_text_tv);
            mTimeTv = itemView.findViewById(R.id.tech_call_item_time_tv);
            mStatusIv = itemView.findViewById(R.id.tech_call_item_status_iv);
            mSubTextTv = itemView.findViewById(R.id.tech_call_item_subtext_tv);
            mMachineNameTv = itemView.findViewById(R.id.tech_call_item_machine_name_tv);
            if (mMachineLine != null && mMachineLine.getLineID() > 0){
                mMachineNameTv.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMoreInfoLil.setVisibility(isMoreInfoOpen ? View.GONE : View.VISIBLE);
                    isMoreInfoOpen = !isMoreInfoOpen;
                }
            });
        }
    }

    public interface TechCallItemListener{
        void onRemoveCallPressed(TechCallInfo techCallInfo);

        void onManageCall(TechCallInfo techCallInfo);
    }
}
