package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

class PlayerinfoTest {
    Playerinfo player1 = new Playerinfo("player1",0,10,true,true);
    Playerinfo player2 = new Playerinfo("player2",5,3,false,false);

    @Test
    void getId() {
        assertEquals(0,player1.getId());
        assertEquals(5,player2.getId());
    }

    @Test
    void getName() {
        assertEquals("player1",player1.getName());
        assertEquals("player2",player2.getName());
    }

    @Test
    void getArmies() {
        assertEquals(10,player1.getArmies());
        assertEquals(3,player2.getArmies());
    }

    @Test
    void isBot() {
        assertTrue(player1.isBot());
        assertFalse(player2.isBot());
    }

    @Test
    void addUnits() {
        player1.addUnits(1);
        assertEquals(11, player1.army);
        player1.addUnits(1);
        assertEquals(12, player1.army);
        try {
            player1.addUnits(-1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void subtractUnits() {
        player1.subtractUnits(1);
        assertEquals(9, player1.army);
        player1.subtractUnits(1);
        assertEquals(8, player1.army);
        try {
            player1.subtractUnits(11);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            player1.subtractUnits(-1);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void isLive() {
        assertTrue(player1.isLive());
        assertFalse(player2.isLive());
    }

    @Test
    void setLive() {
        player1.setLive(false);
        assertFalse(player1.isLive());

        player2.setLive(true);
        assertTrue(player2.isLive());
    }
}