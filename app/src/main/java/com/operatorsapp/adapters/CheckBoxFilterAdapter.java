package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.common.SelectableString;
import com.operatorsapp.R;

import java.util.ArrayList;

public class CheckBoxFilterAdapter extends RecyclerView.Adapter<CheckBoxFilterAdapter.ViewHolder> {

    private final ArrayList<SelectableString> mFilterList;
    private CheckBoxFilterAdapterListener mCheckBoxFilterAdapterListener;
    private boolean onBind;

    public CheckBoxFilterAdapter(ArrayList<SelectableString> list, CheckBoxFilterAdapterListener checkBoxFilterAdapterListener) {
        mFilterList = list;
        mCheckBoxFilterAdapterListener = checkBoxFilterAdapterListener;
    }

    @NonNull
    @Override
    public CheckBoxFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CheckBoxFilterAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckBoxFilterAdapter.ViewHolder viewHolder, final int position) {
        onBind = true;
        viewHolder.mCheckBox.setText(mFilterList.get(position).getString());
        viewHolder.mCheckBox.setChecked(mFilterList.get(position).isSelected());
        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mFilterList.get(position).getId().equals(SelectableString.SELECT_ALL_ID)) {
                    updateAll(b);
                } else if (!onBind){
                    mFilterList.get(position).setSelected(b);
                    updateSelectAllItem(mFilterList);
                }
                mCheckBoxFilterAdapterListener.onItemCheck(mFilterList.get(position));

            }
        });
        onBind = false;
    }

    private void updateSelectAllItem(ArrayList<SelectableString> selectableStrings) {
        boolean b = selectableStrings.get(1).isSelected();
        int counter = 1;
        for (SelectableString selectableStrings1 : selectableStrings.subList(1, selectableStrings.size())) {
            if (selectableStrings1.isSelected() && b
                    || !selectableStrings1.isSelected() && !b){
                counter++;
            }else {
                break;
            }
        }
        if (counter == selectableStrings.size()){
            selectableStrings.get(0).setSelected(b);
        }
    }

    private void updateAll(boolean b) {
        for (SelectableString string : mFilterList) {
            string.setSelected(b);
        }
    }

    @Override
    public int getItemCount() {
        if (mFilterList != null) {
            return mFilterList.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.ICB_check_box);

        }

    }

    public interface CheckBoxFilterAdapterListener {
        void onItemCheck(SelectableString selectableString);
    }
}
