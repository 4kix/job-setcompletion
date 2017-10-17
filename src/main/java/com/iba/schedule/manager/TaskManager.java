package com.iba.schedule.manager;

import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.task.Task;
import com.iba.schedule.threadpool.manager.ThreadPoolManager;
import com.iba.schedule.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class TaskManager extends AbstractManager<TaskResponseModel> {

    //private ConcurrentMap<String, Thread> activeThreads = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Task> activeTasks = new ConcurrentHashMap<>(); // Or model? Or store separately from inactive?

    //private ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();

    @Autowired
    UUIDGenerator uuidGenerator;

    @Override
    public TaskResponseModel createTaskModel(String body, String currentState) {
        String uuid = uuidGenerator.generateUUID();
        TaskResponseModel taskResponseModel = new TaskResponseModel(uuid, body, currentState);

        Task task = new Task(taskResponseModel);
        //Thread taskThread = new Thread(task);
        //activeThreads.put(uuid, taskThread);
        activeTasks.put(uuid, task);
        //taskThread.start();

        ThreadPoolManager.getInstance().execute(task);

        return taskResponseModel;
    }

    @Override
    public String getTaskBody(String uuid) {
        String body = activeTasks.get(uuid).getModel().getBody();
        return body;
    }

    @Override
    public String getTaskState(String uuid) {
        String state = activeTasks.get(uuid).getModel().getCurrentStatus();
        return state;
    }

    @Override
    public void deleteTask(String uuid) {
        //TODO close task and kill thread
        //activeThreads.get(uuid).interrupt();
        //activeThreads.remove(uuid); // (Object key, Object value)implementation?
        ThreadPoolManager.getInstance().stopThread(uuid);
        activeTasks.get(uuid).getModel().setCurrentStatus("STOPPED");
    }


    @Override
    public void getJVMThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println(threadSet);
    }


}
