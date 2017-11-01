package com.iba.schedule.manager;

import com.iba.schedule.model.BaseModel;

public abstract class AbstractManager<T extends BaseModel> {

    public abstract T createTaskModel(String body, String currentState);

    public abstract void createTaskModel(String UUID);

    public abstract String getTaskBody(String id);

    public abstract String getTaskState(String id);

    public abstract void stopTask(String id);

}
