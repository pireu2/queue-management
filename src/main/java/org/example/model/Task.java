package org.example.model;

public class Task {
    private final int Id;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int Id, int arrivalTime, int serviceTime){
        this.Id = Id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public String toString(){
        return "Task " + Id + " [at=" + arrivalTime + ", st=" + serviceTime + "]";
    }

    public int getId(){
        return Id;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getServiceTime(){
        return serviceTime;
    }
    public void decrementServiceTime(){
        if(serviceTime > 0){
            serviceTime--;
        }
    }
}
