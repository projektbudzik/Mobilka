package m.example.wakeapp2.Alarm.Model;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.Device.Model.DeviceScanerMAC;
import m.example.wakeapp2.R;


public class AddAlarm extends Fragment implements AddAlarmDevice.OnFragmentInteractionListener{

    private OnFragmentInteractionListener mListener;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    private SharedPreferences sharedpreferences;
    private Button btn_cofnij,btn_dodaj_alarm;
    private TimePicker timePicker;
    String devId;
    private String selectedDate, devtit;
    private EditText startAlarm, deviceName, endAlarm;
    public static final String dev_sh = "devKey";
    public static final String Name = "nameKey";
    public static final String Email_sp = "emailKey";
    private CheckBox Poniedzialek, Wtorek, Sroda, Czwartek, Piatek, Sobota, Niedziela;

    public AddAlarm() {
        // Required empty public constructor
    }


    public static AddAlarm newInstance() {
        AddAlarm fragment = new AddAlarm();
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

        View view = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        Poniedzialek = view.findViewById(R.id.Pon);
        Wtorek = view.findViewById(R.id.Wt);
        Sroda = view.findViewById(R.id.Sr);
        Czwartek = view.findViewById(R.id.Czw);
        Piatek = view.findViewById(R.id.Pt);
        Sobota = view.findViewById(R.id.Sb);
        Niedziela = view.findViewById(R.id.Nd);

        endAlarm = view.findViewById(R.id.textView5);
        btn_dodaj_alarm = view.findViewById(R.id.btn_dodaj_alarm);
        timePicker = view.findViewById(R.id.time_picker1);
        timePicker.setIs24HourView(true);

        btn_cofnij = view.findViewById(R.id.btn_cofnij);
        startAlarm = view.findViewById(R.id.textView3);
        deviceName = view.findViewById(R.id.textView4);

        final Switch sw = view.findViewById(R.id.switch1);
        final LinearLayout linearLayout1 = view.findViewById(R.id.LinearLayout1);
        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();


        sharedpreferences = getContext().getSharedPreferences("MyPref", 0);



        sharedpreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (sharedPreferences.contains(dev_sh)) {
                    String defaultValue =  sharedpreferences.getString(dev_sh, "");
                    devId = defaultValue.substring(0,5);
                    devtit = defaultValue.substring(8);
                    deviceName.setText(devtit);

                }
            }
        });

        deviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmDevice fragment = AddAlarmDevice.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_container_addDev, fragment, "BLANK_FRAGMENT").commit();
            }
        });

        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new m.example.wakeapp2.Alarm.Model.DatePicker();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAlarm.this, 11);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        endAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialogFragment newFragment = new m.example.wakeapp2.Alarm.Model.DatePicker();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(AddAlarm.this, 12);
                // show the datePicker
                newFragment.show(fm, "datePicker2");
            }
        });

        btn_dodaj_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String DateStart = startAlarm.getText().toString();
                String Time = timePicker.getHour() + ":" + timePicker.getMinute() + ":00";
                String DeviceId = devId;
                String Create_by = sharedpreferences.getString(Email_sp, "");
                String Sequence = "";
                String DateEnd = "";

                if(sw.isChecked()) {

                    String pon = "", wt = "", sr = "", czw = "", pt = "", sob = "", ndz = "";
                    if (Poniedzialek.isChecked()) {
                        pon = "1";
                    }
                    if (Wtorek.isChecked()) {
                        wt = "2";
                    }
                    if (Sroda.isChecked()) {
                        sr = "3";
                    }
                    if (Czwartek.isChecked()) {
                        czw = "4";
                    }
                    if (Piatek.isChecked()) {
                        pt = "5";
                    }
                    if (Sobota.isChecked()) {
                        sob = "6";
                    }
                    if (Niedziela.isChecked()) {
                        ndz = "7";
                    }

                    Sequence = pon + wt + sr + czw + pt + sob + ndz;
                    if (Sequence.length() == 0){
                        Toast.makeText(getContext(), "Newybrano żadnego dnia w sekwencji", Toast.LENGTH_LONG).show();
                    }else{
                        DateEnd = endAlarm.getText().toString();
                        if (DateStart.length() > 1 && DeviceId.length() > 1 && DateEnd.length() > 1 ) {
                            String type = "addAlarm";
                            BackgroundTask backgroundTask = new BackgroundTask(getContext());
                            backgroundTask.execute(type, DateStart, Time, DeviceId, Create_by, Sequence, DateEnd);
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().finish();
                            startActivity(getActivity().getIntent());
                        }else {
                            Toast.makeText(getContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (DateStart.length() > 1 && deviceName.getText().length() > 1 ) {
                        String type = "addAlarms";
                        BackgroundTask backgroundTask = new BackgroundTask(getContext());
                        backgroundTask.execute(type, DateStart, Time, DeviceId, Create_by);
                        getActivity().getSupportFragmentManager().popBackStack();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }else {
                        Toast.makeText(getContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                    }
                }
                sharedpreferences.registerOnSharedPreferenceChangeListener(listener);
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayout1.setVisibility(View.VISIBLE);
                } else {
                    linearLayout1.setVisibility(View.GONE);
                }
            }
        });

        btn_cofnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity(). finish();
                startActivity(getActivity().getIntent());

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            selectedDate = data.getStringExtra("selectedDate");
            Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                date = format.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            c.add(Calendar.DATE, -1);

            if(date.before(c.getTime())){
                Toast.makeText(getContext(), "Wybrana data już mineła", Toast.LENGTH_LONG).show();
            }else {
                startAlarm.setText(selectedDate);
            }
        }



            if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
                if (startAlarm.getText().length() > 1) {
                    Date date = null, date2 = null;
                    selectedDate = data.getStringExtra("selectedDate");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        date = format.parse(startAlarm.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date2 = format.parse(selectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date.before(date2)) {
                        endAlarm.setText(selectedDate);
                    } else {
                        Toast.makeText(getContext(), "Ustaw datę późniejszą niż start", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getContext(), "Nie wybrano daty uruchomienia alarmu", Toast.LENGTH_LONG).show();
                }
            }
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String sendBackText);
    }

}



