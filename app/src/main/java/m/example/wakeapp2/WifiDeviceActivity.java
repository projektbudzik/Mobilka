package m.example.wakeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class WifiDeviceActivity extends AppCompatActivity {


    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    BluetoothAdapter myBluetooth = null;
    String BT_address, et_ssid_text, et_pass_text;
    Button btn_send_wifi, btn_send_wifi2;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    EditText et_ssid, et_pass_wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_device);
        btn_send_wifi2 = findViewById(R.id.btn_polacz2);
        btn_send_wifi = findViewById(R.id.btn_polacz);

        et_ssid = findViewById(R.id.et_ssid);
        et_pass_wifi = findViewById(R.id.et_pass_wifi);
//      Intent intent = getIntent();
//      BT_address = intent.getStringExtra("BT_ADDRESS");
        BT_address = "FC:F5:C4:46:87:AA";

        new ConnectBT().execute();


        et_pass_text = et_pass_wifi.getText().toString();

        btn_send_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_ssid_text = et_ssid.getText().toString();
                sendWifi(et_ssid_text);
                et_pass_wifi.setVisibility(View.VISIBLE);
                btn_send_wifi2.setVisibility(View.VISIBLE);

            }
        });

           btn_send_wifi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pass_text = et_pass_wifi.getText().toString();
                sendWifi(et_pass_text);
            }
        });
    }


    private void sendWifi(String s)    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(s.getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),
                        "Błąd: Dane nie zostały wysłane"
                        ,Toast.LENGTH_LONG).show();
            }
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(BT_address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)            {
                ConnectSuccess = false;//if the try failed, you can check the exception here

                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(),
                        "Połączenie bluetooth nie udane."
                        ,Toast.LENGTH_LONG).show();

                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Połączono"
                        ,Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }

        }

    }

}

