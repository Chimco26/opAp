package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.operators.infra.Machine;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.SendReportUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class AutoCompleteAdapter extends ArrayAdapter<Machine> implements Filterable {
    private ArrayList<Machine> mMachines;
    private ArrayList<Machine> machinesResults;


    public AutoCompleteAdapter(Context context, ArrayList<Machine> machines) {
        super(context, 0, machines);
        mMachines = machines;
        machinesResults = new ArrayList<>();
        machinesResults.addAll(mMachines);
    }

    static class ViewHolder {
        TextView text;
    }

    @Override
    public int getCount() {
        return machinesResults.size();
    }

    @Override
    public Machine getItem(int index) {
        try {

            return machinesResults.get(index);

        } catch (IndexOutOfBoundsException e) {

            SendReportUtil.sendAcraExeption(e, "machinesResults size = " + machinesResults.size()
                    + "mMachines size = " + mMachines.size()
                    + "index  = " + index);

            return machinesResults.get(0);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_item_layout, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.search_view_item_text);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        Machine machine;
        try {

            machine = machinesResults.get(position);

        } catch (IndexOutOfBoundsException e) {

            SendReportUtil.sendAcraExeption(e, "machinesResults size = " + machinesResults.size()
                    + "mMachines size = " + mMachines.size()
                    + "position  = " + position);

            machine = machinesResults.get(0);
        }
        int machineId = machine.getId();
        String nameByLang = OperatorApplication.isEnglishLang() ? machine.getMachineEName() : machine.getMachineLName();
        String machineName = nameByLang == null ? "" : nameByLang;
        holder.text.setText(new StringBuilder(machineId).append(" - ").append(machineName));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
//                machinesResults.clear();
//                machinesResults.addAll(mMachines);
                FilterResults filterResults = new FilterResults();
                ArrayList<Machine> tempArray = new ArrayList<>(mMachines);
                Iterator<Machine> iterator = tempArray.iterator();
                while (iterator.hasNext()) {
                    Machine machine = iterator.next();
                    int machineId = machine.getId();
                    String nameByLang = OperatorApplication.isEnglishLang() ? machine.getMachineEName() : machine.getMachineLName();
                    String machineName = nameByLang == null ? "" : nameByLang;
                    if (!(machineId + " - " + machineName).toLowerCase().contains(constraint.toString().toLowerCase())) {
                        iterator.remove();
                    }
                }

                filterResults.values = tempArray;
                filterResults.count = tempArray.size();

//                if (constraint != null) {
//
//                    Iterator<Machine> iterator = machinesResults.iterator();
//                    while (iterator.hasNext()) {
//                        Machine machine = iterator.next();
//                        int machineId = machine.getId();
//                        String nameByLang = OperatorApplication.isEnglishLang() ? machine.getMachineEName() : machine.getMachineLName();
//                        String machineName = nameByLang == null ? "" : nameByLang;
//                        if (!(machineId + " - " + machineName).toLowerCase().contains(constraint.toString().toLowerCase())) {
//                            iterator.remove();
//                        }
//                    }
//
//                    filterResults.values = machinesResults;
//                    filterResults.count = machinesResults.size();
//                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                machinesResults.clear();
                if (results != null && results.count > 0) {
                    //noinspection unchecked
                    machinesResults.addAll((ArrayList<Machine>)results.values);
                    notifyDataSetChanged();
                } else {
                    machinesResults.addAll(mMachines);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
