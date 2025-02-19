package com.example.codertask;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class UpdateTask extends AppCompatActivity {
    private EditText updateDes, txtDate, txtTime;
    private TextView nameTask;
    private Switch btnAlert;
    private Button btnUpdate;
    private ImageView btnBack, btnDate, btnTime;
    private LinearLayout viewDate,viewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);


        nameTask = findViewById(R.id.addNameTask);
        updateDes = findViewById(R.id.updateDes);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);

        btnBack = findViewById(R.id.icon_Uback);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnDate = findViewById(R.id.setDate);
        btnTime = findViewById(R.id.setTime);
        viewDate = findViewById(R.id.UDateView);
        viewTime = findViewById(R.id.UTimeView);

        DataBase db = new DataBase();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = nameTask.getText().toString();
                String desTask = updateDes.getText().toString();
                String dateTask = txtTime.getText().toString();

                db.UpdateTask(UpdateTask.this,taskName,desTask,dateTask);


            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
                finish();
            }
        });

        //Set Date
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDate();
            }
        });

        //Set Time
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTime();
            }
        });
    }
    private void openDate() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String selectedDate = day + "/" + (month + 1) + "/" + year; // Format
                txtDate.setText(selectedDate);
            }
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }


    // Function to open time
    private void openTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedTime = hourOfDay + ":" + minute; // Format
                txtTime.setText(selectedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}