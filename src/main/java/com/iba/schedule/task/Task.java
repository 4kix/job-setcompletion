package com.iba.schedule.task;

import com.iba.schedule.model.TaskResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Task implements Runnable {

    private volatile TaskResponseModel model;

    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    private static final AtomicReferenceFieldUpdater<Task, TaskResponseModel> updater =
            AtomicReferenceFieldUpdater.newUpdater(Task.class, TaskResponseModel.class, "model");

    public Task() {super();}
    public Task(TaskResponseModel taskResponseModel) {
        this.model = taskResponseModel;
    }

    @Override
    public void run() {
        boolean taskStatus = true;

        model.setCurrentStatus("PROCESSING");
        logger.info("TASK STARTED");
        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException e) {
            logger.error("Thread sleep failed");
            taskStatus = false;
            model.setCurrentStatus("TASK INTERRUPTED");
        }

        if(taskStatus){
            model.setCurrentStatus("DONE");
        }
        logger.info("TASK STOPPED");

    }

    public TaskResponseModel getModel() {
        return updater.get(this);
    }

    public void setModel(TaskResponseModel model) {
        updater.compareAndSet(this, this.model, model);
    }
}
