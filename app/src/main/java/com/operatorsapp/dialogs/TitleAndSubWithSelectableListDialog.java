package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.common.SelectableString;
import com.example.common.department.MachinesLineDetail;
import com.operatorsapp.R;
import com.operatorsapp.adapters.CheckBoxFilterAdapter;

import java.util.ArrayList;

import static org.litepal.LitePalApplication.getContext;

public class TitleAndSubWithSelectableListDialog implements View.OnClickListener {
    private final Activity mContext;

    private final String mPositiveBtnTxt;
    private final TitleAndSubWithSelectableListDialogListener mListener;
    private final String mTitle;
    private final String mSubTitle;
    private final ArrayList<MachinesLineDetail> mMachineLineList;
    private AlertDialog mAlarmAlertDialog;
    private RecyclerView mRv;
    private CheckBoxFilterAdapter mAdapter;
    private ArrayList<Integer> mSelectedMachineIds;


    public TitleAndSubWithSelectableListDialog(Activity activity, final TitleAndSubWithSelectableListDialogListener listener
            , String title, String subTitle, String positiveBtn, ArrayList<MachinesLineDetail> arrayList) {
        mContext = activity;
        mTitle = title;
        mSubTitle = subTitle;
        mPositiveBtnTxt = positiveBtn;
        mListener = listener;
        mMachineLineList = arrayList;
    }

    public AlertDialog showTitleAndSubWithSelectableListDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = mContext.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.alert_machine_list, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.AML_text_tv);
        TextView subTitle = view.findViewById(R.id.AML_subtitle_tv);
        TextView positiveBtn = view.findViewById(R.id.AML_positive_btn);

        setRv(view);
        title.setText(mTitle);
        subTitle.setText(mSubTitle);
        if (mPositiveBtnTxt != null) {
            positiveBtn.setText(mPositiveBtnTxt);
        } else {
            positiveBtn.setVisibility(View.GONE);
        }

        builder.setCancelable(true);
        mAlarmAlertDialog = builder.create();

        positiveBtn.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);

        mAlarmAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        return mAlarmAlertDialog;
    }

    private void setRv(View view) {
        mSelectedMachineIds = new ArrayList<>();
        ArrayList<SelectableString> selectableStrings = new ArrayList<>();
        for (MachinesLineDetail machine : mMachineLineList) {
            selectableStrings.add(new SelectableString(machine.getMachineName(), false, String.valueOf(machine.getMachineID()), mContext.getResources().getColor(R.color.blue1)));
        }
        mRv = view.findViewById(R.id.AML_rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new CheckBoxFilterAdapter(selectableStrings, new CheckBoxFilterAdapter.CheckBoxFilterAdapterListener() {
            @Override
            public void onItemCheck(SelectableString selectableString) {
                if (selectableString.isSelected()) {
                    if (!mSelectedMachineIds.contains(selectableString)) {
                        mSelectedMachineIds.add(Integer.valueOf(selectableString.getId()));
                    }
                } else {
                    if (mSelectedMachineIds.contains(Integer.parseInt(selectableString.getId()))) {
                        mSelectedMachineIds.remove((Integer) Integer.parseInt(selectableString.getId()));
                    }
                }
            }
        });
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_cancel:
//                mListener.onClickNegativeBtn();
                mAlarmAlertDialog.dismiss();
                break;
            case R.id.AML_positive_btn:
                ArrayList<MachinesLineDetail> machinesLineDetails = new ArrayList<>();
                for (MachinesLineDetail machinesLineDetail : mMachineLineList) {
                    for (Integer integer : mSelectedMachineIds) {
                        if (integer.equals(machinesLineDetail.getMachineID()))
                            machinesLineDetails.add(machinesLineDetail);
                    }
                }
                mListener.onClickPositiveBtn(machinesLineDetails);
                mAlarmAlertDialog.dismiss();
                break;
        }
    }

    public interface TitleAndSubWithSelectableListDialogListener {

        void onClickPositiveBtn(ArrayList<MachinesLineDetail> machinesLineDetails);

        void onClickNegativeBtn();
    }
}