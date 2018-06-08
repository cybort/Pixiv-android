package com.example.administrator.essim.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.essim.R;
import com.example.administrator.essim.fragments.FragmentSpecialCollec;

public class SpecialCollectionActivity extends AppCompatActivity {

    public FragmentSpecialCollec mFragmentSpecialCollec;
    //两个Fragment互相hide解决问题，实在不想多建activity了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_collection);

        mFragmentSpecialCollec = new FragmentSpecialCollec();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!mFragmentSpecialCollec.isAdded()) {
            transaction.add(R.id.special_collec_container, mFragmentSpecialCollec);
        }
        transaction.show(mFragmentSpecialCollec).commit();
    }
}
