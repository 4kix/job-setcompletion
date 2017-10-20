package com.iba.schedule.threadpool.threadfactory;

import com.iba.schedule.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(CustomThreadFactory.class);

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

        Task task = getTaskFromWorker(r);
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0) {
            @Override
            public void interrupt() {
                task.cancel();
                super.interrupt();

            }
        };
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        addThreadToRunningThreads(task.getModel().getId(), t);

        return t;
    }

    private void addThreadToRunningThreads(String uuid, Thread thread) {
        runningThreads.put(uuid, thread);
    }


    //Kludge
    private Task getTaskFromWorker(Runnable worker){
        try {
            Class<?> workerClass = worker.getClass();

            Field firstTask= workerClass.getDeclaredField("firstTask");
            firstTask.setAccessible(true);
            Task value = (Task) firstTask.get(worker);

            return value;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.error("this shit doesn't work");
        }
        return null;
    }
}
