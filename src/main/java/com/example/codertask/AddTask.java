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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    private EditText addNameTask, addDesTask, inputDate, inputTime;
    private Switch btnAlert;
    private Button btnCreateTask;
    private ImageView btnBack, btnDate, btnTime;
    private LinearLayout viewDate,viewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addNameTask = findViewById(R.id.addNameTask);
        addDesTask = findViewById(R.id.addDes);
        btnAlert = findViewById(R.id.btnAlert);
        btnCreateTask = findViewById(R.id.btnCreateTask);
        inputDate = findViewById(R.id.inputDate);
        inputTime = findViewById(R.id.inputTime);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnBack = findViewById(R.id.icon_back);
        viewDate = findViewById(R.id.DateView);
        viewTime = findViewById(R.id.TimeView);

        DataBase db = new DataBase();
        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = addNameTask.getText().toString();
                String taskDes = addDesTask.getText().toString();
                String taskDate = inputDate.getText().toString();

                if (taskDes.isEmpty() || taskName.isEmpty())
                    Toast.makeText(AddTask.this, "Please give a Name and Description of your Task!", Toast.LENGTH_SHORT).show();
                else {
                    db.storeTask(getApplicationContext(), taskName, taskDes, taskDate);
                }
            }
        });

        // Switch to Add Alert
        btnAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewDate.setVisibility(View.VISIBLE);
                    viewTime.setVisibility(View.VISIBLE);
                } else {
                    viewDate.setVisibility(View.GONE);
                    viewTime.setVisibility(View.GONE);
                }
            }
        });

        // Back Button
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

    // Function to open date
    private void openDate() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String selectedDate = day + "/" + (month + 1) + "/" + year; // Format
                inputDate.setText(selectedDate);
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
                inputTime.setText(selectedTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
