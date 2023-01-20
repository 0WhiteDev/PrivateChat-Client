package me.whitedev.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

public class Main {

    String version = "v1.0";
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("PrivateChat Client " + version);
    String username;
    JTextField textField = new JTextField(20);
    JTextArea messageArea = new JTextArea(25, 40);
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel optionsPanel = new JPanel();
    JPanel chatPanel = new JPanel();
    JButton button1 = new JButton("Disconnect");

    public Main() {

        textField.setEditable(true);
        messageArea.setEditable(false);

        chatPanel.add(new JLabel("Chat"));
        chatPanel.add(textField);
        chatPanel.add(new JScrollPane(messageArea));
        chatPanel.add(button1, 2);
        button1.setLocation(400, 20);
        optionsPanel.add(new JLabel("Options"));
        tabbedPane.addTab("Chat", chatPanel);
        tabbedPane.addTab("Options", optionsPanel);
        frame.add(tabbedPane);
        frame.setVisible(true);
        frame.setSize(500, 500);

        textField.addActionListener(e -> {
            if(!textField.getText().equals("")) {
                out.println(encrypt("[" + username + "] " + textField.getText()));
                textField.setText("");
            }
        });

        button1.addActionListener(e -> {
            try {
                messageArea.setText("");
                run();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private String[] getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the ChatRoom: (Example 127.0.0.1:8888)",
                "Welcome to the PrivateChat",
                JOptionPane.QUESTION_MESSAGE).split(":");
    }

    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter your Username:",
                "Username Selection",
                JOptionPane.QUESTION_MESSAGE);
    }

    private void run() throws IOException {
        button1.setVisible(false);
        String[] serverAddress = getServerAddress();
        while (serverAddress.length == 1) {
            serverAddress = getServerAddress();
        }
        Socket socket = new Socket(serverAddress[0], Integer.parseInt(serverAddress[1]));
        username = getName();
        if (username == null || username.equals("")) {
            username = "User" + new Random().nextInt(1000);
        }
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        messageArea.setText("[System] Welcome on private chat! - " + serverAddress[0] + ":" + serverAddress[1]);
        button1.setVisible(true);
        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    if(decrypt(message).startsWith("["))
                        messageArea.setText(decrypt(message) + "\n" + messageArea.getText());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Main client = new Main();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

    static StringBuilder builder = new StringBuilder();

    static String encrypt(Object input) {
        builder.setLength(0);
        for (char object : String.valueOf(input).toCharArray()) {
            builder.append(Character.toChars(object * 354));
        }
        return builder.toString();
    }

    static String decrypt(Object input) {
        builder.setLength(0);
        for (char object : String.valueOf(input).toCharArray()) {
            builder.append(Character.toChars(object / 354));
        }
        return builder.toString();
    }

}
