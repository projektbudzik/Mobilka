package m.example.wakeapp2.group_log_reg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.JSONreader;
import m.example.wakeapp2.R;
import m.example.wakeapp2.info_log_reg.Login3Activity;


public class groupRegisterFragment extends Fragment {

    private EditText etxt_name, etxt_password, etxt_repassword;
    private TextView valid_name, valid_pass, valid_repass;
    private static final String GroupName = "groupNameKey";
    private static final String Role = "roleKey";
    private static final String Group = "groupKey";
    private SharedPreferences sharedpreferences;

    public groupRegisterFragment() {
// Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_register, container, false);
        etxt_name = view.findViewById(R.id.et_name);
        etxt_password = view.findViewById(R.id.et_password);
        etxt_repassword = view.findViewById(R.id.et_repassword);
        Button btn_register = view.findViewById(R.id.btn_register);
        valid_name = view.findViewById(R.id.valid_grp_name);
        valid_pass = view.findViewById(R.id.valid_password);
        valid_repass = view.findViewById(R.id.valid_repassword);

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String textName = etxt_name.getText().toString();

                if (textName.length() == 0){
                    valid_name.setVisibility(View.GONE);
                }else if (textName.length() > 20 || textName.length() < 3){
                    valid_name.setVisibility(View.VISIBLE);
                } else {
                    valid_name.setVisibility(View.GONE);
                }

                String textPassword = etxt_password.getText().toString();
                if (textPassword.length() == 0){
                    valid_pass.setVisibility(View.GONE);
                }else if (textPassword.length() > 20 || textPassword.length() < 3){
                    valid_pass.setVisibility(View.VISIBLE);
                } else {
                    valid_pass.setVisibility(View.GONE);
                }

                String textRePassword = etxt_repassword.getText().toString();
                if (textRePassword.length() == 0){
                    valid_repass.setVisibility(View.GONE);
                }else if (!textRePassword.equals(textPassword)){
                    valid_repass.setVisibility(View.VISIBLE);
                } else {
                    valid_repass.setVisibility(View.GONE);
                }
            }

        };

        etxt_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
        etxt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
        etxt_repassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        etxt_name.addTextChangedListener(textWatcher);
        etxt_password.addTextChangedListener(textWatcher);
        etxt_repassword.addTextChangedListener(textWatcher);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((LoginActivity)getActivity()).getEmail();
                String username = etxt_name.getText().toString();
                String password = etxt_password.getText().toString();
                String type = "groupreg";
                BackgroundTask backgroundTask = new BackgroundTask(getActivity().getApplicationContext());

                String callbackMsg = "";

                try {
                    callbackMsg = backgroundTask.execute(type, username, password, email).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                JSONreader jsoNreader = new JSONreader();
                String getStatus = jsoNreader.readJSONdata(callbackMsg, "status");
                String getGroupId = jsoNreader.readJSONdata(callbackMsg, "GroupId");

                if (getStatus.equals("Gotowe")){

                    sharedpreferences = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Role, "SuperUser");
                    editor.putString(Group, getGroupId);
                    editor.putString(GroupName, etxt_name.getText().toString());
                    editor.apply();

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), Login3Activity.class);
                    startActivity(intent);

                }

            }
        });
        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}