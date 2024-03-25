package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public interface Strategy {
    public  void addTask(List<Server> serverList, Task task);
}
