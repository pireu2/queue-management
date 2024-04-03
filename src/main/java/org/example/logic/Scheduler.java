package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, StrategyPolicy policy){
        servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        if(policy == StrategyPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        else{
            strategy = new TimeStrategy();
        }
    }
    public boolean allServersEmpty(){
        for(Server server : servers){
            if(server.getNoTasks() != 0){
                return false;
            }
        }
        return true;
    }

    public int getServersActive(){
        int count = 0;
        for(Server server : servers){
            if(server.getNoTasks() != 0){
                count++;
            }
        }
        return count;
    }

    public ArrayList<Server> getServers(){
        return servers;
    }

    public void addServer(){
        if(servers.size() == maxNoServers){
            return;
        }
        Server server = new Server(maxTasksPerServer);
        servers.add(server);
        Thread t = new Thread(server);
        t.start();
    }

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }
}
