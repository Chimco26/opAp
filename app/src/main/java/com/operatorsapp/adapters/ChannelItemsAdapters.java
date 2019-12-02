package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;

import java.util.List;
import java.util.Locale;

public class ChannelItemsAdapters extends RecyclerView.Adapter<ChannelItemsAdapters.ViewHolder> {

    public final List<BaseSplits> baseSplits;
    private View mMainView;
    private ChannelItemsAdaptersListener mListener;
    private float mTitleSize = 15;

    public ChannelItemsAdapters(List<BaseSplits> channelSplits, ChannelItemsAdaptersListener listener) {

        baseSplits = channelSplits;
        mListener = listener;
    }

    @NonNull
    @Override
    public ChannelItemsAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        mMainView = inflater.inflate(R.layout.item_split, parent, false);

        return new ChannelItemsAdapters.ViewHolder(mMainView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChannelItemsAdapters.ViewHolder viewHolder, final int position) {

        String nameByLang = OperatorApplication.isEnglishLang() ? baseSplits.get(position).getPropertyEName() : baseSplits.get(position).getPropertyHName();
        viewHolder.mTitle.setText(nameByLang);
        viewHolder.mNumber.setText(baseSplits.get(position).getFValue());
        viewHolder.mRange.setText(String.format(Locale.getDefault(), "%s-%s", baseSplits.get(position).getLValue(), baseSplits.get(position).getHValue()));
        setEditModeFun(viewHolder, position);
    }

    public void setEditModeFun(@NonNull final ViewHolder viewHolder, final int position) {
        if (mListener != null && baseSplits.get(position).getIsEditable() && baseSplits.get(position).getIsEnabled()) {

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onEditMode(baseSplits.get(position));
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (baseSplits != null) {
            return baseSplits.size();
        } else return 0;
    }

    public boolean hasNotListener() {
        return mListener == null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout mDisplayOrEditLy;
        private View mDsiplayLy;
        private View mEditLy;
        private EditText mEditEt;
        private View mCancelBtn;
        private TextView mTitle;
        private TextView mNumber;
        private TextView mRange;

        ViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.IS_tv);
            mNumber = itemView.findViewById(R.id.IS_tv_2);
            mRange = itemView.findViewById(R.id.IS_range_tv);
            mDisplayOrEditLy = itemView.findViewById(R.id.IS_display_or_edit_ly);
            mDsiplayLy = itemView.findViewById(R.id.IS_number_ly);
            mEditLy = itemView.findViewById(R.id.IS_edit_ly);
            mEditEt = itemView.findViewById(R.id.IS_edit_et);
            mCancelBtn = itemView.findViewById(R.id.IS_cancel_btn);

        }

    }

    public interface ChannelItemsAdaptersListener {

        void onEditMode(BaseSplits item);
    }
}
