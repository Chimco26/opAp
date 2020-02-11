package com.operatorsapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.server.responses.Notification;

import java.util.ArrayList;

public class NotificationsTemplateAdapter extends RecyclerView.Adapter<NotificationsTemplateAdapter.ViewHolder> {


    private final TemplateAdapterListener mListener;
    private ArrayList<Notification> mTemplateList;

    public NotificationsTemplateAdapter(ArrayList<Notification> mTemplateList, TemplateAdapterListener mListener) {
        this.mTemplateList = mTemplateList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NotificationsTemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        return new NotificationsTemplateAdapter.ViewHolder(inflater.inflate(R.layout.item_notification_template, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsTemplateAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mTv.setText(mTemplateList.get(position).getmBody(viewHolder.itemView.getContext()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTemplateClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTemplateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv;

        ViewHolder(View itemView) {
            super(itemView);

            mTv = itemView.findViewById(R.id.item_template_text_tv);


        }

    }

    public interface TemplateAdapterListener{
        void onTemplateClicked(int position);
    }
}
