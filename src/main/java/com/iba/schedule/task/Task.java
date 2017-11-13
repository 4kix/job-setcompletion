package com.iba.schedule.task;

import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.interfaces.CancellableRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Task implements CancellableRunnable {

    private AtomicReference<TaskResponseModel> model = new AtomicReference<>();

    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    public Task(TaskResponseModel taskResponseModel) {
        this.model.set(taskResponseModel);
    }

    @Override
    public void run() {
        boolean taskStatus = true;

        model.get().setCurrentStatus("RUNNING");
        logger.info("TASK STARTED");
        try {
            TimeUnit.MINUTES.sleep(3);
        } catch (InterruptedException e) {
            logger.error("Thread sleep failed");
            taskStatus = false;
            model.get().setCurrentStatus("INTERRUPTED");
        }

        if(taskStatus){
            model.get().setCurrentStatus("OK");
            model.get().setBody("DONE");
        }
        logger.info("TASK STOPPED");

    }

    public TaskResponseModel getModel() {
        return model.get();
    }

    //do we need it?
    public void setModel(TaskResponseModel taskResponseModel) {
        model.set(taskResponseModel);
    }

    public void cancel() {
        //TODO close streams and connections
        logger.info("Cancel method invoked. Closing connections...");
    }

    @Override
    public String getRunnableUUID() {
        return this.model.get().getUUID();
    }
}
