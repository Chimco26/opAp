package com.operatorsapp.adapters;

import android.content.Context;
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

    private final Context mContext;
    public final List<BaseSplits> baseSplits;
    private final OnKeyboardManagerListener mKeyBoardListener;
    private View mMainView;

    public ChannelItemsAdapters(Context context, List<BaseSplits> channelSplits, OnKeyboardManagerListener onKeyboardManagerListener) {

        mContext = context;
        baseSplits = channelSplits;
        mKeyBoardListener = onKeyboardManagerListener;
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

        viewHolder.mEditEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = viewHolder.mEditEt.getInputType(); // backup the input type
                viewHolder.mEditEt.setInputType(InputType.TYPE_NULL); // disable soft input
                viewHolder.mEditEt.onTouchEvent(event); // call native handler
                viewHolder.mEditEt.setInputType(inType); // restore input type
                setMode(true, viewHolder, baseSplits.get(position));
                return false; // consume touch event
            }
        });
        viewHolder.mDsiplayLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMode(true, viewHolder, baseSplits.get(position));
            }
        });
        viewHolder.mEditEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baseSplits.get(position).setEditValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setWeight(double weight, View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = (float) weight;
        view.setLayoutParams(params);
        view.requestLayout();
    }

    private void setKeyBoard(final EditText editText, String[] complementChars) {
        if (mKeyBoardListener != null) {
            mKeyBoardListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                }
            }, editText.getText().toString(), complementChars);
        }
    }

    private void setEditCapabilities(final ViewHolder viewHolder, final BaseSplits baseSplits) {
        if (mKeyBoardListener != null) {
            setMode(baseSplits.isEditMode(), viewHolder, baseSplits);
            viewHolder.mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMode(false, viewHolder, baseSplits);
                    mKeyBoardListener.onCloseKeyboard();
                }
            });
        }
    }

    private void setMode(boolean edit, ViewHolder viewHolder, BaseSplits baseSplits) {
        if (edit) {
            baseSplits.setEditMode(true);
            viewHolder.mEditEt.setText(baseSplits.getEditValue());
            viewHolder.mDsiplayLy.setVisibility(View.GONE);
            viewHolder.mEditLy.setVisibility(View.VISIBLE);
            setWeight(6, viewHolder.mDisplayOrEditLy);
            setWeight(4, viewHolder.mTitle);
            setKeyBoard(viewHolder.mEditEt, new String[]{".", "-"});
        } else {
            baseSplits.setEditMode(false);
            viewHolder.mDsiplayLy.setVisibility(View.VISIBLE);
            viewHolder.mEditLy.setVisibility(View.GONE);
            setWeight(7, viewHolder.mTitle);
            setWeight(3, viewHolder.mDisplayOrEditLy);
        }
    }

    @Override
    public int getItemCount() {
        if (baseSplits != null) {
            return baseSplits.size();
        } else return 0;
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

    public interface OnKeyboardManagerListener {
        void onOpenKeyboard(SingleLineKeyboard.OnKeyboardClickListener listener, String text, String[] complementChars);

        void onCloseKeyboard();
    }
}
