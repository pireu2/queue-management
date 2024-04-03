package org.example.gui;

import org.example.logic.StrategyPolicy;

import javax.swing.*;
import java.awt.*;

public class Configuration extends JFrame {

    private JTextField timeLimitField;
    private JTextField maxProcessingTimeField;
    private JTextField minProcessingTimeField;
    private JTextField numberOfServersField;
    private JTextField numberOfClientsField;
    private JComboBox<String> strategyBox;
    private JButton startButton;

    public Configuration(){
        setTitle("Queue Management System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        prepareFields();
    }
    private void prepareFields(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Queue Management System Configuration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        panel.add(titlePanel);

        JPanel timeLimitPanel = new JPanel();
        timeLimitPanel.setLayout(new GridLayout(1, 2));
        timeLimitPanel.setBackground(Color.WHITE);
        JLabel timeLimitLabel = new JLabel("Time limit");
        timeLimitField = new JTextField(5);
        timeLimitPanel.add(timeLimitLabel);
        timeLimitPanel.add(timeLimitField);
        panel.add(timeLimitPanel);

        JPanel maxProcessingTimePanel = new JPanel();
        maxProcessingTimePanel.setLayout(new GridLayout(1, 2));
        maxProcessingTimePanel.setBackground(Color.WHITE);
        JLabel maxProcessingTimeLabel = new JLabel("Max processing time");
        maxProcessingTimeField = new JTextField(5);
        maxProcessingTimePanel.add(maxProcessingTimeLabel);
        maxProcessingTimePanel.add(maxProcessingTimeField);
        panel.add(maxProcessingTimePanel);

        JPanel minProcessingTimePanel = new JPanel();
        minProcessingTimePanel.setLayout(new GridLayout(1, 2));
        minProcessingTimePanel.setBackground(Color.WHITE);
        JLabel minProcessingTimeLabel = new JLabel("Min processing time");
        minProcessingTimeField = new JTextField(5);
        minProcessingTimePanel.add(minProcessingTimeLabel);
        minProcessingTimePanel.add(minProcessingTimeField);
        panel.add(minProcessingTimePanel);

        JPanel numberOfServersPanel = new JPanel();
        numberOfServersPanel.setLayout(new GridLayout(1, 2));
        numberOfServersPanel.setBackground(Color.WHITE);
        JLabel numberOfServersLabel = new JLabel("Number of servers");
        numberOfServersField = new JTextField(5);
        numberOfServersPanel.add(numberOfServersLabel);
        numberOfServersPanel.add(numberOfServersField);
        panel.add(numberOfServersPanel);

        JPanel numberOfClientsPanel = new JPanel();
        numberOfClientsPanel.setLayout(new GridLayout(1, 2));
        numberOfClientsPanel.setBackground(Color.WHITE);
        JLabel numberOfClientsLabel = new JLabel("Number of clients");
        numberOfClientsField = new JTextField(5);
        numberOfClientsPanel.add(numberOfClientsLabel);
        numberOfClientsPanel.add(numberOfClientsField);
        panel.add(numberOfClientsPanel);

        JPanel strategyPanel = new JPanel();
        strategyPanel.setLayout(new GridLayout(1, 2));
        strategyPanel.setBackground(Color.WHITE);
        JLabel strategyLabel = new JLabel("Strategy");
        String[] strategies = {"Shortest Queue", "Time"};
        strategyBox = new JComboBox<>(strategies);
        strategyPanel.add(strategyLabel);
        strategyPanel.add(strategyBox);
        panel.add(strategyPanel);


        timeLimitField.setText("20");
        maxProcessingTimeField.setText("5");
        minProcessingTimeField.setText("2");
        numberOfServersField.setText("3");
        numberOfClientsField.setText("20");

        JLabel emptySeparator = new JLabel(" ");
        panel.add(emptySeparator);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e ->{
            int timeLimit;
            int maxProcessingTime;
            int minProcessingTime;
            int numberOfServers;
            int numberOfClients;
            StrategyPolicy strategyPolicy = strategyBox.getSelectedIndex() == 0 ? StrategyPolicy.SHORTEST_QUEUE : StrategyPolicy.SHORTEST_TIME;

            try{
                timeLimit = Integer.parseInt(timeLimitField.getText());
                maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText());
                minProcessingTime = Integer.parseInt(minProcessingTimeField.getText());
                numberOfServers = Integer.parseInt(numberOfServersField.getText());
                numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                if(timeLimit <= 0 || maxProcessingTime <= 0 || minProcessingTime <= 0 || numberOfServers <= 0 || numberOfClients <= 0){
                    throw new NumberFormatException();
                }
                if(maxProcessingTime < minProcessingTime){
                    throw new NumberFormatException();
                }
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Invalid input");
                return;
            }
            Configuration.this.dispose();
            SimulationView simulationView = new SimulationView(timeLimit, maxProcessingTime, minProcessingTime, numberOfServers, numberOfClients, strategyPolicy);
            simulationView.setVisible(true);
        });
        panel.add(startButton);

        setContentPane(panel);
    }
}
