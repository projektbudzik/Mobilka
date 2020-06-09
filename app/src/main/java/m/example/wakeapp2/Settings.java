package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import m.example.wakeapp2.info_log_reg.Login3Activity;
import m.example.wakeapp2.user_log_reg.Login2Activity;

public class Settings extends AppCompatActivity {
    public static final String Name = "nameKey";
    public static final String GroupName = "groupNameKey";
    public static final String Email = "emailKey";
    public static final String Role = "roleKey";
    public static final String phoneNumber = "numberKey";

    Intent intent;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView tv_sets_name = findViewById(R.id.tv_sets_name);
        TextView tv_sets_email = findViewById(R.id.tv_sets_email);
        TextView tv_sets_phone = findViewById(R.id.tv_sets_phone);
        TextView tv_sets_group = findViewById(R.id.tv_sets_group);
        TextView tv_sets_phoneN = findViewById(R.id.tv_sets_phoneN);
        Button btn_cofnij = findViewById(R.id.btn_back);
        Button btn_logout = findViewById(R.id.btn_logout);
        Button btn_phoneCh = findViewById(R.id.btn_phoneCh);
        intent = new Intent(this, AlarmService.class);

        sharedPreferences = getSharedPreferences("MyPref", 0);
        if (sharedPreferences.contains(Name)){
            String tName = sharedPreferences.getString(Name,"");
            tv_sets_name.setText(tName);
        }
        if (sharedPreferences.contains(Email)){
            String tEmail = sharedPreferences.getString(Email,"");
            tv_sets_email.setText(tEmail);
        }
        if (sharedPreferences.contains(Role)){
            String tRole = sharedPreferences.getString(Role,"");
            tv_sets_phone.setText(tRole);
        }
        if (sharedPreferences.contains(GroupName)){
            String tGroup = sharedPreferences.getString(GroupName,"");
            tv_sets_group.setText(tGroup);
        }
        if (sharedPreferences.contains(phoneNumber)){
            String tGroup = sharedPreferences.getString(phoneNumber,"");
            tv_sets_phoneN.setText(tGroup);
        }

        btn_phoneCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Login3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                stopService(intent);
                finish();
                Intent intent = new Intent(v.getContext(), Login2Activity.class);
                startActivity(intent);
            }
        });
    }
}
