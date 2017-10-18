package com.iba.schedule.threadpool.threadfactory;

import com.iba.schedule.task.Task;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private ConcurrentMap<String, Thread> runningThreads;

    public CustomThreadFactory (ConcurrentMap<String, Thread> threadsMap) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
        this.runningThreads = threadsMap;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);

        try {
            Task task = getTaskFromWorker(r);
            addThreadToRunningThreads(task.getModel().getId(), t);
        } catch (NoSuchFieldException e) {
            System.err.println("this shit doesn't work");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return t;
    }

    private void addThreadToRunningThreads(String uuid, Thread thread) {
        runningThreads.put(uuid, thread);
    }


    //Kludge
    private Task getTaskFromWorker(Runnable worker) throws NoSuchFieldException, IllegalAccessException {
        Class<?> workerClass = worker.getClass();

        Field firstTask= workerClass.getDeclaredField("firstTask");
        firstTask.setAccessible(true);
        Task value = (Task) firstTask.get(worker);

        return value;
    }
}
