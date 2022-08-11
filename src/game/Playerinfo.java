package game;

import java.util.ArrayList;

import static util.constants.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class Playerinfo {
    String name;
    int id;
    int army;
    int turns;
    boolean bot;
    boolean live;
    PlayersCards playersCards;

    /*players' information*/

    public Playerinfo(String name, int id, int army, boolean bot, boolean live) {
        this.name = name;
        this.army = army;
        this.id = id;
        this.bot = bot;
        this.live = live;
        playersCards = new PlayersCards();
        turns = 0;
    }

    /*get player's id*/
    public int getId() {
        return id;
    }

    /*get player's name*/
    public String getName() {
        return name;
    }

    /*get armies player owns*/
    public int getArmies() {
        return army;
    }

    public boolean isBot(){
        return bot;
    }

    public void addUnits (int inNum) {
        army = army + inNum;
    }

    public void subtractUnits (int inNum) {
        army = army - inNum;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public ArrayList<Card> getCards(){ return playersCards.getCardsParse(); }

    public void addCard(Card card) { playersCards.addCards(card); }

    public void tradeCard(String card1, String card2, String card3, int noSet) {
        int cardNo1 = Integer.parseInt(card1);
        int cardNo2 = Integer.parseInt(card2);
        int cardNo3 = Integer.parseInt(card3);
        playersCards.tradingCards(cardNo1, cardNo2, cardNo3);
        //noSet is the number of set of cards player is trading
        if(noSet>=6){
            army+=15+(5*(noSet-6));
        }
        army += 4+(2*noSet);
    }

    public boolean checkRemoveCards(String card1, String card2, String card3) {
        int cardNo1 = Integer.parseInt(card1);
        int cardNo2 = Integer.parseInt(card2);
        int cardNo3 = Integer.parseInt(card3);
        return playersCards.canTrade(cardNo1, cardNo2, cardNo3);
    }

    public boolean checkNeedForTrading(){ return playersCards.mustTrade(); }
    public boolean checkunabletoTrade(){ return playersCards.cantTrade(); }

    /**
     * Get neutral names
     * @param userInterface userInterface
     * @param player player
     * @throws InterruptedException sleep
     */
    public static void getNeutralNames(UserInterface userInterface, ArrayList<Playerinfo> player) throws InterruptedException {
        int playerId;
        String name;
        for (playerId = NUM_PLAYERS; playerId < NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
            Thread.sleep(500);
            String m = "" + (playerId - 1);
            name = "neutral " + m;
            player.add(new Playerinfo(name,playerId,24,true,true));
            userInterface.displayString(name + " ("+PLAYER_COLORS_NAMES[playerId]+") has joined game.");
        }
    }

    /**
     * Get player names
     * @param userInterface userInterface
     * @param player player
     * @throws InterruptedException sleep
     */
    public static void getPlayerNames(UserInterface userInterface, ArrayList<Playerinfo> player) throws InterruptedException {
        int playerId;
        String name;
        userInterface.displayString("ENTER PLAYER NAMES");
        for (playerId = 0; playerId < NUM_PLAYERS; playerId++) {
            Thread.sleep(500);
            userInterface.displayString("Enter the name of player " + (playerId + 1) + " ("+PLAYER_COLORS_NAMES[playerId]+")");
            name = userInterface.getCommand();
            //store the name
            player.add(new Playerinfo(name,playerId,36,false,true));
            userInterface.displayString("> " + name);
            userInterface.displayString(name + " has joined game.");
            userInterface.displayString("");
        }
    }

    /**
     * This method is to test if a player is dead
     * @param userInterface userInterface
     * @param player player
     * @param board board
     * @throws InterruptedException sleep
     */
    static public void eliminated(UserInterface userInterface, ArrayList<Playerinfo> player, Board board) throws InterruptedException {
        for(int i=0; i<NUM_PLAYERS_PLUS_NEUTRALS; i++){

            // If there is at least one troop on a controlled country, eliminate is false, otherwise eliminate is true
            boolean eliminate = true;
            for (int j=0; j< NUM_COUNTRIES; j++){
                if(COUNTRY_CONTROL[j] == player.get(i).getId() && board.getNumUnits(i)>0){
                    eliminate = false;
                }
            }

            // If eliminate
            if(eliminate){
                if (player.get(i).getId() == 2 || player.get(i).getId() == 3 || player.get(i).getId() == 4 || player.get(i).getId() == 5){
                    player.get(i).setLive(false);
                    userInterface.displayString(">>>> "+player.get(i).getName()+" eliminated! Good Bye!<<<<");
                } else {
                    // For player, if eliminate is true, this player loses and the competitor is the winner.
                    userInterface.displayString("\n\n>>>>  ==========GAME OVER========== <<<<");
                    userInterface.displayString(">>>> "+player.get(i).getName()+" eliminated! <<<<");
                    userInterface.displayString(">>>> "+player.get((i+1)%2).getName()+" is the winner! <<<<");
                    Thread.sleep(1000000);
                    System.exit(0);
                }
            }
        }
    }

}