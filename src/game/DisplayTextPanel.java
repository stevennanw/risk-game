package game;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import static util.constants.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

/**
 * A panel that allows display of text message instructing the player on what is happening,
 * prompts them with what to do and shows previous user inputs.
 */
public class DisplayTextPanel extends JPanel{
    private static final int TEXT_AREA_HEIGHT = 6;
    private static final int CHARACTER_WIDTH = 75;


    JTextArea TextArea = new JTextArea(TEXT_AREA_HEIGHT,CHARACTER_WIDTH);
    JScrollPane scrolll = new JScrollPane(TextArea);
    DefaultCaret caret = (DefaultCaret)TextArea.getCaret();

    DisplayTextPanel () {
        setBackground(Color.GRAY);
        TextArea.setEditable(false);
        TextArea.setFont(new Font("Helvetica Ultra Light", Font.PLAIN, DISPLAY_TEXT_PANEL_FONT_SIZE));
        TextArea.setLineWrap(true);
        TextArea.setWrapStyleWord(true);
        scrolll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(scrolll);
    }

    public void addText(String text) {
        TextArea.setText(TextArea.getText()+"\n"+text);
    }

}
