package m.example.wakeapp2.User.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.Alarm.Model.ListMySQl;
import m.example.wakeapp2.Alarm.Model.ListMySqlAdapter;
import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;

public class UserActivity extends AppCompatActivity {
    ListView listview;
    List<ListUser> userList;
    String type,  GroupId;
    SharedPreferences sharedpreferences;
    Button btn_back;
    public static final String Name = "nameKey";
    public static final String Group_sp = "groupKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        listview = findViewById(R.id.list_alarm);
        btn_back = findViewById(R.id.btn_back);
        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
        userList = new ArrayList<>();
        sharedpreferences = getSharedPreferences("MyPref", 0);
        type="getUsers";
        if (sharedpreferences.contains(Name)) {
            GroupId = sharedpreferences.getString(Group_sp, "");
        }

        try {
            String s = backgroundTask.execute(type, GroupId).get();
            JSONObject obj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            JSONArray array = obj.getJSONArray("users");
            for (int i=0; i <array.length(); i++){
                JSONObject alarm = array.getJSONObject(i);
                ListUser p = new ListUser(
                        alarm.getString("UserId"),alarm.getString("Name"),alarm.getString("Email"),alarm.getString("UserRole"),alarm.getString("Create_on")
                );
                userList.add(p);
            }

            ListUserAdapter adapter = new ListUserAdapter(userList, UserActivity.this);
            listview.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
