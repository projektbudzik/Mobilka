package m.example.wakeapp2.user_log_reg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.group_log_reg.LoginActivity;
import m.example.wakeapp2.R;


public class userLoginFragment extends Fragment {

    private static final String Name = "nameKey";
    private static final String Email = "emailKey";
    private static final String Role = "roleKey";
    private static final String Group = "groupKey";

    public userLoginFragment() {

    }
    private EditText txtLogin, txtPassword;
    private TextView ValidEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);



        ValidEmail = view.findViewById(R.id.valid_email_log);
        txtLogin = view.findViewById(R.id.et_login);
        txtPassword = view.findViewById(R.id.et_password);
        Button btn_login = view.findViewById(R.id.btn_login_user);


        TextWatcher textWatcher = new TextWatcher() {

            String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String textEmail = txtLogin.getText().toString();
                if (textEmail.length() == 0) {
                    ValidEmail.setVisibility(View.GONE);
                } else if (!textEmail.matches(emailPattern)) {
                    ValidEmail.setVisibility(View.VISIBLE);
                } else {
                    ValidEmail.setVisibility(View.GONE);
                }
            }
        };

        txtLogin.addTextChangedListener(textWatcher);

        txtLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
        return view;
    }

    public void openMain(){


        String callbackMsg1 = "";
        String type="userGroup";
        String type1="userlog";

        BackgroundTask backgroundTask2 = new BackgroundTask(getActivity().getApplicationContext());

        JSONreader jsoNreader = new JSONreader();

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedpreferences.edit();


        String username = txtLogin.getText().toString();
        String password = txtPassword.getText().toString();
        BackgroundTask backgroundTask = new BackgroundTask(getActivity().getApplicationContext());
        String callbackMsg = "";
            try {
                callbackMsg = backgroundTask2.execute(type, username, password).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        String getGroup = jsoNreader.readJSONdata(callbackMsg, "groupID");
        Log.e("Grupowo", getGroup);

            try {
                callbackMsg1 = backgroundTask.execute(type1, username, password).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        String getStatus = jsoNreader.readJSONdata(callbackMsg1, "status");

        if (getStatus.equals("OK")){
            if (getGroup != "null") {
                String getRole = jsoNreader.readJSONdata(callbackMsg, "UserRole");
                String getName = jsoNreader.readJSONdata(callbackMsg, "Name");
                String getEmail = jsoNreader.readJSONdata(callbackMsg, "Email");
                String getgroupName = jsoNreader.readJSONdata(callbackMsg1, "groupName");
                editor.putString(Email, getEmail);
                editor.putString(Name, getName);
                editor.putString(Group, getGroup);

                editor.putString(Role, getRole);
                editor.putString("groupNameKey", getgroupName);
                hideKeyboard(this.getView());
                editor.commit();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {

                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("EMAIL", username);
                startActivity(intent);
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
