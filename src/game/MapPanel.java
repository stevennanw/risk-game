package game;
import java.awt.*;
import javax.swing.JPanel;
import static util.constants.*;
import java.awt.geom.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class MapPanel extends JPanel{
    private final Board board;

    MapPanel (Board inBoard) {
        board = inBoard;
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setBackground(Color.WHITE);
        setFont(new Font("Helvetica Ultra Light", Font.PLAIN, MAP_PANEL_FONT_SIZE));
    }

    /**
     * Draw countries, adjacent lines, country names, armies
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(1.5f));

        for(int i=0; i<NUM_COUNTRIES; i++){
            g2.setColor(LINE_COLOR);
            for(int j : ADJACENT[i])
                if(COUNTRY_NAMES[i].equals("Alaska") && j==22){
                    g2.drawLine(COUNTRY_COORD[i][0], COUNTRY_COORD[i][1], 0, COUNTRY_COORD[i][1]-5);
                } else if(COUNTRY_NAMES[i].equals("Kamchatka") && j==8){
                    g2.drawLine(COUNTRY_COORD[i][0], COUNTRY_COORD[i][1], FRAME_WIDTH, COUNTRY_COORD[i][1]-10);
                } else {
                    g2.drawLine(COUNTRY_COORD[i][0], COUNTRY_COORD[i][1], COUNTRY_COORD[j][0], COUNTRY_COORD[j][1]);
                }
        }

        for(int i=0; i<NUM_COUNTRIES; i++){
            //CONTINENT_COLORS
            g2.setColor(Color.decode(CONTINENT_COLORS[CONTINENT_IDS[i]]));
            g2.fillOval(COUNTRY_COORD[i][0]-COUNTRY_RADIUS,COUNTRY_COORD[i][1]-COUNTRY_RADIUS,2*COUNTRY_RADIUS,2*COUNTRY_RADIUS);
            g2.setColor(TEXT_COLOR);
            g2.drawString(COUNTRY_NAMES[i]+"("+COUNTRY_NAMES_SHORT[i]+")",COUNTRY_COORD[i][0]-COUNTRY_NAMES[i].length()-COUNTRY_NAMES_SHORT[i].length()-COUNTRY_RADIUS-6,COUNTRY_COORD[i][1]-COUNTRY_RADIUS-4);
        }

        // Display players units
        for (int i=0; i<NUM_COUNTRIES; i++) {
            if (board.isOccupied(i)) {
                g2.setColor(PLAYER_COLORS[board.getOccupier(i)]);

                Ellipse2D.Double ellipse = new Ellipse2D.Double(COUNTRY_COORD[i][0] - PLAYER_RADIUS,COUNTRY_COORD[i][1] - PLAYER_RADIUS,2*PLAYER_RADIUS,2*PLAYER_RADIUS);
                g2.fill(ellipse);

                g2.setColor(TEXT_COLOR);
                g2.drawString(String.valueOf(board.getNumUnits(i)),COUNTRY_COORD[i][0]-COUNTRY_RADIUS/2+2,COUNTRY_COORD[i][1]+COUNTRY_RADIUS*2);
            }
        }
    }

    public void refresh () {
        revalidate();
        repaint();
    }
}