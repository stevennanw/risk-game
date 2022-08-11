package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

/**
 * A panel that allows the user to enter textual commands.
 */
public class EnterTextPanel extends JPanel {
    private static final int FONT_SIZE = 14;
    private final JTextField textField = new JTextField(75);
    private final LinkedList<String> textBuffer = new LinkedList<>();

    EnterTextPanel () {
        setBackground(Color.darkGray);
        JButton button = new JButton("GIVE ORDER");
        button.setFont(new Font("Helvetica Ultra Light", Font.BOLD, 12));
        button.setBounds(0,0,20,45);
        class AddActionListener implements ActionListener {
            public void actionPerformed(ActionEvent event)	{
                synchronized (textBuffer) {
                    textBuffer.add(textField.getText());
                    textField.setText("");
                    textBuffer.notify();
                }
            }
        }
        ActionListener listener = new AddActionListener();
        textField.addActionListener(listener);
        button.addActionListener(listener);
        textField.setFont(new Font("Helvetica Ultra Light", Font.PLAIN, FONT_SIZE));
        add(textField, BorderLayout.CENTER);
        add(button);
    }

    public String getCommand() {
        String command;
        synchronized (textBuffer) {
            while (textBuffer.isEmpty()) {
                try {
                    textBuffer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            command = textBuffer.pop();
        }
        return command;
    }
}