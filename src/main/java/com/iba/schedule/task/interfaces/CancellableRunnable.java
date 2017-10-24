package com.iba.schedule.task.interfaces;

public interface CancellableRunnable extends Runnable { //why can't extend?

    public void cancel();

    @Override
    public void run();
}
