package com.fayzak.whereamiver_2;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

public class KeywordListAdapter extends ArrayAdapter<Keyword> {
    private Handler mainHandler;
    // the in scan process hashset will help me determine if i can run a new scan or not,
    // also help me make the scan buttons disapear when we run scan on all the keywords
    public HashSet<Keyword> inScanProcess;
    private Website website;

    public KeywordListAdapter(Context context, ArrayList<Keyword> keywords, Handler handler,
                              Website website){
        super(context, R.layout.keyword_list_item, keywords);
        this.mainHandler = handler;
        this.inScanProcess = new HashSet<>();
        this.website = website;
    }

    // triggered by the layout manager
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Keyword current = getItem(position);
        // if the there is no view for this position, creates it
        if(convertView == null)
            convertView = LayoutInflater.from((getContext())).inflate(R.layout.keyword_list_item,
                    parent, false);
        // get all resource files
        TextView keywordTitle = convertView.findViewById(R.id.kli_keyword);
        TextView page = convertView.findViewById(R.id.kli_pageNum);
        TextView pos = convertView.findViewById(R.id.kli_position);
        TextView date = convertView.findViewById(R.id.kli_lastSearchDate);
        Button searchBtn = convertView.findViewById(R.id.kli_searchButton);
        Button statsBtn = convertView.findViewById(R.id.kli_stats);

        // now set all the values in the new website item layer with the relevant data
        keywordTitle.setText(current.getKeyWord());

        // this part will update last scan data if there was one
        if(inScanProcess.contains(current))
            searchBtn.setVisibility(View.GONE);
        else
            searchBtn.setVisibility(View.VISIBLE);

        Scan lastScan = current.getLastScanInfo();
        if (lastScan != null) {
            String page_str = "-  " + lastScan.getPage() + "  -";
            page.setText(page_str);
            String position_str = "-  " + lastScan.getGooglePosition() + "  -";
            pos.setText(position_str);
            date.setText(lastScan.getDateTime());
        }
        else{
            page.setText("---");
            pos.setText("---");
            date.setText("---");
        }
        // figure out the change part later

        // setting a listener on the button to start a search if pressed.
        searchBtn.setOnClickListener((View v)-> {
            searchBtn.setVisibility(View.GONE);
            this.inScanProcess.add(current);
            // maybe add a loading bar??
            Thread thread = new Thread(()->{
               Scan scan = Scan.scanKeyword(current);
               // i cant use post function because the views are changing
               // so insted i used a differnt handler to only let the main therad know
               // that there is a change in the dataset ( maybe there is some better way to update
               // but for now its ok
                this.inScanProcess.remove(current);
                mainHandler.post(() ->{notifyDataSetChanged();});
            });
            thread.start();
        });

        // show stats using the new activity
        statsBtn.setOnClickListener((View v) ->{
            Intent intent = new Intent(getContext(), KeywordInfo.class);
            intent.putExtra("parentUrl", website.getUrl());
            intent.putExtra("keyword", current.getKeyWord());
            getContext().startActivity(intent);
        });
        return convertView;
    }

}
