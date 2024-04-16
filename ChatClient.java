/*************************************************************
 * Dupla: Mauro Roberto Trevisan e Ramon Godoy Izidoro
 * Modificações: 
 * 1. Foram adicionadas verificações para diferenciar mensagens 
 * de sistema de mensagens de usuário;
 * 2. A JTextArea foi subtituída por um JTextPane, para fosse
 * possível difinir atributos como cor e fonte ao texto;
*************************************************************/

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextPane messagePane = new JTextPane();

    Color green = new Color(0,255,0);
    Color black = new Color(0,0,0);

    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;

        textField.setEditable(false);
        messagePane.setEditable(false);

        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messagePane), BorderLayout.CENTER);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {
        try {
            @SuppressWarnings("resource")
            var socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            messagePane.setForeground(black);

            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getName());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    appendMessage(line.substring(8) + "\n", black, "courier new");
                } else if(line.startsWith("SYSTEM MESSAGE")) {
                    appendMessage(line.substring(15) + "\n", green, "Lucida Sans");
                }
            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    private void appendMessage(String message, Color color, String font) {
        StyledDocument doc = messagePane.getStyledDocument();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, color);
        StyleConstants.setFontFamily(attributes, font);
        StyleConstants.setFontSize(attributes, 14);

        try {
            doc.insertString(doc.getLength(), message, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var client = new ChatClient(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(600, 400);
        client.frame.setVisible(true);
        client.run();
    }
}
