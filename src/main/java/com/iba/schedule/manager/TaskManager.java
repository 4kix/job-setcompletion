package com.iba.schedule.manager;


import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.threadpool.manager.ThreadPoolManager;
import com.iba.schedule.util.UUIDGenerator;
import com.iba.schedule.exception.ExecuteTaskException;
import com.iba.schedule.exception.NoSuchTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        String UUID = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(UUID, body, currentState);

        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getId());
        //Thread taskThread = new Thread(task);
        //activeThreads.put(uuid, taskThread);
        activeTasks.put(UUID, task);
        //taskThread.start();

        try {
            ThreadPoolManager.getInstance().execute(task);
        } catch (ExecutionException e) {
            logger.error("Error: " + e);
            throw new ExecuteTaskException(e);
        }

        return taskResponseModel;
    }

    @Override
    public void createTaskModel(String UUID) {

        TaskResponseModel taskResponseModel = new TaskResponseModel(UUID);
        Task task = new Task(taskResponseModel);
        logger.info("Create new task(id of TaskResponseModel): " + task.getModel().getId());
        //Thread taskThread = new Thread(task);
        //activeThreads.put(uuid, taskThread);
        activeTasks.put(UUID, task);
        //taskThread.start();
        try {
            ThreadPoolManager.getInstance().execute(task);
        } catch (ExecutionException e) {
            logger.error("Error: " + e);
            throw new ExecuteTaskException(e);
        }

    }


    @Override
    public void runTask(String id)
    {
        Runnable r = null;
        try {
            r = activeTasks.get(id);
        } catch (NoSuchElementException e) {
            logger.error("Error: " + e);
            throw new NoSuchTaskException(e);
        }
        Thread t  = new Thread(r);
        t.start();
        logger.info("Started task with id: " + id);
    }

    @Override
    public String getTaskBody(String uuid) {
        String body = null;
        try {
            body = activeTasks.get(uuid).getModel().getBody();
        } catch (NoSuchElementException e) {
            logger.error("Error: " + e);
            throw new NoSuchTaskException(e);
        }
        logger.info("Get task body: " + body );
        return body;
    }

    @Override
    public Object getTaskState(String uuid) {
//        //parse model or status to the TaskController as a string
//        try {
//            if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("PROCESSING")) {
//                return "PROCESSING";
//            } else if (activeTasks.get(uuid).getModel().getCurrentStatus().equals("OK")) {
//                logger.info("MODEL: " + activeTasks.get(uuid).getModel());
//                return activeTasks.get(uuid).getModel();
//            }
//        } catch (NoSuchElementException e) {
//            logger.error("Error: " + e);
//            throw new NoSuchTaskException(e);
//        }
//        //String state = activeTasks.get(uuid).getModel().getCurrentStatus();
//        logger.info("Get task state: ");
////        return null;

        TaskResponseModel taskModel = null;
        Task task = null;

        try {
            task = activeTasks.get(uuid);
        } catch (NoSuchElementException e) {
            logger.error("Error: " + e);
            throw new NoSuchTaskException(e);
        }

        taskModel = task.getModel();

        logger.info("TASK: " + task.toString());
        logger.info("TASK_MODEL:" +taskModel.toString()    );


        if (taskModel.getCurrentStatus().equals("PROCESSING")) {
            return "PROCESSING";
        } else if (taskModel.getCurrentStatus().equals("OK")) {
            logger.info("MODEL: " + activeTasks.get(uuid).getModel());
            return activeTasks.get(uuid).getModel();
        }
        logger.info("Get task state: ");
        return null;

    }

    @Override
    public void deleteTask(String uuid) {
        //TODO close task and kill thread
        //activeThreads.get(uuid).interrupt();
        //activeThreads.remove(uuid); // (Object key, Object value)implementation?
        try {
            ThreadPoolManager.getInstance().stopThread(uuid);
            activeTasks.get(uuid).getModel().setCurrentStatus("STOPPED");
        } catch (NoSuchElementException e) {
            logger.error("Error: " + e);
            throw new NoSuchTaskException(e);
        }
    }

    @Override
    public void getJVMThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }
}
