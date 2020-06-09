package m.example.wakeapp2.Alarm.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;

public class AlarmActivity extends AppCompatActivity implements AddAlarm.OnFragmentInteractionListener, DatePickerDialog.OnDateSetListener {
    ListView listview;
    List<ListMySQl> AlarmList;
    String type, Role, Email, GroupId;
    SharedPreferences sharedpreferences;
    public static final String Name = "nameKey";
    public static final String Email_sp = "emailKey";
    public static final String Role_sp = "roleKey";
    public static final String Group_sp = "groupKey";
    private FrameLayout fragmentContainer;
    Button btn_back, btn_add_new_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

        btn_back = findViewById(R.id.btn_back);
        listview = findViewById(R.id.list_alarm);
        fragmentContainer = findViewById(R.id.fragment_container);
        btn_add_new_alarm = findViewById(R.id.btn_add_new_alarm);
        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
        AlarmList = new ArrayList<>();
        sharedpreferences = getSharedPreferences("MyPref", 0);
        type="getAlarms";
        if (sharedpreferences.contains(Name)) {
            Role = sharedpreferences.getString(Role_sp, "");
            Email = sharedpreferences.getString(Email_sp, "");
            GroupId = sharedpreferences.getString(Group_sp, "");
        }

        try {
            String s = backgroundTask.execute(type, Role, Email, GroupId).get();
            JSONObject obj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            JSONArray array = obj.getJSONArray("alarms");

            for (int i=0; i <array.length(); i++){
                JSONObject alarm = array.getJSONObject(i);
                ListMySQl p = new ListMySQl(
                        alarm.getString("AlarmId"),alarm.getString("DateStart"),alarm.getString("Sequence"),alarm.getString("DateEnd"),alarm.getString("Time"),alarm.getString("DeviceId"),alarm.getString("Comment"),alarm.getString("Create_by"),alarm.getString("UserName"),alarm.getString("DeviceName")
                );
                AlarmList.add(p);

            }
            ListMySqlAdapter adapter = new ListMySqlAdapter(AlarmList, AlarmActivity.this, this);
            listview.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Name;
                TextView tvTime = view.findViewById(R.id.tvTime);
                Log.e("ListView:",tvTime.getText().toString());
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_add_new_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
    }



    public void openFragment() {
        AddAlarm fragment = AddAlarm.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "BLANK_FRAGMENT").commit();
    }

    @Override
    public void onFragmentInteraction(String sendBackText) {

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
