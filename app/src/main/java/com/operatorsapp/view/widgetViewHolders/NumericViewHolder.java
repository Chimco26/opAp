package com.operatorsapp.view.widgetViewHolders;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.interfaces.OnKeyboardManagerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.responses.ResponseKPIS;
import com.operatorsapp.view.SingleLineKeyboard;

import java.text.DecimalFormat;
import java.util.Locale;

import static com.operatorsapp.activities.DashboardActivity.REPORT_REJECT_TAG;
import static com.operatorsapp.activities.DashboardActivity.REPORT_UNIT_CYCLE_TAG;

public class NumericViewHolder extends RecyclerView.ViewHolder {

    private final View mEditStep2Ly;
    private final Spinner mSpinner1;
    private final View mSpinner1BtnRv;
    private final Spinner mSpinner2;
    private final View mStep2CancelBtn;
    private final View mStep2ReportBtn;
    private final View mEditCycleLy;
    private final EditText mEditCycleEt;
    private final View mEditCycleCancelBtn;
    private final View mEditCycleReportBtn;
    private final View mEditLy;
    private final RadioButton mWeightRadioBtn;
    private boolean mShowAddRejectsBtn = true;
    private boolean mShowChangeUnitInCycle = true;
    private View mEditIc;
    private View mDisplayLy;
    private View mEditStep1Ly;
    private EditText mEditNumberEt;
    private RadioButton mUnitRadioBtn;
    //    private RadioButton mWeightRadioBtn;
    private View mStep1CancelBtn;
    private TextView mStep1NextBtn;
    private LinearLayout mParentLayout;
    private View mDivider;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mValue;
    private TextView mEditBtn;
    private DashboardCentralContainerListener mDashboardCentralContainerListener;
    private int mHeight;
    private int mWidth;
    private int mSelectedCauseId;
    private int mSelectedReasonId;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private OnKeyboardManagerListener mOnKeyboardManagerListener;

    public NumericViewHolder(View itemView, DashboardCentralContainerListener listener, OnKeyboardManagerListener onKeyboardManagerListener,
                             ReportFieldsForMachine reportFieldsForMachine, int height, int width, boolean showChangeUnitInCycleBtn, boolean showAddRejectsBtn) {
        super(itemView);
        mDashboardCentralContainerListener = listener;
        mOnKeyboardManagerListener = onKeyboardManagerListener;
        mReportFieldsForMachine = reportFieldsForMachine;
        mHeight = height;
        mWidth = width;
        mShowChangeUnitInCycle = showChangeUnitInCycleBtn;
        mShowAddRejectsBtn = showAddRejectsBtn;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.numeric_widget_title);
        mSubtitle = itemView.findViewById(R.id.numeric_widget_subtitle);
        mValue = itemView.findViewById(R.id.numeric_widget_value);
        mEditBtn = itemView.findViewById(R.id.NWC_edit_btn);
        mEditLy = itemView.findViewById(R.id.NWC_edit_ly);
        mEditIc = itemView.findViewById(R.id.numeric_widget_edit_ic);
        mDisplayLy = itemView.findViewById(R.id.NWC_display_ly);

        mEditStep1Ly = itemView.findViewById(R.id.NWC_edit_step_1_ly);
        mEditNumberEt = itemView.findViewById(R.id.NWC_edit_number_et);
        mStep1NextBtn = itemView.findViewById(R.id.NWC_edit_next_btn);
        mEditNumberEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = mEditNumberEt.getInputType(); // backup the input type
                mEditNumberEt.setInputType(InputType.TYPE_NULL); // disable soft input
                mEditNumberEt.onTouchEvent(event); // call native handler
                mEditNumberEt.setInputType(inType); // restore input type
                setKeyBoard(mEditNumberEt, new String[]{".", "-"}, mStep1NextBtn);
                return false; // consume touch event
            }
        });
        mUnitRadioBtn = itemView.findViewById(R.id.NWC_edit_unit_btn);
        mWeightRadioBtn = itemView.findViewById(R.id.NWC_edit_weight_btn);
        View unitTv = itemView.findViewById(R.id.NWC_units_tv);
        View weightTv = itemView.findViewById(R.id.NWC_weight_tv);
        initUnitAndWeight(unitTv, weightTv);
        mStep1CancelBtn = itemView.findViewById(R.id.NWC_edit_cancel_btn);
        ResponseKPIS trans = PersistenceManager.getInstance().getTranslationForKPIS();
        if (trans != null) {
            ((TextView) itemView.findViewById(R.id.NWC_units_tv)).setText(trans.getKPIByName("Units"));
        }
        mEditStep2Ly = itemView.findViewById(R.id.NWC_edit_step_2_ly);
        mSpinner1 = itemView.findViewById(R.id.NWC_reason_spinner);
        mSpinner1BtnRv = itemView.findViewById(R.id.NWC_reason2_spinner_rl);
        mSpinner2 = itemView.findViewById(R.id.NWC_reason2_spinner);
        mStep2CancelBtn = itemView.findViewById(R.id.NWC_edit_step2_cancel_btn);
        mStep2ReportBtn = itemView.findViewById(R.id.NWC_edit_step2_next_btn);

        mEditCycleLy = itemView.findViewById(R.id.NWC_edit_quantity_ly);
        mEditCycleEt = itemView.findViewById(R.id.NWC_edit_quantity_value_et);
        mEditCycleReportBtn = itemView.findViewById(R.id.NWC_edit_quantity_next_btn);
        if (PersistenceManager.getInstance().getUnitsInCycleType() <= 1) {
            mEditCycleEt.setHint(String.format(Locale.getDefault(), "%s %s", mEditCycleEt.getContext().getString(R.string.max_value_is), PersistenceManager.getInstance().getMaxUnitReport()));
        }
        mEditCycleEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = mEditCycleEt.getInputType(); // backup the input type
                mEditCycleEt.setInputType(InputType.TYPE_NULL); // disable soft input
                mEditCycleEt.onTouchEvent(event); // call native handler
                mEditCycleEt.setInputType(inType); // restore input type
                setKeyBoard(mEditCycleEt, new String[]{"."}, mEditCycleReportBtn);
                return false; // consume touch event
            }
        });
        mEditCycleCancelBtn = itemView.findViewById(R.id.NWC_edit_quantity_cancel_btn);

    }

    public void clearTouchListener() {
        mEditNumberEt.setOnTouchListener(null);
        mEditCycleEt.setOnTouchListener(null);
    }

    private void initUnitAndWeight(View unitTv, View weightTv) {
        switch (PersistenceManager.getInstance().getReportRejectDefaultUnits()) {
            case 1:
                mUnitRadioBtn.setVisibility(View.VISIBLE);
                unitTv.setVisibility(View.VISIBLE);
                mUnitRadioBtn.setChecked(true);
                mWeightRadioBtn.setVisibility(View.GONE);
                weightTv.setVisibility(View.GONE);
                break;
            case 2:
                mWeightRadioBtn.setVisibility(View.VISIBLE);
                weightTv.setVisibility(View.VISIBLE);
                mWeightRadioBtn.setChecked(true);
                mUnitRadioBtn.setVisibility(View.GONE);
                unitTv.setVisibility(View.GONE);
                break;
            case 3:
                mWeightRadioBtn.setVisibility(View.VISIBLE);
                weightTv.setVisibility(View.VISIBLE);
                mUnitRadioBtn.setVisibility(View.VISIBLE);
                unitTv.setVisibility(View.VISIBLE);
                mUnitRadioBtn.setChecked(true);
                break;
        }
    }

    public void setNumericItem(Widget widget, boolean showChangeUnitInCycleBtn, boolean showAddRejectsBtn) {
        mShowChangeUnitInCycle = showChangeUnitInCycleBtn;
        mShowAddRejectsBtn = showAddRejectsBtn;
        switch (widget.getTargetScreen()) {
            case REPORT_REJECT_TAG:
                setupNumericRejectItem(widget);
                break;
            case REPORT_UNIT_CYCLE_TAG:
                setupNumericCycleItem(widget);
                break;
            default:
                initNumericDiplayLy(widget);
                break;
        }
    }


    private void setupNumericCycleItem(Widget widget) {
        if (widget.getEditStep() == 0) {
            initNumericDiplayLy(widget);
        } else if (widget.getEditStep() == 1 && widget.getTargetScreen().equals(REPORT_UNIT_CYCLE_TAG)) {
            initEditCycleLy(widget);
            setKeyBoard(mEditCycleEt, new String[]{"."}, mEditCycleReportBtn);
        }

        closeKeyboard(widget.getEditStep());

    }

    private void setupNumericRejectItem(Widget widget) {
        if (widget.getEditStep() == 0) {
            initNumericDiplayLy(widget);
        } else if (widget.getEditStep() == 1 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep1(widget);
            setKeyBoard(mEditNumberEt, new String[]{".", "-"}, mStep1NextBtn);
        } else if (widget.getEditStep() == 2 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep2(widget);
        }
        closeKeyboard(widget.getEditStep());
    }

    private void setKeyBoard(final EditText editText, String[] complementChars, final View nextBtn) {
        if (mOnKeyboardManagerListener != null) {
            mOnKeyboardManagerListener.onOpenKeyboard(editText.getContext(), new SingleLineKeyboard.OnKeyboardClickListener() {
                @Override
                public void onKeyboardClick(String text) {
                    editText.setText(text);
                    enableNextBtn(text, nextBtn, editText);
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

    public void enableNextBtn(String text, View nextBtn, EditText editText) {
        if (!text.isEmpty()) {
            nextBtn.setBackgroundColor(editText.getContext().getResources().getColor(R.color.blue1));
        } else {
            nextBtn.setBackgroundColor(editText.getContext().getResources().getColor(R.color.grey_lite));
        }
    }

    private void initEditCycleLy(final Widget widget) {
        mDisplayLy.setVisibility(View.GONE);
        mEditStep1Ly.setVisibility(View.GONE);
        mEditStep2Ly.setVisibility(View.GONE);
        mEditCycleLy.setVisibility(View.VISIBLE);
        mEditCycleReportBtn.setEnabled(true);
//        mEditCycleEt.requestFocus();
//        KeyboardUtils.showKeyboard(context);

        mEditCycleCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                mEditCycleEt.clearFocus();
                setupNumericCycleItem(widget);
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
            }
        });
        mEditCycleReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditCycleEt.getText().toString().isEmpty()) {
                    mEditCycleReportBtn.setEnabled(false);
                    widget.setEditStep(0);
                    mDashboardCentralContainerListener.onReportCycleUnit(String.valueOf(Double.parseDouble(mEditCycleEt.getText().toString())));
                    mEditCycleEt.clearFocus();
                    setupNumericCycleItem(widget);
                    mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
                }
            }
        });
    }

    private String getCycleReportValue(Double value) {
        if (value <= 0) {
            return String.valueOf(1);
        }
        float max = PersistenceManager.getInstance().getMaxUnitReport();
        if (value > max && PersistenceManager.getInstance().getUnitsInCycleType() <= 1) {
            return String.valueOf(max);
        }
        return String.valueOf(value);
    }

    private void initEditNumericStep2(final Widget widget) {
        mDisplayLy.setVisibility(View.GONE);
        mEditStep1Ly.setVisibility(View.GONE);
        mEditCycleLy.setVisibility(View.GONE);
        mEditStep2Ly.setVisibility(View.VISIBLE);

        if (!PersistenceManager.getInstance().getDisplayRejectFactor()) {
            mSpinner1BtnRv.setVisibility(View.GONE);
        }
        setNumericReportRejctStep2Spinners();

        mStep2CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(1);
                setupNumericRejectItem(widget);
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
            }
        });
        mStep2ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                mDashboardCentralContainerListener.onReportReject(
                        mEditNumberEt.getText().toString(),
                        mUnitRadioBtn.isChecked(),
                        mSelectedCauseId,
                        mSelectedReasonId);
                setupNumericRejectItem(widget);
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
            }
        });
    }

    private void setNumericReportRejctStep2Spinners() {
        if (mReportFieldsForMachine != null) {

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(mSpinner1.getContext(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            mSpinner1.setAdapter(reasonSpinnerArrayAdapter);
            mSpinner1.getBackground().setColorFilter(ContextCompat.getColor(mSpinner1.getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
                    reasonSpinnerArrayAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getDisplayRejectFactor()) {
                final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(mSpinner2.getContext(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
                causeSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
                mSpinner2.setAdapter(causeSpinnerArrayAdapter);
                mSpinner2.getBackground().setColorFilter(ContextCompat.getColor(mSpinner2.getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

                mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (mReportFieldsForMachine.getRejectCauses().size() > 0) {
                            mSelectedCauseId = mReportFieldsForMachine.getRejectCauses().get(position).getId();
                        }
                        causeSpinnerArrayAdapter.setTitle(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {
                mSelectedCauseId = 0;
            }
        }
    }

    private void initEditNumericStep1(final Widget widget) {

        mDisplayLy.setVisibility(View.GONE);
        mEditStep1Ly.setVisibility(View.VISIBLE);
        mEditStep2Ly.setVisibility(View.GONE);
        mEditCycleLy.setVisibility(View.GONE);
//        mEditNumberEt.requestFocus();
//        KeyboardUtils.showKeyboard(context);

        mStep1CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                setupNumericRejectItem(widget);
                mEditNumberEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
            }
        });
        mStep1NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditNumberEt.getText().toString().isEmpty() &&
                        !mEditNumberEt.getText().toString().equals("0")) {
                    widget.setEditStep(2);
                    setupNumericRejectItem(widget);
                    mEditNumberEt.clearFocus();
                    mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
                }
            }
        });
    }

    private void initNumericDiplayLy(final Widget widget) {
        mDisplayLy.setVisibility(View.VISIBLE);
        mEditStep1Ly.setVisibility(View.GONE);
        mEditStep2Ly.setVisibility(View.GONE);
        mEditCycleLy.setVisibility(View.GONE);
        mEditNumberEt.setText("");
        mEditCycleEt.setText("");
//        mDivider.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.MarginLayoutParams mItemViewParams1;
//                mItemViewParams1 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
//                mItemViewParams1.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
//                mDivider.requestLayout();
//            }
//        });
//
        setSizes(mParentLayout);
        String nameByLang1 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang1);
        mSubtitle.setVisibility(View.INVISIBLE);
        if (widget.getCurrentValue().contains(".")) {
            try {
                mValue.setText(new DecimalFormat("#.##").format(Double.parseDouble(widget.getCurrentValue())));
            } catch (Exception e) {
                mValue.setText(widget.getCurrentValue());
            }
        } else {
            mValue.setText(widget.getCurrentValue());
        }
        mValue.setSelected(true);

        if (widget.getTargetScreen() != null && widget.getTargetScreen().length() > 0) {
            mEditLy.setVisibility(View.VISIBLE);

            switch (widget.getTargetScreen()) {
                case REPORT_REJECT_TAG:
                    if (mShowAddRejectsBtn) {
                        addEditClickListener(widget);
                        mEditLy.setVisibility(View.VISIBLE);
                        mEditBtn.setText(mEditBtn.getContext().getResources().getString(R.string.add_rejects));
                    } else {
                        mEditLy.setVisibility(View.GONE);
                    }
                    break;
                case REPORT_UNIT_CYCLE_TAG:
                    if (mShowChangeUnitInCycle) {
                        mEditLy.setVisibility(View.VISIBLE);
                        String txt = mEditBtn.getContext().getResources().getString(R.string.report_cycle_units);
                        txt = txt.replace(mEditBtn.getContext().getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units"));
                        mEditBtn.setText(txt);
                        addEditClickListener(widget);
                    } else {
                        mEditLy.setVisibility(View.GONE);
                    }
                    break;
            }


            addEditClickListener(widget);

        } else {

            mEditLy.setVisibility(View.GONE);
        }
    }

    private void addEditClickListener(final Widget widget) {
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editAction(widget);
            }
        });

        mEditIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editAction(widget);
            }
        });
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

    public void editAction(Widget widget) {
        switch (widget.getTargetScreen()) {
            case REPORT_REJECT_TAG:
                widget.setEditStep(1);
                setupNumericRejectItem(widget);
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
                break;
            case REPORT_UNIT_CYCLE_TAG:
                widget.setEditStep(1);
                setupNumericCycleItem(widget);
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
                break;
            default:
                mDashboardCentralContainerListener.onOpenNewFragmentInCentralDashboardContainer(widget.getTargetScreen());
                break;
        }
    }

}
