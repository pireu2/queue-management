package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    public void addTask(List<Server> serverList, Task task){
        int min = Integer.MAX_VALUE;
        Server minServer = null;
        for(Server server : serverList){
            if(server.getNoTasks() < min){
                min = server.getNoTasks();
                minServer = server;
            }
        }
        assert minServer != null;
        minServer.addTask(task);
    }
}
