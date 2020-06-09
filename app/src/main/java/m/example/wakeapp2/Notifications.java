package m.example.wakeapp2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {



    public static final String CHANNEL_1_ID = "channel1";



    @Override
    public void onCreate() {
        super.onCreate();

        createNotifactionChannels();

    }

    private void  createNotifactionChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "CHANNEL1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Notyfikacja podczas uruchomienia alarmu");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }
}
