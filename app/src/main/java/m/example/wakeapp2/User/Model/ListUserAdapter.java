package m.example.wakeapp2.User.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.Alarm.Model.ListMySQl;
import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.JSONreader;
import m.example.wakeapp2.R;
import m.example.wakeapp2.group_log_reg.LoginActivity;

public class ListUserAdapter extends ArrayAdapter<ListUser> {

    private List<ListUser> SqlList;
    private Context mCtx;
    SharedPreferences sharedpreferences;
    public static final String Emailsh = "emailKey";
    public static final String Rolesh = "roleKey";

    public ListUserAdapter(List<ListUser> P, Context C){
        super (C, R.layout.listuser, P);
        this.SqlList = P;
        this.mCtx = C;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listuser, null, true);
        TextView id = view.findViewById(R.id.tvID);
        TextView userName = view.findViewById(R.id.tvName);
        TextView userEmail = view.findViewById(R.id.tvEmail);
        TextView userRore = view.findViewById(R.id.tvRole);
        TextView userCreateOn = view.findViewById(R.id.tvCreateOn);
        Button zmienRole = view.findViewById(R.id.zmienRole);

        final ListUser listuser = SqlList.get(position);
        sharedpreferences = mCtx.getSharedPreferences("MyPref", 0);
        String myEmail = sharedpreferences.getString(Emailsh, "");
        String myRole = sharedpreferences.getString(Rolesh, "");

        userName.setText(listuser.getName());
        id.setText(listuser.getUserId());
        userEmail.setText(listuser.getEmail());
        userRore.setText(listuser.getUserRole());
        userCreateOn.setText(listuser.getCreate_on());

        if(listuser.getEmail().equals(myEmail) || myRole.equals("User")){
            zmienRole.setVisibility(View.GONE);
        }

        zmienRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Potwierdź operacje")
                        .setMessage("Czy na pewno chcesz zmienić rolę " + listuser.getName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String email = listuser.getEmail();
                                String nowaRola;
                                if (listuser.getUserRole().equals("User")){
                                    nowaRola = "SuperUser";
                                }else{
                                    nowaRola = "User";
                                }
                                String type = "changeRole";
                                BackgroundTask backgroundTask = new BackgroundTask(mCtx);
                                backgroundTask.execute(type, email, nowaRola);
                                dialog.dismiss();
                            }})
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }})
                        .create()
                        .show();


            }
    });
        return view;
    }
}
