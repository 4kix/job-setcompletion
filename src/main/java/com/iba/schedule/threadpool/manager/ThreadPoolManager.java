package com.iba.schedule.threadpool.manager;

import com.iba.schedule.threadpool.handler.RejectedExecutionHandlerImpl;
import com.iba.schedule.threadpool.monitor.MyMonitorThread;
import com.iba.schedule.threadpool.threadfactory.CustomThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
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
        private static final ThreadPoolManager instance = new ThreadPoolManager();
    }

    public static ThreadPoolManager threadPoolManager()  {
        return new ThreadPoolManager();
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
