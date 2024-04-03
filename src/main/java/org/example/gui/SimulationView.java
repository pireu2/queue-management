package org.example.gui;

import org.example.logic.SimulationManager;
import org.example.logic.StrategyPolicy;
import org.example.model.Server;
import org.example.model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationView extends JFrame {
    private final SimulationManager manager;
    private final int numberOfServers;

    public SimulationView(int timeLimit,int maxProcessingTime,int minProcessingTime,int maxArrivalTime, int minArrivalTime,int numberOfServers,int numberOfClients,StrategyPolicy strategyPolicy){
        setTitle("Queue Management System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.numberOfServers = numberOfServers;
        manager = new SimulationManager(this,timeLimit,maxProcessingTime,minProcessingTime,maxArrivalTime,minArrivalTime,numberOfServers,numberOfClients,strategyPolicy);
        prepareGui();
        Thread t = new Thread(manager);
        t.start();
    }

    public void showResults(double averageWaitingTime, double averageServiceTime, int peakHour){
        JOptionPane.showMessageDialog(this, "Average waiting time: " + averageWaitingTime + "\nAverage service time: " + averageServiceTime + "\nPeak hour: " + peakHour);
    }

    public void update(){
        getContentPane().removeAll();
        prepareGui();
        revalidate();
        repaint();
    }
    private void prepareGui(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Queue Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        JLabel timeLabel = new JLabel("Time: " + manager.currentTime);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(timeLabel);
        panel.add(prepareServersPanel());
        panel.add(prepareWaitingList());
        setContentPane(panel);
    }


    private JPanel prepareServersPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(numberOfServers, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());


        for(int i = 0; i < numberOfServers; i++){
            JPanel serverPanel = new JPanel();
            serverPanel.setBackground(Color.WHITE);
            Server server = manager.getServers().get(i);
            serverPanel.setBorder(BorderFactory.createTitledBorder("Server " + i + " - waiting time: " + server.getWaitingPeriod() + " tasks: " + server.getNoTasks()));
            JList<String> tasksList = new JList<>();

            Task[] tasks = server.getTasks();
            DefaultListModel<String> model = new DefaultListModel<>();
            for(Task t : tasks){
                model.addElement(t.toString() + " remaining time: " + t.getServiceTime());
            }
            tasksList.setModel(model);
            tasksList.setBackground(panel.getBackground());

            serverPanel.add(tasksList);
            panel.add(serverPanel);
        }
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel prepareWaitingList(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Waiting List"));

        ArrayList<Task> tasks = manager.getGeneratedTasks();
        panel.setLayout(new BorderLayout());

        JList<String> waitingList = new JList<>();
        DefaultListModel<String> model = new DefaultListModel<>();
        for(Task t : tasks){
            model.addElement(t.toString());
        }
        waitingList.setBackground(panel.getBackground());
        waitingList.setModel(model);

        JScrollPane scrollPane = new JScrollPane(waitingList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
