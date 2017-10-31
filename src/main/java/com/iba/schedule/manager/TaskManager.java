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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private ConcurrentMap<String, Thread> activeThreads = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Task> activeTasks = new ConcurrentHashMap<>(); // Or model? Or store separately from inactive?

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
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getUUID());
        //Thread taskThread = new Thread(task);
        //activeThreads.put(uuid, taskThread);
        activeTasks.put(uuid, task);
        //taskThread.start();

        threadPoolManager.execute(task);

        return taskResponseModel;
    }

    @Override
    public void createTaskModel(String UUID) {
        clearTaskMap();
        TaskResponseModel taskResponseModel = new TaskResponseModel(UUID);
        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getUUID());
        //Thread taskThread = new Thread(task);
        //activeThreads.put(uuid, taskThread);
        activeTasks.put(UUID, task);
        //taskThread.start();
//        try {
            threadPoolManager.execute(task);
//        } catch (ExecutionException e) {
//            logger.error("Error: " + e);
//            throw new ExecuteTaskException(e);
//        }

    }


    @Override
    public void runTask(String id)
    {
        Task task = activeTasks.get(id);
        threadPoolManager.execute(task);
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
        //parse model or status to the TaskController as a string
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
    public void deleteTask(String uuid) {
        //TODO close task and kill thread
        //activeThreads.get(uuid).interrupt();
        //activeThreads.remove(uuid); // (Object key, Object value)implementation?
        threadPoolManager.stopThread(uuid);
        activeTasks.get(uuid).getModel().setCurrentStatus("STOPPED");
    }

    public void clearTaskMap()
    {
        if (activeTasks.size() == 100)
        {
            for (ConcurrentHashMap.Entry<String, Task> entry: activeTasks.entrySet())
            {
                if (entry.getValue().getModel().getCurrentStatus().equals("OK") || entry.getValue().getModel().getCurrentStatus().equals("INTERRUPTED"))
                {
                    activeTasks.remove(entry.getKey());
                }
            }
        }
    }

    @Override
    public void getJVMThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }
}
