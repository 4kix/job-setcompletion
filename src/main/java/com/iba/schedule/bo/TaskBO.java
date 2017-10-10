package com.iba.schedule.bo;

import com.iba.schedule.model.Task;
import com.iba.schedule.model.TaskResponseModel;

public class TaskBO extends Task {

    //static?
    public void createNewTask(String uuid) {
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid);
        this.setTaskResponseModel(taskResponseModel);
        //create and start new thread
    }
}
