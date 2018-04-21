package com.example.administrator.essim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.administrator.essim.fragments.FragmentTagResult;

public class TagResultActivity extends SingleFragmentActivity {

    public int index;
    public String words;

    @Override
    protected Fragment createFragment() {
        return new FragmentTagResult();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        index = intent.getIntExtra("which one is selected", 0);
        words = intent.getStringExtra("what is searching");
    }
}
