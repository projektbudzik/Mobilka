package m.example.wakeapp2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AlarmCheck extends Service {
    Handler handler;
    SharedPreferences sharedpreferences;

    public static final String Name = "nameKey";
    public static final String phoneNumber = "numberKey";
    private String  callbackMsg1;
    Runnable test;
    Intent intent;
    String alarmDate_service= "";
    String alarmTime_service="";

    public AlarmCheck() {

        handler = new Handler();
        test = new Runnable() {

            @Override
            public void run() {

                sharedpreferences = getSharedPreferences("MyPref", 0);
                intent = new Intent(AlarmCheck.this, AlarmService.class);
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

                Log.e("Alarm", "Sprawdzono sygnał... "+alarmDate_service+" / " + alarmDate + alarmTime_service + " / " + alarmTime + " / " + !alarmDate.equals(alarmDate_service) );

                if (!alarmDate.equals(alarmDate_service) || !alarmTime.equals(alarmTime_service)) {
                    alarmDate_service = alarmDate ;
                    alarmTime_service = alarmTime;
                    ServiceCaller(intent, alarmTime_service, alarmDate_service);
                }
                handler.postDelayed(test, 15000);
            }
        };
        handler.postDelayed(test, 0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void ServiceCaller(Intent intent, String time, String aDate){

        stopServices(intent);

        int aHour =  Integer.parseInt(time.substring(0,2));
        int aMinute =  Integer.parseInt(time.substring(3,5));
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(c);

        if (aDate.equals(today)){
            intent.putExtra("alarmHour", aHour);
            intent.putExtra("alarmMinute", aMinute);
            startService(intent);
        }
    }
    private void stopServices(Intent intent){
        stopService(intent);
    }
}
