package org.example.gui;

import org.example.logic.StrategyPolicy;

import javax.swing.*;
import java.awt.*;

public class ConfigurationView extends JFrame {

    private JTextField timeLimitField;
    private JTextField maxProcessingTimeField;
    private JTextField minProcessingTimeField;
    private JTextField numberOfServersField;
    private JTextField numberOfClientsField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JComboBox<String> strategyBox;

    public ConfigurationView(){
        setTitle("Queue Management System");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        prepareFields();
    }
    private void prepareFields(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,1));
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

        JPanel minProcessingTimePanel = new JPanel();
        minProcessingTimePanel.setLayout(new GridLayout(1, 2));
        minProcessingTimePanel.setBackground(Color.WHITE);
        JLabel minProcessingTimeLabel = new JLabel("Min processing time");
        minProcessingTimeField = new JTextField(5);
        minProcessingTimePanel.add(minProcessingTimeLabel);
        minProcessingTimePanel.add(minProcessingTimeField);
        panel.add(minProcessingTimePanel);

        JPanel maxProcessingTimePanel = new JPanel();
        maxProcessingTimePanel.setLayout(new GridLayout(1, 2));
        maxProcessingTimePanel.setBackground(Color.WHITE);
        JLabel maxProcessingTimeLabel = new JLabel("Max processing time");
        maxProcessingTimeField = new JTextField(5);
        maxProcessingTimePanel.add(maxProcessingTimeLabel);
        maxProcessingTimePanel.add(maxProcessingTimeField);
        panel.add(maxProcessingTimePanel);

        JPanel minArrivalTimePanel = new JPanel();
        minArrivalTimePanel.setLayout(new GridLayout(1, 2));
        minArrivalTimePanel.setBackground(Color.WHITE);
        JLabel minArrivalTimeLabel = new JLabel("Min arrival time");
        minArrivalTimeField = new JTextField(5);
        minArrivalTimePanel.add(minArrivalTimeLabel);
        minArrivalTimePanel.add(minArrivalTimeField);
        panel.add(minArrivalTimePanel);

        JPanel maxArrivalTimePanel = new JPanel();
        maxArrivalTimePanel.setLayout(new GridLayout(1, 2));
        maxArrivalTimePanel.setBackground(Color.WHITE);
        JLabel maxArrivalTimeLabel = new JLabel("Max arrival time");
        maxArrivalTimeField = new JTextField(5);
        maxArrivalTimePanel.add(maxArrivalTimeLabel);
        maxArrivalTimePanel.add(maxArrivalTimeField);
        panel.add(maxArrivalTimePanel);

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


        timeLimitField.setText("60");
        maxProcessingTimeField.setText("7");
        minProcessingTimeField.setText("1");
        minArrivalTimeField.setText("2");
        maxArrivalTimeField.setText("40");
        numberOfServersField.setText("5");
        numberOfClientsField.setText("50");

        JLabel emptySeparator = new JLabel(" ");
        panel.add(emptySeparator);

        JButton startButton = getStartButton();
        panel.add(startButton);

        setContentPane(panel);
    }

    private JButton getStartButton() {
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e ->{
            int timeLimit;
            int maxProcessingTime;
            int minProcessingTime;
            int numberOfServers;
            int numberOfClients;
            int minArrivalTime;
            int maxArrivalTime;
            StrategyPolicy strategyPolicy = strategyBox.getSelectedIndex() == 0 ? StrategyPolicy.SHORTEST_QUEUE : StrategyPolicy.SHORTEST_TIME;

            try{
                timeLimit = Integer.parseInt(timeLimitField.getText().trim());
                maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText().trim());
                minProcessingTime = Integer.parseInt(minProcessingTimeField.getText().trim());
                numberOfServers = Integer.parseInt(numberOfServersField.getText().trim());
                numberOfClients = Integer.parseInt(numberOfClientsField.getText().trim());
                minArrivalTime = Integer.parseInt(minArrivalTimeField.getText().trim());
                maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText().trim());
                if(timeLimit <= 0 || maxProcessingTime <= 0 || minProcessingTime <= 0 || numberOfServers <= 0 || numberOfClients <= 0 || minArrivalTime <= 0 || maxArrivalTime <= 0){
                    throw new NumberFormatException();
                }
                if(maxProcessingTime < minProcessingTime){
                    throw new NumberFormatException();
                }
                if(maxArrivalTime < minArrivalTime){
                    throw new NumberFormatException();
                }
            }
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Invalid input");
                return;
            }
            ConfigurationView.this.dispose();
            SimulationView simulationView = new SimulationView(timeLimit, maxProcessingTime, minProcessingTime, maxArrivalTime,minArrivalTime,numberOfServers, numberOfClients, strategyPolicy);
            simulationView.setVisible(true);
        });
        return startButton;
    }
}
