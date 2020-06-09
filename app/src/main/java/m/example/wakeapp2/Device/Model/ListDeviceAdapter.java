package m.example.wakeapp2.Device.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.R;

public class ListDeviceAdapter extends ArrayAdapter<ListDevice> {

    private List<ListDevice> SqlList;
    private Context mCtx;
    SharedPreferences sharedpreferences;
    public static final String Rolesh = "roleKey";
    public static final String phoneNumber = "numberKey";
    private Button confWIF, usunDevice;
    public ListDeviceAdapter(List<ListDevice> P, Context C){
        super (C, R.layout.listdevice, P);
        this.SqlList = P;
        this.mCtx = C;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.listdevice, null, true);

        TextView id = view.findViewById(R.id.tvID);
        TextView deviceName = view.findViewById(R.id.tvName);
        TextView deviceTypes = view.findViewById(R.id.tvDeviceType);
        TextView deviceMAC = view.findViewById(R.id.tvMac);
        TextView deviceUser = view.findViewById(R.id.tvOwner);
        final ListDevice listDevice = SqlList.get(position);

        sharedpreferences = mCtx.getSharedPreferences("MyPref", 0);
        String myRole = sharedpreferences.getString(Rolesh, "");
        String myNumer = sharedpreferences.getString(phoneNumber, "");

        usunDevice = view.findViewById(R.id.usunDevice);
        deviceName.setText(listDevice.getName());
        id.setText(listDevice.getDeviceId());
        deviceTypes.setText(listDevice.getDeviceType());
        deviceMAC.setText(listDevice.getMAC());
        deviceUser.setText(listDevice.getUserName());
        confWIF = view.findViewById(R.id.confWIF);

        if(listDevice.getDeviceType().equals("telefon")){
            confWIF.setVisibility(View.GONE);
        }

        if(myRole.equals("User")){
            usunDevice.setVisibility(View.GONE);
        }

        usunDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Potwierdź operacje")
                        .setMessage("Czy na pewno chcesz usunąć " + listDevice.getName() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String dId = listDevice.getDeviceId();
                                String type = "removeDevice";
                                BackgroundTask backgroundTask = new BackgroundTask(mCtx);
                                backgroundTask.execute(type, dId);
                                dialog.dismiss();
                                ((Activity)mCtx).finish();
                                mCtx.startActivity( ((Activity)mCtx).getIntent());
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

        confWIF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceWifi fragment = DeviceWifi.newInstance();
                FragmentTransaction transaction = ((DeviceActivity)mCtx).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.exit_from_right, R.anim.exit_to_right, R.anim.exit_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_container_device, fragment, "BLANK_FRAGMENT").commit();
            }
        });


        return view;
    }
}
