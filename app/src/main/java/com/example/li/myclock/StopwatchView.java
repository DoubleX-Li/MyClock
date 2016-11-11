package com.example.li.myclock;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Li on 2016/10/13 0013.
 */

public class StopwatchView extends LinearLayout{

    private Button btnStart, btnPause, btnResume, btnReset;
    private EditText etHour, etMinute, etSecond;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;

    private int allTimeCount = 0;       // 秒表剩下的总时间

    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MSG_WHAT_TIME_TICK = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Time is up")
                            .setMessage("Time is up")
                            .setNegativeButton("Cancel", null)
                            .show();
                    btnReset.setVisibility(View.GONE);
                    btnResume.setVisibility(View.GONE);
                    btnPause.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                    break;
                case MSG_WHAT_TIME_TICK:
                    int hour = allTimeCount / 60 / 60;
                    int minute = (allTimeCount / 60) % 60;
                    int second = allTimeCount % 60;
                    etHour.setText(hour + "");
                    etMinute.setText(minute + "");
                    etSecond.setText(second + "");
                    break;
                default:
                    break;
            }
        }
    };

    public StopwatchView(Context context) {
        super(context);
    }

    public StopwatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopwatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StopwatchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // findViewById
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnResume = (Button) findViewById(R.id.btnResume);
        btnReset = (Button) findViewById(R.id.btnReset);

        etHour = (EditText) findViewById(R.id.etHour);
        etMinute = (EditText) findViewById(R.id.etMinute);
        etSecond = (EditText) findViewById(R.id.etSecond);

        // 初始化
        // 设置visibility
        btnStart.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);

        // 设置OnClickLIstener
        // 设置btnStart的OnClickListener
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopwatch();

                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnPause的OnClickLstener
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopStopwatch();

                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnResume的OnClickLstener
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopwatch();

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        // 设置btnReset的OnClickLstener
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopStopwatch();

                etHour.setText("0");
                etMinute.setText("0");
                etSecond.setText("0");

                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);

            }
        });

        // 设置etHour的addTextChangedListener
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    }else if (value < 0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 设置etMinute的addTextChangedListener
        etMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etMinute.setText("59");
                    }else if (value < 0) {
                        etMinute.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 设置etSecond的addTextChangedListener
        etSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etSecond.setText("59");
                    }else if (value < 0) {
                        etSecond.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    // 检查是否启用btnStart
    private void checkToEnableBtnStart() {
        // 当时分秒其中之一不为空且大于0时启用btnStart
        btnStart.setEnabled(
                (!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etMinute.getText()) && Integer.parseInt(etMinute.getText().toString()) > 0)
                || (!TextUtils.isEmpty(etSecond.getText()) && Integer.parseInt(etSecond.getText().toString()) > 0)
                );
    }

    // 开始计时
    private void startStopwatch() {
        if (timerTask == null) {
            // 计算剩余秒数allTimeCount
            allTimeCount = Integer.parseInt(etHour.getText().toString())*60*60
                    +Integer.parseInt(etMinute.getText().toString())*60
                    + Integer.parseInt(etSecond.getText().toString());
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    allTimeCount--;
                    handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

                    if (allTimeCount <=0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopStopwatch();
                    }
                }
            };
            timer.schedule(timerTask, 1000, 1000);


        }
    }

    // 停止秒表
    private void stopStopwatch() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
