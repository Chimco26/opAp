package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.common.QCModels.SamplesDatum;
import com.operatorsapp.R;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.ArrayList;

import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT;

public class QCSamplesMultiTypeAdapter extends RecyclerView.Adapter {

    public static final int LAST = -1;
    private final Integer mInputType;
    private ArrayList<SamplesDatum> list;
    private ChannelItemsAdapters.ChannelItemsAdaptersListener mOnKeyboardManagerListener;

    public QCSamplesMultiTypeAdapter(Integer inputType, ArrayList<SamplesDatum> list) {
        mInputType = inputType;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN:
                return new QCSamplesMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_boolean, parent, false));
            case FIELD_TYPE_NUM:
                return new QCSamplesMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num, parent, false));
            case FIELD_TYPE_TEXT:
                return new QCSamplesMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
            case LAST:
                return new QCSamplesMultiTypeAdapter.LastMinusViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_last, parent, false));
            default:
                return new QCSamplesMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);

        switch (type) {

            case FIELD_TYPE_BOOLEAN:
                break;
            case FIELD_TYPE_NUM:
                break;
            case FIELD_TYPE_TEXT:
                break;
            case LAST:
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mInputType) {
            case FIELD_TYPE_BOOLEAN:
                return FIELD_TYPE_BOOLEAN;
            case FIELD_TYPE_NUM:
                return FIELD_TYPE_NUM;
            case FIELD_TYPE_TEXT:
                return FIELD_TYPE_TEXT;
            case LAST:
                return LAST;
            default:
                return FIELD_TYPE_TEXT;

        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        private EditText mTextEt;

        TextViewHolder(View itemView) {
            super(itemView);

            mTextEt = itemView.findViewById(R.id.IQCPHT_et);
        }

    }

    public class BooleanViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mBooleanCheckBox;

        BooleanViewHolder(View itemView) {
            super(itemView);

            mBooleanCheckBox = itemView.findViewById(R.id.IQCPHB_check_box);

        }

    }

    public class NumViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditNumberEt;

        NumViewHolder(View itemView) {
            super(itemView);

            mEditNumberEt = itemView.findViewById(R.id.IQCPHN_et);
            mEditNumberEt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = mEditNumberEt.getInputType(); // backup the input type
                    mEditNumberEt.setInputType(InputType.TYPE_NULL); // disable soft input
                    mEditNumberEt.onTouchEvent(event); // call native handler
                    mEditNumberEt.setInputType(inType); // restore input type
                    setKeyBoard(mEditNumberEt, new String[]{".", "-"});
                    return false; // consume touch event
                }
            });
        }

        private void setKeyBoard(final EditText editText, String[] complementChars) {
            if (mOnKeyboardManagerListener != null) {
                mOnKeyboardManagerListener.onOpenKeyboard(new SingleLineKeyboard.OnKeyboardClickListener() {
                    @Override
                    public void onKeyboardClick(String text) {
                        editText.setText(text);
                    }
                }, editText.getText().toString(), complementChars);
            }
        }

        private void closeKeyboard(int editStop) {
            if (editStop != 1) {
                if (mOnKeyboardManagerListener != null)
                    mOnKeyboardManagerListener.onCloseKeyboard();
            }
        }
    }

    public class LastMinusViewHolder extends RecyclerView.ViewHolder {

        private ImageView mLastMinusTv;

        LastMinusViewHolder(View itemView) {
            super(itemView);

            mLastMinusTv = itemView.findViewById(R.id.IQCPHL_minus_btn);

        }

    }

    public interface QCMultiTypeAdapterListener {
        void onItemCheck();
    }
}
