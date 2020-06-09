package m.example.wakeapp2.Device.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;
import m.example.wakeapp2.User.Model.ListUser;
import m.example.wakeapp2.User.Model.ListUserAdapter;
import m.example.wakeapp2.User.Model.UserActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddDiviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddDiviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDiviceFragment extends Fragment  implements DeviceScanerMAC.OnFragmentInteractionListener {


    private EditText et_deviceName, et_deviceMAC, et_deviceUser;
    private TextView deviceMAC, deviceUser;
    private RadioGroup radioDevice;
    private RadioButton radioTelefon;
    private String defaultValue, userName, userRole, type;
    private Button btn_cofnij, btn_dodaj_device;
    ListView listUser;
    int position_list;
    List<String> userList;
    private String GroupId;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    public static final String Name = "nameKey";
    public static final String Group_sp = "groupKey";
    public static final String Role = "roleKey";
    public static final String adrMAC = "numberAdrMAC";

    private OnFragmentInteractionListener mListener;

    public AddDiviceFragment() {
        // Required empty public constructor
    }


    public static AddDiviceFragment newInstance() {
        AddDiviceFragment fragment = new AddDiviceFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_divice, container, false);
        btn_dodaj_device = view.findViewById(R.id.btn_dodaj_device);
        et_deviceName = view.findViewById(R.id.et_deviceName);
        radioDevice = view.findViewById(R.id.radioDevice);
        et_deviceMAC = view.findViewById(R.id.et_deviceMAC);
        deviceMAC = view.findViewById(R.id.deviceMAC);
        deviceUser= view.findViewById(R.id.deviceUser);
        radioTelefon = view.findViewById(R.id.radioTelefon);
        btn_cofnij = view.findViewById(R.id.btn_cofnij);
        listUser =view.findViewById(R.id.listUser);
        userList = new ArrayList<>();

        BackgroundTask backgroundTask = new BackgroundTask(getContext());
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);
        type="getUsers";
        userName = sharedpreferences.getString(Name, "");
        userRole = sharedpreferences.getString(Role, "");

        if (sharedpreferences.contains(Name)) {
           GroupId = sharedpreferences.getString(Group_sp, "");
        }

        try {
            String s = backgroundTask.execute(type, GroupId).get();
            JSONObject obj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            JSONArray array = obj.getJSONArray("users");
            for (int i=0; i <array.length(); i++){
                JSONObject alarm = array.getJSONObject(i);
                userList.add(alarm.getString("Name"));
            }


            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_activated_1,userList);
            listUser.setAdapter(arrayAdapter);

            et_deviceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

            et_deviceMAC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e)  {

        }

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (prefs.contains(adrMAC)) {
                    defaultValue = prefs.getString(adrMAC, "");
                    et_deviceMAC.setText(defaultValue);
                    prefs.edit().remove(adrMAC).apply();}
            }
        };
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);



        radioDevice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb= getView().findViewById(checkedId);

                if (!radioTelefon.isChecked()){
                    deviceMAC.setText("Adres MAC");
                    et_deviceMAC.setHint("Kliknij aby zeskanować");
                    et_deviceMAC.setFocusable(false);

                } else {
                    deviceMAC.setText("Numet telefonu");
                    et_deviceMAC.setHint("Numet telefonu");
                    et_deviceMAC.setFocusableInTouchMode(true);
                    et_deviceMAC.setFocusable(true);

                }
            }
        });

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position_list = position;

               if (!userList.get(position_list).equals(userName) && userRole.equals("User")){
                   Toast.makeText(getContext(), "Możesz wybrać tylko siebie", Toast.LENGTH_LONG).show();
                   listUser.clearChoices();
                   listUser.requestLayout();
                   position_list = -1;
               }
            }
        });

        et_deviceMAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioTelefon.isChecked()){
                    DeviceScanerMAC fragment = DeviceScanerMAC.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
                    transaction.addToBackStack(null);
                    transaction.add(R.id.fragment_container_deviceMAC, fragment, "BLANK_FRAGMENT").commit();
                    hideKeyboard(v);

                }
            }
        });

        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        btn_dodaj_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackgroundTask backgroundTask = new BackgroundTask(getContext());
                String dType;

                if (radioTelefon.isChecked()) {
                     dType = "telefon";
                }else{
                    dType = "arduino";
                }

                String dName = et_deviceName.getText().toString();
                String dMac = et_deviceMAC.getText().toString();
                String dUser =  userList.get(position_list);

                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);
                String dGroupId = sharedpreferences.getString(Group_sp, "");

                if (dName.length() > 0 && dMac.length() >0 && dUser.length()>0 && position_list>=0) {
                    backgroundTask.execute("addDevice", dName, dType, dMac, dUser, dGroupId);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());

                }else{
                    Toast.makeText(getContext(), "Wypełnij wszystkie pola", Toast.LENGTH_LONG).show();
                }
            }
        });

        return  view;
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPref", 0);

        if (sharedpreferences.contains(adrMAC)) {
            defaultValue = sharedpreferences.getString(adrMAC, "");
            et_deviceMAC.setText(defaultValue);
            sharedpreferences.edit().remove(adrMAC).apply();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
