package com.operatorsapp.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.TooltipCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.common.QCModels.TestFieldsDatum;
import com.operatorsapp.R;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.RangeView2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_BOOLEAN_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_DATE;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_DATE_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_INTERVAL_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_NUM_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TEXT_INT;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TIME;
import static com.example.common.QCModels.TestDetailsResponse.FIELD_TYPE_TIME_INT;
import static com.operatorsapp.utils.TimeUtils.ONLY_DATE_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SIMPLE_HM_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_NO_T_FORMAT;

public class QCMultiTypeAdapter extends RecyclerView.Adapter {

    private static final String TAG = QCMultiTypeAdapter.class.getSimpleName();
    private List<TestFieldsDatum> list;
    private boolean isEditMode = true;

    public QCMultiTypeAdapter(List<TestFieldsDatum> list, boolean isEditMode) {
        this.list = list;
        this.isEditMode = isEditMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN_INT:
                return new QCMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_boolean_custom_radio, parent, false));
            case FIELD_TYPE_NUM_INT:
                return new QCMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num_parameter, parent, false));
            case FIELD_TYPE_INTERVAL_INT:
                return new QCMultiTypeAdapter.IntervalViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_interval, parent, false));
            case FIELD_TYPE_TEXT_INT:
                return new QCMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text_parameter, parent, false));
            case FIELD_TYPE_TIME_INT:
                return new QCMultiTypeAdapter.TimeTextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time, parent, false));
            case FIELD_TYPE_DATE_INT:
                return new QCMultiTypeAdapter.DateViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time, parent, false));
            default:
                return new QCMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text_parameter, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);
        final TestFieldsDatum item = list.get(position);

        if (item.isFailed()) {
            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
        } else if (item.getRequiredField()) {
            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.blue1));
        } else {
            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.black));
        }
       switch (type) {

            case FIELD_TYPE_BOOLEAN_INT:
                ((BooleanViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((BooleanViewHolder) viewHolder).title, item.getLName());
                if (item.getCurrentValue().toLowerCase().equals(Boolean.toString(true))) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(true);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(false);
                } else if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()){
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(true);
                }
                if (item.getAllowEntry() && isEditMode) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(true);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            list.get(position).setCurrentValue(Boolean.toString(b));
                            list.get(position).setUpsertType(3);
                        }
                    });
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            list.get(position).setCurrentValue(Boolean.toString(!b));
                            list.get(position).setUpsertType(3);
                        }
                    });
                } else {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setEnabled(false);
                }
                break;
            case FIELD_TYPE_NUM_INT:
                ((NumViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((NumViewHolder) viewHolder).title, item.getLName());
                ((NumViewHolder) viewHolder).mEditNumberEt.setText(item.getCurrentValue());
                setEditableMode(position, item, ((NumViewHolder) viewHolder).mEditNumberEt, viewHolder);
                break;
            case FIELD_TYPE_TEXT_INT:
                ((TextViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((TextViewHolder) viewHolder).title, item.getLName());
                ((TextViewHolder) viewHolder).mTextEt.setText(item.getCurrentValue());
                setEditableMode(position, item, ((TextViewHolder) viewHolder).mTextEt, viewHolder);
                break;
            case FIELD_TYPE_INTERVAL_INT:
                ((IntervalViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((IntervalViewHolder) viewHolder).title, item.getLName());
                ((IntervalViewHolder) viewHolder).mValueEt.setText(String.valueOf(item.getCurrentValue()));
                setRangeView(item, ((IntervalViewHolder) viewHolder).mRangeView);
                ((IntervalViewHolder) viewHolder).mRangeView.setLowLimit(1);
                ((IntervalViewHolder) viewHolder).mRangeView.setHighLimit(5);
                ((IntervalViewHolder) viewHolder).mValueEt.setText(item.getCurrentValue());
                setEditableMode(position, item, ((IntervalViewHolder) viewHolder).mValueEt, viewHolder);
                break;
            case FIELD_TYPE_TIME_INT:
                if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()) {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setText(TimeUtils.getDateFromFormat(
                            new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT)),
                            SIMPLE_HM_FORMAT));
                }
                ((TimeTextViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((TimeTextViewHolder) viewHolder).title, item.getLName());
                if (item.getAllowEntry() && isEditMode) {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((TimeTextViewHolder) viewHolder).showHourPicker(((TimeTextViewHolder) viewHolder).itemView.getContext(), item);
                        }
                    });
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setBackgroundColor(((TimeTextViewHolder) viewHolder).mTextTimeTv.getContext().getResources().getColor(R.color.white));
                }else {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setOnClickListener(null);
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setBackgroundColor(((TimeTextViewHolder) viewHolder).mTextTimeTv.getContext().getResources().getColor(R.color.grey_transparent));
                }
                break;
            case FIELD_TYPE_DATE_INT:
                ((DateViewHolder) viewHolder).title.setText(item.getLName());
                TooltipCompat.setTooltipText(((DateViewHolder) viewHolder).title, item.getLName());
                if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty() && !item.getCurrentValue().equals("0")) {
                    ((DateViewHolder) viewHolder).mTextDateTv.setText(TimeUtils.getDate(
                            TimeUtils.convertDateToMillisecond(item.getCurrentValue(), SQL_NO_T_FORMAT),
                            ONLY_DATE_FORMAT));
                }
                if (item.getAllowEntry() && isEditMode) {
                    ((DateViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDatePicker(((DateViewHolder) viewHolder), item);
                        }
                    });
                    ((DateViewHolder) viewHolder).mTextDateTv.setBackgroundColor(((DateViewHolder) viewHolder).mTextDateTv.getContext().getResources().getColor(R.color.white));
                } else {
                    ((DateViewHolder) viewHolder).itemView.setOnClickListener(null);
                    ((DateViewHolder) viewHolder).mTextDateTv.setBackgroundColor(((DateViewHolder) viewHolder).mTextDateTv.getContext().getResources().getColor(R.color.grey_transparent));
                }
                break;

        }
    }

    public void setEditableMode(int position, TestFieldsDatum item, EditText mEditNumberEt, RecyclerView.ViewHolder viewHolder) {
        if (item.getAllowEntry() && isEditMode) {
            setTextWatcher(position, mEditNumberEt, viewHolder, item);
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

    public void showDatePicker(final DateViewHolder viewHolder, final TestFieldsDatum item) {
        final Calendar calendar = Calendar.getInstance();
        if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty() && !item.getCurrentValue().equals("0")) {
            calendar.setTime(new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT)));
        } else {
            calendar.setTime(new Date());
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                viewHolder.itemView.getContext(), R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                item.setCurrentValue(TimeUtils.getDate(calendar.getTime().getTime(), SQL_NO_T_FORMAT));
//                item.setCurrentValue(String.format(Locale.US, "%d-%d-%d %d:%d:%d", i, i1 + 1, i2, 0, 0, 0));
                item.setUpsertType(3);
                viewHolder.mTextDateTv.setText(TimeUtils.getDate(
                        TimeUtils.convertDateToMillisecond(item.getCurrentValue(), SQL_NO_T_FORMAT),
                        ONLY_DATE_FORMAT));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setRangeView(final TestFieldsDatum item, final RangeView2 mRangeView) {
        mRangeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRangeView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateRangeView(item, mRangeView);
            }
        });

    }

    public void updateRangeView(TestFieldsDatum item, RangeView2 mRangeView) {
        if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()) {
            try {
                mRangeView.setCurrentValue(Float.parseFloat(item.getCurrentValue()));
                mRangeView.setDefaultMode(false);
            }catch (Exception e){
                mRangeView.setCurrentValue(0);
                mRangeView.setDefaultMode(true);
            }
        } else {
            mRangeView.setCurrentValue(0);
            mRangeView.setDefaultMode(true);
        }
        mRangeView.setHighLimit(item.getHValue());
        mRangeView.setLowLimit(item.getLValue());
        mRangeView.setWidth((int) (mRangeView.getWidth()));
    }

    public void setTextWatcher(final int position, EditText mTextEt, final RecyclerView.ViewHolder viewHolder, final TestFieldsDatum item) {
        mTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.get(position).setCurrentValue(charSequence.toString());
                list.get(position).setUpsertType(3);
                if (viewHolder instanceof IntervalViewHolder) {
                    updateRangeView(item, ((IntervalViewHolder) viewHolder).mRangeView);
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
        switch (list.get(position).getFieldType()) {
            case FIELD_TYPE_BOOLEAN:
                return FIELD_TYPE_BOOLEAN_INT;
            case FIELD_TYPE_NUM:
                if (list.get(position).getHValue() != null) {
                    return FIELD_TYPE_INTERVAL_INT;
                }
                return FIELD_TYPE_NUM_INT;
            case FIELD_TYPE_TEXT:
                return FIELD_TYPE_TEXT_INT;
            case FIELD_TYPE_TIME:
                return FIELD_TYPE_TIME_INT;
//                return FIELD_TYPE_DATE_INT;
            case FIELD_TYPE_DATE:
                return FIELD_TYPE_DATE_INT;
            default:
                return FIELD_TYPE_TEXT_INT;

        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.QCP_parameter_txt);
        }
    }

    public class TextViewHolder extends CustomViewHolder {

        private EditText mTextEt;

        TextViewHolder(View itemView) {
            super(itemView);

            title.setVisibility(View.VISIBLE);
            mTextEt = itemView.findViewById(R.id.IQCPHT_et);
        }

    }

    public class BooleanViewHolder extends CustomViewHolder {

        private RadioButton mRadioPassed;
        private RadioButton mRadioFailed;

        BooleanViewHolder(View itemView) {
            super(itemView);

            mRadioPassed = itemView.findViewById(R.id.QCP_parameter_radio_passed);
            mRadioFailed = itemView.findViewById(R.id.QCP_parameter_radio_failed);
            title.setVisibility(View.VISIBLE);

        }

    }

    public class NumViewHolder extends CustomViewHolder {

        private EditText mEditNumberEt;

        NumViewHolder(View itemView) {
            super(itemView);
            title.setVisibility(View.VISIBLE);

            mEditNumberEt = itemView.findViewById(R.id.IQCPHN_et);
        }

    }

    public class IntervalViewHolder extends CustomViewHolder {

        private final RangeView2 mRangeView;
        private EditText mValueEt;

        IntervalViewHolder(View itemView) {
            super(itemView);
            title.setVisibility(View.VISIBLE);

            mValueEt = itemView.findViewById(R.id.IQCPHN_et);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);
            mRangeView = itemView.findViewById(R.id.IQCPI_range);

        }

    }

    public class DateViewHolder extends CustomViewHolder {

        private TextView mTextDateTv;

        DateViewHolder(View itemView) {
            super(itemView);

            mTextDateTv = itemView.findViewById(R.id.IQCPHTime_tv);
            title.setVisibility(View.VISIBLE);

        }

    }

    public class TimeTextViewHolder extends CustomViewHolder {

        private TextView mTextTimeTv;

        TimeTextViewHolder(View itemView) {
            super(itemView);
            mTextTimeTv = itemView.findViewById(R.id.IQCPHTime_tv);
            title.setVisibility(View.VISIBLE);

        }

        public void showHourPicker(Context context, final TestFieldsDatum item) {
            final Calendar calendar = Calendar.getInstance();
            if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty() && !item.getCurrentValue().equals("0")) {
                calendar.setTime(new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT)));
            } else {
                calendar.setTime(new Date());
            }

            TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (view.isShown()) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        item.setCurrentValue(TimeUtils.getDate(calendar.getTime().getTime(), SQL_NO_T_FORMAT));
                        item.setUpsertType(3);
                        mTextTimeTv.setText(TimeUtils.getDateFromFormat(
                                new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT)),
                                SIMPLE_HM_FORMAT));
                    }
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, R.style.TimePickerTheme, myTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(String.format("%s :", context.getString(R.string.choose_hour)));
            timePickerDialog.show();
        }
    }
}
