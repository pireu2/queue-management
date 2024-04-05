package org.example.logic;

import org.example.gui.SimulationView;
import org.example.model.Server;
import org.example.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationManager  implements Runnable{
    public int currentTime = 0;
    private final SimulationView view;
    private final int timeLimit;
    private final int maxProcessingTime;
    private final int minProcessingTime;
    private final int maxArrivalTime;
    private final int minArrivalTime;
    private final int numberOfServers;
    private final Scheduler scheduler;
    private final ArrayList<Task> generatedTasks;
    private final Logger logger;
    private int peakHour = -1;
    private final ExecutorService executorService;

    public SimulationManager(SimulationView view, int timeLimit, int maxProcessingTime, int minProcessingTime, int maxArrivalTime,int minArrivalTime,int numberOfServers, int numberOfClients, StrategyPolicy strategyPolicy){
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.view = view;
        executorService = Executors.newFixedThreadPool(100);
        scheduler = new Scheduler(numberOfServers, numberOfClients, strategyPolicy);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks(numberOfClients);
        for(int i = 0; i < numberOfServers; i++){
            Server server = new Server(numberOfClients);
            scheduler.addServer(server);
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
        logger.log("Simulation started");
        startServers();
        try {
            logger.writer = new BufferedWriter(new FileWriter("log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentTime = 0;
        int peakServer = 0;
        while(currentTime <= timeLimit){
            logger.logTask(scheduler, currentTime);
            view.update();
            if(generatedTasks.isEmpty() && scheduler.allServersEmpty()){
                break;
            }
            if (peakServer < scheduler.getServersActive()) {
                peakServer = scheduler.getServersActive();
                peakHour = currentTime;
            }
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
        stop();
    }

    private void startServers(){
        for(Server server : scheduler.getServers()){
            executorService.submit(server);
        }
    }
    private void stop(){
        for(Server server : scheduler.getServers()){
            server.setSimEnd(true);
        }
        double averageWaitingTime = getAverageWaitingTime();
        double averageServiceTime = getAverageServiceTime();
        logger.log("Simulation ended");
        logger.log("Average waiting time: " + averageWaitingTime);
        logger.log("Average service time: " + averageServiceTime);
        logger.log("Peak hour: " + peakHour);
        view.showResults(averageWaitingTime, averageServiceTime, peakHour);
    }
    private void generateNRandomTasks(int n){
        for(int i = 0; i < n; i++){
            int arrivalTime = (int)(Math.random() * (maxArrivalTime - minArrivalTime)) + minArrivalTime;
            int serviceTime = (int)(Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime);
            Task task = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(task);
        }
    }
    private double getAverageServiceTime(){
        double sum = 0;
        for(Server server : scheduler.getServers()){
            sum += server.getServiceTime();
        }
        return sum / numberOfServers;
    }
    private double getAverageWaitingTime(){
        double sum = 0;
        for(Server server : scheduler.getServers()){
            sum += server.getAverageWaitingTime();
        }
        return sum / numberOfServers;
    }

    private static class Logger{
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
        public void logTask(Scheduler scheduler, int currentTime){
            this.log(
                    "Time: " + currentTime
            );
            for(Server server : scheduler.getServers()){
                this.log(
                        "\tServer " + scheduler.getServers().indexOf(server) + " has " + server.getNoTasks() + " tasks and a waiting period of " + server.getWaitingPeriod()
                );
                for(Task task : server.getTasks()){
                    this.log(
                            "\t\tTask " + task.getId() + " has " + task.getServiceTime() + " remaining service time and arrived at " + task.getArrivalTime()
                    );;
                }
            }
        }
    }
}
