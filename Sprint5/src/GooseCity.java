// put your code here

import java.util.ArrayList;
import java.util.Comparator;

public class GooseCity implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example

	private BoardAPI board;
	private PlayerAPI player;
	private ArrayList<Attack> availableAttacks = new ArrayList<>();
	private ArrayList<Attack> availableThreats = new ArrayList<>();
	private ArrayList<Country> border;
	private ArrayList<Country> countries;

	GooseCity (BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;
		player = inPlayer;
		countries = newCountriesList();
	}
	private ArrayList<Country> newCountriesList(){
		ArrayList<Country> countrylist = new ArrayList<>();
		int i = 0;
		while(i<GameData.NUM_COUNTRIES){
			countrylist.add(new Country(i));
			i++;
		}
		return countrylist;
	}

	public String getName () {
		String command = "";
		command = "GooseCityBot";
		return(command);
	}

	public String getReinforcement () {
		String command = "";
		String country = getCountry();

		country = country.replaceAll("\\s", "");
		command = country + " 1";
		return(command);
	}

	public String getPlacement (int forPlayer) {
		String command = "";
		String country = getPlacementCountry(forPlayer);

		country = country.replaceAll("\\s", "");
		command = country;
		return(command);
	}

	public String getCardExchange () {
		return player.isForcedExchange() ? getTradeCards() : "skip";
	}

	public String getBattle () {
		String command = "";
		//find all the available attacks to make
		availableAttacks = new ArrayList<>();
		getAvailableAttacksAndThreats();
		//skip battle phase if there's no available attacks to make
		if(!availableAttacks.isEmpty()) {
			int countryAtkFrom = availableAttacks.get(0).countryAtkFrom;
			int countryAtkTo = availableAttacks.get(0).countryAtkTo;
			int maxDifference = availableAttacks.get(0).advantage;
			//find the Attack where the attacker has the most advantage
			for (Attack availableAttack : availableAttacks) {
				if (maxDifference < availableAttack.advantage) {
					maxDifference = availableAttack.advantage;
					countryAtkFrom = availableAttack.countryAtkFrom;
					countryAtkTo = availableAttack.countryAtkTo;
				}
			}
			command += GameData.COUNTRY_NAMES[countryAtkFrom].replaceAll("\\s", "");
			command += " ";
			command += GameData.COUNTRY_NAMES[countryAtkTo].replaceAll("\\s", "");
			//decide how many armies to send
			if (board.getNumUnits(countryAtkFrom) == 2) {
				command += " 1";
			} else if (board.getNumUnits(countryAtkFrom) == 3) {
				command += " 2";
			} else {
				command += " 3";
			}
			if (maxDifference == 1) {
				command = "skip";
			}
		}else {
			command="skip";
		}
		return(command);
	}

	public String getDefence (int countryId) {
		String command = "";
		command = (board.getNumUnits(countryId)==1) ? "1" : "2";
		return(command);
	}

	public String getMoveIn (int attackCountryId) {
		String command = "";
		command = (board.getNumUnits(attackCountryId)<10) ? String.valueOf(board.getNumUnits(attackCountryId)/2) : String.valueOf(board.getNumUnits(attackCountryId)/3*2);
		return(command);
	}

	public String getFortify () {
		String command = "";
		//find all the available attacks to make form enemies
		availableThreats = new ArrayList<>();
		getAvailableAttacksAndThreats();
		//skip Fortify phase if there's no available threats
		if(!availableThreats.isEmpty()) {
			int countryFortFrom = availableThreats.get(0).countryAtkTo;
			int countryFortTo = availableThreats.get(0).countryAtkFrom;
			int maxDifference = availableThreats.get(0).advantage;
			int maxUnit = 1;
			//find the Attack where the attacker has the most advantage
			for (Attack availableThreats : availableThreats) {
				//advantage here is sub zero
				if (maxDifference > availableThreats.advantage) {
					maxDifference = availableThreats.advantage;
					countryFortTo = availableThreats.countryAtkTo;
				}
			}
			//find the territory that have most armies
			for (int j=0; j<GameData.ADJACENT[countryFortTo].length; j++) {
				if (board.isConnected((GameData.ADJACENT[countryFortTo][j]), countryFortTo)) {
					if(maxUnit<board.getNumUnits(GameData.ADJACENT[countryFortTo][j])){
						maxUnit=board.getNumUnits(GameData.ADJACENT[countryFortTo][j]);
						countryFortFrom = GameData.ADJACENT[countryFortTo][j];
					}
				}
			}
			command += GameData.COUNTRY_NAMES[countryFortFrom].replaceAll("\\s", "");
			command += " ";
			command += GameData.COUNTRY_NAMES[countryFortTo].replaceAll("\\s", "");
			if (board.getNumUnits(countryFortFrom) == 1) {
				command = "skip";
			}else if (board.getNumUnits(countryFortFrom) == 2) {
				command += " 1";
			}else {
				command += " ";
				command += board.getNumUnits(countryFortFrom)*2/3;
			}

		}else {
			command="skip";
		}
		return(command);
	}

	//helper functions

	private String getTradeCards() {
		ArrayList<Card> cards = player.getCards();
		int[] cardType = new int[4];
		for(Card card : cards)
			cardType[card.getInsigniaId()]++;

		// INFANTRY = 0; CAVALRY = 1; ARTILLERY = 2;
		if(cardType[0]>=3) return "iii";
		if(cardType[1]>=3) return "ccc";
		if(cardType[2]>=3) return "aaa";

		if(cardType[0]>=2 && cardType[3]>=1) return "iiw";
		if(cardType[1]>=2 && cardType[3]>=1) return "ccw";
		if(cardType[2]>=2 && cardType[3]>=1) return "aaw";

		if(cardType[0]>=1 && cardType[1]>=1 && cardType[3]>=1) return "icw";
		if(cardType[0]>=1 && cardType[2]>=1 && cardType[3]>=1) return "iwa";
		if(cardType[1]>=1 && cardType[2]>=1 && cardType[3]>=1) return "wca";

		if(cardType[0]>=1 && cardType[3]>=2) return "iww";
		if(cardType[1]>=1 && cardType[3]>=2) return "cww";
		if(cardType[2]>=1 && cardType[3]>=2) return "aww";

		if(cardType[0]>=1 && cardType[1]>=1 && cardType[2]>=1) return "ica";
		return "www";
	}
	public class Attack{
		int countryAtkFrom;
		int countryAtkTo;
		int advantage;

		public Attack(int countryAtkFrom, int countryAtkTo){
			this.countryAtkFrom = countryAtkFrom;
			this.countryAtkTo = countryAtkTo;
			//amount of advantage or disadvantage bot has to the enemies
			advantage = board.getNumUnits(countryAtkFrom) - board.getNumUnits(countryAtkTo);
		}
	}

	private void getAvailableAttacksAndThreats(){
		for (int i=0; i < GameData.NUM_COUNTRIES; i++){
			if (board.getOccupier(i) == 1){
				for (int j=0; j<GameData.ADJACENT[i].length; j++){
					//if the surrounding enemies has less armies than the bot, add it to available attacks
					if (board.getOccupier(GameData.ADJACENT[i][j]) != player.getId() && board.getNumUnits(i)>board.getNumUnits(j)){
						availableAttacks.add(new Attack(i, GameData.ADJACENT[i][j]));
					}
					if (board.getOccupier(GameData.ADJACENT[i][j]) != player.getId() && board.getNumUnits(i)<board.getNumUnits(j)){
						//if the surrounding enemies has more armies than the bot, add it to available threats
						for (int c=0; c < GameData.NUM_COUNTRIES; c++) {
							if (board.getOccupier(c) == 1) {
								if (board.isConnected((GameData.ADJACENT[i][j]),c)){
									availableThreats.add(new Attack(i, GameData.ADJACENT[i][j]));
								}
							}
						}
					}
				}
			}
		}
	}

	private ArrayList<Country> getEnemyBorder(int neuralID){
		int enemyId;
		if(player.getId()==1){
			enemyId = 0;
		}else{
			enemyId = 1;
		}
		ArrayList<Country> adjacentEnemy = new ArrayList<>();
		for(Country country: countries){
			if(board.getOccupier(country.index) == enemyId){
				for (int i = 0; i<country.adjacents.length; i ++){
					Country borderCo = countries.get(country.adjacents[i]);
					if(board.getOccupier(country.index)== neuralID){
						adjacentEnemy.add(borderCo);
					}
				}
			}
		}
		return adjacentEnemy;
	}

	class Country{
		public String name;
		public int index;
		public int continent;
		public int[] adjacents;

		public Country(int index){
			this.index = index;
			name = GameData.COUNTRY_NAMES[index];
			continent = GameData.CONTINENT_IDS[index];
			adjacents = GameData.ADJACENT[index];
		}
		public int numArmy(){return board.getNumUnits(index);}
	}

	private void getBorder(){
		for (int i = 0; i < GameData.NUM_COUNTRIES;i++){
			if (board.getOccupier(i) == player.getId()){
				for (int j = 0; j < GameData.ADJACENT[i].length; j++){
					if(board.getOccupier(GameData.ADJACENT[i][j])!= player.getId()) {
						border.add(new Country(i));
					}
				}
			}
		}
	}

 	private String getCountry(){
		String country;
	 	Comparator<Country> compareCountryByArmy = Comparator.comparingInt(Country::numArmy);
	 	border = new ArrayList<>();
	 	getBorder();
	 	if(border.size()>0){
			 border.sort(compareCountryByArmy);
		 	country = border.get(0).name;
	 	}else{
	 		//if no border country
		 	int countryId = (int)(Math.random() * GameData.NUM_COUNTRIES);
		 	while(board.getOccupier(countryId) != player.getId()) {
				 countryId = (int) (Math.random() * GameData.NUM_COUNTRIES);
		 	}
		 	country = GameData.COUNTRY_NAMES[countryId];
	 	}
	 	return country;
 	}

 	private String getPlacementCountry(int forPlayer){
		String country;
	 	Comparator<Country> compareCountryByArmy = Comparator.comparingInt(Country::numArmy);

	 	ArrayList<Country> adjacentEnemy = getEnemyBorder(forPlayer);
	 	if(adjacentEnemy.size() !=0 ){
			 adjacentEnemy.sort(compareCountryByArmy);
			 country = adjacentEnemy.get(0).name;
		 }else{
		 	int countryId = (int)(Math.random() * GameData.NUM_COUNTRIES);
		 	while(board.getOccupier(countryId) != player.getId()) {
				 countryId = (int) (Math.random() * GameData.NUM_COUNTRIES);
		 	}
		 	country = GameData.COUNTRY_NAMES[countryId];
	 	}
		 return country;
 	}
}

