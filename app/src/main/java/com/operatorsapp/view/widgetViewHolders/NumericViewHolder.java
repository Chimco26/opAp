package com.operatorsapp.view.widgetViewHolders;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.KeyboardUtils;

import me.grantland.widget.AutofitTextView;

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
    private View mEditIc;
    private View mDisplayLy;
    private View mEditStep1Ly;
    private EditText mEditNumberEt;
    private RadioButton mUnitRadioBtn;
//    private RadioButton mWeightRadioBtn;
    private View mStep1CancelBtn;
    private TextView mStep1NextBtn;
    private RelativeLayout mParentLayout;
    private View mDivider;
    private AutofitTextView mTitle;
    private AutofitTextView mSubtitle;
    private TextView mValue;
    private TextView mChangeMaterial;
    private Activity mContext;
    private DashboardCentralContainerListener mDashboardCentralContainerListener;
    private int mHeight;
    private int mWidth;
    private int mSelectedCauseId;
    private int mSelectedReasonId;
    private ReportFieldsForMachine mReportFieldsForMachine;

    public NumericViewHolder(View itemView, Activity activity, DashboardCentralContainerListener listener,
                             ReportFieldsForMachine reportFieldsForMachine, int height, int width) {
        super(itemView);

        mContext = activity;
        mDashboardCentralContainerListener = listener;
        mReportFieldsForMachine = reportFieldsForMachine;
        mHeight = height;
        mWidth = width;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.numeric_widget_title);
        mSubtitle = itemView.findViewById(R.id.numeric_widget_subtitle);
        mValue = itemView.findViewById(R.id.numeric_widget_value);
        mChangeMaterial = itemView.findViewById(R.id.numeric_widget_change_material);
        mEditIc = itemView.findViewById(R.id.numeric_widget_edit_ic);
        mDisplayLy = itemView.findViewById(R.id.NWC_display_ly);

        mEditStep1Ly = itemView.findViewById(R.id.NWC_edit_step_1_ly);
        mEditNumberEt = itemView.findViewById(R.id.NWC_edit_number_et);
        mUnitRadioBtn = itemView.findViewById(R.id.NWC_edit_unit_btn);
//        mWeightRadioBtn = itemView.findViewById(R.id.NWC_edit_weight_btn);
        mStep1CancelBtn = itemView.findViewById(R.id.NWC_edit_cancel_btn);
        mStep1NextBtn = itemView.findViewById(R.id.NWC_edit_next_btn);

        mEditStep2Ly = itemView.findViewById(R.id.NWC_edit_step_2_ly);
        mSpinner1 = itemView.findViewById(R.id.NWC_reason_spinner);
        mSpinner1BtnRv = itemView.findViewById(R.id.NWC_reason2_spinner_rl);
        mSpinner2 = itemView.findViewById(R.id.NWC_reason2_spinner);
        mStep2CancelBtn = itemView.findViewById(R.id.NWC_edit_step2_cancel_btn);
        mStep2ReportBtn = itemView.findViewById(R.id.NWC_edit_step2_next_btn);

        mEditCycleLy = itemView.findViewById(R.id.NWC_edit_quantity_ly);
        mEditCycleEt = itemView.findViewById(R.id.NWC_edit_quantity_value_et);
        mEditCycleCancelBtn = itemView.findViewById(R.id.NWC_edit_quantity_cancel_btn);
        mEditCycleReportBtn = itemView.findViewById(R.id.NWC_edit_quantity_next_btn);

    }

    public void setNumericItem(Widget widget) {
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
        }
    }

    private void setupNumericRejectItem(Widget widget) {
        if (widget.getEditStep() == 0) {
            initNumericDiplayLy(widget);
        } else if (widget.getEditStep() == 1 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep1(widget);
        } else if (widget.getEditStep() == 2 && widget.getTargetScreen().equals(REPORT_REJECT_TAG)) {
            initEditNumericStep2(widget);
        }
    }

    private void initEditCycleLy(final Widget widget) {
        mDisplayLy.setVisibility(View.GONE);
        mEditStep1Ly.setVisibility(View.GONE);
        mEditStep2Ly.setVisibility(View.GONE);
        mEditCycleLy.setVisibility(View.VISIBLE);
        mEditCycleEt.requestFocus();
        KeyboardUtils.showKeyboard(mContext);

        mEditCycleCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                setupNumericCycleItem(widget);
                mEditCycleEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
            }
        });
        mEditCycleReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.setEditStep(0);
                mDashboardCentralContainerListener.onReportCycleUnit(
                        mEditCycleEt.getText().toString());
                setupNumericCycleItem(widget);
                mEditCycleEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());

            }
        });
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

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner1.setAdapter(reasonSpinnerArrayAdapter);
            mSpinner1.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

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
                final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(mContext, R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
                causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner2.setAdapter(causeSpinnerArrayAdapter);
                mSpinner2.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

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
        mEditNumberEt.requestFocus();
        KeyboardUtils.showKeyboard(mContext);

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
                widget.setEditStep(2);
                setupNumericRejectItem(widget);
                mEditNumberEt.clearFocus();
                mDashboardCentralContainerListener.onScrollToPosition(getAdapterPosition());
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
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams1;
                mItemViewParams1 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                mItemViewParams1.setMargins(0, (int) (mParentLayout.getHeight() * 0.4), 0, 0);
                mDivider.requestLayout();
            }
        });

        setSizes(mParentLayout);
        String nameByLang1 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang1);
        mSubtitle.setVisibility(View.INVISIBLE);
        mValue.setText(widget.getCurrentValue());
        mValue.setSelected(true);

        mChangeMaterial.setVisibility(View.INVISIBLE);

        if (widget.getTargetScreen() != null && widget.getTargetScreen().length() > 0) {

            mEditIc.setVisibility(View.VISIBLE);

            mEditIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
            });

        } else {

            mEditIc.setVisibility(View.GONE);
        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
