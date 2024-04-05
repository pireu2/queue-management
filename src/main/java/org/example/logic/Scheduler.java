package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public class Scheduler {
    private final ArrayList<Server> servers;
    private final int maxNoServers;
    private final Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, StrategyPolicy policy){
        servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
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

    public void addServer(Server server){
        if(servers.size() == maxNoServers){
            return;
        }
        servers.add(server);
    }

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }
}
