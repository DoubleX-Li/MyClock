package com.example.li.myclock;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Li on 2016/10/13 0013.
 */

public class TimeCounterView extends LinearLayout {

    private Button btnStartTC, btnPauseTC, btnResumeTC, btnCountTime, btnResetTC;
    private TextView tvHour, tvMinute, tvSecond, tvMSecond;
    private ListView lvTimeCounter;
    private ArrayAdapter<String> arrayAdapter;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int tenMSecond = 0;
    private TimerTask showTimeTask = null;

    private static final int MSG_WHAT_SHOW_TIME = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSecond/100/60/60 + "");
                    tvMinute.setText(tenMSecond/100/60%60 + "");
                    tvSecond.setText(tenMSecond/100%60 + "");
                    tvMSecond.setText(tenMSecond%100 + "");
                    break;
                default:
                    break;
            }
        }
    };

    public TimeCounterView(Context context) {
        super(context);
    }

    public TimeCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimeCounterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimeTask, 200, 200);

        // findViewById
        tvHour = (TextView) findViewById(R.id.tvHour);
        tvMinute = (TextView) findViewById(R.id.tvMinute);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvMSecond = (TextView) findViewById(R.id.tvMSecond);
        btnStartTC = (Button) findViewById(R.id.btnStartTC);
        btnPauseTC = (Button) findViewById(R.id.btnPauseTC);
        btnResumeTC = (Button) findViewById(R.id.btnResumeTC);
        btnCountTime = (Button)findViewById(R.id.btnCountTime);
        btnResetTC = (Button) findViewById(R.id.btnResetTC);
        lvTimeCounter = (ListView) findViewById(R.id.lvTimeCounter);

        // 初始化控件
        // 设置TextView的text
        tvHour.setText("0");
        tvMinute.setText("0");
        tvSecond.setText("0");
        tvMSecond.setText("0");
        // 设置Button的Visibility
        btnPauseTC.setVisibility(View.GONE);
        btnResumeTC.setVisibility(View.GONE);
        btnCountTime.setVisibility(View.GONE);
        btnResetTC.setVisibility(View.GONE);
        // 设置ListView的Adapter
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        lvTimeCounter.setAdapter(arrayAdapter);

        // 设置监听器
        // 设置btnStartTC的OnClickListener
        btnStartTC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTC();

                btnStartTC.setVisibility(View.GONE);
                btnPauseTC.setVisibility(View.VISIBLE);
                btnCountTime.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnPauseTC的OnClickListener
        btnPauseTC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTC();
                btnPauseTC.setVisibility(View.GONE);
                btnResumeTC.setVisibility(View.VISIBLE);
                btnCountTime.setVisibility(View.GONE);
                btnResetTC.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnResumeTC的OnClickListener
        btnResumeTC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTC();
                btnResumeTC.setVisibility(View.GONE);
                btnPauseTC.setVisibility(View.VISIBLE);
                btnResetTC.setVisibility(View.GONE);
                btnCountTime.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnResetTC的OnClickListener
        btnResetTC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTC();
                tenMSecond = 0;
                arrayAdapter.clear();
                btnStartTC.setVisibility(View.VISIBLE);
                btnPauseTC.setVisibility(View.GONE);
                btnResumeTC.setVisibility(View.GONE);
                btnCountTime.setVisibility(View.GONE);
                btnResetTC.setVisibility(View.GONE);
            }
        });

        // 设置btnCountTime的OnClickListener
        btnCountTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.insert(String.format("%d:%d:%d.%d", tenMSecond/100/60/60, tenMSecond/100/60%60, tenMSecond/100%60, tenMSecond%100),0);
            }
        });
    }

    private void startTC() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMSecond++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }

    private void stopTC() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void onDestroy() {
        timer.cancel();
    }
}
