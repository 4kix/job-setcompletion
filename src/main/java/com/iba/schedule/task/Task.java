package com.iba.schedule.task;

import com.iba.schedule.model.BaseModel;
import com.iba.schedule.model.TaskResponseModel;

import java.util.concurrent.TimeUnit;


public class Task implements Runnable {

    private TaskResponseModel model;

    public Task(TaskResponseModel taskResponseModel) {
        this.model = taskResponseModel;
    }

    @Override
    public void run() {
        model.setCurrentStatus("PROCESSING");

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            System.err.println("Thread sleep failed");
            e.printStackTrace();
        }

        model.setCurrentStatus("Done");
    }
}
