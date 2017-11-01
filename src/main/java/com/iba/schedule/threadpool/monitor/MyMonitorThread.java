package com.iba.schedule.threadpool.monitor;

import java.util.concurrent.ThreadPoolExecutor;

public class MyMonitorThread implements Runnable
{
    private ThreadPoolExecutor executor;

    private int seconds;

    private volatile int timer;

    private boolean run = true;

    public MyMonitorThread(ThreadPoolExecutor executor, int delay)
    {
        this.executor = executor;
        this.seconds = delay;
        this.timer = 15 / delay;
    }

    public void shutdown(){
        this.run = false;
        toZeroTimer();
    }

    public void allowRun() {
        this.run = true;
    }


    @Override
    public void run()
    {
        while(run){
            if (this.executor.getActiveCount() == 0) {
                timer --;
                if (timer <= 0 ) shutdown();
            }
            System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));

            try {
                Thread.sleep(seconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void toZeroTimer() {
        timer = 15 / seconds;
    }
}