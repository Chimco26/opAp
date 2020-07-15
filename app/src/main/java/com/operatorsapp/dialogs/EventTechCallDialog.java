package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.common.StopLogs.Event;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.ChangeLang;

import java.util.ArrayList;
import java.util.List;

public class EventTechCallDialog extends Dialog {

    private final List<Technician> mTechnicianList;
    private final EventTechCallDialogListener mListener;
    private final Event mEvent;
    private TextView mEventStartTimeTv;
    private TextView mSendCall;
    private TextView mMachineNameTv;
    private TextView mEventReasonTv;
    private Spinner mTechSppiner;
    private LinearLayout mTechSppinerFrame;
    private EditText mDescription;
    private int mSelectedTech = -1;


    public EventTechCallDialog(@NonNull Context context, Event event, EventTechCallDialogListener listener) {
        super(context);
        mTechnicianList = PersistenceManager.getInstance().getTechnicianList();
        mEvent = event;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_tech_call_dialog);

        mMachineNameTv = findViewById(R.id.ETCD_machine_name_tv);
        mEventReasonTv = findViewById(R.id.ETCD_stop_event_reason_tv);
        mEventStartTimeTv = findViewById(R.id.ETCD_event_start_time_tv);
        mTechSppiner = findViewById(R.id.ETCD_select_tech_spnr);
        mTechSppinerFrame = findViewById(R.id.ETCD_select_tech_frame);
        mDescription = findViewById(R.id.ETCD_description_et);
        mSendCall = findViewById(R.id.ETCD_apply_tv);

        mEventReasonTv.setText(mEvent.getEventTitle());
        mEventStartTimeTv.setText(mEvent.getEventTime());
        mMachineNameTv.setText(PersistenceManager.getInstance().getCurrentLang().equals("en") ? mEvent.getEventSubTitleEname() : mEvent.getEventSubTitleLname());

        mSendCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        setTechniciansSpinner();
    }

    private void checkFields() {
        if (mSelectedTech < 0){
            mTechSppinerFrame.setBackgroundColor(Color.RED);
        }else {
            mListener.onNewCallPressed(mTechnicianList.get(mSelectedTech), mEvent, mDescription.getText().toString());
        }
    }

    private void setTechniciansSpinner() {
        ArrayList<String> technicianArray = new ArrayList<>();
        if (mTechnicianList != null) {
            technicianArray.add(getContext().getResources().getString(R.string.select_technician));
            for (Technician technician : mTechnicianList) {
                technicianArray.add(ChangeLang.defaultIsEng() ? technician.getEName() : technician.getLName());
            }
        }
        mTechSppiner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, technicianArray));
        mTechSppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTech = position -1;
                mTechSppinerFrame.setBackgroundColor(getContext().getResources().getColor(R.color.grey_lite));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public interface EventTechCallDialogListener{
        void onNewCallPressed(Technician technician, Event event, String additionalText);
    }
}
