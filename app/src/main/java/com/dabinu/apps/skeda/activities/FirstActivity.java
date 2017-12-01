package com.dabinu.apps.skeda.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.dabinu.apps.skeda.R;
import com.dabinu.apps.skeda.adapters.ViewPagerAdapter;

public class FirstActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    int exitCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(0, 0);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("System"));
        tabLayout.addTab(tabLayout.newTab().setText("Connectivity"));
        tabLayout.addTab(tabLayout.newTab().setText("Commun."));


        mViewPager = findViewById(R.id.container);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setCurrentItem(1);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
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

    @Override
    public void onBackPressed(){
        if(mViewPager.getCurrentItem() != 1){
            mViewPager.setCurrentItem(1);
        }
        else{
            exitCount++;

            switch(exitCount){
                case 1:
                    Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG).show();
                    CountDownTimer cdt = new CountDownTimer(3000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished){
                        }

                        @Override
                        public void onFinish(){
                            exitCount = 0;
                        }
                    }.start();
                    break;
                case 2:
                    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        }
    }

}