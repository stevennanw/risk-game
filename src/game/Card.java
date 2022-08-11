package game;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

import java.util.ArrayList;

import static util.constants.*;
import static util.constants.NUMBER_OF_SETS;

public class Card {
    public final int countryId;
    public final String cardTypes;
    public final String cardName;

    public Card(int inCountryId,String inCardTypes, String inCountryName) {
        countryId = inCountryId;
        cardTypes = inCardTypes;
        cardName = inCountryName;
    }

    public int getCardId() {
        return countryId;
    }
    public String getCardTypes () {
        return cardTypes;
    }
    public String getCardName() {
        return cardName;
    }

    /**
     *
     * @param userInterface userInterface
     * @param player player
     * @param deck deck
     * @param playerNameWithColor playerNameWithColor
     */
    public static void drawCards(UserInterface userInterface, ArrayList<Playerinfo> player, Deck deck, String[] playerNameWithColor) {
        for (int playerId=0; playerId<NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
            for (int i=0; i<42; i++) {
                //Only draw cards form deck if the player control the place and the deck is not empty
                if (COUNTRY_CONTROL[i] == playerId && !deck.isEmpty()) {
                    Card card = deck.draw(i);
                    if(card == null){
                        break;
                    }
                    userInterface.displayString(playerNameWithColor[playerId] + " draws the " + card.getCardName() + " card, the type of card is: " + card.getCardTypes());
                    //add the card drawn to the player's ownership
                    player.get(playerId).addCard(card);
                }
            }
        }
        userInterface.displayMap();
    }

    /**
     *
     * @param userInterface userInterface
     * @param player player
     * @param playerNameWithColor playerNameWithColor
     */
    public static void exchangeCards(UserInterface userInterface, ArrayList<Playerinfo> player, String[] playerNameWithColor) throws InterruptedException {
        //this this the three cards for trading

        Thread.sleep(500);
        userInterface.displayString("\n==========CARD TRADING==========");
        Thread.sleep(500);
        for (int playerId = 0; playerId < NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
            if (player.get(playerId).getId() == 2 || player.get(playerId).getId() == 3 || player.get(playerId).getId() == 4 || player.get(playerId).getId() == 5) { //if it is a bot
                userInterface.displayString("Bots don't have cards");
            } else {
                ArrayList<Card> cards = player.get(playerId).getCards();
                userInterface.displayString("\nHere's the cards " + playerNameWithColor[playerId] + " own\n");
                //Show the players about the card they own
                for (Card card : cards) {
                    userInterface.displayString(playerNameWithColor[playerId] + " has the card " + card.getCardName() + ", the card type: " + card.getCardTypes() + ", the card id: " + card.getCardId() + ".");
                }
                //check if there's need to trade

                if (player.get(playerId).checkNeedForTrading()) {
                    userInterface.displayString("\n" + playerNameWithColor[playerId] + ": You have too many cards, you need to trade cards for armies");

                    userInterface.displayString(playerNameWithColor[playerId] + ": You can enter the id of the card to decide which card you wish to exchange");
                    boolean correct = false;
                    while (!correct) {
                        String tradingCards1 = PlayersCards.getTradingCard1(userInterface, cards);
                        String tradingCards2 = PlayersCards.getTradingCard2(userInterface, cards, tradingCards1);
                        String tradingCards3 = PlayersCards.getTradingCard3(userInterface, cards, tradingCards1, tradingCards2);

                        userInterface.displayString(playerNameWithColor[playerId] + ": The id of cards you chose to exchange are: " + tradingCards1 + " " + tradingCards2 + " " + tradingCards3);

                        //check if it can be traded
                        if (player.get(playerId).checkRemoveCards(tradingCards1, tradingCards2, tradingCards3)) {
                            player.get(playerId).tradeCard(tradingCards1, tradingCards2, tradingCards3, NUMBER_OF_SETS[playerId]);
                            NUMBER_OF_SETS[playerId] += 1;
                            userInterface.displayString(playerNameWithColor[playerId] + ": You have trade three insignia cards and received armies");
                            correct = true;
                        } else {
                            userInterface.displayString(playerNameWithColor[playerId] + ": You can't trade these three cards, please enter again.");
                        }
                    }
                }


                //over 3, but less than 5 cards
                else if (!player.get(playerId).checkNeedForTrading() && !player.get(playerId).checkunabletoTrade()) {
                    boolean skip;
                    cards = player.get(playerId).getCards();
                    userInterface.displayString("\nHere's the cards " + playerNameWithColor[playerId] + " own\n");
                    //Show the players about the card they own
                    for (Card card : cards) {
                        userInterface.displayString(playerNameWithColor[playerId] + " has the card " + card.getCardName() + ", the card type: " + card.getCardTypes() + ", the card id: " + card.getCardId() + ".");
                    }
                    userInterface.displayString("\n" + playerNameWithColor[playerId] + ": Do you want to exchange your cards ? If you don't, Enter skip (Case sensitive)");
                    String isSkip = userInterface.getCommand();
                    userInterface.displayString(">" + isSkip);
                    skip = isSkip.equals("skip");
                    while (!skip) {
                        Thread.sleep(500);
                        userInterface.displayString("\n==========CARD TRADING==========");
                        Thread.sleep(500);

                        userInterface.displayString(playerNameWithColor[playerId] + ": You can enter the id of the card to decide which card you wish to exchange");
                        boolean correct = false;
                        while (!correct) {

                            userInterface.displayString(playerNameWithColor[playerId] + ": If you change your mind, want to stop exchange cards, Enter skip (Case sensitive), otherwise tap enter");
                            isSkip = userInterface.getCommand();
                            userInterface.displayString(">" + isSkip);
                            if (isSkip.equals("skip")) {
                                skip = true;
                                break;
                            }
                            String tradingCards1 = PlayersCards.getTradingCard1(userInterface, cards);
                            String tradingCards2 = PlayersCards.getTradingCard2(userInterface, cards, tradingCards1);
                            String tradingCards3 = PlayersCards.getTradingCard3(userInterface, cards, tradingCards1, tradingCards2);

                            userInterface.displayString(playerNameWithColor[playerId] + ": The id of cards you chose to exchange are: " + tradingCards1 + " " + tradingCards2 + " " + tradingCards3);


                            //check if it can be traded

                            if (player.get(playerId).checkRemoveCards(tradingCards1, tradingCards2, tradingCards3)) {
                                player.get(playerId).tradeCard(tradingCards1, tradingCards2, tradingCards3, NUMBER_OF_SETS[playerId]);
                                NUMBER_OF_SETS[playerId] += 1;
                                userInterface.displayString(playerNameWithColor[playerId] + ": You have trade three insignia cards and received armies");
                                correct = true;
                                skip = true;
                            } else {
                                userInterface.displayString(playerNameWithColor[playerId] + ": You can't trade these three cards, please enter again.");
                            }
                        }
                    }
                }
                //less than 3 cards
                if (player.get(playerId).checkunabletoTrade()) {
                    userInterface.displayString(playerNameWithColor[playerId] + ": You have less than 3 cards that means you cannot exchange cards.");
                }
                Thread.sleep(1000);
            }
        }
    }
}
