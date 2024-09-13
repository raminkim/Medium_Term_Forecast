package com.example.medium_term_forecast_app;


import static com.example.medium_term_forecast_app.WeatherFragment.time_refresh;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_HELP = 102;
    class MyPagerAdapter extends FragmentStateAdapter {
        ArrayList<Fragment> fragList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new WeatherFragment();
                case 1:
                    return new Fragment2();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
    Toolbar toolbar;
    WeatherFragment fragment1;
    Fragment2 fragment2;
    ViewPager2 viewPager2;
    static HashMap<String, Integer> weatherMap = new HashMap<>();
    static HashMap<String, String> codeMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherMap.put("맑음", R.drawable.sunny);
        weatherMap.put("구름많음", R.drawable.cloudy);
        weatherMap.put("흐림", R.drawable.overcast);
        weatherMap.put("비", R.drawable.rainy);
        weatherMap.put("눈", R.drawable.snow);
        weatherMap.put("소나기", R.drawable.shower);
        weatherMap.put("비/눈", R.drawable.snow_and_rainy);
        weatherMap.put("예외", R.drawable.baseline_warning_24);

        codeMap.put("서울", "11B00000");
        codeMap.put("인천", "11B00000");
        codeMap.put("경기도", "11B00000");
        codeMap.put("강원도영서", "11D10000");
        codeMap.put("강원도영동", "11D20000");
        codeMap.put("대전", "11C20000");
        codeMap.put("세종", "11C20000");
        codeMap.put("충청남도", "11C20000");
        codeMap.put("충청북도", "11C10000");
        codeMap.put("전라남도", "11F20000");
        codeMap.put("광주", "11F20000");
        codeMap.put("전라북도", "11F10000");
        codeMap.put("경상북도", "11H10000");
        codeMap.put("대구", "11H10000");
        codeMap.put("경상남도", "11H20000");
        codeMap.put("부산", "11H20000");
        codeMap.put("울산", "11H20000");
        codeMap.put("제주도", "11G00000");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        fragment1 = new WeatherFragment();
        fragment2 = new Fragment2();


        TabLayout tabs = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setOffscreenPageLimit(2);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabs, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) { tab.setText("날씨조회"); }
                else if (position == 1) { tab.setText("저작권 출처"); }
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        if (curId == R.id.time_refresh) {
            Toast.makeText(this, "시간 새로고침 완료.", Toast.LENGTH_SHORT).show();
            time_refresh();
        } else if (curId == R.id.help) {
            Toast.makeText(this, "도움말 클릭", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivityForResult(intent, REQUEST_CODE_HELP);
        }
        return super.onOptionsItemSelected(item);
    }

    long preTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - preTime >= 2000) { // 2초 지나기 전에 한번 더 뒤로가기 시 종료됨.
            preTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }
}