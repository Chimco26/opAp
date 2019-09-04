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
import android.widget.TextView;

import com.example.common.QCModels.TestFieldsDatum;
import com.operatorsapp.R;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.List;

import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_DATE;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_INTERVAL;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TIME;
import static com.operatorsapp.adapters.QCSamplesMultiTypeAdapter.LAST;

public class QCMultiTypeAdapter extends RecyclerView.Adapter {

    private List<TestFieldsDatum> list;
    private ChannelItemsAdapters.ChannelItemsAdaptersListener mOnKeyboardManagerListener;

    public QCMultiTypeAdapter(List<TestFieldsDatum> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN:
                return new QCMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_boolean, parent, false));
            case FIELD_TYPE_NUM:
                return new QCMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num, parent, false));
            case FIELD_TYPE_INTERVAL:
                return new QCMultiTypeAdapter.IntervalViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_interval, parent, false));
            case FIELD_TYPE_TEXT:
                return new QCMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
            case FIELD_TYPE_TIME:
                return new QCMultiTypeAdapter.TimeTextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time, parent, false));
            case FIELD_TYPE_DATE:
                return new QCMultiTypeAdapter.DateViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time, parent, false));
            default:
                return new QCMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
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
            case FIELD_TYPE_INTERVAL:
                break;
            case FIELD_TYPE_TEXT:
                break;
            case FIELD_TYPE_TIME:
                break;
            case FIELD_TYPE_DATE:
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
        switch (list.get(position).getInputType()) {
            case FIELD_TYPE_BOOLEAN:
                return FIELD_TYPE_BOOLEAN;
            case FIELD_TYPE_NUM:
                if (list.get(position).getHValue() != null) {
                    return FIELD_TYPE_INTERVAL;
                }
                return FIELD_TYPE_NUM;
            case FIELD_TYPE_TEXT:
                return FIELD_TYPE_TEXT;
            case FIELD_TYPE_TIME:
                return FIELD_TYPE_TIME;
            case FIELD_TYPE_DATE:
                return FIELD_TYPE_DATE;
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

            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);
            mTextEt = itemView.findViewById(R.id.IQCPHT_et);
        }

    }

    public class BooleanViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mBooleanCheckBox;

        BooleanViewHolder(View itemView) {
            super(itemView);

            mBooleanCheckBox = itemView.findViewById(R.id.IQCPHB_check_box);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

        }

    }

    public class NumViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditNumberEt;

        NumViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

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

    }

    public class IntervalViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditMaxEt;
        private EditText mEditMinEt;

        IntervalViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

            mEditMinEt = itemView.findViewById(R.id.IQCPI_min_et);
            setOnTouchListener(mEditMinEt);
            mEditMaxEt = itemView.findViewById(R.id.IQCPI_max_et);
            setOnTouchListener(mEditMaxEt);

        }

        private void setOnTouchListener(final EditText editText) {
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = editText.getInputType(); // backup the input type
                    editText.setInputType(InputType.TYPE_NULL); // disable soft input
                    editText.onTouchEvent(event); // call native handler
                    editText.setInputType(inType); // restore input type
                    setKeyBoard(editText, new String[]{".", "-"});
                    return false; // consume touch event
                }
            });
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

        }

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

    public class TimeTextViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTimeTv;

        TimeTextViewHolder(View itemView) {
            super(itemView);
            mTextTimeTv = itemView.findViewById(R.id.IQCPHTime_tv);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

        }

    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextDateTv;

        DateViewHolder(View itemView) {
            super(itemView);

            mTextDateTv = itemView.findViewById(R.id.IQCPHTime_tv);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

        }

    }

    public interface QCMultiTypeAdapterListener {
        void onItemCheck();
    }
}
