package com.ranran.persistence.task;

/**
 * @author ranran
 * 动态任务抽象类
 */
public abstract class DynamicTask implements Runnable {

    abstract void execute();

    @Override
    public void run() {
        execute();
    }
}
