package org.example.logic;

import org.example.gui.SimulationView;
import org.example.model.Server;
import org.example.model.Task;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    private Logger logger;

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
        logger = new Logger();
    }

    public ArrayList<Task> getGeneratedTasks(){
        return generatedTasks;
    }
    public ArrayList<Server> getServers(){
        return scheduler.getServers();
    }

    public void run(){
        try {
            logger.writer = new BufferedWriter(new FileWriter("log.txt")); // Overwrite the existing content of the log file
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentTime = 0;
        while(currentTime <= timeLimit){
            logger.log(
                    "Time: " + currentTime
            );
            for(Server server : scheduler.getServers()){
                logger.log(
                        "\tServer " + scheduler.getServers().indexOf(server) + " has " + server.getNoTasks() + " tasks and a waiting period of " + server.getWaitingPeriod()
                );
                for(Task task : server.getTasks()){
                    logger.log(
                            "\t\tTask " + task.getId() + " has " + task.getServiceTime() + " remaining service time and arrived at " + task.getArrivalTime()
                    );;
                }
            }
            view.update();
            Iterator<Task> taskIterator = generatedTasks.iterator();
            while(taskIterator.hasNext()){
                Task task = taskIterator.next();
                if(task.getArrivalTime() == currentTime + 1){
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

    private class Logger{
        private BufferedWriter writer;

        public Logger(){
            try {
                writer = new BufferedWriter(new FileWriter("log.txt", true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void log(String message){
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
