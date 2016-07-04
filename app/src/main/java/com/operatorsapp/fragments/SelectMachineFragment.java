package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operatorsapp.R;
import com.operatorsapp.view.BaseSearchView;

import java.util.ArrayList;

public class SelectMachineFragment extends Fragment implements BaseSearchView.TextChangedListener, AdapterView.OnItemClickListener {

    private static final String MACHINES_LIST = "machines_list";
    private BaseSearchView mMachineIdName;
    private RelativeLayout mGoBtn;
    private ArrayList<Machine> mMachinesList = new ArrayList<>();

    public static SelectMachineFragment newInstance(ArrayList<Machine> machines) {

        Bundle args = new Bundle();
        args.putSerializable(MACHINES_LIST, machines);

        SelectMachineFragment fragment = new SelectMachineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMachinesList = (ArrayList<Machine>) getArguments().getSerializable(MACHINES_LIST);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_select_machine, container, false);

        final LinearLayout space = (LinearLayout) rootView.findViewById(R.id.layout_space);
        final TextView title = (TextView) rootView.findViewById(R.id.select_machine_title);
        space.post(new Runnable() {
            @Override
            public void run() {
                title.setY(space.getHeight() / 3);
                mMachineIdName.setY(space.getHeight() / 4);
                mGoBtn.setY(space.getHeight() / 4);
            }
        });
        mMachineIdName = (BaseSearchView) rootView.findViewById(R.id.machine_id_name);
        mMachineIdName.setOnTextChangedListener(this);
        mMachineIdName.setOnItemClickListener(this);

        mGoBtn = (RelativeLayout) rootView.findViewById(R.id.goBtn);
        mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setActionBar();
        return rootView;
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString s = new SpannableString(getString(R.string.login_screen_title));
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_view, null);
            ((TextView) view.findViewById(R.id.title)).setText(s);
            // used custom view to add font style to the title
            actionBar.setCustomView(view);
        }
    }


    @Override
    public void onTextChanged(CharSequence s) {
        mMachineIdName.clear();
        for (Machine machine : mMachinesList) {
            mMachineIdName.addResult(machine);
        }
        mMachineIdName.showResults();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Machine machine = mMachinesList.get(position);
        mMachineIdName.setText(machine.getMachineId() + " - " + machine.getMachineName());
        mMachineIdName.dismissDropDown();
    }
}
