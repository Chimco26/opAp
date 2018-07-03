package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class JobHeadersAdaper extends RecyclerView.Adapter<JobHeadersAdaper.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Header> mHeaders;
    private final HashMap<String, Header> mHashMapHeader;

    private JobHeadersAdaperListener mListener;


    public JobHeadersAdaper(ArrayList<Header> list, HashMap<String, Header> hashMapHeaders, JobHeadersAdaperListener listener, Context context) {

        mListener = listener;

        mContext = context;

        mHeaders = list;

        mHashMapHeader = hashMapHeaders;

    }


    @NonNull
    @Override
    public JobHeadersAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobHeadersAdaper.ViewHolder(inflater.inflate(R.layout.item_product_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobHeadersAdaper.ViewHolder viewHolder, final int position) {

        viewHolder.mTv.setText(mHeaders.get(position).getDisplayName());

        if (mHashMapHeader.get(mHeaders.get(position).getName()).isSelected()) {

            updateView(viewHolder, R.color.blue1, R.color.white);

        }else {

            updateView(viewHolder, R.color.white, R.color.blue1);

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mHashMapHeader.get(mHeaders.get(position).getName()).isSelected()){

                    for (Header header: mHeaders) {
                        mHashMapHeader.get(header.getName()).setSelected(false);
                        updateView(viewHolder, R.color.white, R.color.blue1);
                    }
                }else {

                    for (Header header: mHeaders) {
                        mHashMapHeader.get(header.getName()).setSelected(false);
                        updateView(viewHolder, R.color.white, R.color.blue1);
                    }
                    mHashMapHeader.get(mHeaders.get(position).getName()).setSelected(true);
                    updateView(viewHolder, R.color.blue1, R.color.white);

                }
                notifyDataSetChanged();
                mListener.onHeaderSelected(mHashMapHeader);
            }
        });

    }

    public void updateView(@NonNull ViewHolder viewHolder, int blue1, int white) {
        viewHolder.mTv.setBackgroundColor(mContext.getResources().getColor(blue1));
        viewHolder.mTv.setTextColor(mContext.getResources().getColor(white));
    }

    @Override
    public int getItemCount() {
        if (mHeaders != null) {
            return mHeaders.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv;

        ViewHolder(View itemView) {
            super(itemView);

            mTv = itemView.findViewById(R.id.IPS_search_tv);

        }

    }

    public interface JobHeadersAdaperListener {

        void onHeaderSelected(HashMap<String, Header> mHashMapHeader);
    }
}
