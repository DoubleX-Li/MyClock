package com.example.li.myclock;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayAlarmAty extends AppCompatActivity {

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mp = MediaPlayer.create(this, R.raw.hello);
        mp.start();
        setContentView(R.layout.activity_play_alarm_aty);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mp.stop();
        mp.release();
    }
}
