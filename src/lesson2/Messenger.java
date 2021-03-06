package lesson2;

import javax.swing.*;
import java.awt.*;

public class Messenger {

    static ChatClient chat;
    private static JTextArea textArea;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Чат");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LayoutManager manager = new BorderLayout();

        JPanel panel = new JPanel(manager);

        textArea = new JTextArea(20,20);
        textArea.setEditable(false);

        panel.add(textArea, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();

        JTextField textField = new JTextField(20);
        inputPanel.add(textField);
        textField.addActionListener((e)->{
            sendText(textField);
        });

        JButton sendButton = new JButton("Отправить");
        inputPanel.add(sendButton);

        sendButton.addActionListener((e)->{
            sendText(textField);
        });

        panel.add(inputPanel,BorderLayout.SOUTH);

        panel.setPreferredSize(new Dimension(400,400));

        frame.add(panel);
//        frame.setSize(400,400);


        frame.pack();

        frame.setVisible(true);

        chat = new ChatClient();

        chat.init(Messenger::placeText);

    }

    private static void sendText(JTextField textField) {
        String text = textField.getText();
        textField.setText("");
        chat.sendTextServer(text);
    }

    private static void placeText(String text) {
        textArea.append(text+'\n');
    }

}
