package com.operatorsapp.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.common.QCModels.TestFieldsDatum;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.RangeView2;
import com.operatorsapp.view.SingleLineKeyboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import static com.operatorsapp.utils.TimeUtils.ONLY_DATE_FORMAT;

public class QCMultiTypeAdapter extends RecyclerView.Adapter {

    private static final String TAG = QCMultiTypeAdapter.class.getSimpleName();
    private final OnKeyboardManagerListener mOnKeyboardManagerListener;
    private List<TestFieldsDatum> list;

    public QCMultiTypeAdapter(List<TestFieldsDatum> list, OnKeyboardManagerListener onKeyboardManagerListener) {
        mOnKeyboardManagerListener = onKeyboardManagerListener;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN_INT:
                return new QCMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_boolean, parent, false));
            case FIELD_TYPE_NUM_INT:
                return new QCMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num_parameter, parent, false));
            case FIELD_TYPE_INTERVAL_INT:
                return new QCMultiTypeAdapter.IntervalViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_interval, parent, false));
            case FIELD_TYPE_TEXT_INT:
                return new QCMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text_parameter, parent, false));
//            case FIELD_TYPE_TIME_INT:
//                return new QCMultiTypeAdapter.TimeTextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time, parent, false));
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
        switch (type) {

            case FIELD_TYPE_BOOLEAN_INT:
                ((BooleanViewHolder) viewHolder).mBooleanCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        list.get(position).setCurrentValue(Boolean.toString(b));
                    }
                });
                break;
            case FIELD_TYPE_NUM_INT:
                ((NumViewHolder) viewHolder).mEditNumberEt.setText(item.getCurrentValue());
                setTextWatcher(position, ((NumViewHolder) viewHolder).mEditNumberEt);
                break;
            case FIELD_TYPE_TEXT_INT:
                ((TextViewHolder) viewHolder).mTextEt.setText(item.getCurrentValue());
                setTextWatcher(position, ((TextViewHolder) viewHolder).mTextEt);
                break;
            case FIELD_TYPE_INTERVAL_INT:
                ((IntervalViewHolder) viewHolder).mEditMinEt.setText(String.valueOf(item.getLValue()));
                ((IntervalViewHolder) viewHolder).mEditMaxEt.setText(String.valueOf(item.getHValue()));
                setRangeView(item, ((IntervalViewHolder) viewHolder).mRangeView);
                ((IntervalViewHolder) viewHolder).mRangeView.setLowLimit(1);
                ((IntervalViewHolder) viewHolder).mRangeView.setHighLimit(5);

                break;
//            case FIELD_TYPE_TIME_INT:
//                ((TimeTextViewHolder)viewHolder).mTextTimeTv.setText(item.getCurrentValue());
//                ((TimeTextViewHolder)viewHolder).showHourPicker(viewHolder.itemView.getContext());
//                break;
            case FIELD_TYPE_DATE_INT:
                ((DateViewHolder)viewHolder).mTextDateTv.setText(item.getCurrentValue());
                ((DateViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker(viewHolder, item);
                    }
                });
                break;

        }
    }

    public void showDatePicker(@NonNull RecyclerView.ViewHolder viewHolder, final TestFieldsDatum item) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), ONLY_DATE_FORMAT)));
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                viewHolder.itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                item.setCurrentValue(String.format(Locale.US, "%d/%d/%d", i, i1, i2));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setRangeView(final TestFieldsDatum item, final RangeView2 mRangeView) {


        mRangeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mRangeView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()) {
                    mRangeView.setCurrentValue(Float.parseFloat(item.getCurrentValue()));
                }else {
                    mRangeView.setCurrentValue(0);
                }
                mRangeView.setHighLimit(item.getHValue());
                mRangeView.setLowLimit(item.getLValue());
                mRangeView.setWidth((int) (mRangeView.getWidth()));
            }
        });

    }

    public void setTextWatcher(final int position, EditText mTextEt) {
        mTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.get(position).setCurrentValue(charSequence.toString());
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
//                return FIELD_TYPE_TIME_INT;
                return FIELD_TYPE_DATE_INT;
            case FIELD_TYPE_DATE:
                return FIELD_TYPE_DATE_INT;
            default:
                return FIELD_TYPE_TEXT_INT;

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

        private final RangeView2 mRangeView;
        private EditText mEditMaxEt;
        private EditText mEditMinEt;

        IntervalViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);

            mEditMinEt = itemView.findViewById(R.id.IQCPI_min_et);
            setOnTouchListener(mEditMinEt);
            mEditMaxEt = itemView.findViewById(R.id.IQCPI_max_et);
            setOnTouchListener(mEditMaxEt);
            itemView.findViewById(R.id.QCP_parameter_txt).setVisibility(View.VISIBLE);
            mRangeView = itemView.findViewById(R.id.IQCPI_range);

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

        public void showHourPicker(Context context) {
            final Calendar myCalender = Calendar.getInstance();
            int hour = myCalender.get(Calendar.HOUR_OF_DAY);
            int minute = myCalender.get(Calendar.MINUTE);


            TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (view.isShown()) {
                        myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalender.set(Calendar.MINUTE, minute);

                    }
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
            timePickerDialog.setTitle("Choose hour:");
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            timePickerDialog.show();
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

}
