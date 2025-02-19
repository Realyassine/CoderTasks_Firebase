package com.example.codertask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnGetstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetstarted = findViewById(R.id.btnGetstarted);
        DataBase db = new DataBase();

        /*if(db.stayConnected()){   // Doesn't work!!!!!!!!!!!
            Intent i = new Intent(MainActivity.this,Home.class);
            startActivity(i);
            finish();
        }*/
        // Button Get Started
        btnGetstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, signIn.class);
                startActivity(i);
                finish();
            }
        });
    }
}