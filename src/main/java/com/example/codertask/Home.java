package com.example.codertask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Home extends AppCompatActivity {

    //RecyclerView Attributes
    private TextView taskName, taskDes, taskDate;
    private RadioButton btnTaskDone;
    private ImageView btnTaskEdit;
    private Button btnaddTask;

    private EditText taskId;


    // Firebase
    private RecyclerView rv;
    private ArrayList<TaskData> dataSource;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;
    //Attributes
    private TextView username, txtDate;

    private ImageView Logout;
    DataBase db = new DataBase();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference taskRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.recyclerview);
        username = findViewById(R.id.nameProfil);
        Logout = findViewById(R.id.imgLogout);
        txtDate = findViewById(R.id.txtDate);
        btnaddTask = findViewById(R.id.btnAddTask);

        //RecyclerView Attributes
        taskName = findViewById(R.id.txtNameTask);
        taskDes = findViewById(R.id.txtTaskDes);
        taskDate = findViewById(R.id.taskDate);
        btnTaskDone = findViewById(R.id.btnDone);
        btnTaskEdit = findViewById(R.id.editTask);

        //taskId = findViewById(R.id.)
        // Showing Date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        txtDate.setText(currentDate); //set text

        // Set name of Users in Home
        Intent i = getIntent();
        //username.setText(i.getStringExtra("username")); // Still doesn't work!!!!

        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference userRef = fdb.getReference("User");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(this, "User's details not available!", Toast.LENGTH_SHORT).show();
        } else{
            showUsersName(firebaseUser);
        }





        //Adapter
        dataSource = new ArrayList<>();
        myAdapter = new MyAdapter(this,dataSource);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(myAdapter);

        db.RecyclerviewData(dataSource,myAdapter);


        btnaddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, AddTask.class);
                startActivity(i);
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.Logout();
                                Intent i = new Intent(Home.this, signIn.class);
                                startActivity(i);
                                finish();
                            }
                        }).show();
            }
        });
    }

    // methods
    private void showUsersName(FirebaseUser firebaseUser){
        String userId = firebaseUser.getUid();

        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");
        referenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userdata = snapshot.getValue(UserData.class);
                if (userdata != null){
                    String name = firebaseUser.getDisplayName(); //change to getName
                    username.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Couldn't get the Username!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.cardview_menu, popupMenu.getMenu());

        popupMenu.getMenu().findItem(R.id.action_edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(getApplicationContext(),UpdateTask.class);
                startActivity(i);
                finish();
                return true;
            }
        });


        popupMenu.getMenu().findItem(R.id.action_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String TaskName = taskName.getText().toString();
                db.DeleteTask(Home.this, TaskName);
                return true;
            }
        });

        popupMenu.show();
    }




}
