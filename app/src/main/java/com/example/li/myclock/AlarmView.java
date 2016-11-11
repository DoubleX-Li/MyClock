package com.example.li.myclock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Li on 2016/10/12 0012.
 */

public class AlarmView extends LinearLayout {

    private Button btnAddAlarm;
    private ListView lvAlarm;
    private ArrayAdapter arrayAdapter;
    private AlarmManager alarmManager;
    private static final String KEY_ALARM_LIST = "alarmlist";

    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // findViewById
        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarm = (ListView) findViewById(R.id.lvAlarm);

        // 设置ListView的Adapter
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
        lvAlarm.setAdapter(arrayAdapter);
        readSavedAlarmList();

        // 设置监听
        // btnAddAlarm的OnClickListener
        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });

        // 长按ListView中item的OnItemLongClickListener
        lvAlarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(getContext())
                        .setTitle("操作选项")
                        .setItems(new CharSequence[]{"删除"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        switch (i) {
                                            case 0:
                                                deleteAlarm(position);
                                                break;
                                        }
                                    }
                                })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });
    }

    // 添加闹钟
    private void addAlarm() {
        Calendar c = Calendar.getInstance();
        // new一个时间选择窗口
        new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        Calendar currentTime = Calendar.getInstance();
                        // 如果要设置的时间比当前时间小，往后推一天
                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                        }

                        AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                        arrayAdapter.add(ad);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                ad.getTime(),
                                5 * 60 * 1000,
                                PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
                        saveAlarmList();
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true).show();
    }

    // 保存闹钟列表
    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext()
                .getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE)
                .edit();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            sb.append(((AlarmData) arrayAdapter.getItem(i)).getTime()).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }

        editor.commit();
    }

    // 读取闹钟列表
    private void readSavedAlarmList() {
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST, null);

        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                arrayAdapter.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }

    // 删除闹钟
    private void deleteAlarm(int position) {
        AlarmData ad = (AlarmData) arrayAdapter.getItem(position);
        arrayAdapter.remove(ad);
        saveAlarmList();

        alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
                ad.getId(), new Intent(getContext(),
                AlarmReceiver.class), 0));
    }

    // 内部类，AlarmData
    private static class AlarmData {

        private String timeLable = "";
        private long time = 0;
        private Calendar date;

        public AlarmData(long time) {
            this.time = time;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLable = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DATE),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
        }

        public long getTime() {
            return time;
        }

        public String getTimeLable() {
            return timeLable;
        }

        public int getId() {
            return (int) (getTime() / 1000 / 60);
        }

        @Override
        public String toString() {
            return getTimeLable();
        }

    }
}