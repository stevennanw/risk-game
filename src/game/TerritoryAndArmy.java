package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static util.constants.*;
import static util.constants.COUNTRY_NAMES_SHORT;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class TerritoryAndArmy {
    private static boolean phase = false;

    /**
     * Allocate Territories
     * @param board board
     */

    public static void allocateTerritories(UserInterface userInterface, Board board){
        Random ran1 = new Random();
        int[] count = {0,0,0,0,0,0};
        int i = 0;

        while (i < NUM_COUNTRIES){
            int m = ran1.nextInt(6);

            switch (m){
                case 0:
                    if(count[0]<INIT_COUNTRIES_PLAYER){
                        COUNTRY_CONTROL[i]=m;
                        count[0]++;
                    } else {
                        i--;
                    }
                    break;
                case 1:
                    if(count[1]<INIT_COUNTRIES_PLAYER){
                        COUNTRY_CONTROL[i]=m;
                        count[1]++;
                    } else {
                        i--;
                    }
                    break;
                case 2:
                    if(count[2]<INIT_COUNTRIES_NEUTRAL){
                        COUNTRY_CONTROL[i]=m;
                        count[2]++;
                    } else {
                        i--;
                    }
                    break;
                case 3:
                    if(count[3]<INIT_COUNTRIES_NEUTRAL){
                        COUNTRY_CONTROL[i]=m;
                        count[3]++;
                    } else {
                        i--;
                    }
                    break;
                case 4:
                    if(count[4]<INIT_COUNTRIES_NEUTRAL){
                        COUNTRY_CONTROL[i]=m;
                        count[4]++;
                    } else {
                        i--;
                    }
                    break;
                case 5:
                    if(count[5]<INIT_COUNTRIES_NEUTRAL){
                        COUNTRY_CONTROL[i]=m;
                        count[5]++;
                    } else {
                        i--;
                    }
                    break;
            }
            i++;
        }

        for (int j = 0; j < NUM_COUNTRIES; j++) {
            board.addUnits(j, COUNTRY_CONTROL[j], 1);
        }
        userInterface.displayMap();
    }


    /**
     * Take territories
     * @param userInterface userInterface
     * @param player player
     * @param board board
     * @throws InterruptedException sleep
     */
    public static void takeTerritories(UserInterface userInterface, ArrayList<Playerinfo> player, Board board, String[] playerNameWithColor) throws InterruptedException {
        Thread.sleep(1000);
        userInterface.displayString("==========TAKE TERRITORY==========");
        Thread.sleep(500);
        while(player.get(0).getArmies() != 0&&player.get(1).getArmies() != 0&&player.get(2).getArmies() != 0&&player.get(3).getArmies() != 0&&player.get(4).getArmies() != 0&&player.get(5).getArmies() != 0) {
            for (int i = 0; i < NUM_PLAYERS_PLUS_NEUTRALS; i++) {
                userInterface.displayString("");

                int j = PLAYERS_ORDER[i];

                placeArmy(userInterface, player, board, playerNameWithColor, i, j);

                userInterface.displayMap();
            }
        }
    }

    /**
     *
     * @param userInterface userInterface
     * @param player player
     * @param board board
     * @param playerNameWithColor playerNameWithColor
     * @param i i
     * @param j j
     */
    public static void placeArmy(UserInterface userInterface, ArrayList<Playerinfo> player, Board board, String[] playerNameWithColor, int i, int j) {
        String Country;
        boolean Controled = false;
        if (player.get(j).getId() == 2 || player.get(j).getId() == 3 || player.get(j).getId() == 4 || player.get(j).getId() == 5) { //if it is a bot

            int m = (int) (NUM_COUNTRIES * Math.random());
            while (COUNTRY_CONTROL[m] != player.get(j).getId()) {
                m = (int) (NUM_COUNTRIES * Math.random());
            }
            Country = COUNTRY_NAMES_SHORT[m];
            addArmy(PLAYERS_ORDER[j], Country, board, 1);
            player.get(j).subtractUnits(1);
            userInterface.displayString(playerNameWithColor[j] + " has " + player.get(j).getArmies() + " armies remaining.");
            userInterface.displayString(playerNameWithColor[j] + " place the army on " + COUNTRY_NAMES[m]);
        } else { //if it is a real player

                userInterface.displayString(playerNameWithColor[j] + " place the army (Case Sensitive)");
                do {
                    Country = userInterface.getCommand();
                    userInterface.displayString("> " + Country);
                    while (!Arrays.asList(COUNTRY_NAMES_SHORT).contains(Country)) {
                        userInterface.displayString(Country + " is not a validate input, enter another input (Case Sensitive)");
                        Country = userInterface.getCommand();
                        userInterface.displayString("> " + Country);
                    }
                    if (COUNTRY_CONTROL[Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)] == PLAYERS_ORDER[i]) {
                        Controled = true;
                    } else {
                        userInterface.displayString("You have no control in that country. Please enter another country (Case Sensitive)");
                    }
                } while (!Controled);
            if (phase) {
                boolean correctInput = false;
                userInterface.displayString(playerNameWithColor[j] + " has " + player.get(j).getArmies() + " armies remaining.");
                userInterface.displayString("Please enter the number of armies you wish to deploy");
                String Deploynum_String = null;
                while (!correctInput) {
                    Deploynum_String = userInterface.getCommand();
                    userInterface.displayString("> " + Deploynum_String);
                    if (Deploynum_String.equals("") || Deploynum_String.equals(" ") || Deploynum_String.equals("\n")) {
                        userInterface.displayString("You need to enter a correct number");
                        continue;
                    }
                    for (int d = 0; d < Deploynum_String.length(); d++) {
                        while (!Character.isDigit(Deploynum_String.charAt(d))) {
                            userInterface.displayString("You need to enter a number");
                            Deploynum_String = userInterface.getCommand();
                            userInterface.displayString("> " + Deploynum_String);
                        }
                    }
                    if (Integer.parseInt(Deploynum_String) <= player.get(j).getArmies() && Integer.parseInt(Deploynum_String) > 0) {
                        correctInput = true;
                    } else {
                        userInterface.displayString("You need to enter a correct number");
                    }
                }
                addArmy(PLAYERS_ORDER[i], Country, board, Integer.parseInt(Deploynum_String));
                player.get(j).subtractUnits(Integer.parseInt(Deploynum_String));
                userInterface.displayString(playerNameWithColor[j] + " has " + player.get(j).getArmies() + " armies remaining.");
                userInterface.displayString("Your armies have been successfully deployed on " + Arrays.asList(COUNTRY_NAMES).get(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)) + ".");
            }else {

                addArmy(PLAYERS_ORDER[i], Country, board, 3);
                player.get(j).subtractUnits(3);
                userInterface.displayString(playerNameWithColor[j] + " has " + player.get(j).getArmies() + " armies remaining.");
                userInterface.displayString("Your armies have been successfully deployed on " + Arrays.asList(COUNTRY_NAMES).get(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)) + ".");

            }
        }
    }


    /**
     * Add armies
     * @param PLAYER PLAYER
     * @param COUNTRY COUNTRY
     * @param board board
     * @param Num Number of army
     */
    public static void addArmy(int PLAYER, String COUNTRY, Board board, int Num){
        int CountryID = Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(COUNTRY);
        board.addUnits(CountryID, PLAYER,Num);
    }

    public static void switchPhase(){
        if(!phase) {
            phase = true;
        }
    }



    /**
     *
     * @param PLAYER PLAYER
     * @param COUNTRY COUNTRY
     * @param board board
     * @param Num Num
     */
    public static void takeArmy(int PLAYER, String COUNTRY, Board board, int Num){
        int CountryID = Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(COUNTRY);
        board.subUnits(CountryID, PLAYER,Num);
    }

    /**
     *
     * @param userInterface userInterface
     * @param board board
     * @param Country Country
     * @return Attack_Army
     */
    public static int getAttackNum(UserInterface userInterface, Board board, String Country) {
        userInterface.displayString("How many troops for attacking?");
        userInterface.displayString("You can use from 1 to 3 armies to attack and remain as least 1 army.");
        //get user input for army_num
        String Army_num = userInterface.getCommand();
        userInterface.displayString("> " + Army_num);
        boolean pass = false;
        do {
            //if this is not a positive integer

            if(!CommandCheck.isOkNumber(Army_num, board, Country, 1, 3)){
                userInterface.displayString("You need to enter a positive integer, from 1 to 3. PLease reentry");
                Army_num = userInterface.getCommand();
                userInterface.displayString("> " + Army_num);
            }

            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString("You choose "+Integer.parseInt(Army_num)+" armies to attack");
        return Integer.parseInt(Army_num);
    }

    /**
     *
     * @param userInterface userInterface
     * @param board board
     * @param Invaded_Country Invaded_Country
     * @return Defender_Army
     */
    public static int getDefenderNum(UserInterface userInterface, Board board, String Invaded_Country) {
        userInterface.displayString("How many defenders you want to use?");
        userInterface.displayString("You can use from 1 to 2 armies to defend, if you have only one army here, you can only choose 1.");
        String Army_num = userInterface.getCommand();
        userInterface.displayString("> " + Army_num);
        boolean pass = false;
        //enter defend number
        do {
            //if this is neither 1 or 2
            if(!CommandCheck.isOkNumber(Army_num, board, Invaded_Country, 1, 2)){
                userInterface.displayString("You need to enter a positive integer, from 1 to 2. PLease reentry");
                Army_num = userInterface.getCommand();
                userInterface.displayString("> " + Army_num);
            }

            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString(Invaded_Country + " defend with "+Integer.parseInt(Army_num)+"");
        return Integer.parseInt(Army_num);
    }

    /**
     *
     * @param userInterface userInterface
     * @param board board
     * @param Country Country
     * @param Invaded_Country Invaded_Country
     * @return Move_Arnmy
     */
    public static int getMoveNum(UserInterface userInterface, Board board, String Country, String Invaded_Country) {
        userInterface.displayString("You have defeat enemy, How many army you want to move?");
        String Army_num = userInterface.getCommand();
        userInterface.displayString("> " + Army_num);
        boolean pass = false;
        do {
            //if this is neither 1 or 2 //todo
            if(!CommandCheck.isOkNumber(Army_num, board, Country, 0, board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)))){
                userInterface.displayString("TSET:"+board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country)));
                userInterface.displayString("Invalid input. PLease reentry");
                Army_num = userInterface.getCommand();
                userInterface.displayString("> " + Army_num);
            }

            else {
                pass = true;
            }
        } while (!pass);
        userInterface.displayString("You move "+Integer.parseInt(Army_num)+" armies to " + Invaded_Country);
        return Integer.parseInt(Army_num);
    }

    /**
     * to check if attacker win
     * @param Attack_Army Attack_Army
     * @param Defend_Army Defend_Army
     * @param userinterface userinterface
     * @return attacker win
     */
    public static boolean battle(int Attack_Army, int Defend_Army ,UserInterface userinterface){
        int die1=0;
        int temp;
        int die2=0;
        for(int i=0;i < Attack_Army; i++){
            temp = die1;
            die1 = (int) (Math.random() * 6 +1);
            if(temp > die1){
                die1 = temp;
            }
        }
        userinterface.displayString("Attacker's die is "+ die1);
        for(int i=0;i < Defend_Army; i++){
            temp = die2;
            die2 = (int) (Math.random() * 6 +1);
            if(temp > die2){
                die2 = temp;
            }
        }
        userinterface.displayString("Defender's die is "+ die2);
        if(die1>die2){
            userinterface.displayString("Attacker's "+ die1+" > " + "Defender's "+die2 + " Attacker wins");
            return true;
        }else if(die1==die2){
            userinterface.displayString("Attacker's "+ die1+" = " + "Defender's "+die2 + " defender wins");
            return false;
        }else{
            userinterface.displayString("Attacker's "+ die1+" < " + "Defender's "+die2 + " defender wins");
            return false;
        }
    }
}
