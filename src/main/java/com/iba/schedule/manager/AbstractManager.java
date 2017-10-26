package com.iba.schedule.manager;

import com.iba.schedule.model.BaseModel;
import com.iba.schedule.model.TaskResponseModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractManager<T extends BaseModel> {

    public abstract T createTaskModel(String body, String currentState);

    public abstract void createTaskModel(String UUID);

    public abstract String getTaskBody(String id);

    public abstract Object getTaskState(String id);

    public abstract void deleteTask(String id);

    public abstract void runTask(String id);

    public abstract void getJVMThreads();

}
