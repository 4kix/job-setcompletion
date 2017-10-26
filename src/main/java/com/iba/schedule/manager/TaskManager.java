package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.task.interfaces.CancellableRunnable;
import com.iba.schedule.threadpool.manager.ThreadPoolManager;
import com.iba.schedule.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
        //TODO rewrite for creating new, not for starting existing task
        logger.info("Run task with id: " + UUID);
        Task task = activeTasks.get(UUID);
        threadPoolManager.execute(task);
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
    public Object getTaskState(String uuid) {
        //parse model or status to the TaskController as a string
        if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("PROCESSING")) {
            return "PROCESSING";
        } else if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("OK")) {
            return activeTasks.get(uuid).getModel();
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

    @Override
    public void getJVMThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }
}
