package org.example.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private ArrayBlockingQueue<Task> tasks;
    private AtomicInteger noTasks;
    private AtomicInteger waitingPeriod;

    public Server(int maxNoTasks){
        tasks = new ArrayBlockingQueue<Task>(maxNoTasks);
        noTasks = new AtomicInteger(0);
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
        int currentTime = 0;
        Task t = getCurrentTask();

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            currentTime+=1;
            if (currentTime == t.getServiceTime() ){
                noTasks.addAndGet(-1);
                waitingPeriod.addAndGet(-t.getServiceTime());
                currentTime = 0;
                t = getCurrentTask();
            }
        }
    }

    private Task getCurrentTask(){
        Task t = null;
        try {
            t = tasks.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return t;
    }
}
