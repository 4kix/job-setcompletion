package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    private List<TaskResponseModel> taskResponseModels = new ArrayList<>();

    //new map
    private ConcurrentMap<String, Thread> activeThreads = new ConcurrentHashMap<>();

    @Autowired
    UUIDGenerator uuidGenerator;

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        String id = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(id, body, currentState);
        taskResponseModels.add(taskResponseModel);

        /*Thread taskThread = new Thread(new Task(taskResponseModel));*/

        return taskResponseModel;
    }

    @Override
    public String getTaskBody(String id) {

        String body = null;
        for (TaskResponseModel taskResponseModel: taskResponseModels)
        {
            if (taskResponseModel.getId().equals(id)) body = taskResponseModel.getBody();
        }
        return body;
    }

    @Override
    public String getTaskState(String id) {
        String state = null;
        for (TaskResponseModel taskResponseModel: taskResponseModels)
        {
            if (taskResponseModel.getId().equals(id)) state = taskResponseModel.getCurrentStatus();
        }
        return state;
    }

    @Override
    public void deleteTask(String id) {
        for (TaskResponseModel taskResponseModel: taskResponseModels)
        {
            if (taskResponseModel.getId().equals(id)) taskResponseModels.remove(taskResponseModel);
        }
    }

    public List<TaskResponseModel> getTaskResponseModels() {
        return taskResponseModels;
    }

    public void setTaskResponseModels(List<TaskResponseModel> taskResponseModels) {
        this.taskResponseModels = taskResponseModels;
    }
}
