import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class ColoredTextPaneExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Colored TextPane Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JTextPane textPane = new JTextPane();
            frame.getContentPane().add(new JScrollPane(textPane), BorderLayout.CENTER);

            JPanel controlsPanel = new JPanel();
            frame.getContentPane().add(controlsPanel, BorderLayout.SOUTH);

            JComboBox<String> colorComboBox = new JComboBox<>(new String[]{"Black", "Red", "Blue", "Green"});
            controlsPanel.add(colorComboBox);

            JButton sendButton = new JButton("Send");
            controlsPanel.add(sendButton);

            sendButton.addActionListener(e -> {
                String selectedColor = (String) colorComboBox.getSelectedItem();
                Color color = Color.BLACK;
                switch (selectedColor) {
                    case "Red":
                        color = Color.RED;
                        break;
                    case "Blue":
                        color = Color.BLUE;
                        break;
                    case "Green":
                        color = Color.GREEN;
                        break;
                }

                StyledDocument doc = textPane.getStyledDocument();
                SimpleAttributeSet attributes = new SimpleAttributeSet();
                StyleConstants.setForeground(attributes, color);
                StyleConstants.setFontFamily(attributes, "Arial");
                StyleConstants.setFontSize(attributes, 14);

                try {
                    doc.insertString(doc.getLength(), "This is a colored line.\n", attributes);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            });

            frame.setVisible(true);
        });
    }
}
