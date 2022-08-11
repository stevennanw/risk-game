package game;

import java.util.ArrayList;
import java.util.Arrays;
import static util.constants.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class RollDice {
    /**
     * Prepare work for rolling dice
     * @param userInterface userInterface
     * @param player player
     */
    public static void roll(UserInterface userInterface, ArrayList<Playerinfo> player) {
        for (int i = 0; i < ROLL_NUM; i++) {
            PLAYERS_ORDER[i] = i;
        }
        userInterface.displayString("==========ROLL DICE==========");
        rollDice(userInterface, PLAYERS_ORDER, ROLL_DICE);
        userInterface.displayString("==========FINAL RESULT==========");
        userInterface.displayString("ROLL_DICE: "+ Arrays.toString(ROLL_DICE));
        userInterface.displayString("PLAYER_ORDER: "+Arrays.toString(PLAYERS_ORDER));
        StringBuilder orderString = new StringBuilder();
        for(int i=0; i<ROLL_NUM;i++)
            orderString.append(player.get(PLAYERS_ORDER[i]).getName()).append(", ");
        userInterface.displayString("ORDER to play: "+ orderString + ".");

    }

    /**
     * Roll dice
     * @param userInterface UserInterface
     * @param PLAYERS_ORDER PLAYERS_ORDER
     * @param ROLL_DICE ROLL_DICE
     */
    public static void rollDice(UserInterface userInterface,  int[] PLAYERS_ORDER, int[] ROLL_DICE){
        for (int i = 0; i < ROLL_NUM; i++) {
            ROLL_DICE[i] = (int) (Math.random() * 6);
        }
        selectionSort(ROLL_DICE, PLAYERS_ORDER);
        for(int i=0; i<ROLL_NUM-1; i++){
            if(ROLL_DICE[i]==ROLL_DICE[i+1]){
                userInterface.displayString("Dice: "+Arrays.toString(ROLL_DICE)+", Re-roll if a draw");
                rollDice(userInterface, PLAYERS_ORDER, ROLL_DICE);
                break;
            }
        }
    }

    /**
     * Sort order by dice
     * @param array1 ROLL_DICE
     * @param array2 PLAYERS_ORDER
     */
    public static void selectionSort(int[] array1, int[] array2){
        for(int i=0; i<array1.length-1; i++){
            int min_index = i;
            for(int j=i+1; j<array1.length; j++){
                if(array1[min_index]>array1[j])
                    min_index = j;
            }
            int temp1 = array1[i];
            int temp2 = array2[i];
            array1[i] = array1[min_index];
            array2[i] = array2[min_index];
            array1[min_index] = temp1;
            array2[min_index] = temp2;
        }
    }
}
