package com.example.administrator.popwinlin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.soar.popwlib.SoarPopWindow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout pa = (RelativeLayout)findViewById(R.id.parentPanel);


        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_test , null);
                SoarPopWindow.getInstance(MainActivity.this).setPopType(view , Color.parseColor("#50FF00FF") , SoarPopWindow.TYPE_POSITION_DOWN).show();
            }
        });

    }
}
