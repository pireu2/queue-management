package org.example.model;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private final ArrayBlockingQueue<Task> tasks;
    private final AtomicInteger noTasks;
    private final AtomicInteger waitingPeriod;
    private final ArrayList<Integer> waitingTimes;
    private final AtomicInteger totalServiceTime;
    private AtomicBoolean simEnd;
    public void setSimEnd(boolean simEnd) {
        this.simEnd = new AtomicBoolean(simEnd);
    }

    public Server(int maxNoTasks){
        tasks = new ArrayBlockingQueue<>(maxNoTasks);
        noTasks = new AtomicInteger(0);
        waitingPeriod = new AtomicInteger(0);
        waitingTimes = new ArrayList<>();
        simEnd = new AtomicBoolean(false);
        totalServiceTime = new AtomicInteger(0);
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
    public double getAverageWaitingTime(){
        double sum = 0;
        for(Integer i : waitingTimes){
            sum += i;
        }
        return sum / waitingTimes.size();
    }
    public int getServiceTime(){
        return  totalServiceTime.get() ;
    }
    public void run(){
        Task t = getCurrentTask();
        while(!simEnd.get() || t != null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(t == null){
                t = getCurrentTask();
                continue;
            }
            incrementWaitingTime();
            t.decrementServiceTime();
            totalServiceTime.getAndIncrement();
            waitingPeriod.addAndGet(-1);
            if (t.getServiceTime() == 0){
                noTasks.addAndGet(-1);
                try {
                    tasks.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                waitingTimes.add(t.getWaitingTime());
                t = getCurrentTask();
            }
        }
    }

    private Task getCurrentTask(){
        Task t;
        t= tasks.peek();
        return t;
    }
    private void incrementWaitingTime(){
        for(Task t : tasks){
            t.incrementWaitingTime();
        }
    }
}
