package com.iba.schedule.task.interfaces;

public interface CancellableRunnable extends Runnable {

    void cancel();

    String getRunnableUUID();

    @Override
    void run();
}
