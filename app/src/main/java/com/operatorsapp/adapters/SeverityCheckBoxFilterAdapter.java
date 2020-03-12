package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.SelectableString;
import com.operatorsapp.R;

import java.util.ArrayList;

public class SeverityCheckBoxFilterAdapter extends RecyclerView.Adapter<SeverityCheckBoxFilterAdapter.ViewHolder> {

    private final ArrayList<SelectableString> mList;
    private final boolean mSelectAllOption;
    private final boolean mAtLeastOneSelected;
    private final boolean mEditable;
    private SeverityCheckBoxFilterAdapterListener mListener;

    public SeverityCheckBoxFilterAdapter(ArrayList<SelectableString> list, SeverityCheckBoxFilterAdapterListener listener, boolean selectAllOption, boolean atLeastOneSelected, boolean editable) {
        mList = list;
        mListener = listener;
        mSelectAllOption = selectAllOption;
        mAtLeastOneSelected = atLeastOneSelected;
        mEditable = editable;
    }

    @NonNull
    @Override
    public SeverityCheckBoxFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new SeverityCheckBoxFilterAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box_horizontal_match_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.mCheckBox.setText(mList.get(position).getString());
        viewHolder.mCheckBox.setTextColor(mList.get(position).getColor());
        setCheckBoxColor(viewHolder.mCheckBox, mList.get(position).getColor(), mList.get(position).getColor());
        viewHolder.mCheckBox.setChecked(mList.get(position).isSelected());
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = ((CheckBox) view).isChecked();
                if (mAtLeastOneSelected){
                    if (!check || !mEditable){
                        ((CheckBox) view).setChecked(!check);
                        return;
                    }else {
                        updateAll(false);
                    }
                }
                mList.get(position).setSelected(check);
                if (mList.get(position).getId().equals(SelectableString.SELECT_ALL_ID)) {
                    updateAll(check);
                } else {
                    mList.get(position).setSelected(check);
                    if (mSelectAllOption) {
                        updateSelectAllItem(mList);
                    }
                }
                mListener.onItemCheck(mList.get(position));
                notifyDataSetChanged();
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
        for (SelectableString string : mList) {
            string.setSelected(b);
        }
    }    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatCheckBox mCheckBox;

        ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.ICB_check_box);

        }

    }

    public interface SeverityCheckBoxFilterAdapterListener {
        void onItemCheck(SelectableString selectableString);
    }
}
