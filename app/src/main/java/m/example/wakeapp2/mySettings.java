package m.example.wakeapp2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;


public class mySettings extends PreferenceActivity {

    private static final String PREFERENCES_NAME = "MyPref";
    private SharedPreferences preferences;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String phoneNumber = "numberKey";
    public static final String Group = "groupKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

        
        initPreferences();

    }

    private void initPreferences() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();

    }

    private void savePreferences() {

    }
}