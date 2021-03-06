package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Action;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class JobActionsAdapter extends RecyclerView.Adapter<JobActionsAdapter.ViewHolder> {

    private final ArrayList<Action> mActions;

    private JobActionsAdapterListener mListener;


    public JobActionsAdapter(List<Action> list, JobActionsAdapterListener listener) {

        mListener = listener;

        mActions = (ArrayList<Action>) list;
    }


    @NonNull
    @Override
    public JobActionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobActionsAdapter.ViewHolder(inflater.inflate(R.layout.item_job_action_actions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobActionsAdapter.ViewHolder viewHolder, final int position) {

        updateCheckBox(viewHolder, viewHolder.getAdapterPosition());

        viewHolder.mTv.setText(mActions.get(viewHolder.getAdapterPosition()).getText());

        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mActions.get(viewHolder.getAdapterPosition()).setIsSelected(isChecked);

                updateCheckBox(viewHolder, viewHolder.getAdapterPosition());

                mListener.onActionChecked(mActions.get(viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.mOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onOpenActionDetails(mActions.get(position));
            }
        });
    }

    private void updateCheckBox(@NonNull ViewHolder viewHolder, int position) {
        if (mActions.get(position).getIsSelected()){

            viewHolder.mCheckBox.setChecked(true);

        }else {

            viewHolder.mCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (mActions != null) {
            return mActions.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox mCheckBox;

        private final TextView mTv;

        private final View mOpenBtn;

        ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.IJAA_checkbox);

            mTv = itemView.findViewById(R.id.IJAA_tv);

            mOpenBtn = itemView.findViewById(R.id.IJAA_open_btn);

        }

    }

    public interface JobActionsAdapterListener {

        void onActionChecked(Action action);

        void onOpenActionDetails(Action action);
    }
}
