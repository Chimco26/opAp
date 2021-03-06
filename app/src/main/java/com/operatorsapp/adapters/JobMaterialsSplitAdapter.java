package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Material;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;

import java.util.List;

public class JobMaterialsSplitAdapter extends RecyclerView.Adapter<JobMaterialsSplitAdapter.ViewHolder> {

    private final List<Material> mMaterials;


    public JobMaterialsSplitAdapter(List<Material> list) {

        mMaterials = list;
    }


    @NonNull
    @Override
    public JobMaterialsSplitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobMaterialsSplitAdapter.ViewHolder(inflater.inflate(R.layout.item_splits_job_action_material, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobMaterialsSplitAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mNameTv.setText(mMaterials.get(position).getName());

        viewHolder.mCatalogTv.setText(mMaterials.get(position).getCatalog());

        String str = mMaterials.get(position).getAmount() + " ";
        if (mMaterials.get(position).getAmountUnits().contains("1")){
            str += PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units");
//            str += context.getString(R.string.units);
        }else {
            str += viewHolder.itemView.getContext().getString(R.string.kg);
        }
        viewHolder.mAmountTv.setText(str);
    }

    @Override
    public int getItemCount() {
        if (mMaterials != null) {
            return mMaterials.size();
        } else
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mNameTv;

        private final TextView mCatalogTv;

        private final TextView mAmountTv;

        ViewHolder(View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.ISJAM_tv1);

            mCatalogTv = itemView.findViewById(R.id.ISJAM_tv2);

            mAmountTv = itemView.findViewById(R.id.ISJAM_tv3);

        }

    }
}
