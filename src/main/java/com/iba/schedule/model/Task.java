package com.iba.schedule.model;

public class Task extends BaseModel{

    //protected?
    private TaskResponseModel taskResponseModel;
    private Thread thread;

    public Task() {
    }

    public Task(String uuid) {
        this.taskResponseModel = new TaskResponseModel(uuid);
        this.thread = new Thread();
    }

    public TaskResponseModel getTaskResponseModel() {
        return taskResponseModel;
    }

    public void setTaskResponseModel(TaskResponseModel taskResponseModel) {
        this.taskResponseModel = taskResponseModel;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
