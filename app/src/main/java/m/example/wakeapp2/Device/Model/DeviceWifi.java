package m.example.wakeapp2.Device.Model;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.UUID;

import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.WifiDeviceActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceWifi.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceWifi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceWifi extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SurfaceView surfaceView;
    private TextView camSerialNum, MACwifi, tv_title;
    private BarcodeDetector barcodeDetector;
    private String BT_address;
    private EditText et_wifi_ssid, et_wifi_pass;
    private CameraSource cameraSource;
    private Button btn_cofnij3,btn_wifi_ssid, btn_wifi_pass_sent;
    private LinearLayout LinearLayoutPass, LinearLayoutSSID;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    BluetoothAdapter myBluetooth = null;

    public DeviceWifi() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DeviceWifi newInstance() {
        DeviceWifi fragment = new DeviceWifi();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_wifi, container, false);
        MACwifi =view.findViewById(R.id.MACwifi);
        et_wifi_ssid = view.findViewById(R.id.et_wifi_ssid);
        btn_wifi_pass_sent = view.findViewById(R.id.btn_wifi_pass_sent);
        btn_cofnij3 = view.findViewById(R.id.btn_cofnij3);
        btn_wifi_ssid = view.findViewById(R.id.btn_wifi_ssid);
        et_wifi_pass = view.findViewById(R.id.et_wifi_pass);
        surfaceView = view.findViewById(R.id.camerapreview);
        camSerialNum = view.findViewById(R.id.cam_serialnum);
        LinearLayoutPass = view.findViewById(R.id.LinearLayoutPass);
        LinearLayoutSSID = view.findViewById(R.id.LinearLayoutSSID);
        tv_title = view.findViewById(R.id.tv_title);
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(getContext(),barcodeDetector)
                .setRequestedPreviewSize(640,480)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }   });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0){
                    camSerialNum.post(new Runnable() {
                        @Override
                        public void run() {
                            String Mac = (qrCodes.valueAt(0).displayValue).substring(18);
                            camSerialNum.setText("Wprowadź SSID a następnie hasło do twojej sieci WIFI");
                            cameraSource.stop();
                            surfaceView.setVisibility(View.INVISIBLE);
                            LinearLayoutSSID.setVisibility(View.VISIBLE);
                            MACwifi.setText(Mac);
                            Toast.makeText(getContext(), "Zeskanowano", Toast.LENGTH_LONG).show();
                            BT_address = Mac;
                            tv_title.setText("Ustawienia Wi-fi");
                            new ConnectBT().execute();
                        }

                    });
                }
            }
        });

        btn_cofnij3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        btn_wifi_pass_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWifi(et_wifi_pass.getText().toString());
                getActivity().finish();
                startActivity(getActivity().getIntent());
                Toast.makeText(getContext(), "Dane przesłane", Toast.LENGTH_LONG).show();
            }
        });

        btn_wifi_ssid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWifi(et_wifi_ssid.getText().toString());
                MACwifi.setText(et_wifi_ssid.getText());
                LinearLayoutSSID.setVisibility(View.INVISIBLE);
                LinearLayoutPass.setVisibility(View.VISIBLE);
            }
        });

        return view;
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
                Toast.makeText(getContext(),
                        "Błąd: Dane nie zostały wysłane"
                        ,Toast.LENGTH_LONG).show();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }

    public void openMain(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
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
                Toast.makeText(getContext(),
                        "Połączenie bluetooth nie udane."
                        ,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(),
                        "Połączono"
                        ,Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }

        }

    }


}
