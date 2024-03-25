package org.example.logic;

import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager  implements Runnable{
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private StrategyPolicy strategyPolicy;
    private Scheduler scheduler;
    private List<Task> generatedTasks;

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, StrategyPolicy strategyPolicy){
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.strategyPolicy = strategyPolicy;
        scheduler = new Scheduler(numberOfServers, numberOfClients, strategyPolicy);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks(numberOfClients);
    }

    public void run(){
        int currentTime = 0;
        while(currentTime < timeLimit){
            for(Task task : generatedTasks){
                if(task.getArrivalTime() == currentTime){
                    scheduler.dispatchTask(task);
                }
            }
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            currentTime++;
        }
    }

    private void generateNRandomTasks(int n){
        for(int i = 0; i < n; i++){
            int arrivalTime = (int)(Math.random() * timeLimit);
            int serviceTime = (int)(Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime);
            Task task = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(task);
        }
    }
}
