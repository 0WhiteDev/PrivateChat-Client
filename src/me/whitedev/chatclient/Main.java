package me.whitedev.chatclient;

import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

public class Main {

    public static String VERSION = "v1.1";
    boolean sound = true;
    BufferedReader in;
    PrintWriter out;
    static JFrame frame = new JFrame("PrivateChat Client " + VERSION);
    String username;
    JTextField textField = new JTextField(20);
    JTextArea messageArea = new JTextArea(25, 40);
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel optionsPanel = new JPanel();
    JPanel chatPanel = new JPanel();
    JButton button1 = new JButton("Disconnect");
    JButton button2 = new JButton("Join to ChatRoom");
    JCheckBox checkBox = new JCheckBox("Message Sound");
    Encryption encryption = new Encryption();
    Announcement announcement = new Announcement();
    SoundNotify soundNotify = new SoundNotify();

    public Main() {

        //Layout of Gui
        textField.setEditable(true);
        messageArea.setEditable(false);

        chatPanel.add(new JLabel("Chat"));
        chatPanel.add(textField);
        chatPanel.add(new JScrollPane(messageArea));
        chatPanel.add(button1, 2);
        chatPanel.add(button2, 2);

        checkBox.setSelected(true);

        optionsPanel.add(new JLabel("Options"));
        optionsPanel.add(checkBox);

        tabbedPane.addTab("Chat", chatPanel);
        tabbedPane.addTab("Options", optionsPanel);

        frame.setSize(500, 520);
        frame.add(tabbedPane);
        frame.setVisible(true);

        // Listener for TextBox (TextField)
        textField.addActionListener(e -> {
            if(!textField.getText().equals("")) {
                out.println(encryption.encrypt("[" + username + "] " + textField.getText()));
                textField.setText("");
            }
        });

        // Listener for Buttons
        button1.addActionListener(e -> {
            try {
                out.println(encryption.encrypt("[" + username + " Left the ChatRoom] "));
                messageArea.setText("");
                run();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        button2.addActionListener(e -> {
            try {
                run();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Listener for CheckBox
        checkBox.addItemListener(e -> sound = e.getStateChange() == ItemEvent.SELECTED);

    }


    private void run() throws IOException {
        button1.setVisible(false);
        button2.setVisible(true);

        String[] serverAddress = announcement.getServerAddress();

        while (serverAddress.length == 1) {
            serverAddress = announcement.getServerAddress();
        }

        Socket socket = new Socket(serverAddress[0], Integer.parseInt(serverAddress[1]));
        username = announcement.getName();
        if (username == null || username.equals("")) {
            username = "User" + new Random().nextInt(1000);
        }

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        messageArea.setText("[System] Welcome on private chat! - " + serverAddress[0] + ":" + serverAddress[1]);

        button2.setVisible(false);
        button1.setVisible(true);

        out.println(encryption.encrypt("[" + username + " Joined the ChatRoom] "));

        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    if(encryption.decrypt(message).startsWith("["))
                        if(sound) soundNotify.getMessage();
                        messageArea.setText(encryption.decrypt(message) + "\n" + messageArea.getText());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Main client = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        client.run();
    }

}
