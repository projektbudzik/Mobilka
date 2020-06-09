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
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import m.example.wakeapp2.BackgroundTask;
import m.example.wakeapp2.JSONreader;
import m.example.wakeapp2.MainActivity;
import m.example.wakeapp2.R;
import m.example.wakeapp2.group_log_reg.LoginActivity;


public class userRegisterFragment extends Fragment {

    Button btnReg;
    EditText txtName, txtEmail, txtPassword, txtConfirmPasswod;
    TextView ValidName, ValidEmail, ValidPassword, ValidConfirmPassword;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Role = "roleKey";
    SharedPreferences sharedpreferences;

    public userRegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_register, container, false);


        btnReg = view.findViewById(R.id.btn_register);
        txtName = view.findViewById(R.id.et_name);
        txtEmail = view.findViewById(R.id.et_email);
        txtPassword = view.findViewById(R.id.et_password);
        txtConfirmPasswod = view.findViewById(R.id.et_repassword);
        ValidName = view.findViewById(R.id.valid_login);
        ValidEmail = view.findViewById(R.id.valid_email);
        ValidPassword = view.findViewById(R.id.valid_password);
        ValidConfirmPassword = view.findViewById(R.id.valid_repassword);
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

                String textName = txtName.getText().toString();

                if (textName.length() == 0) {
                    ValidName.setVisibility(View.GONE);
                } else if (textName.length() > 20 || textName.length() < 3) {
                    ValidName.setVisibility(View.VISIBLE);
                } else {
                    ValidName.setVisibility(View.GONE);
                }

                String textEmail = txtEmail.getText().toString();
                if (textEmail.length() == 0) {
                    ValidEmail.setVisibility(View.GONE);
                } else if (!textEmail.matches(emailPattern)) {
                    ValidEmail.setVisibility(View.VISIBLE);
                } else {
                    ValidEmail.setVisibility(View.GONE);
                }

                String textPassword = txtPassword.getText().toString();
                if (textPassword.length() == 0) {
                    ValidPassword.setVisibility(View.GONE);
                } else if (textPassword.length() > 20 || textPassword.length() < 8) {
                    ValidPassword.setVisibility(View.VISIBLE);
                } else {
                    ValidPassword.setVisibility(View.GONE);
                }

                String textRePassword = txtConfirmPasswod.getText().toString();
                if (textRePassword.length() == 0) {
                    ValidConfirmPassword.setVisibility(View.GONE);
                } else if (!textRePassword.equals(textPassword)) {
                    ValidConfirmPassword.setVisibility(View.VISIBLE);
                } else {
                    ValidConfirmPassword.setVisibility(View.GONE);
                }
            }
        };

        txtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        txtConfirmPasswod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        txtConfirmPasswod.addTextChangedListener(textWatcher);
        txtEmail.addTextChangedListener(textWatcher);
        txtPassword.addTextChangedListener(textWatcher);
        txtName.addTextChangedListener(textWatcher);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                String type = "userreg";
                String callbackMsg = "";
                BackgroundTask backgroundTask = new BackgroundTask(getActivity().getApplicationContext());

                try {
                    callbackMsg = backgroundTask.execute(type, username, password, email).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                JSONreader jsoNreader = new JSONreader();

                String getStatus = jsoNreader.readJSONdata(callbackMsg, "status");
                Log.e("Co je grane", getStatus);
                if (getStatus.equals("Gotowe")) {

                    sharedpreferences = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    String getRole = jsoNreader.readJSONdata(callbackMsg, "role");
                    String getName = jsoNreader.readJSONdata(callbackMsg, "name");
                    String getEmail = jsoNreader.readJSONdata(callbackMsg, "Email");


                    editor.putString(Email, getEmail);
                    editor.putString(Name, getName);
                    editor.putString(Role, getRole);

                    editor.commit();
                    hideKeyboard(view);
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("EMAIL", email);
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

