package com.example.codertask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class
signUp extends AppCompatActivity {
    private EditText txtCEmail,txtCpsw, txtCname;
    private TextView txtSignIn;
    private Button btnCreateAcc;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase fdb;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtCname = findViewById(R.id.txtCname);
        txtCEmail = findViewById(R.id.txtCemail);
        txtCpsw = findViewById(R.id.txtCpsw);
        btnCreateAcc = findViewById(R.id.btnCreateAccount);
        txtSignIn = findViewById(R.id.txtSignin);
        progressBar = findViewById(R.id.ProgressBar);

        DataBase db = new DataBase();
        fdb = FirebaseDatabase.getInstance();
        userRef = fdb.getReference("User");

        // button create account
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtCname.getText().toString().trim();
                String email = txtCEmail.getText().toString().trim();
                String password = txtCpsw.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signUp.this, "Email and Password are required.", Toast.LENGTH_SHORT).show();
                } else {
                  db.SignupFirebase(signUp.this, name,email, password, progressBar);

                }
            }
        });

        // text sign in
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signUp.this, signIn.class);
                startActivity(i);
                finish();
            }
        });

    }
}