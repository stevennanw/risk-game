package game;

import javax.swing.*;
import java.awt.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class UserInterface {
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 800;

    private final MapPanel mapPanel;
    private final DisplayTextPanel displayTextPanel = new DisplayTextPanel();
    private final EnterTextPanel enterTextPanel = new EnterTextPanel();

    public UserInterface(Board board) {
        mapPanel = new MapPanel(board);
        JFrame frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("Risk");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mapPanel, BorderLayout.NORTH);
        frame.add(displayTextPanel, BorderLayout.CENTER);
        frame.add(enterTextPanel,BorderLayout.SOUTH);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public String getCommand () {
        return enterTextPanel.getCommand();
    }

    public void displayMap () {
        mapPanel.refresh();
    }

    public void displayString(String string) {
        displayTextPanel.addText(string);
    }

}

