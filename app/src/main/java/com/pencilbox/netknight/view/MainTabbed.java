package com.pencilbox.netknight.view;


import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.pencilbox.netknight.R;
import com.pencilbox.netknight.service.NetKnightService;
import com.pencilbox.netknight.utils.MyLog;

public class MainTabbed extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return new MainApp();
                case 1:
                    return new MainIp();
                case 2:
                    return new MainAddress();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "应用";
                case 1:
                    return "IP";
                case 2:
                    return "域名";
            }
            return null;
        }
    }


    private final int REQ_START_VPN = 100;

    /**
     * 开启vpnservice
     */
    protected void startVpnService() {
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, REQ_START_VPN);

        } else {
            onActivityResult(REQ_START_VPN, RESULT_OK, null);
        }

    }

    /**
     * 停止vpnService
     */
    protected void stopVpnService() {
        NetKnightService.isCalledByUser = true;
        NetKnightService.isRunning = false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQ_START_VPN:
                MyLog.logd(this, "start Vpn Service");
                Intent intent = new Intent(this, NetKnightService.class);
                startService(intent);
                break;
            default:
                break;

        }

    }

}
