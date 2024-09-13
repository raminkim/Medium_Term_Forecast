package com.example.medium_term_forecast_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    static ArrayList<String> locationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);
         locationList = new ArrayList<>(Arrays.asList("경기도", "서울", "인천",
        "강원도영서", "강원도영동", "충청남도", "대전", "세종", "충청북도", "전라남도", "광주", "전라북도",
        "경상북도", "대구", "경상남도", "부산", "울산", "제주도"));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("val", locationList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}