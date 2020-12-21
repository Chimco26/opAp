package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.common.QCModels.SamplesDatum;
import com.example.common.QCModels.TestSampleFieldsDatum;
import com.operatorsapp.R;

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
    private final QCSamplesMultiTypeAdapterListener mQcSamplesMultiTypeAdapterListener;
    private final TestSampleFieldsDatum mTestSample;
    private ArrayList<SamplesDatum> list;
    private boolean isEditMode = true;

    public QCSamplesMultiTypeAdapter(String inputType, TestSampleFieldsDatum testSample,
                                     QCSamplesMultiTypeAdapterListener qcSamplesMultiTypeAdapterListener, boolean isEditMode) {
        mInputType = inputType;
        mQcSamplesMultiTypeAdapterListener = qcSamplesMultiTypeAdapterListener;
        mTestSample = testSample;
        this.list = (ArrayList<SamplesDatum>) testSample.getSamplesData();
        this.isEditMode = isEditMode;
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
                if (item.getValue() != null && item.getValue().toLowerCase().equals(Boolean.toString(true))) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(true);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(false);
                } else if (item.getValue() != null && !item.getValue().isEmpty()){
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(true);
                }
                if (mTestSample.getAllowEntry() && isEditMode) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(true);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            item.setValue(Boolean.toString(b));
                            if (item.getUpsertType() != 2) {
                                item.setUpsertType(3);
                                mTestSample.setUpsertType(3);
                            }
                        }
                    });
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            item.setValue(Boolean.toString(!b));
                            if (item.getUpsertType() != 2) {
                                item.setUpsertType(3);
                                mTestSample.setUpsertType(3);
                            }
                        }
                    });
                } else {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setEnabled(false);
                }
//                if (item.isFailed()) {
//                    ((BooleanViewHolder) viewHolder).mBooleanCheckBox.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
//                } else {
//                    ((BooleanViewHolder) viewHolder).mBooleanCheckBox.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.transparentColor));
//                }
               //
                break;
            case FIELD_TYPE_NUM_INT:
                ((NumViewHolder) viewHolder).mEditNumberEt.setText(item.getValue());
                if (item.isFailed()) {
                    ((NumViewHolder) viewHolder).mEditNumberEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
                } else {
                    ((NumViewHolder) viewHolder).mEditNumberEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.white));
                }
                setEditableMode(position, item, ((NumViewHolder) viewHolder).mEditNumberEt);
                break;
            case FIELD_TYPE_TEXT_INT:
                ((TextViewHolder) viewHolder).mTextEt.setText(item.getValue());
                if (item.isFailed()) {
                    ((TextViewHolder) viewHolder).mTextEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
                } else {
                    ((TextViewHolder) viewHolder).mTextEt.setBackgroundColor(viewHolder.itemView.getResources().getColor(R.color.white));
                }
                setEditableMode(position, item, ((TextViewHolder) viewHolder).mTextEt);
                break;
            case FIELD_TYPE_LAST_INT:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mQcSamplesMultiTypeAdapterListener.onDeleteLine(position);
                    }
                });
                break;
        }
    }

    public void setEditableMode(int position, SamplesDatum item, EditText mEditNumberEt) {
        if (mTestSample.getAllowEntry() && isEditMode) {
            setTextWatcher(position, mEditNumberEt);
            mEditNumberEt.setEnabled(true);
            mEditNumberEt.setFocusable(true);
            mEditNumberEt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            mEditNumberEt.setClickable(true);
            mEditNumberEt.setBackgroundColor(mEditNumberEt.getContext().getResources().getColor(R.color.white));

        } else {
            mEditNumberEt.setEnabled(false);
            mEditNumberEt.setFocusable(false);
            mEditNumberEt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            mEditNumberEt.setClickable(false);
            mEditNumberEt.setBackgroundColor(mEditNumberEt.getContext().getResources().getColor(R.color.grey_transparent));
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
                    mTestSample.setUpsertType(3);
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

        private RadioButton mRadioPassed;
        private RadioButton mRadioFailed;

        BooleanViewHolder(View itemView) {
            super(itemView);

            mRadioPassed = itemView.findViewById(R.id.QCP_parameter_radio_passed);
            mRadioFailed = itemView.findViewById(R.id.QCP_parameter_radio_failed);

        }

    }

    public class NumViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditNumberEt;

        NumViewHolder(View itemView) {
            super(itemView);

            mEditNumberEt = itemView.findViewById(R.id.IQCPHN_et);
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
