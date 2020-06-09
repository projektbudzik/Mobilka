package m.example.wakeapp2.Alarm.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.Device.Model.DeviceActivity;
import m.example.wakeapp2.Device.Model.ListDevice;
import m.example.wakeapp2.Device.Model.ListDeviceAdapter;
import m.example.wakeapp2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddAlarmDevice.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAlarmDevice#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddAlarmDevice extends Fragment {

    private String GroupId;
    private String Email;
    private String Role;
    private List<String> userList;
    public static final String Name = "nameKey";
    private static final String Email_sp = "emailKey";
    private static final String Role_sp = "roleKey";
    private static final String Group_sp = "groupKey";
    private static final String dev_sh = "devKey";
    private SharedPreferences sharedpreferences;

    private OnFragmentInteractionListener mListener;

    public AddAlarmDevice() {
        // Required empty public constructor
    }

    static AddAlarmDevice newInstance() {
        AddAlarmDevice fragment = new AddAlarmDevice();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_alarm_device, container, false);
        ListView listUser = view.findViewById(R.id.list_addalarm);
        userList = new ArrayList<>();
        Button btn_back = view.findViewById(R.id.btn_cofnij2);
        BackgroundTask backgroundTask = new BackgroundTask(getContext());

        sharedpreferences = getContext().getSharedPreferences("MyPref", 0);

        String type="getDevices";
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
                    userList.add(device.getString("DeviceId") + " - " + device.getString("Name"));
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_activated_1,userList);
                listUser.setAdapter(arrayAdapter);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException ignored)  {

            }

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String devicess =  userList.get(position);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(dev_sh, devicess);
                editor.commit();

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
