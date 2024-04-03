package org.example;

import org.example.gui.ConfigurationView;

import javax.swing.*;

public class QueueManagementApp {
    public static void main(String[] args) {
        JFrame frame =  new ConfigurationView();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}