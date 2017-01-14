package com.jeromyang.transmssion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnlineActivity extends AppCompatActivity {


    RecyclerView onlineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        onlineView = (RecyclerView) findViewById(R.id.online_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        onlineView.setLayoutManager(layoutManager);


    }
}
