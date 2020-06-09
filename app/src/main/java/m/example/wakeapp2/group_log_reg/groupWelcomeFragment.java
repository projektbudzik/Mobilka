package m.example.wakeapp2.group_log_reg;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import m.example.wakeapp2.Notifications;
import m.example.wakeapp2.R;

public class groupWelcomeFragment extends Fragment {


    public groupWelcomeFragment() {
// Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_welcome, container, false);


        return view;
    }

//NOTIFCATION TEST


}