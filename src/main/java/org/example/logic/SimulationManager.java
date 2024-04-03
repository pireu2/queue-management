package org.example.logic;

import org.example.gui.SimulationView;
import org.example.model.Server;
import org.example.model.Task;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SimulationManager  implements Runnable{
    public int currentTime = 0;
    private SimulationView view;
    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int numberOfServers;
    private int numberOfClients;
    private StrategyPolicy strategyPolicy;
    private Scheduler scheduler;
    private ArrayList<Task> generatedTasks;

    public SimulationManager(SimulationView view, int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, StrategyPolicy strategyPolicy){
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.strategyPolicy = strategyPolicy;
        this.view = view;
        scheduler = new Scheduler(this.numberOfServers, this.numberOfClients, this.strategyPolicy);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks(this.numberOfClients);
        for(int i = 0; i < this.numberOfServers; i++){
            scheduler.addServer();
        }
    }

    public ArrayList<Task> getGeneratedTasks(){
        return generatedTasks;
    }
    public ArrayList<Server> getServers(){
        return scheduler.getServers();
    }

    public void run(){
        currentTime = 0;
        while(currentTime <= timeLimit){
            view.update();
            Iterator<Task> taskIterator = generatedTasks.iterator();
            while(taskIterator.hasNext()){
                Task task = taskIterator.next();
                if(task.getArrivalTime() == currentTime){
                    taskIterator.remove();
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

        for(Server server : scheduler.getServers()){
            server.setSimEnd(true);
        }
    }

    private void generateNRandomTasks(int n){
        for(int i = 0; i < n; i++){
            int arrivalTime = (int)(Math.random() * (timeLimit - 1)) + 1;
            int serviceTime = (int)(Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime);
            Task task = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(task);
        }
    }
}
