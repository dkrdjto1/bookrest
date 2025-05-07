package com.example.myapplication.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String itemId = intent.getStringExtra("itemId");
        String title = intent.getStringExtra("title");
        String memo = intent.getStringExtra("memo");
        String idx = intent.getStringExtra("idx");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(idx), notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "bookrest");

        // OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.notification_icon); // mipmap 사용 시 OREO 이상에서 시스템 UI 에러 발생

            String channelName ="독서기록장 알림채널";
            String description = "독서기록장 앱에서 정해진 시간에 알림합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; // 소리와 알림 메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("bookrest", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        } else builder.setSmallIcon(R.mipmap.ic_launcher); // OREO 이하에서 mipmap 사용하지 않을 시 Couldn't create icon: StatusBarIcon 에러 발생

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("독서기록장 메모 알림")
                .setContentTitle(title)
                .setContentText(memo)
                .setContentInfo("INFO")
                .setContentIntent(pendingIntent);

        if (notificationManager != null) {
            // 노티피케이션 동작 시킴
            notificationManager.notify(10101, builder.build());
            
            // 알림 삭제
            MainActivity.sQLiteHelper.updateReadingAlarmColumn(idx, itemId, "");
        }
    }
}