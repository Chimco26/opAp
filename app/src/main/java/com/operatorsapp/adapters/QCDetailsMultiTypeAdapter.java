package com.operatorsapp.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.QCModels.TestDetailsForm;
import com.example.common.QCModels.ValueList;
import com.example.common.SelectableString;
import com.operatorsapp.R;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_BOOLEAN_INT;
import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_COMBO_INT;
import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_DATE_INT;
import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_NUMBER_INT;
import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_TEXT_INT;
import static com.example.common.QCModels.TestDetailsForm.FIELD_TYPE_TIME_INT;
import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_NO_T_FORMAT;

public class QCDetailsMultiTypeAdapter extends RecyclerView.Adapter {

    private static final String TAG = QCDetailsMultiTypeAdapter.class.getSimpleName();
    private List<TestDetailsForm> list;
    private boolean isEditMode = true;

    public QCDetailsMultiTypeAdapter(List<TestDetailsForm> list, boolean isEditMode) {
        this.list = list;
        this.isEditMode = isEditMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case FIELD_TYPE_BOOLEAN_INT:
                return new QCDetailsMultiTypeAdapter.BooleanViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_boolean_custom_radio, parent, false));
            case FIELD_TYPE_TEXT_INT:
                return new QCDetailsMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text_parameter, parent, false));
            case FIELD_TYPE_TIME_INT:
                return new QCDetailsMultiTypeAdapter.TimeTextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_time_2, parent, false));
            case FIELD_TYPE_NUMBER_INT:
                return new QCDetailsMultiTypeAdapter.NumViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_num_parameter_2, parent, false));
            case FIELD_TYPE_COMBO_INT:
                return new QCDetailsMultiTypeAdapter.ComboViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_spinner, parent, false));
            default:
                return new QCDetailsMultiTypeAdapter.TextViewHolder(inflater.inflate(R.layout.item_qc_paramters_horizontal_text_parameter, parent, false));
        }
    }

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enabled   <code>true</code> to enable, <code>false</code> to disable
     *                  the views.
     */
    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);
        final TestDetailsForm item = list.get(position);

        enableDisableViewGroup((ViewGroup) viewHolder.itemView, isEditMode && item.getAllowEntry());

//        if (item.isFailed()) {
//            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.red_line_alpha));
//        } else
        if (!item.getAllowNull()) {
            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.blue1));
        } else {
            ((CustomViewHolder) viewHolder).title.setTextColor(viewHolder.itemView.getResources().getColor(R.color.black));
        }
        switch (type) {

            case FIELD_TYPE_BOOLEAN_INT:
                ((BooleanViewHolder) viewHolder).title.setText(item.getDisplayEName());
                TooltipCompat.setTooltipText(((BooleanViewHolder) viewHolder).title, item.getDisplayEName());
                if (item.getCurrentValue().toLowerCase().equals(Boolean.toString(true))) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(true);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(false);
                } else if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setChecked(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setChecked(true);
                }
                if (item.getAllowEntry() && isEditMode) {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(true);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            list.get(position).setCurrentValue(Boolean.toString(b));
                        }
                    });
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            list.get(position).setCurrentValue(Boolean.toString(!b));
                        }
                    });
                } else {
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioPassed.setEnabled(false);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setOnCheckedChangeListener(null);
                    ((BooleanViewHolder) viewHolder).mRadioFailed.setEnabled(false);
                }
                break;
            case FIELD_TYPE_NUMBER_INT:
                ((QCDetailsMultiTypeAdapter.NumViewHolder) viewHolder).title.setText(item.getDisplayEName());
                TooltipCompat.setTooltipText(((QCDetailsMultiTypeAdapter.NumViewHolder) viewHolder).title, item.getDisplayEName());
                ((QCDetailsMultiTypeAdapter.NumViewHolder) viewHolder).mEditNumberEt.setText(item.getCurrentValue());
                setEditableMode(position, item, ((QCDetailsMultiTypeAdapter.NumViewHolder) viewHolder).mEditNumberEt, viewHolder);
                break;
            case FIELD_TYPE_TEXT_INT:
                ((TextViewHolder) viewHolder).title.setText(item.getDisplayEName());
                TooltipCompat.setTooltipText(((TextViewHolder) viewHolder).title, item.getDisplayEName());
                ((TextViewHolder) viewHolder).mTextEt.setText(item.getCurrentValue());
                setEditableMode(position, item, ((TextViewHolder) viewHolder).mTextEt, viewHolder);
                break;
            case FIELD_TYPE_COMBO_INT:
                ((ComboViewHolder) viewHolder).title.setText(item.getDisplayEName());
                TooltipCompat.setTooltipText(((ComboViewHolder) viewHolder).title, item.getDisplayEName());
                if (item.getListValues() != null && item.getListValues().size() > 0) {
                    ((ComboViewHolder) viewHolder).initSpinner(((ComboViewHolder) viewHolder).mSpinner, item);
                }
                break;
            case FIELD_TYPE_TIME_INT:
                if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty()) {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setText(item.getCurrentValue());
                }
                ((TimeTextViewHolder) viewHolder).title.setText(item.getDisplayEName());
                TooltipCompat.setTooltipText(((TimeTextViewHolder) viewHolder).title, item.getDisplayEName());
                if (item.getAllowEntry() && isEditMode) {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((TimeTextViewHolder) viewHolder).showDatePicker((TimeTextViewHolder) viewHolder, item);
                        }
                    });
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setBackgroundColor(((TimeTextViewHolder) viewHolder).mTextTimeTv.getContext().getResources().getColor(R.color.white));
                } else {
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setOnClickListener(null);
                    ((TimeTextViewHolder) viewHolder).mTextTimeTv.setBackgroundColor(((TimeTextViewHolder) viewHolder).mTextTimeTv.getContext().getResources().getColor(R.color.grey_transparent));
                }
                break;
        }
    }

    private void setEditableMode(int position, TestDetailsForm item, EditText mEditNumberEt, RecyclerView.ViewHolder viewHolder) {
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

    private void setTextWatcher(final int position, EditText mTextEt, final RecyclerView.ViewHolder viewHolder, final TestDetailsForm item) {
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
        switch (list.get(position).getDisplayType()) {
            case FIELD_TYPE_BOOLEAN_INT:
                return FIELD_TYPE_BOOLEAN_INT;
            case FIELD_TYPE_TEXT_INT:
                return FIELD_TYPE_TEXT_INT;
            case FIELD_TYPE_NUMBER_INT:
                return FIELD_TYPE_NUMBER_INT;
            case FIELD_TYPE_TIME_INT:
            case FIELD_TYPE_DATE_INT:
                return FIELD_TYPE_TIME_INT;
            case FIELD_TYPE_COMBO_INT:
                return FIELD_TYPE_COMBO_INT;
            default:
                return FIELD_TYPE_TEXT_INT;

        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;

        CustomViewHolder(@NonNull View itemView) {
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

    public class ComboViewHolder extends CustomViewHolder {

        private final Spinner mSpinner;

        ComboViewHolder(View itemView) {
            super(itemView);

            mSpinner = itemView.findViewById(R.id.IQCPHS_spinner);
            title.setVisibility(View.VISIBLE);

        }

        private void initSpinner(final Spinner spinner, final TestDetailsForm item) {
            if (item == null) {
                return;
            }
            final List<SelectableString> list = buildSelectableStringList(item.getListValues());
            final SimpleSpinnerAdapter dataAdapter = new SimpleSpinnerAdapter(spinner.getContext(), R.layout.base_spinner_item, list);
            dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            SelectableString.getById((ArrayList<SelectableString>) list, String.valueOf(item.getCurrentValue())).setSelected(true);
            int selectedPosition = SelectableString.getSelectedItemPosition((ArrayList<SelectableString>) list);
            spinner.setSelection(selectedPosition, false);
            dataAdapter.setTitle(selectedPosition);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    dataAdapter.setTitle(i);
                    list.get(i).selectThisItemOnly((ArrayList<SelectableString>) list);
                    item.setCurrentValue(list.get(i).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        private List<SelectableString> buildSelectableStringList(List<ValueList> listValues) {
            ArrayList<SelectableString> selectableStrings = new ArrayList<>();
            for (ValueList value : listValues) {
                selectableStrings.add(new SelectableString(value.getValue(), false, String.valueOf(value.getID())));
            }
            return selectableStrings;
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


    public class TimeTextViewHolder extends CustomViewHolder {

        private TextView mTextTimeTv;

        TimeTextViewHolder(View itemView) {
            super(itemView);
            mTextTimeTv = itemView.findViewById(R.id.IQCPHTime_tv);
            title.setVisibility(View.VISIBLE);

        }

        void showHourPicker(TimeTextViewHolder viewHolder, final TestDetailsForm item, final Calendar calendar) {

            TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (view.isShown()) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        item.setCurrentValue(TimeUtils.getDate(calendar.getTime().getTime(), SQL_NO_T_FORMAT));
                        mTextTimeTv.setText(TimeUtils.getDateFromFormat(
                                new Date(TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT)),
                                SIMPLE_FORMAT_FORMAT));
                    }
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(viewHolder.itemView.getContext(), R.style.TimePickerTheme, myTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(String.format("%s :", viewHolder.itemView.getContext().getString(R.string.choose_hour)));
            timePickerDialog.show();
        }

        public void showDatePicker(final TimeTextViewHolder viewHolder, final TestDetailsForm item) {
            final Calendar calendar = Calendar.getInstance();
            if (item.getCurrentValue() != null && !item.getCurrentValue().isEmpty() && !item.getCurrentValue().equals("0")) {
                long time = TimeUtils.getLongFromDateString(item.getCurrentValue(), SQL_NO_T_FORMAT);
                if (time == 0) {
                    time = TimeUtils.getLongFromDateString(item.getCurrentValue(), SIMPLE_FORMAT_FORMAT);
                }
                calendar.setTime(new Date(time));
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

                    showHourPicker(viewHolder, item, calendar);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }
}
