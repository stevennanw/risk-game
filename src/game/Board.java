package game;

import static util.constants.*;

/*
 * @Team_Name GooseCity
 * @Student_Number 18210184 19353281 19204160
 */

public class Board {

    private final boolean[] occupied = new boolean [NUM_COUNTRIES];
    private final int[] occupier = new int [NUM_COUNTRIES];
    private final int[] numUnits = new int [NUM_COUNTRIES];

    public Board() {
        for (int i = 0; i< NUM_COUNTRIES; i++) {
            occupied[i] = false ;
            numUnits[i] = 0;
        }
    }

    public void addUnits (int country, int player, int addNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!occupied[country]) {
            occupied[country] = true;
            occupier[country] = player;
        }
        numUnits[country] = numUnits[country] + addNumUnits;
    }
    public void subUnits (int country, int player, int subNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!occupied[country]) {
            occupied[country] = true;
            occupier[country] = player;
        }
        numUnits[country] = numUnits[country] - subNumUnits;
    }


    public boolean isOccupied(int country) {
        return occupied[country];
    }

    public int getOccupier (int country) {
        return occupier[country];
    }

    public int setOccupier(int country, int player){
        occupier[country]=player;
        COUNTRY_CONTROL[country]=player;
        return occupier[country];
    }

    public int getNumUnits (int country) {
        return numUnits[country];
    }

}
