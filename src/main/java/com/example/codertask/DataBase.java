package com.example.codertask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference userRef = fdb.getReference("User");
    FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();
    DatabaseReference taskRef = fdb.getReference("Task");


    //Sign in & Sign up Methods

    //Sign Ininte
    public void signInFirebase(Activity activity, String email ,String password, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar when signing in
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar after completion
                    if (task.isSuccessful()) {
                        Log.d("Sign in","Sign in sucessfully");
                        Intent intent = new Intent(activity, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();

                    } else {
                        // Sign-in failed, show error message
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(activity, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Error during login: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //  Sign Up
    public void SignupFirebase(Activity activity, String name, String email, String password, ProgressBar progressBar) {

        progressBar.setVisibility(View.VISIBLE); // Show progress bar when signing up

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE); // Hide progress bar after completion
                    if (task.isSuccessful()) {
                        // User creation successful
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            UserData userdata = new UserData(name, email, password);

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                            referenceProfile.child(uid).setValue(userdata).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d("Sign in","Sign in sucessfully");
                                    Intent i = new Intent(activity, Home.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //i.putExtra("username",name);
                                    activity.startActivity(i);
                                    activity.finish();
                                } else {
                                    Toast.makeText(activity, "Failed to store user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(activity, "Current user is null.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(activity, "This email is already registered.", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(activity, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Failed during registration: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Forget Password
    public void ForgetpswFirebase(Context context,String email, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                            Toast.makeText(context, "Password reset email sent. Check your email inbox.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "Failed to send password reset email. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Logout
    public void Logout(){firebaseAuth.signOut();}

    // Keep users Connected
    public Boolean stayConnected(){return firebaseAuth.getCurrentUser() == null;}


    //-----------------------

    //Store & Retrieve from Realtime DB Tasks
    public void storeTask(Context context, String taskName, String taskDes, String taskDate) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Create a TaskData object with the task details
            TaskData taskData = new TaskData(uid, taskName, taskDes, taskDate);

            // Create a map to store the task data where the task name acts as the key
            Map<String, Object> taskValues = new HashMap<>();
            taskValues.put(taskName, taskData);

            // Store the task data in the database using the task name as the key
            taskRef.updateChildren(taskValues)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Task Added Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to Add Task. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "User is not authenticated. Please sign in.", Toast.LENGTH_SHORT).show();
            // You might want to redirect the user to the sign-in page or handle the situation differently
        }
    }

    public void RecyclerviewData(ArrayList<TaskData> dataSource, MyAdapter myAdapter) {
        if (firebaseuser != null) {
            String uid = firebaseuser.getUid();
            Query userTaskQuery = taskRef.orderByChild("userID").equalTo(uid);
            userTaskQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataSource.clear(); // Clear existing data
                    for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                        TaskData taskData = taskSnapshot.getValue(TaskData.class);
                        dataSource.add(taskData);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });
        } else {

        }
    }


    //-----------------------

    // --Update & Delete & Done Tasks-- 'Still not created'
    public void UpdateTask(Context context, String TaskName, String updatedTaskDes, String updatedTaskDate) {
        DatabaseReference taskToUpdateRef = taskRef.child(TaskName);
        taskToUpdateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TaskData taskData = snapshot.getValue(TaskData.class);
                    if (taskData != null) {
                        taskData.setTaskDes(updatedTaskDes);
                        taskData.setTaskDate(updatedTaskDate);

                        // Update the task in the database
                        taskToUpdateRef.setValue(taskData)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Task updated successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to update task. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Task data is null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Task does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    public void DeleteTask(Activity activity, String taskName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Task").child(taskName);
                        taskRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(activity, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Failed to delete task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void DoneTasks(){}


}
