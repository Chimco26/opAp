package com.operatorsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.QCModels.TestDetailsResponse;
import com.operatorsapp.R;

import java.util.ArrayList;

public class MultiEditTypeAdapter extends RecyclerView.Adapter<MultiEditTypeAdapter.ViewHolder> {

    private ArrayList list;

    public MultiEditTypeAdapter() {
    }

    @NonNull
    @Override
    public MultiEditTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case TestDetailsResponse
                    .FIELD_TYPE_BOOLEAN:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
            case TestDetailsResponse
                    .FIELD_TYPE_NUM:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
            case TestDetailsResponse
                    .FIELD_TYPE_TEXT:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
            case TestDetailsResponse
                    .FIELD_TYPE_DATE:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
            case TestDetailsResponse
                    .FIELD_TYPE_TIME:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));


            default:
                return new MultiEditTypeAdapter.ViewHolder(inflater.inflate(R.layout.item_check_box, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MultiEditTypeAdapter.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);

        switch (type) {

            case TestDetailsResponse
                    .FIELD_TYPE_BOOLEAN:
                break;
            case TestDetailsResponse
                    .FIELD_TYPE_NUM:
                break;

            case TestDetailsResponse
                    .FIELD_TYPE_TEXT:
                break;

            case TestDetailsResponse
                    .FIELD_TYPE_DATE:
                break;

            case TestDetailsResponse
                    .FIELD_TYPE_TIME:
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);

        }

    }

    public interface MultiEditTypeAdapterListener {
        void onItemCheck();
    }
}
