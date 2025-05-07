package com.example.myapplication.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.receiver.AlarmReceiver;
import com.example.myapplication.util.RangeTimePickerDialog;
import com.example.myapplication.vo.ReadingVo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {

    EditText readingpagenumberEditText;
    TextView memoEditText;
    TextView alarmDateText;
    TextView alarmTimeText;
    CheckBox checkboxAlarm;
    String readingpagenumber;
    String memo;
    String idx;
    String itemId;
    String alarm;
    String position;
    String title;
    Toolbar toolbar;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // Toolbar setting
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView cancel = findViewById(R.id.cancel_button);
        TextView save = findViewById(R.id.save);

        readingpagenumberEditText = findViewById(R.id.readingpagenumber);
        memoEditText = findViewById(R.id.memo);
        checkboxAlarm = findViewById(R.id.view3);
        // 알림 날짜
        alarmDateText = findViewById(R.id.alarmDate);
        // 알림 시각
        alarmTimeText = findViewById(R.id.alarmTime);

        Intent intent = getIntent();

        readingpagenumber = intent.getStringExtra("readingpagenumber");
        memo = intent.getStringExtra("memo");
        itemId = intent.getStringExtra("itemId");
        idx = intent.getStringExtra("idx");
        alarm = intent.getStringExtra("alarm");
        position = intent.getStringExtra("position");
        title = intent.getStringExtra("title");

        if (!"".equals(idx)) {
            readingpagenumberEditText.setText(readingpagenumber);
            memoEditText.setText(memo);
            if(alarm != null && !"".equals(alarm.trim())) {

                String [] tmp = alarm.split(" ");
                alarmDateText.setText(tmp[0]);
                alarmTimeText.setText(tmp[1]);
                checkboxAlarm.setChecked(true);
            }
        }

        // 취소
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 알림 취소
        checkboxAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkboxAlarm.isChecked()) {

                } else {
                    alarmDateText.setText("");
                    alarmTimeText.setText("");
                }
            }
        });

        // 저장
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readingpagenumber = readingpagenumberEditText.getText().toString().trim();
                memo = memoEditText.getText().toString().trim();

                String alarmDate = alarmDateText.getText().toString().trim();
                String alarmTime = alarmTimeText.getText().toString().trim();
                alarm = alarmDateText.getText().toString().trim() + " " + alarmTimeText.getText().toString().trim();

                if ("".equals(readingpagenumber) || "".equals(memo)) {
                    Toast.makeText(getApplicationContext(), "페이지 번호와 메모를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    String flag = "I"; // INSERT(새롭게 메모 추가)
                    int resultIdx = -1;

                    if ("".equals(idx)) {
                        resultIdx = (int) MainActivity.sQLiteHelper.insertReadingColumn(itemId, readingpagenumber, memo, alarm);
                    } else {
                        flag = "U"; // UPDATE(기존 메모 수정)
                        resultIdx = Integer.parseInt(idx);
                        MainActivity.sQLiteHelper.updateReadingColumn(idx, itemId, readingpagenumber, memo, alarm);
                    }

                    String alarmIdx = itemId + String.valueOf(resultIdx);
                    // 알림 설정 값이 있으면
                    if (!"".equals(alarmDate) && !"".equals(alarmTime)) {
                        notification(String.valueOf(resultIdx), alarmDate, alarmTime, title, memo);
                    } else {
                        alarm = "";
                    }

                    DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    String date = format.format(Calendar.getInstance().getTime());

                    // 추가 및 수정된 Object 를 전달
                    Intent intent = new Intent(getApplicationContext(), ReadingRecdActivity.class);
                    ReadingVo vo = new ReadingVo();
                    vo.setIdx(String.valueOf(resultIdx));
                    vo.setItemId(itemId);
                    vo.setReadingpagenumber(readingpagenumber);
                    vo.setAlarm(alarm);
                    vo.setMemo(memo);
                    vo.setRegDate(date);
                    vo.setUpdDate(date);
                    intent.putExtra("vo", vo);
                    intent.putExtra("FLAG", flag);
                    intent.putExtra("position", position);
                    setResult(MainActivity.REQUEST_CODE, intent);

                    finish();
                }
            }
        });

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String format = "yyyyMMdd";
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
                    alarmDateText.setText(sdf.format(calendar.getTime()));

                    String alarmDate = alarmDateText.getText().toString().trim();
                    String alarmTime = alarmTimeText.getText().toString().trim();

                    if(!"".equals(alarmDate) && !"".equals(alarmTime)) {
                        checkboxAlarm.setChecked(true);
                    }
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        );

        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        // 알림 날짜 클릭 시
        alarmDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // 알림 시각 클릭 시
        alarmTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                RangeTimePickerDialog timePicker;
                timePicker = new RangeTimePickerDialog (MemoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        // EditText 에 출력할 형식 지정
                        alarmTimeText.setText(hour + ":" + minute);

                        String alarmDate = alarmDateText.getText().toString().trim();
                        String alarmTime = alarmTimeText.getText().toString().trim();

                        if (!"".equals(alarmDate) && !"".equals(alarmTime)) {
                            checkboxAlarm.setChecked(true);
                        }
                    }
                }, hour, minute, true); // true 의 경우 24시간 형식의 TimePicker 출현
                timePicker.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // toolbar 의 back  키 눌렀을 때 동작
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 알림 등록
     * @param alarmDate
     * @param alarmTIme
     * @param title
     * @param memo
     */
    public void notification(String idx, String alarmDate, String alarmTIme, String title, String memo) {
        Boolean notifyFlag = true;

        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.putExtra("itemId", itemId);
        alarmIntent.putExtra("title", title);
        alarmIntent.putExtra("memo", memo);
        alarmIntent.putExtra("idx", idx);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(idx), alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 알람시각 calendar 에 set 해줌
        Calendar calendar = Calendar.getInstance();
        String year = alarmDate.substring(0,4);
        String month = alarmDate.substring(4,6);
        String day = alarmDate.substring(6,8);
        String [] tmpTime = alarmTIme.split(":");
        String hour = tmpTime[0];
        String minute = tmpTime[1];

        calendar.set(Integer.parseInt(year),
                Integer.parseInt(month)-1,
                Integer.parseInt(day),
                Integer.parseInt(hour),
                Integer.parseInt(minute),
                0);

        // 알람을 허용했다면
        if (notifyFlag) {
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.RTC, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }
}
