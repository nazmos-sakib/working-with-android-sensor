package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button compass1, compass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compass1 = findViewById(R.id.btnCompass1);
        compass2 = findViewById(R.id.btnCompass2);

        compass1.setOnClickListener(View->{
            Intent intent = new Intent(MainActivity.this, compass1.class);
            startActivity(intent);
        });

        compass2.setOnClickListener(View->{
            Intent intent = new Intent(MainActivity.this, compass2.class);
            startActivity(intent);
        });
    }
}