package com.fayzak.whereamiver_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddContentDialog.DialogListener {

    // members i need to use in the
    private WebsiteListAdapter websiteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataManager.setContext(MainActivity.this);

        // the Listview management part
        ArrayList<Website> websites = DataManager.getInstance().getWebsites();
        websiteListAdapter = new WebsiteListAdapter(this, websites);
        ListView websitesList = findViewById(R.id.website_listView);
        websitesList.setAdapter(websiteListAdapter);
        websitesList.setClickable(true);
        websitesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, KeywordViewer.class);
                intent.putExtra("parentUrl", websites.get(position).getUrl());
                startActivity(intent);
            }
        });

        // Add new Website
        Button add = findViewById(R.id.new_website_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContentDialog dialog = new AddContentDialog("Add the website url");
                dialog.show(getSupportFragmentManager(), "Add new website");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        websiteListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataManager.getInstance().saveDataState();
    }

    @Override
    public void createNewContent(String content) {
        DataManager.getInstance().addWebsite(new Website(content));
        websiteListAdapter.notifyDataSetChanged();
    }


}