package com.example.li.myclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;
    private TimeCounterView timeCounterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabClock").setIndicator("时钟").setContent(R.id.tabClock));
        tabHost.addTab(tabHost.newTabSpec("tabStopwatch").setIndicator("计时").setContent(R.id.tabStopwatch));
        tabHost.addTab(tabHost.newTabSpec("tabTimeCounter").setIndicator("秒表").setContent(R.id.tabTimeCounter));

        timeCounterView = (TimeCounterView) findViewById(R.id.tabTimeCounter);
    }

    @Override
    protected void onDestroy() {
        timeCounterView.onDestroy();
        super.onDestroy();
    }
}
