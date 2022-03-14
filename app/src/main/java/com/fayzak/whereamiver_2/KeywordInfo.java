package com.fayzak.whereamiver_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class KeywordInfo extends AppCompatActivity implements ConfirmDeleteDialog.DeleteListener {
    private Website parent;
    private Keyword curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_info);
        // get previous information
        try {
            parent = DataManager.getInstance().
                    getWebsite(getIntent().getExtras().getString("parentUrl"));
            curr = parent.getKeyWordByName(getIntent().getExtras().getString("keyword"));
        }
        catch (Exception e){
            // get back to the main activity code - i think i can get to this place
            // if android kills my app on this activity and i have no website url i came
            // from
            Intent intent = new Intent(KeywordInfo.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        // get all views we need
        TextView urlTextView = findViewById(R.id.ki_website_url);
        TextView keywordTextView = findViewById(R.id.ki_keyword);
        TextView pageTextView = findViewById(R.id.ki_currPage);
        TextView posTextView = findViewById(R.id.ki_currPosition);
        Button deleteBtn = findViewById(R.id.ki_delete_keyword);

        // set views to current data
        urlTextView.setText(parent.getUrl());
        keywordTextView.setText(curr.getKeyWord());

        Scan lastScan = curr.getLastScanInfo();
        if (lastScan != null) {
            String page_str = "PAGE :   " + lastScan.getPage() + "  ";
            pageTextView.setText(page_str);
            String position_str = "POSITION :   " + lastScan.getGooglePosition() + "  ";
            posTextView.setText(position_str);
        }
        else{
            pageTextView.setText("page : ---");
            posTextView.setText("position : ---");
        }

        setGraph();

        // delete btn
        deleteBtn.setOnClickListener((View v)->{
            ConfirmDeleteDialog dialog = new ConfirmDeleteDialog("Sure you want to delete: "
                    + curr.getKeyWord() + " ?");
            dialog.show(getSupportFragmentManager(), "Delete keyword");
        });
    }

    public void setGraph(){
        NegativeGraph graph = findViewById(R.id.ki_keywordGraph);
        LinkedList<GraphPoint> list = new LinkedList<>();
        LinkedList<Scan> scans = curr.getScans();
        Scan prev = curr.getFirstScanInfo();
        if (prev != null) {
            // create one line for the case where we only had one scan
            list.add(new GraphPoint(0, prev.getGooglePosition()));
            list.add(new GraphPoint(1, prev.getGooglePosition()));
        }
        if (scans.size() > 2) {
            int day = 2;
            for (int i = 1; i < scans.size(); i++) {
                int diff = dayDiff(LocalDate.parse(prev.getDateTime()),
                        LocalDate.parse(scans.get(i).getDateTime()));
                for (int j = 0; j < diff; j++) {
                    list.add(new GraphPoint(day, prev.getGooglePosition()));
                    day++;
                }
                prev = scans.get(i);
            }
            list.add(new GraphPoint(day, prev.getGooglePosition()));
        }
        graph.loadPoints(list);
        // requests a view to redraw itself
        graph.invalidate();
    }

    public int dayDiff(LocalDate date1, LocalDate date2){
        if (date1.getYear() < date2.getYear())
            return 365*(date2.getYear()-date1.getYear()) + date2.getDayOfYear() - date1.getDayOfYear();
        else
            return  date2.getDayOfYear() - date1.getDayOfYear();
    }

    @Override
    public void deleteConfirmed() {
        parent.removeKeyword(curr);
        finish();
    }
}