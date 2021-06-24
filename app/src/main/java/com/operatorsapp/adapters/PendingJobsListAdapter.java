package com.operatorsapp.adapters;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.text.BidiFormatter;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextDirectionHeuristics;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PendingJobsListAdapter extends RecyclerView.Adapter<PendingJobsListAdapter.ViewHolder> {

    private static final String PRODUCT_IMAGE_PATH = "productimagepath";
    private static final String ID_NAME = "ID";
    private static final String ERP_JOB_ID_NAME = "ERPJobID";
    private static final String CATALOG_ID = "ProductCatalogID";
    private final ArrayList<PendingJob> mPandingjobs;
    private final String[] mOrderedHederasKey;

    private PendingJobsAdapter.PendingJobsAdapterListener mListener;


    public PendingJobsListAdapter(ArrayList<PendingJob> list, String[] orderedHederasKey, PendingJobsAdapter.PendingJobsAdapterListener listener) {

        mListener = listener;
        mPandingjobs = list;
        mOrderedHederasKey = orderedHederasKey;
    }


    @NonNull
    @Override
    public PendingJobsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PendingJobsListAdapter.ViewHolder(inflater.inflate(R.layout.item_job_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingJobsListAdapter.ViewHolder viewHolder, final int position) {

        ArrayList<Property> properties = (ArrayList<Property>) mPandingjobs.get(position).getProperties();
        HashMap<String, String> propertiesHashMap = listToHashMap(properties);

        setImageOrText(propertiesHashMap, 0, viewHolder.m1Img, viewHolder.m1Tv, viewHolder.m1Rl);
        setImageOrText(propertiesHashMap, 1, viewHolder.m2Img, viewHolder.m2Tv, viewHolder.m2Rl);
        setImageOrText(propertiesHashMap, 2, viewHolder.m3Img, viewHolder.m3Tv, viewHolder.m3Rl);
        setImageOrText(propertiesHashMap, 3, viewHolder.m4Img, viewHolder.m4Tv, viewHolder.m4Rl);
        setImageOrText(propertiesHashMap, 4, viewHolder.m5Img, viewHolder.m5Tv, viewHolder.m5Rl);
        setImageOrText(propertiesHashMap, 5, viewHolder.m6Img, viewHolder.m6Tv, viewHolder.m6Rl);
        setImageOrText(propertiesHashMap, 6, viewHolder.m7Img, viewHolder.m7Tv, viewHolder.m7Rl);
        setImageOrText(propertiesHashMap, 7, viewHolder.m8Img, viewHolder.m8Tv, viewHolder.m8Rl);
        setImageOrText(propertiesHashMap, 8, viewHolder.m9Img, viewHolder.m9Tv, viewHolder.m9Rl);
        setImageOrText(propertiesHashMap, 9, viewHolder.m10Img, viewHolder.m10Tv, viewHolder.m10Rl);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPendingJobSelected(mPandingjobs.get(position));

            }
        });
    }

    private void setImageOrText(HashMap<String, String> propertiesHashMap, int i, ImageView imageView, TextView textView, View parent) {
        if(mOrderedHederasKey.length <= i){
            parent.setVisibility(View.GONE);
//            imageView.setVisibility(View.GONE);
//            textView.setVisibility(View.GONE);
            return;
        }

        if (mOrderedHederasKey[i] != null && mOrderedHederasKey[i].toLowerCase().equals(PRODUCT_IMAGE_PATH)) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[i]), imageView);
            textView.setText("");
        } else if(mOrderedHederasKey[i] != null){
            ImageLoader.getInstance().displayImage("", imageView);
            if (mOrderedHederasKey[i].equals(ID_NAME) || mOrderedHederasKey[i].equals(ERP_JOB_ID_NAME) || mOrderedHederasKey[i].equals(CATALOG_ID)) {
                textView.setText(propertiesHashMap.get(mOrderedHederasKey[i]));
            }else {

                String s = propertiesHashMap.get(mOrderedHederasKey[i]);

                textView.setText(s);
            }
        }else {
            ImageLoader.getInstance().displayImage("", imageView);
            textView.setText("");
            parent.setVisibility(View.GONE);
        }
    }


    private String toLineAtIndex(String text, int maxChartByLine, char spaceChar){
        int lastSpace = 0;
        for (int i = 0; i < text.length(); i++){
            if (text.charAt(i) == spaceChar){
                lastSpace = i;
            }
            if (i%maxChartByLine == 0){
                text.toCharArray()[lastSpace] = "$".toCharArray()[0];
            }
        }
        return text.replaceAll("$", System.getProperty("line.separator"));
    }

    public static Spannable colorized(final String text, final String word, final int argb) {
        final Spannable spannable = new SpannableString(text);
        int substringStart=0;
        int start;
        while((start=text.indexOf(word,substringStart))>=0){
            spannable.setSpan(
                    new ForegroundColorSpan(argb),start,start+word.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            substringStart = start+word.length();
        }
        return spannable;
    }

    private String getText(String s){
        try {
            if (s.endsWith(".0")){
                s = s.substring(0, s.length()-2);
            }
            if (s.startsWith("Units")) {
                double number = Double.parseDouble(s);
                return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
            }else {
                return s;
            }
        }catch (NullPointerException | NumberFormatException e){
            return s;
        }
    }

    private HashMap<String, String> listToHashMap(ArrayList<Property> properties) {

        HashMap<String, String> propertiesHashmap = new HashMap<>();

        for (Property property : properties) {

            propertiesHashmap.put(property.getKey(), property.getValue());
        }
        return propertiesHashmap;
    }

    @Override
    public int getItemCount() {
        if (mPandingjobs != null) {
            return mPandingjobs.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView m1Tv;
        private final TextView m2Tv;
        private final TextView m3Tv;
        private final TextView m4Tv;
        private final TextView m5Tv;
        private final TextView m6Tv;
        private final TextView m7Tv;
        private final TextView m8Tv;
        private final TextView m9Tv;
        private final TextView m10Tv;
        private final ImageView m1Img;
        private final ImageView m2Img;
        private final ImageView m3Img;
        private final ImageView m4Img;
        private final ImageView m5Img;
        private final ImageView m6Img;
        private final ImageView m7Img;
        private final ImageView m8Img;
        private final ImageView m9Img;
        private final ImageView m10Img;
        private final RelativeLayout m1Rl;
        private final RelativeLayout m2Rl;
        private final RelativeLayout m3Rl;
        private final RelativeLayout m4Rl;
        private final RelativeLayout m5Rl;
        private final RelativeLayout m6Rl;
        private final RelativeLayout m7Rl;
        private final RelativeLayout m8Rl;
        private final RelativeLayout m9Rl;
        private final RelativeLayout m10Rl;
//        private final View mArrow;

        ViewHolder(View itemView) {
            super(itemView);

            m1Img = itemView.findViewById(R.id.FJL_img_1);
            m2Img = itemView.findViewById(R.id.FJL_img_2);
            m3Img = itemView.findViewById(R.id.FJL_img_3);
            m4Img = itemView.findViewById(R.id.FJL_img_4);
            m5Img = itemView.findViewById(R.id.FJL_img_5);
            m6Img = itemView.findViewById(R.id.FJL_img_6);
            m7Img = itemView.findViewById(R.id.FJL_img_7);
            m8Img = itemView.findViewById(R.id.FJL_img_8);
            m9Img = itemView.findViewById(R.id.FJL_img_9);
            m10Img = itemView.findViewById(R.id.FJL_img_10);
            m1Tv = itemView.findViewById(R.id.FJL_1);
            m2Tv = itemView.findViewById(R.id.FJL_2);
            m3Tv = itemView.findViewById(R.id.FJL_3);
            m4Tv = itemView.findViewById(R.id.FJL_4);
            m5Tv = itemView.findViewById(R.id.FJL_5);
            m6Tv = itemView.findViewById(R.id.FJL_6);
            m7Tv = itemView.findViewById(R.id.FJL_7);
            m8Tv = itemView.findViewById(R.id.FJL_8);
            m9Tv = itemView.findViewById(R.id.FJL_9);
            m10Tv = itemView.findViewById(R.id.FJL_10);

            m1Rl = itemView.findViewById(R.id.FJL_1_rl);
            m2Rl = itemView.findViewById(R.id.FJL_2_rl);
            m3Rl = itemView.findViewById(R.id.FJL_3_rl);
            m4Rl = itemView.findViewById(R.id.FJL_4_rl);
            m5Rl = itemView.findViewById(R.id.FJL_5_rl);
            m6Rl = itemView.findViewById(R.id.FJL_6_rl);
            m7Rl = itemView.findViewById(R.id.FJL_7_rl);
            m8Rl = itemView.findViewById(R.id.FJL_8_rl);
            m9Rl = itemView.findViewById(R.id.FJL_9_rl);
            m10Rl = itemView.findViewById(R.id.FJL_10_rl);
//            mArrow = itemView.findViewById(R.id.FJL_arrow);
        }

    }

    public interface PendingJobsAdapterNewListener {

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
