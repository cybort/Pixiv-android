package com.example.administrator.essim.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentTagResult;
import com.example.administrator.essim.fragments.FragmentWorkItem;
import com.example.administrator.essim.utils.HomeListFragment;

public class TagDetailActivity extends AppCompatActivity {

    public int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.tag_result_container);
        if(fragment == null)
        {
            fragment = new FragmentTagResult();
            fm.beginTransaction()
                    .add(R.id.tag_result_container, fragment)
                    .commit();
        }
    }
}
