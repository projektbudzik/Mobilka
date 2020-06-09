package m.example.wakeapp2;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class AlarmService extends Service {

    private Integer alarmHour;
    private Integer alarmMinute;
    private Ringtone ringtone;
    private Timer t = new Timer();
    int notiStatus;
    static final String CHANNEL_ID = "Budzik_alarm";
    Handler handler;
    SharedPreferences sharedpreferences;
    public static final String Name = "nameKey";
    public static final String phoneNumber = "numberKey";
    private String  callbackMsg1;
    Runnable test;
    String alarmDate_service= "";
    String alarmTime_service="";
    Calendar calendar;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmHour = 0;
        alarmMinute = 0;
        notiStatus = 0;
        handler = new Handler();
        test = new Runnable() {
            @Override
            public void run() {
                sharedpreferences = getSharedPreferences("MyPref", 0);
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                JSONreader jsoNreader = new JSONreader();
                String phoneN = sharedpreferences.getString(phoneNumber, "");
                try {
                    callbackMsg1 = backgroundTask.execute("getAlarm", phoneN ).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                String alarmDate = jsoNreader.readJSONdata(callbackMsg1, "Closed_date");
                String alarmTime = jsoNreader.readJSONdata(callbackMsg1, "Time");

                if (!alarmDate.equals(alarmDate_service) || !alarmTime.equals(alarmTime_service)) {
                    alarmDate_service = alarmDate ;
                    alarmTime_service = alarmTime;
                    alarmHour =  Integer.parseInt(alarmTime_service.substring(0,2));
                    alarmMinute =  Integer.parseInt(alarmTime_service.substring(3,5));
                }
                handler.postDelayed(test, 30000);
            }
        };
        handler.postDelayed(test, 0);

        calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calendar.setTime(Calendar.getInstance().getTime());
                if (calendar.get(Calendar.HOUR_OF_DAY) == alarmHour &&
                        calendar.get(Calendar.MINUTE) == alarmMinute ){

                    if (notiStatus==0) {
                        AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        manager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                        ringtone.play();
                        makeNotification();
                        notiStatus = 1;
                        handler.removeCallbacksAndMessages(null);
                    }
                } else {
                    notiStatus = 0;
                    ringtone.stop();

                }
            }
        }, 0, 5000);

        return super.onStartCommand(intent, flags, startId);
    }

    public void makeNotification(){
        try {

            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "Notyfikacja High",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notyfikacje podczas alarmu");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            Intent notificationIntent = new Intent(this, AlarmON.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID )
                    .setContentTitle("POBUDKA")
                    .setContentText("Czas wstawać. Jest godzina "
                            + alarmHour.toString() + " : " + alarmMinute.toString())
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_access_alarm_white_24dp, "Wyłącz", pendingIntent)
                    .setSmallIcon(R.drawable.ic_access_alarm_white_24dp)
                    .build();

            startForeground(111333, notification);
            openAlarmOn();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (notiStatus == 1){
            ringtone.stop();
            t.cancel();
            handler.removeCallbacksAndMessages(null);
        }
        notiStatus = 0;
        super.onDestroy();
    }
    private void openAlarmOn() {
        Intent intent = new Intent(this, AlarmON.class);
        startActivity(intent);
    }
}
