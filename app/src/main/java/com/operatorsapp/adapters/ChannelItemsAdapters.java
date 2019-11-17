package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.BaseSplits;
import com.operatorsapp.R;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.List;
import java.util.Locale;

public class ChannelItemsAdapters extends RecyclerView.Adapter<ChannelItemsAdapters.ViewHolder> {

    public final List<BaseSplits> baseSplits;
    private View mMainView;
    private ChannelItemsAdaptersListener mListener;
    private float mTitleSize = 15;

    public ChannelItemsAdapters(List<BaseSplits> channelSplits) {

        baseSplits = channelSplits;
    }
    public void addListener(ChannelItemsAdaptersListener listener) {
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

        viewHolder.mTitle.setText(baseSplits.get(position).getPropertyName());
        viewHolder.mNumber.setText(baseSplits.get(position).getFValue());
        viewHolder.mRange.setText(String.format(Locale.getDefault(), "%s-%s", baseSplits.get(position).getLValue(), baseSplits.get(position).getHValue()));
        setEditCapabilities(viewHolder, baseSplits.get(position));

        setEditModeFun(viewHolder, position);
    }

    public void setEditModeFun(@NonNull final ViewHolder viewHolder, final int position) {
        if (mListener != null && baseSplits.get(position).getIsEditable() && baseSplits.get(position).getIsEnabled()) {
            viewHolder.mEditEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = viewHolder.mEditEt.getInputType(); // backup the input type
                    viewHolder.mEditEt.setInputType(InputType.TYPE_NULL); // disable soft input
                    viewHolder.mEditEt.onTouchEvent(event); // call native handler
                    viewHolder.mEditEt.setInputType(inType); // restore input type
                    setMode(true, viewHolder);
                    return false; // consume touch event
                }
            });
            viewHolder.mDsiplayLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMode(true, viewHolder);
                }
            });
            viewHolder.mEditEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    baseSplits.get(viewHolder.getAdapterPosition()).setEditValue(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void setWeight(double weight, View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = (float) weight;
        view.setLayoutParams(params);
        view.requestLayout();
    }

    private void setKeyBoard(final EditText editText, String[] complementChars) {
        if (mListener != null) {
            mListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                }
            }, editText.getText().toString(), complementChars);
        }
    }

    private void setEditCapabilities(final ViewHolder viewHolder, final BaseSplits baseSplits) {
        if (mListener != null) {
            setMode(baseSplits.isEditMode(), viewHolder);
            viewHolder.mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMode(false, viewHolder);
                    mListener.onCloseKeyboard();
                }
            });
        }
    }

    private void setMode(boolean edit, ViewHolder viewHolder) {
        if (edit) {
            baseSplits.get(viewHolder.getAdapterPosition()).setEditMode(true);
            viewHolder.mEditEt.setText(baseSplits.get(viewHolder.getAdapterPosition()).getEditValue());
            viewHolder.mDsiplayLy.setVisibility(View.GONE);
            viewHolder.mEditLy.setVisibility(View.VISIBLE);
            setWeight(6, viewHolder.mDisplayOrEditLy);
            setWeight(4, viewHolder.mTitle);
            setKeyBoard(viewHolder.mEditEt, new String[]{".", "-"});
        } else {
            baseSplits.get(viewHolder.getAdapterPosition()).setEditMode(false);
            baseSplits.get(viewHolder.getAdapterPosition()).setEditValue(null);
            viewHolder.mDsiplayLy.setVisibility(View.VISIBLE);
            viewHolder.mEditLy.setVisibility(View.GONE);
            setWeight(3, viewHolder.mDisplayOrEditLy);
            setWeight(7, viewHolder.mTitle);
        }
        for (BaseSplits baseSplits: baseSplits){
            if (baseSplits.isEditMode()){
                mListener.onEditMode(true);
                return;
            }
        }
        mListener.onEditMode(false);
    }

    @Override
    public int getItemCount() {
        if (baseSplits != null) {
            return baseSplits.size();
        } else return 0;
    }

    public boolean hasListener() {
        return mListener != null;
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
        void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars);

        void onCloseKeyboard();

        void onEditMode(boolean isEditMode);
    }
}
