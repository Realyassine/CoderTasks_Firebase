package com.example.codertask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private ArrayList<TaskData> taskDataList;
    private Context context;

    public MyAdapter(Context context, ArrayList<TaskData> taskDataList) {
        this.context = context;
        this.taskDataList = taskDataList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_edit, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {
        TaskData taskData = taskDataList.get(position);
        holder.taskName.setText(taskData.getTaskName());
        holder.taskDes.setText(taskData.getTaskDes());
        holder.taskDate.setText(taskData.getTaskDate());
    }

    @Override
    public int getItemCount() {
        return taskDataList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private TextView taskName, taskDes, taskDate;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.txtNameTask);
            taskDes = (TextView) itemView.findViewById(R.id.txtTaskDes);
            taskDate = (TextView) itemView.findViewById(R.id.taskDate);
        }
    }
}
