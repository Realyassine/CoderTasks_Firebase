package com.example.codertask;

public class TaskData {

    private String userID,taskName,taskDes,taskDate;
    private boolean taskDone;

    private static int countTask = 0;
    private static int countTaskDone = 0;

   public TaskData(){}
    public TaskData(String userID ,String taskName, String taskDes, String taskDate) {
        this.userID=userID;
        this.taskName = taskName;
        this.taskDes = taskDes;
        this.taskDate = taskDate;
        this.taskDone = false; // By default, task is not done
        countTask++; // Increment total task count when a new task is created
    }

    public String getUserID() {return userID;}

    public void setUserID(String userID) {this.userID = userID;}

    public boolean isTaskDone() {
        return taskDone;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    public static void setCountTask(int countTask) {
        TaskData.countTask = countTask;
    }

    public static void setCountTaskDone(int countTaskDone) {
        TaskData.countTaskDone = countTaskDone;
    }

    public void markTaskAsDone() {
        if (!taskDone) {
            taskDone = true;
            countTaskDone++; // Increment count of completed tasks
        }
    }

    public static int getCountTask() {
        return countTask;
    }

    public static int getCountTaskDone() {
        return countTaskDone;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDes() {
        return taskDes;
    }

    public void setTaskDes(String taskDes) {
        this.taskDes = taskDes;
    }
}
