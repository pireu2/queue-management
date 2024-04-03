package org.example.model;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private ArrayBlockingQueue<Task> tasks;
    private AtomicInteger noTasks;
    private AtomicInteger waitingPeriod;

    private boolean simEnd;
    public void setSimEnd(boolean simEnd) {
        this.simEnd = simEnd;
    }

    public Server(int maxNoTasks){
        tasks = new ArrayBlockingQueue<Task>(maxNoTasks);
        noTasks = new AtomicInteger(0);
        waitingPeriod = new AtomicInteger(0);
        simEnd = false;
    }
    public void addTask(Task t){
        tasks.add(t);
        noTasks.getAndIncrement();
        waitingPeriod.addAndGet(t.getServiceTime());
    }
    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }
    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }
    public int getNoTasks(){
        return noTasks.get();
    }
    public void run(){
        Task t = getCurrentTask();

        while(!simEnd){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(t == null){
                t = getCurrentTask();
                continue;
            }
            t.decrementServiceTime();
            waitingPeriod.addAndGet(-1);
            if (t.getServiceTime() == 0){
                noTasks.addAndGet(-1);
                try {
                    tasks.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                t = getCurrentTask();
            }
        }
    }

    private Task getCurrentTask(){
        Task t = null;
        t= tasks.peek();
        return t;
    }
}
