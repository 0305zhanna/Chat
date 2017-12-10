package ChatEncrypt;

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
        textField.addActionListener(e -> sendEncryptedText(textField));

        JButton sendButton = new JButton("Отправить");
        inputPanel.add(sendButton);

        sendButton.addActionListener(e->sendEncryptedText(textField));

        panel.add(inputPanel,BorderLayout.SOUTH);

        userList = new List(10,false);
        userList.addActionListener(e -> {
            textField.setText(e.getActionCommand()+" ");
        });

        panel.add(userList,BorderLayout.WEST);

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);

        chat = new Communicator();

        chat.init(Messenger::processServerMessage);

    }

    private static void sendText(JTextField textField) {
        String text = textField.getText();
        textField.setText("");
        chat.sendTextServer(text);
    }
    private static void sendEncryptedText(JTextField textField) {
        String text = textField.getText();
        textField.setText("");
        try {
            chat.sendEncryptedTextServer(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void processServerMessage(String text) {
        if(text.startsWith("/name")){
            String[] words = text.split(" ");//разделяем строку на слова через пробелы
            textArea.append("Добро пожаловать в чат, "+words[1]+"\n");
            //userList.add(words[1]);
            return;
        }
        if(text.startsWith("/list")){
            String[] names = text.split(" ");//разделяем строку на слова через пробелы
            for (int i = 1; i<names.length;i++){
                userList.add(names[i]);
            }
            return;
        }
        if(text.startsWith("/add")){
            String[] names = text.split(" ");//разделяем строку на слова через пробелы
            userList.add(names[1]);
            return;
        }
        if(text.startsWith("/remove")){
            String[] names = text.split(" ");//разделяем строку на слова через пробелы
            userList.remove(names[1]);
            return;
        }
        if(text.startsWith("/encrypted")){
            String[] s = text.split(" ");
            text = s[1];
            try {
                text =s[2]+" > "+ chat.decrypt(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textArea.append(text+'\n');
    }

}
