import game.*;

import java.util.ArrayList;
import java.util.Arrays;

import static util.constants.*;
import static util.constants.NUM_PLAYERS_PLUS_NEUTRALS;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

/**
 * This is the main class of the game
 */
public class Main {

    public static void main (String[] args) throws InterruptedException {
        Board board = new Board();
        Deck deck= new Deck();
        UserInterface userInterface = new UserInterface(board);

        ArrayList<Playerinfo> player = new ArrayList<>();


        // display blank board
        userInterface.displayMap();

        // get player names
        Playerinfo.getPlayerNames(userInterface, player);

        // give neutral names
        Playerinfo.getNeutralNames(userInterface, player);

        //create playerNameWithColor
        String[] playerNameWithColor = new String[6];
        for(int i=0;i<6;i++){
            playerNameWithColor[i] = player.get(i).getName() + " ("+PLAYER_COLORS_NAMES[i]+")";
        }

        // Allocate territories and add them to board, add units
        TerritoryAndArmy.allocateTerritories(userInterface, board);

        //TODO these two methods is here only for testing purpose


        /* Roll a dice each to see who places their reinforcements first.
           Highest roll does first. Re-roll if a draw. Inform the user. */
        userInterface.displayString("ROLL DICE TO SEE WHO REINFORCES THEIR COUNTRIES FIRST");

        RollDice.roll(userInterface, player);

        // Take territories
        TerritoryAndArmy.takeTerritories(userInterface, player, board, playerNameWithColor);

        userInterface.displayString("ROLL DICE TO SEE WHO TAKES THE FIRST TURN");
        RollDice.roll(userInterface, player);

        //attack, defend
        turnsingame(userInterface, player, deck, board, playerNameWithColor);
    }

    /**
     * Take turnsingame
     * @param userInterface userInterface
     * @param player player
     * @param board board
     * @throws InterruptedException sleep
     */
    public static void turnsingame(UserInterface userInterface, ArrayList<Playerinfo> player, Deck deck, Board board, String[] playerNameWithColor) throws InterruptedException {

        //game continues until one of players is dead
        while(player.get(0).isLive() && player.get(1).isLive()){
            //check if someone died
            Playerinfo.eliminated(userInterface,player, board);
            // Adding new cards to deck
            Card.drawCards(userInterface, player, deck, playerNameWithColor);
            // Exchanging new cards from the deck
            Card.exchangeCards(userInterface, player, playerNameWithColor);

            for (int i = 0; i < NUM_PLAYERS_PLUS_NEUTRALS; i++) {
                int j = PLAYERS_ORDER[i];

                // If this player is dead, the next player continues
                if(!player.get(j).isLive()){
                    userInterface.displayString(playerNameWithColor[j] + " is dead.");
                    Thread.sleep(200);
                    continue;
                }

                Thread.sleep(500);
                userInterface.displayString("\n==========DEPLOYMENT==========");
                Thread.sleep(500);
                userInterface.displayString("");

                int c = 0;
                int numCountryControl = 0;

                while (c < NUM_COUNTRIES) {
                    if (COUNTRY_CONTROL[c] == player.get(j).getId()) {
                        numCountryControl++;
                    }
                    c++;
                }
                if (numCountryControl < 12) {
                    // Increment armies based on the number of territories occupied
                    player.get(j).addUnits(3);
                    userInterface.displayString(playerNameWithColor[j] + " has gained " + 3 + " armies.");

                } else {
                    player.get(j).addUnits(numCountryControl / 3);
                    userInterface.displayString(playerNameWithColor[j] + " has gained " + numCountryControl / 3 + " armies.");
                }

                TerritoryAndArmy.placeArmy(userInterface, player, board, playerNameWithColor, i, j);

                //Combat phrase

                //reset current country to null
                String Country = null;

                //boolean for check if country controlled by current player
                boolean Controlled = false;
                //boolean for check if attack country and invaded country are adjacent
                boolean Adjacent= false;
                userInterface.displayMap();
                Thread.sleep(200);
                TerritoryAndArmy.switchPhase();
                //if it's bots' turn
                if (player.get(j).getId() == 2 || player.get(j).getId() == 3 || player.get(j).getId() == 4 || player.get(j).getId() == 5){
                    userInterface.displayString("Bot don't attack");
                }else {
                    userInterface.displayString("==========COMBAT==========");
                    Thread.sleep(500);
                    //boolean to check if player want to skip combat phrase
                    boolean skip = false;
                    /*Code for combat*/
                    while (!skip) {
                        userInterface.displayString(playerNameWithColor[j]+", Input territory from which you wish to attack from. If you don't want further attack, enter skip");
                        Country = userInterface.getCommand();
                        userInterface.displayString("> " + Country);
                        int row = Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country);
                        if ("skip".equals(Country)) {
                            skip = true;
                            userInterface.displayString("Skip. Combat phrase end.");
                        } else {
                            do {
                                //if input is not a country's short name
                                if (!Arrays.asList(COUNTRY_NAMES_SHORT).contains(Country)) {
                                    userInterface.displayString(Country + " is not a validate input, enter another input (Case Sensitive)");
                                    Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Country);
                                }

                                //if this country is not controlled by current player.
                                else if (COUNTRY_CONTROL[Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)] != player.get(j).getId()) {
                                    userInterface.displayString("You have no control in that country. Please enter another country (Case Sensitive)");
                                    Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Country);
                                }

                                //if this country has less than 2 armies
                                else if (board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)) < 2) {
                                    userInterface.displayString("You cannot attack from here, since there is only one army. Please enter another country (Case Sensitive)");
                                    Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Country);
                                }else if(CommandCheck.isControlledSurround(Country,j)){
                                    userInterface.displayString("You cannot attack from here, since it's surrounded by countries under your control. Please enter another country (Case Sensitive)");
                                    Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Country);
                                }
                                else {
                                    userInterface.displayString("You are attacking from "+Country);
                                    Controlled = true;
                                }

                            } while (!Controlled);

                            String Invaded_Country;
                            userInterface.displayString("Input territory which you are attacking");
                            //get user input
                            Invaded_Country = userInterface.getCommand();
                            userInterface.displayString("> " + Invaded_Country);
                            do {
                                //============
                                //if input is not a country's short name
                                if (!Arrays.asList(COUNTRY_NAMES_SHORT).contains(Invaded_Country)) {
                                    userInterface.displayString(Invaded_Country + " is not a validate input, enter another input (Case Sensitive)");
                                    Invaded_Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Invaded_Country);
                                }

                                //if this country is controlled by current player.
                                else if (COUNTRY_CONTROL[Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)] == player.get(j).getId()) {
                                    userInterface.displayString("You cannot attack your country. Please enter another country (Case Sensitive)");
                                    Invaded_Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Invaded_Country);
                                }

                               //check adjancent
                                int cols = ADJACENT[row].length;
                                int col = cols - 1;
                                while(col >= 0 && col < cols){
                                    if(ADJACENT[row][col] == Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)){
                                        userInterface.displayString("Correct country is entered");
                                        Adjacent = true;
                                        Thread.sleep(50);
                                        break;
                                    } else  {
                                        col--;
                                    }

                                }
                                if(!Adjacent){
                                    userInterface.displayString("You cannot attack country that not adjacent. Please enter another country (Case Sensitive)");
                                    Invaded_Country = userInterface.getCommand();
                                    userInterface.displayString("> " + Invaded_Country);
                                }
                                Thread.sleep(50);

                            } while (!Adjacent);

                            //attack
                            int Attack_Army = TerritoryAndArmy.getAttackNum(userInterface, board, Country);

                            //if defender is a bot
                            if (board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) == 2 || board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) == 3 || board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) == 4 || board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) == 5) {
                                int Defend_Army = 0;
                                if (board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) > 1) {
                                    Defend_Army = 2;
                                } else if (board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) == 1) {
                                    Defend_Army = 1;
                                }
                                userInterface.displayString(Invaded_Country + " defend with "+Defend_Army+"");

                                //battle result
                                if (TerritoryAndArmy.battle(Attack_Army, Defend_Army,userInterface)) {
                                    board.subUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)), 1);
                                    userInterface.displayString("Attacker: "+Country+" win!");
                                } else {
                                    board.subUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)), 1);
                                    userInterface.displayString("Defender: "+Invaded_Country+" win!");
                                }
                            } else {
                                //if defender is a player

                                int Defend_Army = TerritoryAndArmy.getDefenderNum(userInterface, board, Invaded_Country);

                                //battle
                                if (TerritoryAndArmy.battle(Attack_Army, Defend_Army,userInterface)) {
                                    board.subUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)), 1);
                                } else {
                                    board.subUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)), 1);
                                }
                            }

                            //move
                            if (board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country)) <= 0) {

                                int Move_Army = TerritoryAndArmy.getMoveNum(userInterface, board, Country, Invaded_Country);

                                board.subUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)), Move_Army);
                                board.addUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country), board.getOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)), Move_Army);

                                board.setOccupier(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Invaded_Country),j);

                                userInterface.displayMap();
                            }
                            skip = true;
                        }
                    }

                }

                userInterface.displayMap();

                Thread.sleep(500);
                userInterface.displayString("\n==========FORTIFY==========");
                Thread.sleep(500);

                userInterface.displayString("");
                /*This is for fortify*/
                String FCountry;
                String TCountry;
                if (player.get(j).getId() == 2 || player.get(j).getId() == 3 || player.get(j).getId() == 4 || player.get(j).getId() == 5) { //if it is a bot
                    int m = (int) (NUM_COUNTRIES * Math.random());
                    while (COUNTRY_CONTROL[m] != player.get(j).getId()) {
                        m = (int) (NUM_COUNTRIES * Math.random());
                    }

                    int n = (int) (NUM_COUNTRIES * Math.random());
                    while (COUNTRY_CONTROL[n] != player.get(j).getId()||n==m) {
                        n = (int) (NUM_COUNTRIES * Math.random());
                    }

                    FCountry = COUNTRY_NAMES_SHORT[m];
                    TCountry = COUNTRY_NAMES_SHORT[n];
                    /*num of army moving */
                    int fortnum = (int) (board.getNumUnits(m)-1 * Math.random());

                    TerritoryAndArmy.takeArmy(PLAYERS_ORDER[j], FCountry, board, fortnum);
                    TerritoryAndArmy.addArmy(PLAYERS_ORDER[j], TCountry, board, fortnum);
                    if(fortnum==0)
                    {
                        userInterface.displayString(playerNameWithColor[j] + " has skip the fortify step");
                    }
                    else {
                        userInterface.displayString(playerNameWithColor[j] + " has moved " + fortnum + " armies from " + COUNTRY_NAMES[m] + " to " + COUNTRY_NAMES[n]);
                    }
                } else { //if it is a real player

                    userInterface.displayString(playerNameWithColor[j] + " fortify the army from(Case Sensitive), Enter 'skip' if you don't want to");

                    FCountry = userInterface.getCommand();
                    userInterface.displayString("> " + FCountry);
                    /*if skip is detected skip to the next turn*/
                    if(FCountry.equals("skip")){
                        userInterface.displayString(playerNameWithColor[j] + " has skip the fortify step");
                        continue;
                    }

                    do {
                        userInterface.displayString(playerNameWithColor[j] + " fortify the army to(Case Sensitive)");
                        TCountry = userInterface.getCommand();
                        userInterface.displayString("> " + TCountry);
                        while (!Arrays.asList(COUNTRY_NAMES_SHORT).contains(FCountry)) {
                            userInterface.displayString(Country + " is not a validate input, enter another input (Case Sensitive)");
                            FCountry = userInterface.getCommand();
                            userInterface.displayString("> " + FCountry);
                        }
                        while (!Arrays.asList(COUNTRY_NAMES_SHORT).contains(TCountry)) {
                            userInterface.displayString(Country + " is not a validate input, enter another input (Case Sensitive)");
                            TCountry = userInterface.getCommand();
                            userInterface.displayString("> " + TCountry);
                        }
                        if ((COUNTRY_CONTROL[Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(FCountry)] == PLAYERS_ORDER[i])&&(COUNTRY_CONTROL[Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(TCountry)] == PLAYERS_ORDER[i])) {
                            Controlled = true;
                        } else {
                            userInterface.displayString("You have no control in that country. Please enter another country (Case Sensitive)");
                        }
                    } while (!Controlled);
                    userInterface.displayString("How many armies do you wish to move?");
                    String fortnum_String = userInterface.getCommand();

                    userInterface.displayString("> " + fortnum_String);
                    for (int d = 0; d < fortnum_String.length(); d++) {
                        while (!Character.isDigit(fortnum_String.charAt(d))) {
                            userInterface.displayString("You need to enter a number");
                            fortnum_String = userInterface.getCommand();
                            userInterface.displayString("> " + fortnum_String);
                        }
                    }
                    if(fortnum_String.equals("") || fortnum_String.equals(" ") || fortnum_String.equals("\n")){
                        userInterface.displayString("You need to enter a number");
                    }
                    while (Integer.parseInt(fortnum_String) >= board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(FCountry))) {
                        userInterface.displayString("You need to enter a correct number");
                        fortnum_String = userInterface.getCommand();
                        userInterface.displayString("> " + fortnum_String);
                    }
                    int fortnum = Integer.parseInt(fortnum_String);
                    TerritoryAndArmy.takeArmy(PLAYERS_ORDER[i], FCountry, board, fortnum);
                    TerritoryAndArmy.addArmy(PLAYERS_ORDER[i], TCountry, board, fortnum);
                    player.get(j).subtractUnits(3);

                    userInterface.displayString(playerNameWithColor[j] + " has moved " + fortnum + " armies from "+Arrays.asList(COUNTRY_NAMES).get(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(FCountry))+" to "+Arrays.asList(COUNTRY_NAMES).get(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(TCountry)));
                }
                userInterface.displayMap();
                Thread.sleep(200);

            }
        }
    }
}