package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.threadpool.manager.ThreadPoolManager;
import com.iba.schedule.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private ConcurrentMap<String, Task> tasks = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Future<?>> futures = new ConcurrentHashMap<>();

    @Autowired
    UUIDGenerator uuidGenerator;
    @Autowired
    ThreadPoolManager threadPoolManager;

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        clearTaskMap();
        String uuid = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid, body, currentState);

        Task task = new Task(taskResponseModel);
        Future<?>  future = threadPoolManager.submitRunnable(task);
        futures.put(uuid, future);

        logger.info("Create new task(id of TaskResponseModel): " + task.getRunnableUUID());
        tasks.put(uuid, task);

        return taskResponseModel;
    }

    @Override
    public void createTaskModel(String uuid) {
        clearTaskMap();
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid);
        Task task = new Task(taskResponseModel);
        Future<?>  future = threadPoolManager.submitRunnable(task);
        logger.info("Create new task(id of TaskResponseModel): " + task.getRunnableUUID());
        futures.put(uuid, future);
        tasks.put(uuid, task);

    }

    @Override
    public String getTaskBody(String uuid) {
        String body = tasks.get(uuid).getModel().getBody();
        logger.info("Get task body: " + body );
        return body;
    }

    @Override
    public String getTaskState(String uuid) {
        if (tasks.get(uuid).getModel().getCurrentStatus().equals("RUNNING")) {
            return "RUNNING";
        } else if (tasks.get(uuid).getModel().getCurrentStatus().equals("OK")) {
            return tasks.get(uuid).getModel().toString();
        }
        //String state = tasks.get(uuid).getModel().getCurrentStatus();
        logger.info("Get task state: ");
        return null;
    }

    @Override
    public void stopTask(String uuid) {
        //TODO close task and kill thread
        tasks.get(uuid).cancel();
        futures.get(uuid).cancel(true);
        futures.remove(uuid);
        tasks.get(uuid).getModel().setCurrentStatus("STOPPED");
    }

    public void clearTaskMap()
    {
        if (tasks.size() == 100)
        {
            for (ConcurrentHashMap.Entry<String, Task> entry: tasks.entrySet())
            {
                if (entry.getValue().getModel().getCurrentStatus().equals("OK") || entry.getValue().getModel().getCurrentStatus().equals("INTERRUPTED"))
                {
                    tasks.remove(entry.getKey());
                }
            }
        }
    }
}
