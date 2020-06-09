package m.example.wakeapp2.info_log_reg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m.example.wakeapp2.R;


public class infoStartFragment extends Fragment {

    public infoStartFragment() {
// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_info_start, container, false);

    }
}