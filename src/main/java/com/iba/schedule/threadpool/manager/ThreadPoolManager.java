package com.iba.schedule.threadpool.manager;

import com.iba.schedule.task.interfaces.CancellableRunnable;
import com.iba.schedule.threadpool.handler.RejectedExecutionHandlerImpl;
import com.iba.schedule.threadpool.monitor.MyMonitorThread;
import com.iba.schedule.threadpool.threadfactory.CustomThreadFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ThreadPoolManager {

    private RejectedExecutionHandler rejectionHandler;
    private ThreadFactory threadFactory;
    private ThreadPoolExecutor executorPool;
    private MyMonitorThread monitor;
    private Thread monitorThread;

    ThreadPoolManager() {
        rejectionHandler = new RejectedExecutionHandlerImpl();
        threadFactory =  new CustomThreadFactory(); //Executors.defaultThreadFactory();

        //TODO choose normal executor parameters
        executorPool = new ThreadPoolExecutor(2, 8, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), threadFactory, rejectionHandler);
        executorPool.allowCoreThreadTimeOut(true);
        monitor = new MyMonitorThread(executorPool, 5);
        monitorThread = new Thread(monitor);
    }

    public void shutdownMonitor() {
        monitor.shutdown();

    }

    public void shutdownThread(){
        executorPool.shutdown();
    }

    private void tryStartMonitor() {
        if ("NEW".equals(monitorThread.getState().toString()) || "TERMINATED".equals(monitorThread.getState().toString())) {
            monitor.allowRun();
            monitorThread = new Thread(monitor);
            monitorThread.start();
        }
    }

    public Future<?> submitRunnable(CancellableRunnable runnable) {
        tryStartMonitor();
        monitor.toZeroTimer();
        return executorPool.submit(runnable);
    }
}
