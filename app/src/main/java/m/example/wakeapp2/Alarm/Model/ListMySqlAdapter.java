package m.example.wakeapp2.Alarm.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import m.example.wakeapp2.Alarm.Model.ListMySQl;
import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;
import m.example.wakeapp2.User.Model.UserActivity;

public class ListMySqlAdapter extends ArrayAdapter<ListMySQl> {

    private List<ListMySQl> SqlList;
    private Context mCtx;
    private Activity Act;
    Button btn_editAlarm, btn_usunAlarm;
    public static final String Name = "nameKey";
    public static final String Role = "roleKey";

    public ListMySqlAdapter(List<ListMySQl> P, Context C, Activity A){
        super (C, R.layout.listalarm, P);
        this.SqlList = P;
        this.mCtx = C;
        this.Act = A;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listalarm, null, true);
        TextView id = view.findViewById(R.id.tvID);
        TextView alarmDateStart = view.findViewById(R.id.tvDateStart);
        TextView alarmDtaeEnd = view.findViewById(R.id.tvDateEnd);
        TextView alarmTime = view.findViewById(R.id.tvTime);
        TextView alarmSekwencja = view.findViewById(R.id.tvSekwencja);
        TextView alarmUser = view.findViewById(R.id.tvUser);
        TextView alarmDevice = view.findViewById(R.id.tvDevice);
        btn_editAlarm = view.findViewById(R.id.btn_editAlarm);
        btn_usunAlarm = view.findViewById(R.id.btn_usunAlarm);
        LinearLayout LinSekwencja = view.findViewById(R.id.LinSekwencja);
        LinearLayout LinDataEnd = view.findViewById(R.id.LinDataEnd);
        final ListMySQl listMySQl = SqlList.get(position);
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPref", 0);

        alarmDateStart.setText(listMySQl.getDateStart());
        alarmTime.setText(listMySQl.getTime());

        if (listMySQl.getDateEnd().equals("null") || listMySQl.getDateEnd().substring(0,4).equals("1970")) {

            LinSekwencja.setVisibility(View.GONE);
            LinDataEnd.setVisibility(View.GONE);
        } else {
            alarmDtaeEnd.setText(listMySQl.getDateEnd());
        }

        if (listMySQl.getSequence() == "null") {
        } else {
            String Seq = listMySQl.getSequence();
            String DisplaySeq = "";

            if (Seq.indexOf("1") >= 0) {
                DisplaySeq += "Pn|";
            }
            if (Seq.indexOf("2") >= 0) {
                DisplaySeq += "Wt|";
            }
            if (Seq.indexOf("3") >= 0) {
                DisplaySeq += "Sr|";
            }
            if (Seq.indexOf("4") >= 0) {
                DisplaySeq += "Cz|";
            }
            if (Seq.indexOf("5") >= 0) {
                DisplaySeq += "Pt|";
            }
            if (Seq.indexOf("6") >= 0) {
                DisplaySeq += "Sb|";
            }
            if (Seq.indexOf("7") >= 0) {
                DisplaySeq += "Nd|";
            }
            if (Seq.indexOf("12345") >= 0) {
                DisplaySeq = "Dni powszednie";
            }
            if (Seq.indexOf("1234567") >= 0) {
                DisplaySeq = "Codziennie";
            }
            if (Seq.indexOf("67") >= 0) {
                DisplaySeq = "Weekend";
            }

            alarmSekwencja.setText(DisplaySeq);
        }
            id.setText(listMySQl.getAlarmId());
            alarmDevice.setText(listMySQl.getDeviceName());
            alarmUser.setText(listMySQl.getCreateBy());

            if (sharedpreferences.getString(Name, "").equals("User") && !listMySQl.getCreateBy().equals(sharedpreferences.getString(Name, ""))) {
                btn_editAlarm.setVisibility(View.GONE);
                btn_usunAlarm.setVisibility(View.GONE);
            }

            btn_editAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditAlarm.class);
                    intent.putExtra("dName", listMySQl.getDeviceName());
                    intent.putExtra("Time", listMySQl.getTime());
                    intent.putExtra("DStart", listMySQl.getDateStart());
                    intent.putExtra("DEnd", listMySQl.getDateEnd());
                    intent.putExtra("Sekwencja", listMySQl.getSequence());
                    intent.putExtra("DevId", listMySQl.getDeviceId());
                    intent.putExtra("aId", listMySQl.getAlarmId());
                    getContext().startActivity(intent);
                    Act.finish();

                }
            });

            btn_usunAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                    builder.setTitle("Potwierdź operacje")
                            .setMessage("Czy na pewno chcesz usunąć ten alarm?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String aId = listMySQl.getAlarmId();
                                    String type = "removeAlarm";
                                    BackgroundTask backgroundTask = new BackgroundTask(mCtx);
                                    backgroundTask.execute(type, aId);
                                    dialog.dismiss();
                                    ((Activity) mCtx).finish();
                                    mCtx.startActivity(((Activity) mCtx).getIntent());
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            });

            return view;
        }

}