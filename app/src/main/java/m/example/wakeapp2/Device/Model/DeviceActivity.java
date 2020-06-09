package m.example.wakeapp2.Device.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.Alarm.Model.AddAlarm;
import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;
import m.example.wakeapp2.User.Model.ListUser;
import m.example.wakeapp2.User.Model.ListUserAdapter;

public class DeviceActivity extends AppCompatActivity  implements AddDiviceFragment.OnFragmentInteractionListener, DeviceWifi.OnFragmentInteractionListener {

    ListView listview;
    List<ListDevice> deviceList;
    String type, GroupId, Email, Role;
    SharedPreferences sharedpreferences;
    Button btn_back, btn_add_new_device;

    public static final String Name = "nameKey";
    public static final String Email_sp = "emailKey";
    public static final String Role_sp = "roleKey";
    public static final String Group_sp = "groupKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        btn_add_new_device = findViewById(R.id.btn_add_new_device);
        listview = findViewById(R.id.list_alarm);
        btn_back = findViewById(R.id.btn_back);
        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
        deviceList = new ArrayList<>();
        sharedpreferences = getSharedPreferences("MyPref", 0);

        type="getDevices";
        if (sharedpreferences.contains(Name)) {
            GroupId = sharedpreferences.getString(Group_sp, "");
            Email = sharedpreferences.getString(Email_sp, "");
            Role = sharedpreferences.getString(Role_sp, "");
        }

        try {
            String s = backgroundTask.execute(type, Role, Email, GroupId).get();
            JSONObject obj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            JSONArray array = obj.getJSONArray("devices");

            for (int i=0; i <array.length(); i++){
                JSONObject device = array.getJSONObject(i);
                ListDevice p = new ListDevice(
                        device.getString("DeviceId"),device.getString("Name"),device.getString("DeviceType"),device.getString("Mac"),device.getString("NameUser")
                );
                deviceList.add(p);
            }
            ListDeviceAdapter adapter = new ListDeviceAdapter(deviceList, DeviceActivity.this);
            listview.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_add_new_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openFragment() {
        AddDiviceFragment fragment = AddDiviceFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container_device, fragment, "BLANK_FRAGMENT").commit();
    }



    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
