package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

class CardTest {
    Card card1 = new Card(0,"Cavalry","card1");
    Card card2 = new Card(45,"Infantry","card2");

    @Test
    void getCardId() {
        assertEquals(0,card1.getCardId());
        assertEquals(45,card2.getCardId());
    }

    @Test
    void getCardTypes() {
        assertEquals("Cavalry",card1.getCardTypes());
        assertEquals("Infantry",card2.getCardTypes());
    }

    @Test
    void getCardName() {
        assertEquals("card1",card1.getCardName());
        assertEquals("card2",card2.getCardName());
    }
}