package com.example.li.myclock;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Li on 2016/10/12 0012.
 */

public class CLockView extends LinearLayout {

    private TextView tvTime;

    private Handler timeHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            refreshTime();

            // 当前界面可见时，每隔1s刷新
            if (getVisibility() == View.VISIBLE) {
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    public CLockView(Context context) {
        super(context);
    }

    public CLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CLockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText("hello");

        timeHandler.sendEmptyMessage(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshTime() {
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.VISIBLE) {
            timeHandler.sendEmptyMessage(0);
        }else {
            timeHandler.removeMessages(0);
        }
    }
}
