package Model;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Controller.UI;


public class Game {

	public List<Player> playerList = new ArrayList<>();
	private Random randomGenerator = new Random();//for dice rolls
	private List<Card> solutionEnvelope = new ArrayList<>();
	private Board board;
	private Player currentPlayer;
	private int currentPlayerIndex = 0;
	private List<Player> removedPlayerList = new ArrayList<>();
	private int dice = 0;

	private List<Point> weaponStartPoints = new ArrayList<>();
	public List<WeaponToken> weaponTokens = new ArrayList();
	
	/**
	 * Initialises the game state
	 * @param playerCount = the amount of players in the game
	 */
	public void start(int playerCount) {
		board = new Board();
		for(int i=0; i<playerCount; i++)playerList .add(new Player(String.valueOf(i+1)));
		currentPlayer = playerList.get(0);
		dealCards();
		placeWeapons();
		setPlayerStart();
		
	}
	/**
	 * Assign x and y coordinates to the weapons so they can be drawn on
	 * the board
	 */

	private void placeWeapons() {
		weaponStartPoints.add(new Point(1, 3));
		weaponStartPoints.add(new Point(10, 3));
		weaponStartPoints.add(new Point(19, 3));
		weaponStartPoints.add(new Point(19, 10));
		weaponStartPoints.add(new Point(19, 16));
		weaponStartPoints.add(new Point(19, 23));
		weaponStartPoints.add(new Point(10, 21));
		weaponStartPoints.add(new Point(2, 22));
		weaponStartPoints.add(new Point(2, 12));
		Collections.shuffle(weaponStartPoints);
		
		Point p = weaponStartPoints.remove(0);
		WeaponToken candlestick = new WeaponToken((int) p.x, (int) p.y, "Candlestick ");
		p = weaponStartPoints.remove(0);
		WeaponToken dagger = new WeaponToken((int) p.x, (int) p.y, "Dagger");
		p = weaponStartPoints.remove(0);
		WeaponToken leadpipe = new WeaponToken((int) p.x, (int) p.y, "Lead Pipe");
		p = weaponStartPoints.remove(0);
		WeaponToken revolver = new WeaponToken((int) p.x, (int) p.y, "Revolver ");
		p = weaponStartPoints.remove(0);
		WeaponToken rope = new WeaponToken((int) p.x, (int) p.y, "Rope  ");
		p = weaponStartPoints.remove(0);
		WeaponToken spanner = new WeaponToken((int) p.x, (int) p.y, "Spanner  ");
		
		weaponTokens.add(candlestick);
		weaponTokens.add(dagger);
		weaponTokens.add(leadpipe);
		weaponTokens.add(revolver);
		weaponTokens.add(rope);
		weaponTokens.add(spanner);
		
		board.placeWeapon(weaponTokens);
	}

	/**
	 * This method creates the accusation envelope and deals the cards
	 * out to the players randomly
	 */
	public void dealCards() {
		List<Card> weaponsDeck = new ArrayList<Card>(){{
			add(new Weapon("Candlestick"));
			add(new Weapon("Dagger"));
			add(new Weapon("Lead Pipe"));
			add(new Weapon("Revolver"));
			add(new Weapon("Rope"));
			add(new Weapon("Spanner"));

		}};
		List<Card> roomsDeck = new ArrayList<Card>(){{
			add(new Room("Kitchen"));
			add(new Room("Ballroom"));
			add(new Room("Conservatory"));
			add(new Room("Dining Room"));
			add(new Room("Billiard Room"));
			add(new Room("Library"));
			add(new Room("Study"));
			add(new Room("Hall"));
			add(new Room("Lounge"));

		}};
		List<Card> charactersDeck = new ArrayList<Card>(){{
			add(new Character("Miss Scarlett"));
			add(new Character("Colonel Mustard"));
			add(new Character("Mrs. White"));
			add(new Character("The Reverend Green"));
			add(new Character("Mrs. Peacock"));
			add(new Character("Professor Plum"));

		}};

		int cardIndex = randomGenerator.nextInt(6);
		solutionEnvelope.add(weaponsDeck.remove(cardIndex));
		cardIndex = randomGenerator.nextInt(9);
		solutionEnvelope.add(roomsDeck.remove(cardIndex));
		cardIndex = randomGenerator.nextInt(6);
		solutionEnvelope.add(charactersDeck.remove(cardIndex));

		List<Card> deck = new ArrayList<>();
		deck.addAll(weaponsDeck);
		deck.addAll(roomsDeck);
		deck.addAll(charactersDeck);

		int player = 0;

		Collections.shuffle(deck);
		for(Card c: deck){
			playerList.get(player).addCard(c);
			player++;
			if(player == playerList.size())player = 0;
		}
	}

	/**
	 * Sets the starting positions of the players. This could be in the board
	 * class.
	 */
	private void setPlayerStart(){
		List<Integer> startingPositions = new ArrayList<>();
		startingPositions.add(0);
		startingPositions.add(9);
		startingPositions.add(0);
		startingPositions.add(14);
		startingPositions.add(6);
		startingPositions.add(23);
		startingPositions.add(19);
		startingPositions.add(23);
		startingPositions.add(24);
		startingPositions.add(7);
		startingPositions.add(17);
		startingPositions.add(0);
		int i=0;
		for(Player p: playerList){
			p.setYpos(startingPositions.get(i));
			p.setXpos(startingPositions.get(i+1));
			i=i+2;
		}
	}

	//The list of choices that are used in the turn method.
	private List<String> choicesList = new ArrayList<String>(){{
		add("Roll Dice");
		add("Make a suggestion");
		add("Make an accusation");
		add("See hand");
		add("End Turn");
	}};

	/**
	 * Simple method to reset the choices, this is called when the player ends their turn
	 */
	public void resetChoicesList() {
		choicesList = new ArrayList<String>(){{
			add("Roll Dice");
			add("Make a suggestion");
			add("Make an accusation");
			add("See hand");
			add("End Turn");
		}};
	}

	/**
	 * Takes an accusation and checks if it is valid.
	 */
	public boolean accusation(List<Card> playerAccusation) {	
		for(Card c: playerAccusation){
			if(!solutionEnvelope.contains(c)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Takes a suggestion and returns the Card that was found to contradict
	 * the suggestion
	 */
	public Card suggestion(List<Card> sug, Card room, Card weapon){
		board.moveWeapon(weapon.toString(), room.toString(), weaponStartPoints);
		for(Player p: playerList){
			for(Card c: sug){
				if(p.holding(c) && !p.equals(currentPlayer)){
					System.out.println("playerlist");
					return c;
				}
			}
		}
		for(Player p: removedPlayerList){
			for(Card c: sug){
				if(p.holding(c) && !p.equals(currentPlayer)){
					System.out.println("removed");
					return c;
				}
			}
		}
		return null;
	}
	

	/**
	 * removes a player from the game, called when they get an accusation wrong
	 */
	public void removePlayer() {
		playerList.remove(currentPlayerIndex);
		removedPlayerList.add(currentPlayer);
		if(currentPlayerIndex == playerList.size())currentPlayerIndex = 0;
		resetChoicesList();		
	}
	
	/**
	 * Ends the current players turn.
	 */
	public void endTurn() {
		currentPlayerIndex++;
		if(currentPlayerIndex == playerList.size())currentPlayerIndex = 0;
		currentPlayer = playerList.get(currentPlayerIndex);
		resetChoicesList();		
	}

	/**
	 * Moves the player in the direction supplied
	 */
	public boolean move(String dir) {
		Point newPoint = board.checkMove(currentPlayer.getX(), currentPlayer.getY(), dir, playerList);
		if(newPoint!=null){
			dice = dice-1;
			currentPlayer.move(dir);
			return true;
		}
		return false;

	}
	/*********************************************************/
	/**
	 * Setters and getters.
	 */	
	/*********************************************************/

//	public void setUI(UI ui) {
//		this.ui = ui;
//	}

	public List<String> getChoicesList() {
		return choicesList;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Board getBoard() {
		return board;
	}

	public List<Card> getEnvelope() {
		return solutionEnvelope;

	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public int getDice() {
		return dice;
	}

	public boolean turnEnded() {
		return !(dice > 0);
	}

	public int rollDice() {
		dice = randomGenerator.nextInt(6)+1;
		return dice;
	}
	
	//note that this removes the choice from the list.
	public String getChoice(int choice) {
		return choicesList.remove(choice);
	}
}
