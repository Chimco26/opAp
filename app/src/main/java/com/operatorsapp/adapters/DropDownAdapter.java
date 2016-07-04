package com.operatorsapp.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operatorsapp.R;

import java.util.List;

public class DropDownAdapter extends ArrayAdapter<Machine> {

    private List<Machine> items;
    private String searchString;


    public DropDownAdapter(Context context, List<Machine> objects, String searchString) {
        super(context, 0, objects);
        items = objects;
        this.searchString = searchString;
    }

    static class ViewHolder {

        TextView text;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Machine getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        Machine item = items.get(position);

        holder.text.setText(item.getMachineId() + " - " + item.getMachineName());

        return convertView;
    }


}
