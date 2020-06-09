package m.example.wakeapp2.info_log_reg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;

import m.example.wakeapp2.R;
import m.example.wakeapp2.info_log_reg.infoContextFragment;
import m.example.wakeapp2.info_log_reg.infoStartFragment;

public class Login3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        ViewPager viewPager = findViewById(R.id.viewPager3);

        AuthenticationPagerAdapter pagerAdapterUser = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapterUser.addFragmet(new infoStartFragment());
        pagerAdapterUser.addFragmet(new infoContextFragment());
        viewPager.setAdapter(pagerAdapterUser);

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
}
