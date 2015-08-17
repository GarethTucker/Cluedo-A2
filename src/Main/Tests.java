package Main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Model.Board;
import Model.Card;
import Model.Game;
import Model.Player;
import Model.Room;
import Model.Weapon;
import Model.Character;
import Model.WeaponToken;


public class Tests {
	
	/**
	 * ============================================================
	 * Game Tests
	 * ============================================================
	 */

	Game game = new Game();
	/**
	 * Testing the dealing
	 */
	@Test
	public void gameDeal1Test(){
		Player p = new Player("1");
		game.playerList.add(p);
		game.dealCards();
		List hand = game.playerList.get(0).getHand();
		assertTrue(hand.size() == 18);
	}

	@Test
	public void gameDeal2Test(){
		Player p1 = new Player("1");
		game.playerList.add(p1);
		Player p2 = new Player("2");
		game.playerList.add(p2);
		game.dealCards();
		List hand = game.playerList.get(0).getHand();
		assertTrue(hand.size() == 9);
	}
	/**
	 * Testing an accusation
	 */
	
	@Test
	public void goodAccusationTest(){
		List<Card> envelope = game.getEnvelope();
		boolean gameover = game.accusation(envelope);
		assertTrue(gameover==true);
	}
	/**
	 * Testing a suggestion
	 */

	@Test
	public void suggestionTest(){
		game.start(3);
		List<Card> envelope = game.getEnvelope();
		assertTrue(game.suggestion(envelope, new Room("kitchen"), new Weapon("Dagger"))==null);
	}
	
	/**
	 * Testing the start game makes enough players
	 */
	@Test
	public void startTest(){
		game.start(3);
		assertTrue(game.getPlayerList().size() == 3);
	}
	
	/**
	 * Testing making a choice
	 */
	@Test
	public void choicesListTest(){
		game.resetChoicesList();
		assertTrue(game.getChoicesList().size() == 5);
		assertTrue(game.getChoice(0).equals("Roll Dice"));
	}
	
	/**
	 * Testing removing a player from the game
	 */
	@Test
	public void removePlayerTest(){
		Player p1 = new Player("1");
		game.playerList.add(p1);
		game.removePlayer();
		assertTrue(game.playerList.size() ==0);
	}
	
	/**
	 * Testing if the game ends
	 */
	@Test
	public void endTurnTest(){
		Player p1 = new Player("1");
		Player p2 = new Player("2");
		game.playerList.add(p1);
		game.playerList.add(p2);
		game.endTurn();
		assertTrue(game.getCurrentPlayer()==p2);
		
	}
	
	/**
	 * Testing movement validity
	 */
	@Test
	public void invalidMoveTest(){
		game.start(1);
		assertFalse(game.move("N"));
	}
	
	@Test
	public void validMoveTest(){
		game.start(1);
		assertTrue(game.move("S"));
	}
	
	/**
	 * ============================================================
	 * Board Tests
	 * ============================================================
	 */
	

	
	private int x1, y1, x2, y2;
	Board board = new Board();
	
	//go out of the board
	@Test
	public void invalidMove1() {
		System.out.println();
		System.out.println("===================================================");
		System.out.println("TEST invalid move - bounder check");
		System.out.println("===================================================");
		
		init();
		String dir = "N";
		processMove(dir);
		assertTrue(x1 == 9 && y1 == 0);
	}
	
	//go into a wall
	@Test
	public void invalidMove2() {
		System.out.println();
		System.out.println("===================================================");
		System.out.println("TEST invalid move - wall check");
		System.out.println("===================================================");
		
		init();
		String dir = "W";
		processMove(dir);
		assertTrue(x1 == 9 && y1 == 0);
	}
	
	//go into a room without through the door
	@Test
	public void invalidMove3() {
		System.out.println();
		System.out.println("===================================================================");
		System.out.println("TEST invalid move -  into a door without go through the door");
		System.out.println("===================================================================");
		
		init();
		String dir = "E";
		processMove(dir);
		assertTrue(x1 == 9 && y1 == 0);
	}
	
	//go into the center through the door of hall
	@Test
	public void invalidMove4() {
		System.out.println();
		System.out.println("===================================================");
		System.out.println("TEST invalid move - center check");
		System.out.println("===================================================");
		
		init();
		x1 = 11;
		y1 = 17;
		String dir = "N";
		processMove(dir);
		assertTrue(x1 == 11 && y1 == 17);
	}
	
	//go into conservatory from the special door and cross the wall
	@Test
	public void invalidMove5() {
		System.out.println();
		System.out.println("===================================================");
		System.out.println("TEST invalid move - wall next to special door check");
		System.out.println("===================================================");
		
		init();
		x1 = 18;
		y1 = 5;
		String dir = "E";
		processMove(dir);
		assertTrue(x1 == 18 && y1 == 5);
	}
	
	//go to portal 2 in the conservatory side from the outside
	@Test
	public void invalidMove6() {
		System.out.println();
		System.out.println("================================================================");
		System.out.println("TEST invalid move - into a portal from outside of the room check");
		System.out.println("================================================================");
		
		init();
		x1 = 22;
		y1 = 6;
		String dir = "N";
		processMove(dir);
		assertTrue(x1 == 22 && y1 == 6);
	}

	/**
	 * ============================================================
	 * Part 2 --- valid move check
	 * ============================================================
	 */
	//go into a room through a door
	@Test
	public void validMove1(){
		System.out.println("===================================================");
		System.out.println("TEST valid move - go into a room check");
		System.out.println("===================================================");
		
		init();
		x1 = 18;
		y1 = 5;
		String dir = "N";
		processMove(dir);
		assertTrue(x1 == 18 && y1 == 4);
	}
	
	//go out of a room through a door
	@Test
	public void validMove2(){
		System.out.println("===================================================");
		System.out.println("TEST valid move - go out of a room check");
		System.out.println("===================================================");
		
		init();
		x1 = 18;
		y1 = 4;
		String dir = "S";
		processMove(dir);
		assertTrue(x1 == 18 && y1 == 5);
	}
	
	//portal 1 test
	@Test
	public void validMove3(){
		System.out.println("===================================================");
		System.out.println("TEST valid move - portal 1 check");
		System.out.println("===================================================");
		
		init();
		x1 = 4;
		y1 = 1;
		String dir = "E";
		processMove(dir);
		assertTrue(x1 == 23 && y1 == 21);
		dir = "W";
		processMove(dir);
		assertTrue(x1 == 22 && y1 == 21);
		assertTrue(board.inRoom(x1, y1));
		
		dir = "E";
		processMove(dir);
		assertTrue(x1 == 5 && y1 == 1);
		dir = "W";
		processMove(dir);
		assertTrue(x1 == 4 && y1 == 1);
		assertTrue(board.inRoom(x1, y1));
	}
	
	//portal 2 test
	@Test
	public void validMove4(){
		System.out.println("===================================================");
		System.out.println("TEST valid move - portal 2 check");
		System.out.println("===================================================");
		
		init();
		x1 = 22;
		y1 = 4;
		String dir = "S";
		processMove(dir);
		assertTrue(x1 == 0 && y1 == 19);
		processMove(dir);
		assertTrue(x1 == 0 && y1 == 20);
		assertTrue(board.inRoom(x1, y1));
		
		dir = "N";
		processMove(dir);
		assertTrue(x1 == 22 && y1 == 5);
		processMove(dir);
		assertTrue(x1 == 22 && y1 == 4);
		assertTrue(board.inRoom(x1, y1));
	}
	
	//walk around
	@Test
	public void validMove5(){
		System.out.println("===================================================");
		System.out.println("TEST comp move - complex movement check");
		System.out.println("===================================================");
		
		init();
		String dir = "S";
		processMove(dir);
		assertTrue(x1 == 9 && y1 == 1);
		assertTrue(!board.inRoom(x1, y1));
		
		dir = "W";
		processMove(dir);
		assertTrue(x1 == 8 && y1 == 1);
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 1);
		
		dir = "S";
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 2);
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 3);
		
		dir = "E";
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 3);
		
		dir = "S";
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 4);
		processMove(dir);
		assertTrue(x1 == 7 && y1 == 5);
		
		dir = "E";
		processMove(dir);
		assertTrue(x1 == 8 && y1 == 5);
		processMove(dir);
		assertTrue(x1 == 9 && y1 == 5);
		assertTrue(board.inRoom(x1, y1));
		
	}
	
	/**
	 * ============================================================
	 * Methods used in test
	 * ============================================================
	 */
	private void init(){
		x1 = 9;
		y1 = 0;
		x2 = 14;
		y2 = 0;
	}
	
	private void processMove(String dir) {
		
		List<Player> playerList = new ArrayList<>();
		for(int i=0; i<2; i++)
			playerList.add(new Player(String.valueOf(i+1)));
		
		List<Integer> startingPositions = new ArrayList<>();
		startingPositions.add(y1);
		startingPositions.add(x1);
		startingPositions.add(y2);
		startingPositions.add(x2);
		
		int i=0;
		for(Player p: playerList){
			p.setYpos(startingPositions.get(i));
			p.setXpos(startingPositions.get(i+1));
			i=i+2;
		}
		
		board.drawBoard(playerList);

		Point p = board.checkMove(x1, y1, dir, playerList);
		if (p != null){
			playerList.get(0).setXpos(p.x);
			playerList.get(0).setYpos(p.y);
			x1 = p.x;
			y1 = p.y;
			board.drawBoard(playerList);
		}
		
	}
	
}
