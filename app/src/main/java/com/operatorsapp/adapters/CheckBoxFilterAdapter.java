package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.common.SelectableString;
import com.operatorsapp.R;

import java.util.ArrayList;

public class CheckBoxFilterAdapter extends RecyclerView.Adapter<CheckBoxFilterAdapter.ViewHolder> {

    private final ArrayList<SelectableString> mFilterList;
    private CheckBoxFilterAdapterListener mCheckBoxFilterAdapterListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.mCheckBox.setText(mFilterList.get(position).getString());
        viewHolder.mCheckBox.setTextColor(mFilterList.get(position).getColor());
        setCheckBoxColor(viewHolder.mCheckBox, mFilterList.get(position).getColor(), mFilterList.get(position).getColor());
        viewHolder.mCheckBox.setChecked(mFilterList.get(position).isSelected());
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = ((CheckBox) view).isChecked();
                mFilterList.get(position).setSelected(check);
                if (mFilterList.get(position).getId().equals(SelectableString.SELECT_ALL_ID)) {
                    updateAll(check);
                } else {
                    mFilterList.get(position).setSelected(check);
                    updateSelectAllItem(mFilterList);
                }
                mCheckBoxFilterAdapterListener.onItemCheck(mFilterList.get(position));
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private static void setCheckBoxColor(AppCompatCheckBox checkBox, int uncheckedColor, int checkedColor) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}  // checked
                },
                new int[]{
                        uncheckedColor,
                        checkedColor
                }
        );
        checkBox.setSupportButtonTintList(colorStateList);

    }

    private void updateSelectAllItem(ArrayList<SelectableString> selectableStrings) {
        boolean b = selectableStrings.get(1).isSelected();
        int counter = 0;
        boolean onlyOneUnselected = false;
        for (SelectableString selectableStrings1 : selectableStrings.subList(1, selectableStrings.size())) {
            if (selectableStrings1.isSelected() && b) {
                counter++;
            } else {
                onlyOneUnselected = true;
                break;
            }
        }
        if (counter == selectableStrings.size() - 1) {
            selectableStrings.get(0).setSelected(b);
        } else if (onlyOneUnselected) {
            selectableStrings.get(0).setSelected(false);
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

        private final AppCompatCheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.ICB_check_box);

        }

    }

    public interface CheckBoxFilterAdapterListener {
        void onItemCheck(SelectableString selectableString);
    }
}
