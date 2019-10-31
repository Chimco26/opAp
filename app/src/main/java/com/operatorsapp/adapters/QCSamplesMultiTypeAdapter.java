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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.common.QCModels.SamplesDatum;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.ArrayList;

import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_LAST;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_LAST_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT_INT;

public class QCSamplesMultiTypeAdapter extends RecyclerView.Adapter {

    private final String mInputType;
    private final OnKeyboardManagerListener mOnKeyboardManagerListener;
    private final QCSamplesMultiTypeAdapterListener mQcSamplesMultiTypeAdapterListener;
    private ArrayList<SamplesDatum> list;

    public QCSamplesMultiTypeAdapter(String inputType, ArrayList<SamplesDatum> list,
                                     OnKeyboardManagerListener onKeyboardManagerListener,
                                     QCSamplesMultiTypeAdapterListener qcSamplesMultiTypeAdapterListener) {
        mInputType = inputType;
        mOnKeyboardManagerListener = onKeyboardManagerListener;
        mQcSamplesMultiTypeAdapterListener = qcSamplesMultiTypeAdapterListener;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN_INT:
                return new QCSamplesMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_horizontal_boolean, parent, false));
            case FIELD_TYPE_NUM_INT:
                return new QCSamplesMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num, parent, false));
            case FIELD_TYPE_TEXT_INT:
                return new QCSamplesMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
            case FIELD_TYPE_LAST_INT:
                return new QCSamplesMultiTypeAdapter.LastMinusViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_last, parent, false));
            default:
                return new QCSamplesMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);
        final SamplesDatum item = list.get(position);

        switch (type) {

            case FIELD_TYPE_BOOLEAN_INT:
                if (item.getValue() != null && item.getValue().toLowerCase().equals(Boolean.toString(true))){
                    ((BooleanViewHolder)viewHolder).mBooleanCheckBox.setChecked(true);
                }else {
                    ((BooleanViewHolder)viewHolder).mBooleanCheckBox.setChecked(false);
                }
                ((BooleanViewHolder)viewHolder).mBooleanCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setValue(Boolean.toString(b));
                        if (item.getUpsertType() != 2) {
                            item.setUpsertType(3);
                        }
                    }
                });
                if (item.isFailed()){
                    ((BooleanViewHolder)viewHolder).mBooleanCheckBox.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
                }else {
                    ((BooleanViewHolder)viewHolder).mBooleanCheckBox.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.transparentColor));
                }
                break;
            case FIELD_TYPE_NUM_INT:
                ((NumViewHolder)viewHolder).mEditNumberEt.setText(item.getValue());
                setTextWatcher(position, ((NumViewHolder)viewHolder).mEditNumberEt);
                if (item.isFailed()){
                    ((NumViewHolder)viewHolder).mEditNumberEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
                }else {
                    ((NumViewHolder)viewHolder).mEditNumberEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.white));
                }
                break;
            case FIELD_TYPE_TEXT_INT:
                ((TextViewHolder)viewHolder).mTextEt.setText(item.getValue());
                setTextWatcher(position, ((TextViewHolder) viewHolder).mTextEt);
                if (item.isFailed()){
                    ((TextViewHolder)viewHolder).mTextEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
                }else {
                    ((TextViewHolder)viewHolder).mTextEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.white));
                }
                break;
            case FIELD_TYPE_LAST_INT:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnKeyboardManagerListener.onCloseKeyboard();
                        mQcSamplesMultiTypeAdapterListener.onDeleteLine(position);
                    }
                });
                break;
        }
    }

    public void setTextWatcher(final int position, EditText mTextEt) {
        mTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.get(position).setValue(charSequence.toString());
                if (list.get(position).getUpsertType() != 2) {
                    list.get(position).setUpsertType(3);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                return FIELD_TYPE_BOOLEAN_INT;
            case FIELD_TYPE_NUM:
                return FIELD_TYPE_NUM_INT;
            case FIELD_TYPE_TEXT:
                return FIELD_TYPE_TEXT_INT;
            case FIELD_TYPE_LAST:
                return FIELD_TYPE_LAST_INT;
            default:
                return FIELD_TYPE_TEXT_INT;

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

    public interface QCSamplesMultiTypeAdapterListener {

        void onDeleteLine(int position);
    }

}
