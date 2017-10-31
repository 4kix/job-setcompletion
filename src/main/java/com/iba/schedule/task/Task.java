package com.iba.schedule.task;

import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.interfaces.CancellableRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class Task implements CancellableRunnable {

    private volatile TaskResponseModel model;

    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    private static final AtomicReferenceFieldUpdater<Task, TaskResponseModel> updater =
            AtomicReferenceFieldUpdater.newUpdater(Task.class, TaskResponseModel.class, "model");

    public Task(TaskResponseModel taskResponseModel) {
        this.model = taskResponseModel;
    }

    @Override
    public void run() {
        boolean taskStatus = true;

        model.setCurrentStatus("RUNNING");
        logger.info("TASK STARTED");
        try {
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            logger.error("Thread sleep failed");
            taskStatus = false;
            model.setCurrentStatus("INTERRUPTED");
        }

        if(taskStatus){
            model.setCurrentStatus("OK");
            model.setBody("DONE");
        }
        logger.info("TASK STOPPED");

    }

    public TaskResponseModel getModel() {
        return updater.get(this);
    }

    public void setModel(TaskResponseModel model) {
        updater.compareAndSet(this, this.model, model);
    }

    public void cancel() {
        //TODO close streams and connections
        logger.info("Cancel method invoked. Closing connections...");
    }

    @Override
    public String getRunnableUUID() {
        return this.model.getUUID();
    }
}
