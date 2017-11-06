package lesson3;

import javax.swing.*;
import java.awt.*;

public class Messenger {

    static Communicator chat;
    private static JTextArea textArea;
    private static JScrollPane sp;
    private static List userList;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Чат");

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LayoutManager manager = new BorderLayout();

        JPanel panel = new JPanel(manager);
        panel.setPreferredSize(new Dimension(400,400));

        textArea = new JTextArea(20,20);
        textArea.setEditable(false);

        sp = new JScrollPane(textArea);

        panel.add(sp, BorderLayout.CENTER);

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

        userList = new List(10,false);

        panel.add(userList,BorderLayout.WEST);

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);

        chat = new Communicator();

        chat.init(Messenger::placeText);

    }

    private static void sendText(JTextField textField) {
        String text = textField.getText();
        textField.setText("");
        chat.sendTextServer(text);
    }

    private static void placeText(String text) {
        if(text.startsWith("/name")){
            String[] words = text.split(" ");//разделяем строку на слова через пробелы
            userList.add(words[1]);
        }else {
        textArea.append(text+'\n');
        textArea.setCaretPosition(textArea.getDocument().getLength());}
    }

}
