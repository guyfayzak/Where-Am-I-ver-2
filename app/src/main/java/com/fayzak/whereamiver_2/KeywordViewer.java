package com.fayzak.whereamiver_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.LayoutDirection;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class KeywordViewer extends AppCompatActivity implements AddContentDialog.DialogListener, ConfirmDeleteDialog.DeleteListener {

    private Website parent;
    private KeywordListAdapter listAdapter;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_viewer);
        handler = new Handler();
        // need to make sure i pass the url when i call this activity
        try {
            parent = DataManager.getInstance().
                    getWebsite(getIntent().getExtras().getString("parentUrl"));
            ArrayList<Keyword> keywords = parent.getKeywordArrayList();
            ListView listView = findViewById(R.id.kv_keyword_listView);
            listAdapter = new KeywordListAdapter(this, keywords, handler, parent);
            listView.setAdapter(listAdapter);

            // maybe later i will set it as clickable

        }
        catch (Exception e){
            // get back to the main activity code - i think i can get to this place
            // if android kills my app on this activity and i have no website url i came
            // from
            Intent intent = new Intent(KeywordViewer.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        // URL display
        TextView websiteTextView = findViewById(R.id.kv_website_url);
        websiteTextView.setText(parent.getUrl());

        // Add new Keyword
        Button add = findViewById(R.id.kv_new_keyword_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContentDialog dialog = new AddContentDialog("Add your desired key words");
                dialog.show(getSupportFragmentManager(), "Add new Keyword");
            }
        });

        // Delete website
        Button delete = findViewById(R.id.kv_deleteWebsite);
        delete.setOnClickListener((View v) ->{
            ConfirmDeleteDialog dialog = new ConfirmDeleteDialog("Sure you want to delete: "
            + parent.getUrl() + " ?");
            dialog.show(getSupportFragmentManager(), "Delete website");
        });

        // Scan all keywords code
        Button scanAll = findViewById(R.id.kv_scan_all_btn);
        scanAll.setOnClickListener((View v) ->{
            ArrayList<Keyword> keywords = parent.getKeywordArrayList();
            Toast toast = new Toast(this);
            toast.setText("Started scanning all keywords");
            toast.show();
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (Keyword keyword: keywords) {
                listAdapter.inScanProcess.add(keyword);
                executor.execute(()->{
                    Scan scan = Scan.scanKeyword(keyword);
                    listAdapter.inScanProcess.remove(keyword);
                    handler.post(() ->{listAdapter.notifyDataSetChanged();});
                });
            }
        });
    }

    @Override
    public void createNewContent(String content) {
        parent.addKeyword(content);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataManager.getInstance().saveDataState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteConfirmed() {
        DataManager.getInstance().removeWebsite(parent);
        finish();
    }
}