package com.operatorsapp.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.DropDownAdapter;

import java.util.ArrayList;

/**
 * Created by use on 30/11/2015.
 */
public class BaseSearchView extends FrameLayout implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener {

    private TextInputEditText text;
    private DropDownAdapter adapter;
    private TextChangedListener textChangedListener;
    private ArrayList<Machine> searchStrings;
    private ListView list;
    private LinearLayout listLayout;

    public void setText(String search) {
        text.setText(search);
    }

    public interface TextChangedListener {
        void onTextChanged(CharSequence s);
    }


    public BaseSearchView(Context context) {
        super(context);
        init(context);
    }

    public BaseSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnTextChangedListener(TextChangedListener listener) {
        textChangedListener = listener;
    }


    private void init(Context context) {
        searchStrings = new ArrayList<>();
        View root = LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, false);
        text = (TextInputEditText) root.findViewById(R.id.search_view_text);
        list = (ListView) root.findViewById(R.id.search_view_list);
        listLayout = (LinearLayout) root.findViewById(R.id.search_view_list_layout);
        addView(root);

        text.addTextChangedListener(this);
        text.setOnEditorActionListener(this);
    }


    public void clear() {
        adapter = null;
        searchStrings.clear();

    }

    public void clearText() {
        text.setText("");
    }

    public void setResults(ArrayList<Machine> results) {
        searchStrings = results;
    }

    public void addResult(Machine result) {
        searchStrings.add(result);

    }

    public void showResults() {

        ArrayList<Machine> list = new ArrayList<>();
        list.addAll(searchStrings);
        adapter = new DropDownAdapter(getContext(), list, text.getText().toString());

        this.listLayout.setVisibility(View.VISIBLE);
        this.list.setAdapter(adapter);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() >= 1) {
            if (textChangedListener != null)
                textChangedListener.onTextChanged(s);
        } else if (s.toString().length() == 0) {
            dismissDropDown();
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        list.setOnItemClickListener(listener);
    }

    public Machine getItem(int position) {
        return (Machine) adapter.getItem(position);
    }

    public void dismissDropDown() {
        listLayout.setVisibility(View.GONE);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


        }

    }

}
