package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlarmON extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_on);
        final Intent alarm_intent = new Intent(this, AlarmService.class);

        Button btn_wylacz = findViewById(R.id.btn_koniec);
        btn_wylacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(111333);

                ServiceCaller(alarm_intent);
                openAlarm();
            }
        });
    }

    private void ServiceCaller (Intent intent){
        stopService(intent);
        stopService(intent);
    }

    public void openAlarm(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
