package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private ConcurrentMap<String, Thread> activeThreads = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Task> activeTasks = new ConcurrentHashMap<>(); // Or model? Or store separately from inactive?

    @Autowired
    UUIDGenerator uuidGenerator;

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        String uuid = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid, body, currentState);

        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getId());
        Thread taskThread = new Thread(task);
        activeThreads.put(uuid, taskThread);
        activeTasks.put(uuid, task);
        taskThread.start();

        return taskResponseModel;
    }

    @Override
    public void runTask(String id)
    {
        Runnable r = activeTasks.get(id);
        Thread t  = new Thread(r);
        t.start();
        logger.info("Started task with id: " + id);
    }

    @Override
    public String getTaskBody(String uuid) {
        String body = activeTasks.get(uuid).getModel().getBody();
        logger.info("Get task body: " + body );
        return body;
    }

    @Override
    public String getTaskState(String uuid) {
        String state = activeTasks.get(uuid).getModel().getCurrentStatus();
        logger.info("Get task state: " + state);
        return state;
    }

    @Override
    public void deleteTask(String uuid) {
        //TODO close task and kill thread
        logger.info("Stop task: " + uuid);
        activeThreads.get(uuid).interrupt();
        activeThreads.remove(uuid); // (Object key, Object value)implementation?
        activeTasks.get(uuid).getModel().setCurrentStatus("STOPPED");
    }
}
