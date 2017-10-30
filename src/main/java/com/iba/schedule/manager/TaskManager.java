package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.task.interfaces.CancellableRunnable;
import com.iba.schedule.threadpool.manager.ThreadPoolManager;
import com.iba.schedule.util.UUIDGenerator;
import com.iba.schedule.exception.ExecuteTaskException;
import com.iba.schedule.exception.NoSuchTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private ConcurrentMap<String, Task> activeTasks = new ConcurrentHashMap<>();

    @Autowired
    UUIDGenerator uuidGenerator;
    @Autowired
    ThreadPoolManager threadPoolManager;

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        String uuid = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid, body, currentState);

        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getUUID());
        activeTasks.put(uuid, task);

        threadPoolManager.execute(task);

        return taskResponseModel;
    }

    @Override
    public void createTaskModel(String UUID) {

        TaskResponseModel taskResponseModel = new TaskResponseModel(UUID);
        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getUUID());
        activeTasks.put(UUID, task);

        threadPoolManager.execute(task);

    }

    @Override
    public String getTaskBody(String uuid) {
        String body = activeTasks.get(uuid).getModel().getBody();
        logger.info("Get task body: " + body );
        return body;
    }

    @Override
    public String getTaskState(String uuid) {
        if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("RUNNING")) {
            return "RUNNING";
        } else if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("OK")) {
            return activeTasks.get(uuid).getModel().toString();
        }
        //String state = activeTasks.get(uuid).getModel().getCurrentStatus();
        logger.info("Get task state: ");
        return null;
    }

    @Override
    public void stopTask(String uuid) {
        //TODO close task and kill thread
        //activeThreads.remove(uuid); // (Object key, Object value)implementation?
        threadPoolManager.stopThread(uuid);
        activeTasks.get(uuid).getModel().setCurrentStatus("STOPPED");
    }
}
