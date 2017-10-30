package com.iba.schedule.threadpool.manager;

import com.iba.schedule.task.interfaces.CancellableRunnable;
import com.iba.schedule.threadpool.handler.RejectedExecutionHandlerImpl;
import com.iba.schedule.threadpool.monitor.MyMonitorThread;
import com.iba.schedule.threadpool.threadfactory.CustomThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.*;

@Service
public class ThreadPoolManager {

    private RejectedExecutionHandler rejectionHandler;
    private ThreadFactory threadFactory;
    private ThreadPoolExecutor executorPool;
    private MyMonitorThread monitor;
    private Thread monitorThread;

    private ConcurrentMap<String, Thread> runningThreads= new ConcurrentHashMap<>();

    ThreadPoolManager() {
        rejectionHandler = new RejectedExecutionHandlerImpl();
        threadFactory =  new CustomThreadFactory(runningThreads); //Executors.defaultThreadFactory();

        //TODO choose normal executor parameters
        executorPool = new ThreadPoolExecutor(2, 8, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), threadFactory, rejectionHandler);
        executorPool.allowCoreThreadTimeOut(true);
        monitor = new MyMonitorThread(executorPool, 5);
        monitorThread = new Thread(monitor);
    }

    //parameter should be runnable
    public void execute(CancellableRunnable task) {
        tryStartMonitor();
        executorPool.execute(task);
        monitor.toZeroTimer();
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

    private void tryStartMonitor() {
//        if (monitorThread == null) {
//            monitorThread = new Thread(monitor);
//        }
        if ("NEW".equals(monitorThread.getState().toString()) || "TERMINATED".equals(monitorThread.getState().toString())) {
            monitor.allowRun();
            monitorThread = new Thread(monitor);
            monitorThread.start();
        }
    }
}
