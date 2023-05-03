package com.taskgem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.taskgem.MainActivity;
import com.taskgem.R;

public class videozone extends AppCompatActivity {
    Toolbar toolbar;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videozone);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(videozone.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        home=findViewById(R.id.home);
        home.setOnClickListener(view -> {
            Intent intent=new Intent(videozone.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}