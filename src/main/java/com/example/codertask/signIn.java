package com.example.codertask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class signIn extends AppCompatActivity {


    private EditText txtEmail, txtPsw;;
    private TextView forgetPsw, txtsignup;
    private Button btnConnect;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        
        txtsignup = findViewById(R.id.txtSignin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPsw = findViewById(R.id.txtPsw);
        forgetPsw = findViewById(R.id.txtForgetPsw);
        btnConnect = findViewById(R.id.btnConnect);
        progressBar = findViewById(R.id.ProgressBar);

        DataBase db = new DataBase();
        //Button Connect
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                String email = txtEmail.getText().toString();
                String password = txtPsw.getText().toString();

                if(txtEmail.getText().toString().isEmpty() || txtPsw.getText().toString().isEmpty())
                    Toast.makeText(signIn.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else {
                    db.signInFirebase(signIn.this,email,password,progressBar);

                }
            }
        });


        // Text Forget Password
        forgetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(signIn.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    db.ForgetpswFirebase(getApplicationContext(), email,progressBar);
                }
            }
        });

        // Text sign up
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(signIn.this, signUp.class);
                startActivity(i);
                finish();
            }
        });
    }
}