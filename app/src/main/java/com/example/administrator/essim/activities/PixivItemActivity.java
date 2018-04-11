package com.example.administrator.essim.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.essim.R;

public class PixivItemActivity extends AppCompatActivity {

    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixiv_item);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        Toast.makeText(this, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }
}
