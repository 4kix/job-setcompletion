package com.iba.schedule.threadpool.manager;

import com.iba.schedule.task.Task;
import com.iba.schedule.threadpool.handler.RejectedExecutionHandlerImpl;
import com.iba.schedule.threadpool.monitor.MyMonitorThread;
import com.iba.schedule.threadpool.threadfactory.CustomThreadFactory;

import java.util.concurrent.*;

public class ThreadPoolManager {

    private RejectedExecutionHandler rejectionHandler;
    private ThreadFactory threadFactory;
    private ThreadPoolExecutor executorPool;
    private MyMonitorThread monitor;

    private ConcurrentMap<String, Thread> runningThreads= new ConcurrentHashMap<>();

    ThreadPoolManager() {
        rejectionHandler = new RejectedExecutionHandlerImpl();
        threadFactory =  new CustomThreadFactory(runningThreads); //Executors.defaultThreadFactory();

        //TODO choose normal executor parameters
        executorPool = new ThreadPoolExecutor(2, 8, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), threadFactory, rejectionHandler);
        executorPool.allowCoreThreadTimeOut(true);
        monitor = new MyMonitorThread(executorPool, 5);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
    }

    private static class SingletonHolder {
        public static final ThreadPoolManager instance = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance()  {
        return SingletonHolder.instance;
    }

    //parameter should be runnable
    public void execute(Runnable task) {
        executorPool.execute(task);
    }

    public void shutdownMonitor() {
        monitor.shutdown();

    }

    public void stopThread(String uuid)  {
        runningThreads.get(uuid).interrupt();
        runningThreads.remove(uuid);
    }

    public void shutdownThread(){
        executorPool.shutdown();
    }
}
