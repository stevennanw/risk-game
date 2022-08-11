package game;

import java.util.ArrayList;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */
//This class is for storing the cards the player own
public class PlayersCards {

    ArrayList<Card> playersCards;

    public PlayersCards() {
        playersCards = new ArrayList<>();
    }

    //This is for returning the parse of cards
    public ArrayList<Card> getCardsParse() {
        return playersCards;
    }

    //This is for adding cards to the arraylist of cards
    public void addCards(Card card) { playersCards.add(card); }

    //This is for trading cards, in parse it's only removing the card
    public void tradingCards(int card1, int card2, int card3) {
        if (canTrade(card1, card2, card3)) {
            playersCards.remove(gettingCards(card1));
            playersCards.remove(gettingCards(card2));
            playersCards.remove(gettingCards(card3));
        }
    }

    //This is for checking if player is able to trade the cards
    public boolean canTrade(int card1, int card2, int card3) {
        int cardId1 =playersCards.indexOf(gettingCards(card1));
        int cardId2 =playersCards.indexOf(gettingCards(card2));
        int cardId3 =playersCards.indexOf(gettingCards(card3));
        if (playersCards.size() >= 3) {
            //Player is able to trading if all cards have the same type
            if (playersCards.get(cardId1).getCardTypes().equals(playersCards.get(cardId2).getCardTypes()) && playersCards.get(cardId2).getCardTypes().equals(playersCards.get(cardId3).getCardTypes())) {
                return true;
            //Or player is able to trading if all cards have different types
            } else return !playersCards.get(cardId1).getCardTypes().equals(playersCards.get(cardId2).getCardTypes()) && !playersCards.get(cardId1).getCardTypes().equals(playersCards.get(cardId3).getCardTypes()) && !playersCards.get(cardId2).getCardTypes().equals(playersCards.get(cardId3).getCardTypes());
        }
        return false;
    }


    public Card gettingCards(int card) {
        Card result=null;
        for (Card playersCard : playersCards) {
            if (card == playersCard.getCardId()) {
                result = playersCard;
            }
        }
        return result;
    }
    /**
     *
     * @param userInterface userInterface
     * @param cards cards
     * @return tradingCards1
     */
    public static String getTradingCard1(UserInterface userInterface, ArrayList<Card> cards) {
        userInterface.displayString("Please enter id of the first card");
        String tradingCards1 = userInterface.getCommand();
        userInterface.displayString("> " + tradingCards1);
        boolean pass = false;
        do {
            //if this card ID is not valid
            if(!CommandCheck.isMyCardID(cards, tradingCards1)){
                userInterface.displayString("You need to enter a card ID which you own. PLease try again");
                tradingCards1 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards1);
            }
            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString("The first card is " + tradingCards1);
        return tradingCards1;
    }

    /**
     *
     * @param userInterface userInterface
     * @param cards cards
     * @param tradingCards1 tradingCards1
     * @return tradingCards2
     */
    public static String getTradingCard2(UserInterface userInterface, ArrayList<Card> cards, String tradingCards1) {
        userInterface.displayString("Please enter id of the second card");
        String tradingCards2 = userInterface.getCommand();
        userInterface.displayString("> " + tradingCards2);
        boolean pass = false;
        do {
            //if this card ID is not valid
            if(!CommandCheck.isMyCardID(cards, tradingCards2)){
                userInterface.displayString("You need to enter a card ID which you own. PLease try again");
                tradingCards2 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards2);
            }
            else if(tradingCards2.equals(tradingCards1)){ //the second card cannot be the same as the first one
                userInterface.displayString("You have chosen this card as the first card. PLease try again");
                tradingCards2 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards2);
            }
            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString("The second card is " + tradingCards2);
        return tradingCards2;
    }

    /**
     *
     * @param userInterface userInterface
     * @param cards cards
     * @param tradingCards1 tradingCards1
     * @param tradingCards2 tradingCards2
     * @return tradingCards3
     */
    public static String getTradingCard3(UserInterface userInterface, ArrayList<Card> cards, String tradingCards1, String tradingCards2) {
        userInterface.displayString("Please enter id of the third card");
        String tradingCards3 = userInterface.getCommand();
        userInterface.displayString("> " + tradingCards3);
        boolean pass = false;
        do {
            //if this card ID is not valid
            if(!CommandCheck.isMyCardID(cards, tradingCards3)){
                userInterface.displayString("You need to enter a card ID which you own. PLease try again");
                tradingCards3 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards3);
            }
            else if(tradingCards3.equals(tradingCards1)){ //the third card cannot be the same as the first one
                userInterface.displayString("You have chosen this card as the first card. PLease try again");
                tradingCards3 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards3);
            }
            else if(tradingCards3.equals(tradingCards2)){ //the third card cannot be the same as the second one
                userInterface.displayString("You have chosen this card as the second card. PLease try again");
                tradingCards3 = userInterface.getCommand();
                userInterface.displayString("> " + tradingCards3);
            }
            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString("The third card is " + tradingCards3);
        return tradingCards3;
    }

    //This is for checking if player must trade the cards
    public boolean mustTrade() {
        return playersCards.size() >= 5;
    }
    public boolean cantTrade() {
        return playersCards.size() < 3;
    }

}