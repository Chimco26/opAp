package com.operatorsapp.dialogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.adapters.LegendDialogAdapter;


public class LegendDialog extends DialogFragment implements View.OnClickListener {

    public static LegendDialog newInstants(){
        return new LegendDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_legend, container, false);

        view.findViewById(R.id.DL_close_btn).setOnClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.DL_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LegendDialogAdapter adapter = new LegendDialogAdapter(getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DL_close_btn:
                dismiss();
                break;
        }
    }
}
