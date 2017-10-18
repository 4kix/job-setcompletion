package com.iba.schedule.task;

import com.iba.schedule.model.TaskResponseModel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Task implements Runnable {

    private volatile TaskResponseModel model;

    private static final AtomicReferenceFieldUpdater<Task, TaskResponseModel> updater =
            AtomicReferenceFieldUpdater.newUpdater(Task.class, TaskResponseModel.class, "model");

    public Task() {super();}
    public Task(TaskResponseModel taskResponseModel) {
        this.model = taskResponseModel;
    }

    @Override
    public void run() {
        model.setCurrentStatus("PROCESSING");
        System.out.println("THREAD STARTED");
        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e) {
            System.err.println("Thread sleep failed");
            e.printStackTrace();
        }

        model.setCurrentStatus("DONE");
        System.out.println("THREAD STOPPED");

    }

    public TaskResponseModel getModel() {
        return updater.get(this);
    }

    public void setModel(TaskResponseModel model) {
        updater.compareAndSet(this, this.model, model);
    }
}
