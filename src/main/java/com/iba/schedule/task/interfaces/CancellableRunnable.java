package com.iba.schedule.task.interfaces;

public interface CancellableRunnable extends Runnable { //why can't extend?

    void cancel();

    String getRunnableUUID();

    @Override
    void run();
}
