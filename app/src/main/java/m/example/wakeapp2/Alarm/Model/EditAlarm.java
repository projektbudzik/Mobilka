package m.example.wakeapp2.Alarm.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import m.example.wakeapp2.AlarmCheck;
import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EditAlarm extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    private AddAlarm.OnFragmentInteractionListener mListener;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences sharedpreferences;
    private Button btn_cofnij,btn_dodaj_alarm;
    private TimePicker timePicker;
    String devId;
    private String selectedDate;
    private EditText startAlarm, deviceName, endAlarm;
    public static final String dev_sh = "devKey";
    public static final String Name_sh = "nameKey";
    private CheckBox Poniedzialek, Wtorek, Sroda, Czwartek, Piatek, Sobota, Niedziela;
    DatePickerDialog.OnDateSetListener dateend, datestart;
    String aId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        Poniedzialek = findViewById(R.id.Pon);
        Wtorek = findViewById(R.id.Wt);
        Sroda = findViewById(R.id.Sr);
        Czwartek = findViewById(R.id.Czw);
        Piatek = findViewById(R.id.Pt);
        Sobota = findViewById(R.id.Sb);
        Niedziela = findViewById(R.id.Nd);

        endAlarm = findViewById(R.id.textView5);
        btn_dodaj_alarm = findViewById(R.id.btn_dodaj_alarm);
        timePicker = findViewById(R.id.time_picker1);
        timePicker.setIs24HourView(true);

        btn_cofnij = findViewById(R.id.btn_cofnij);
        startAlarm = findViewById(R.id.textView3);
        deviceName = findViewById(R.id.textView4);

        final Switch sw = findViewById(R.id.switch1);
        final LinearLayout linearLayout1 = findViewById(R.id.LinearLayout1);

        Bundle extras = getIntent().getExtras();
        String dateStart = extras.getString("DStart");
        startAlarm.setText(dateStart);
        aId = extras.getString("aId");
        endAlarm.setText(extras.getString("DEnd"));
        timePicker.setHour(Integer.parseInt(extras.getString("Time").substring(0,2)));
        timePicker.setMinute(Integer.parseInt(extras.getString("Time").substring(3,5)));
        deviceName.setText(extras.getString("dName"));

        Log.e("Grupowo",extras.getString("DEnd")+"" );
        if(extras.getString("DEnd").length() > 5){
            sw.setChecked(true);
            linearLayout1.setVisibility(View.VISIBLE);
            String Seq= extras.getString("Sekwencja");
            if (Seq.indexOf("1") >= 0) {
               Poniedzialek.setChecked(true);
            }
            if (Seq.indexOf("2") >= 0) {
                Wtorek.setChecked(true);
            }
            if (Seq.indexOf("3") >= 0) {
                Sroda.setChecked(true);
            }
            if (Seq.indexOf("4") >= 0) {
                Czwartek.setChecked(true);
            }
            if (Seq.indexOf("5") >= 0) {
                Piatek.setChecked(true);
            }
            if (Seq.indexOf("6") >= 0) {
                Sobota.setChecked(true);
            }
            if (Seq.indexOf("7") >= 0) {
                Niedziela.setChecked(true);
            }
        }

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (sharedpreferences.contains("DevId")){
           sharedpreferences.edit().remove(dev_sh).commit();
        }
        devId = extras.getString("DevId");

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (prefs.contains(dev_sh)) {
                    String defaultValue = prefs.getString(dev_sh, "");
                    deviceName.setText(defaultValue);
                    devId = defaultValue.substring(0,5);

                    deviceName.setText(defaultValue.substring(7));
                    prefs.edit().remove(dev_sh).commit();
                }
            }
        };
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);

        deviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAlarmDevice fragment = AddAlarmDevice.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_container_addDev, fragment, "BLANK_FRAGMENT").commit();
            }
        });


         dateend = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(endAlarm);
            }

        };

        datestart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(startAlarm);
            }};

        startAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAlarm.this, datestart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditAlarm.this, dateend, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btn_dodaj_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String DateStart = startAlarm.getText().toString();
                String Time = timePicker.getHour() + ":" + timePicker.getMinute() + ":00";
                String DeviceId = devId;
                String Create_by = sharedpreferences.getString(Name_sh, "");
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
                        Toast.makeText(getApplicationContext(), "Newybrano żadnego dnia w sekwencji", Toast.LENGTH_LONG).show();
                    }else{
                        DateEnd = endAlarm.getText().toString();
                        if (DateStart.length() > 1 && DeviceId.length() > 1 && DateEnd.length() > 1 ) {
                            String type = "editAlarm";
                            BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                            backgroundTask.execute(type, DateStart, Time, DeviceId, Sequence, DateEnd, aId);
                            finish();
                            Intent intent = new Intent(EditAlarm.this, AlarmActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (DateStart.length() > 1 && deviceName.getText().length() > 1 ) {
                        String type = "editAlarm";
                        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                        backgroundTask.execute(type, DateStart, Time, DeviceId, NULL, "", aId);
                        finish();
                        Intent intent = new Intent(EditAlarm.this, AlarmActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_LONG).show();
                    }

                }


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
                finish();
            }
        });
    }

    private void updateLabel(EditText xx) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        xx.setText(sdf.format(myCalendar.getTime()));
    }
}
