package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
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

    public List<Server> getServers(){
        return servers;
    }

    public void addServer(){
        if(servers.size() == maxNoServers){
            return;
        }
        Server server = new Server(maxTasksPerServer);
        servers.add(server);
    }

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }
}
