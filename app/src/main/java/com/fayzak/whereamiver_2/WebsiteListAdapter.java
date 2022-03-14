package com.fayzak.whereamiver_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WebsiteListAdapter extends ArrayAdapter<Website> {

    public WebsiteListAdapter (Context context, ArrayList<Website> websiteArrayList){
        super(context, R.layout.website_list_item, websiteArrayList);
    }

    // trigerrd by the layout manager
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Website current = getItem(position);
        // if the there is no view for this position, creates it
        if(convertView == null)
            convertView = LayoutInflater.from((getContext())).inflate(R.layout.website_list_item,
                    parent, false);
        // now set all the values in the new website item layer with the relevant data
        TextView websiteText = convertView.findViewById(R.id.wli_text);
        websiteText.setText(current.getUrl());

        return convertView;
    }
}
