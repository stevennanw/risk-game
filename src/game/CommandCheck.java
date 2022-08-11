package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import static util.constants.*;
import static util.constants.ADJACENT;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class CommandCheck {
    /**
     * To check if it is controlled surround
     * @param country country
     * @param j j
     * @return if it is controlled surround
     */
    public static boolean isControlledSurround(String country,int j){
        int row = Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(country);
        int cols = ADJACENT[row].length;
        int col = cols-1;
        while(col>=0 && col<cols){
            if(COUNTRY_CONTROL[ADJACENT[row][col]] != j){
                return false;
            }
            col--;
        }
        return true;
    }

    /**
     * THis method is to test if input is a valid number
     * @param string string
     * @param board board
     * @param Country Country
     * @param low low
     * @param high high
     * @return if input is a valid number
     */
    public static boolean isOkNumber(String string, Board board, String Country, int low, int high) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if(pattern.matcher(string).matches()){
            if(Integer.parseInt(string) > high || Integer.parseInt(string) < low){
                return false;
            }
            else return Integer.parseInt(string) <= board.getNumUnits(Arrays.asList(COUNTRY_NAMES_SHORT).indexOf(Country));
        } else return false;
    }

    /**
     *
     * @param cards cards
     * @param id id
     * @return true if this card is mine
     */
    public static boolean isMyCardID(ArrayList<Card> cards, String id) {
        Pattern pattern = Pattern.compile("^\\d{1,3}$"); //0-999
        if(pattern.matcher(id).matches()){
            for (Card card : cards) {
                if (card.getCardId() == Integer.parseInt(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
