package Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.activity.InvalidActivityException;

import Model.Card;
import Model.Game;
import Model.Player;
import Model.Room;
import Model.Weapon;
import Model.Character;


public class UI {


	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private Game game;

	/**
	 * Constructor that creates an new Game and starts the engine running
	 * @param game
	 */
	public UI(Game game){
		this.game = game;
		int playerCount=0;
		boolean playerCountChoosen = false;
		while(!playerCountChoosen ){
			try {
				System.out.println("How Many Players? 3-6");
				System.out.print("> ");
				String text = input.readLine();
				playerCount = Integer.parseInt(text);
				if(playerCount > 2 && playerCount < 7){
					playerCountChoosen = true;
				} else{
					System.out.println("Please choose a number between 3 and 6");
				}

			} catch(IOException e) {
				System.err.println("I/O Error - " + e.getMessage());
			} catch(NumberFormatException e){
				System.out.println("Not a valid Entry");
				playerCountChoosen = false;
			}
		}
		game.start(playerCount);
		turn();
	}
	
	/**
	 * Asks the User what they would like to do this turn.
	 * @param currentPlayer
	 * @param choicesList
	 * @return
	 */
	public void turn(){
		Player currentPlayer = game.getCurrentPlayer();
		List<String> choicesList = game.getChoicesList();
		List<Player> playerList = game.getPlayerList();		

		if(playerList.size() == 1){
			System.out.println("PLAYER "+currentPlayer.getName()+" is the last person in the game and wins!!!!!");
			System.out.println("Game over");
			return;
		}		
		
		int choice = -1;
		String textInput = "";
		while(!(choice >= 0 && choice < choicesList.size())){
			try {
				System.out.println();
				System.out.println("It is Player "+(currentPlayer.getName())+"'s turn");
				System.out.println("What would you like to do?");
				for(int i=0; i<choicesList.size(); i++){
					System.out.println((i+1)+". "+choicesList.get(i));
				} 
				System.out.print("> ");
				textInput = input.readLine();
				choice = Integer.parseInt(textInput)-1;

			} catch(NumberFormatException e) {
				System.out.println("Invalid Entry");
				textInput = "";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		boolean gameWon = false;
		
		String turnChoice = game.getChoice(choice);
		
		if(turnChoice.equals("Roll Dice")){
			move();
			game.getBoard().drawBoard(game.getPlayerList());
		}else if(turnChoice.equals("Make a suggestion")){
			suggestion();
		}else if(turnChoice.equals("Make an accusation")){
			gameWon = accusation();
			if(!gameWon){
				return;
			}else{
				game.removePlayer();
			}
		}else if(turnChoice.equals("See hand")){
			showHand();
		}else if(turnChoice.equals("End Turn")){
			game.endTurn();
		}
		if(!gameWon){
			turn();
		}
	}
	/**
	 * Rolls the dice and calls for a direction to move that many times
	 */
	
	public void move(){
		int diceRoll = game.rollDice();

		System.out.println("Rolling the dice...");
		System.out.println("You rolled a "+diceRoll);
		
		while(!game.turnEnded()){

			game.getBoard().drawBoard(game.getPlayerList());
			String dir = getDirection();
			boolean validMove = game.move(dir);
			if(!validMove){
				System.out.println("Not a valid move");
			}
		}
	}

	/**
	 * Asks the User what they would like to do this turn.
	 * @param currentPlayer
	 * @param choicesList
	 * @return
	 */
	public int askTurn(Player currentPlayer, List<String> choicesList){
		int choice = -1;
		String textInput = "";
		while(!(choice >= 0 && choice < choicesList.size())){
			try {
				System.out.println();
				System.out.println("It is Player "+(currentPlayer.getName())+"'s turn");
				System.out.println("What would you like to do?");
				for(int i=0; i<choicesList.size(); i++){
					System.out.println((i+1)+". "+choicesList.get(i));
				}
				System.out.print("> ");
				textInput = input.readLine();
				choice = Integer.parseInt(textInput)-1;

			} catch(NumberFormatException e) {
				textInput = "";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return choice;
	}
	/**
	 * Shows the players hand
	 */

	public void showHand() {
		System.out.println("This is your hand");
		game.getCurrentPlayer().showHand();
	}
	
	/**
	 * asks the player to enter a direction and confirms that it is valid
	 * @return the direction choosen
	 */

	public String getDirection() {
		String direction="X";
		System.out.println("Which way do you want to move?");
		System.out.println("You have "+(game.getDice())+" moves left");
		System.out.println("N, E, S, W?");
		System.out.print("> ");

		try {
			direction = input.readLine();
			System.out.println("You choose to move "+direction);
			System.out.println();
		} catch (IOException e) {
			System.err.println("I/O Error - " + e.getMessage());
			System.out.print("> ");
			e.printStackTrace();
		}		
		
		if(!(direction.equalsIgnoreCase("N")||direction.equalsIgnoreCase("E")
				||direction.equalsIgnoreCase("S")||direction.equalsIgnoreCase("W"))){
			System.out.println("Not a valid move");
			System.out.println();
			getDirection();
		}
		return direction;
	}
	
	/**
	 * Gets the player to enter a suggestion and then confirms whether it
	 * was contradicted by any other players. The players will contradict 
	 * it automatically if they hold one of hte cards
	 */
	public void suggestion(){
		List<Card> sug = new ArrayList<>();
		Player currentPlayer = game.getCurrentPlayer();
		String playerRoom = game.getBoard().getRoom(currentPlayer.getX(), currentPlayer.getY());
		String weapon = null;
		String character = null;

		boolean inRoom = game.getBoard().inRoom(currentPlayer.getX(), currentPlayer.getY());

		if(!inRoom){
			System.out.println("You are not in a room, cannot make a suggestion");
			return;
		}
		System.out.println("The room is "+playerRoom);

		weapon = askWeapon(weapon);
		character = askCharacter(character);

		Card roomCard = new Room(playerRoom);
		Card weaponCard = new Weapon(weapon);
		Card characterCard = new Character(character);

		sug.add(roomCard);
		sug.add(weaponCard);
		sug.add(characterCard);

		Card foundCard = game.suggestion(sug, roomCard, weaponCard);
		if(foundCard != null){
			System.out.println("player "+currentPlayer.getName()+" is holding the "+foundCard);
		}else{
			System.out.println("Your suggestion was not countered by any players");
		}
	}
	
	/**
	 * Get the player to create an accusation and checks it againist the 
	 * envelope.
	 * @return whether it was correct or not
	 */
	
	public boolean accusation(){
		List<Card> playerAccusation = new ArrayList<>();

		String room = null;
		String weapon = null;
		String character = null;

		room = askRoom(room);
		weapon = askWeapon(weapon);
		character = askCharacter(character);
		Card roomCard  = new Room(room);
		Card weaponCard = new Weapon(weapon);
		Card characterCard = new Character(character);

		playerAccusation.add(roomCard);
		playerAccusation.add(weaponCard);
		playerAccusation.add(characterCard);
		
		boolean validAccusation = game.accusation(playerAccusation);
		
		if(validAccusation){
			System.out.println("YOU GUESSED CORRECTLY AND ARE THE WINNER!!!!!");
			System.out.println("Game over");
			return true;
		}else{
			System.out.println("You guessed wrong and are out of the game sorry");
			return false;
		}
	}
	
	/**
	 * Get the room that the player is in for the suggestion 
	 * automatically
	 * @param playerRoom
	 * @param inRoom
	 */
	public void suggestionRoom(String playerRoom, boolean inRoom) {
		if(!inRoom){
			System.out.println("You are not in a room, cannot make a suggestion");
			return;
		}
		System.out.println("The room is "+playerRoom);
	}
	/**
	 * asks teh player to choose a weapon for suggestion or accusation
	 * @param weapon
	 * @return
	 */
	public String askWeapon(String weapon){
		try {
			while(weapon == null){
				System.out.println("What weapon?");
				System.out.print("> ");
				String text = input.readLine();
				if(text.equalsIgnoreCase("candlestick")||text.equalsIgnoreCase("dagger")||text.equalsIgnoreCase("Lead Pipe")
						||text.equalsIgnoreCase("Revolver")||text.equalsIgnoreCase("Rope")||text.equalsIgnoreCase("Spanner")){
					weapon = text;
				}else{
					System.out.println("Not a valid weapon");
					System.out.println();
				}
			}


		} catch(IOException e) {
			System.err.println("I/O Error - " + e.getMessage());
		}
		return weapon;
	}
	/**
	 * asks the player to choose a character for accusation or suggestion
	 * @param character
	 * @return
	 */

	public String askCharacter(String character) {

		String text ="";
		while(character == null){
			System.out.println("What character?");
			System.out.print("> ");
			try {
				text = input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(text.equalsIgnoreCase("Miss Scarlett")||text.equalsIgnoreCase("Colonel Mustard")||text.equalsIgnoreCase("Mrs. White")
					||text.equalsIgnoreCase("The Reverend Green")||text.equalsIgnoreCase("Mrs. Peacock")||text.equalsIgnoreCase("Professor Plum")){
				character = text;
			}else{
				System.out.println("Not a valid character");
				System.out.println();
			}
		}
		return character;
	}
	/**
	 * askes the player to choose a room for accusationor suggestions
	 * @param room
	 * @return
	 */

	public String askRoom(String room) {
		String text = "";
		while(room == null){
			System.out.println("What room?");
			System.out.print("> ");
			try {
				text = input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(text.equalsIgnoreCase("kitchen")||text.equalsIgnoreCase("ballroom")||text.equalsIgnoreCase("conservatory")
					||text.equalsIgnoreCase("billiard room")||text.equalsIgnoreCase("library")||text.equalsIgnoreCase("study")
					||text.equalsIgnoreCase("hall")||text.equalsIgnoreCase("lounge")||text.equalsIgnoreCase("dining room")){
				room = text.toLowerCase();
			}else{
				System.out.println("Not a valid room");
				System.out.println();
			}
		}
		return room;
	}
}
