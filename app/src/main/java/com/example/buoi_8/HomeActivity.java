package com.example.buoi_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void bai_1(View view) {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void bai_2(View view) {
        startActivity(new Intent(HomeActivity.this, Bai2Activity.class));
    }
    public void bai_3(View view) {
        startActivity(new Intent(HomeActivity.this, Bai3Activity.class));
    }
}