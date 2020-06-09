package m.example.wakeapp2.group_log_reg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import m.example.wakeapp2.R;
import m.example.wakeapp2.group_log_reg.groupLoginFragment;
import m.example.wakeapp2.group_log_reg.groupRegisterFragment;
import m.example.wakeapp2.group_log_reg.groupStartFragment;
import m.example.wakeapp2.group_log_reg.groupWelcomeFragment;

public class LoginActivity extends AppCompatActivity {
    String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter (getSupportFragmentManager());

        pagerAdapter.addFragmet(new groupStartFragment());
        pagerAdapter.addFragmet(new groupLoginFragment());
        pagerAdapter.addFragmet(new groupRegisterFragment());

        Intent intent = getIntent();
        Email = intent.getStringExtra("EMAIL");
        Log.e("Email", Email);
        viewPager.setAdapter(pagerAdapter);
    }

    class AuthenticationPagerAdapter  extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    public String getEmail(){
        return Email;
    }

}