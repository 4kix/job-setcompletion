package com.iba.schedule.task.interfaces;

public interface CancellableRunnable extends Runnable { //why can't extend?

    void cancel();

    @Override
    void run();
}
