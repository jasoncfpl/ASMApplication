package com.rxandroid.test.asmapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","on Click!!!!!");
//                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
//                startActivity(intent);
            }
        });

        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","show_dialog Click!!!!!");
                MyDialog dialog = new MyDialog(MainActivity.this);
                dialog.show();
            }
        });

        findViewById(R.id.long_click_btn).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("TAG","onLongClick!!!!!");
                return false;
            }
        });
        List listViewList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            listViewList.add("ListViewItem==" + i);
        }
        mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,listViewList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG","ListView onItemClick:" + position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}