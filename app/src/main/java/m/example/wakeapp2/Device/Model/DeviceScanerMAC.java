package m.example.wakeapp2.Device.Model;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import m.example.wakeapp2.R;
import m.example.wakeapp2.WifiDeviceActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceScanerMAC.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceScanerMAC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceScanerMAC extends Fragment {

    private SurfaceView surfaceView;
    private TextView camSerialNum, MACadres;
    private Button btn_cofnij2;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;


    public static final String adrMAC = "numberAdrMAC";


    private OnFragmentInteractionListener mListener;

    public DeviceScanerMAC() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DeviceScanerMAC newInstance() {
        DeviceScanerMAC fragment = new DeviceScanerMAC();
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
        final View view = inflater.inflate(R.layout.fragment_device_scaner_mac, container, false);


        btn_cofnij2 = view.findViewById(R.id.btn_cofnij2);
        surfaceView = view.findViewById(R.id.camerapreview);
        camSerialNum = view.findViewById(R.id.cam_serialnum);
        MACadres =view.findViewById(R.id.MACadres);

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(getContext(),barcodeDetector)
                .setRequestedPreviewSize(640,480)
                .build();


        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

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
            }
        });

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

                            SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            String Mac = (qrCodes.valueAt(0).displayValue).substring(0,17);
                            editor.putString(adrMAC, Mac);
                            editor.commit();
                            sendBack();
                            cameraSource.stop();
                            MACadres.setText(Mac);
                            Toast.makeText(getContext(), "Zeskanowano", Toast.LENGTH_LONG).show();
                        }

                    });
                }
            }
        });






        btn_cofnij2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });


        return view;
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction();

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
        void onFragmentInteraction();
    }
}
