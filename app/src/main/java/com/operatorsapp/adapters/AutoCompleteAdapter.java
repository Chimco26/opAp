package com.operatorsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.operators.infra.Machine;
import com.operatorsapp.R;

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
        return machinesResults.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_item_layout, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.search_view_item_text);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        Machine machine = machinesResults.get(position);

        int machineId = machine.getId();
        String machineName = machine.getMachineName() == null ? "" : machine.getMachineName();
        holder.text.setText(new StringBuilder(machineId + " - " + machineName));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                machinesResults.clear();
                machinesResults.addAll(mMachines);
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    Iterator<Machine> iterator = machinesResults.iterator();
                    while (iterator.hasNext()) {
                        Machine machine = iterator.next();
                        int machineId = machine.getId();
                        String machineName = machine.getMachineName() == null ? "" : machine.getMachineName();
                        if (!(machineId + " - " + machineName).contains(constraint)) {
                            iterator.remove();
                        }
                    }

                    filterResults.values = machinesResults;
                    filterResults.count = machinesResults.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
