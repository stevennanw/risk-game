package game;

import java.util.ArrayList;
import static util.constants.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class Deck {

    private final ArrayList<Card> cards;

    public Deck() {

        int cardId;
        String[] cardTypes = new String[]{"Infantry", "Cavalry", "Artillery"};
        cards = new ArrayList<>();
        for (cardId=0; cardId<NUM_COUNTRIES; cardId++) {
            cards.add(new Card(cardId, cardTypes[cardId / 14], COUNTRY_NAMES[cardId]));
        }
    }

    public Card draw(int i) {

        Card drawCard = cards.get(i);

        cards.set(i,null);

        return drawCard;
    }
    public boolean isEmpty () {
        return cards.isEmpty();
    }

}