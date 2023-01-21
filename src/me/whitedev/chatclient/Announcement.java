package me.whitedev.chatclient;

import javax.swing.*;

public class Announcement {

    public String[] getServerAddress() {
        return JOptionPane.showInputDialog(
                Main.frame,
                "Enter IP Address of the ChatRoom: (Example 127.0.0.1:8888)",
                "Welcome to the PrivateChat",
                JOptionPane.QUESTION_MESSAGE).split(":");
    }

    public String getName() {
        return JOptionPane.showInputDialog(
                Main.frame,
                "Enter your Username:",
                "Username Selection",
                JOptionPane.QUESTION_MESSAGE);
    }
}
