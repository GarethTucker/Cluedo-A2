package Model;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board{
	private String[][] board;
	private Map<String, Room> loc;
	private List<WeaponToken> weapons = new ArrayList<>();
	
	/**
	 * constructor that creates a new board
	 */
	public Board(){
		
		board = new String[25][24];
		
		loc = new HashMap();
		
		//create Rooms
		Room kitchen = new Room("kitchen");
		Room ballRoom = new Room("ballRoom");
		Room conservatory = new Room("conservatory");
		Room billiardRoom = new Room("billiardRoom");
		Room library = new Room ("library");
		Room study = new Room("study");
		Room hall = new Room("hall");
		Room lounge = new Room("lounge");
		Room diningRoom = new Room("diningRoom");
		Room center = new Room("center");
		
		//create other place
		Room ground = new Room("ground");
		Room start = new Room("start");
		Room door = new Room("door");
		Room wall = new Room("wall");
		Room portal = new Room("portal");
		
		//pair the patterns with the positions
		loc.put("K  ", kitchen);
		loc.put("B  ", ballRoom);
		loc.put("C  ", conservatory);
		loc.put("Bi ", billiardRoom);
		loc.put("Li ", library);
		loc.put("St ", study);
		loc.put("H  ", hall);
		loc.put("L  ", lounge);
		loc.put("D  ", diningRoom);
		loc.put("Ce ", center);
		
		loc.put(".  ", ground);
		loc.put("S  ", start);
		loc.put("DD ", door);
		loc.put("XX ", wall);
		loc.put("11 ", portal);
		loc.put("22 ", portal);
		
		createBoard();
	}
	
	/**
	 * passing current X and Y position and the direction player want
	 * to move will return the new position as a point. if the move is 
	 * invalid, it will return a null;
	 * @param preX
	 * @param preY
	 * @param dir
	 * @return newPoint
	 */
	public Point checkMove(int preX, int preY, String dir, List<Player> players){
		
		int newX = preX;
		int newY = preY;
		if(dir.equalsIgnoreCase("N")){
			newY = preY-1;
		}else if(dir.equalsIgnoreCase("E")){
			newX = preX+1;
		}else if(dir.equalsIgnoreCase("S")){
			newY = preY+1;
		}else if(dir.equalsIgnoreCase("W")){
			newX = preX-1;
		}else{
			return null;
		}
		
		Point newPoint = new Point(newX, newY);
		
		if(newX < 0 || newX > 23 || newY < 0 || newY > 24){
			System.out.println("You can not move out of the board.");
			return null;
		}
		
		//System.out.println(preX+ " " + preY + " " + newX + " " + newY);
		Room preLoc = loc.get(board[preY][preX]);
		Room newLoc = loc.get(board[newY][newX]);
		//System.out.println(newLoc);
		
		//center
		if(newLoc.equals(loc.get("Ce "))){
			System.out.println("You can not move into center");
			return null;
		}
		
		//can not move to a place which is already occupied by another player
		for (Player p : players){
			if(newX == p.getX() && newY == p.getY()){
				return null;
			}
		}
		
		//special door
		if((preX == 18 && preY == 5) && (newX == 19 && newY == 5)
				|| (preX == 19 && preY == 5) && (newX == 18 && newY == 5)){
				return null;
			}
		
		//go into/out a room from door
		if(preLoc.equals(loc.get("DD ")) 
			|| newLoc.equals(loc.get("DD "))){
			return newPoint;
		}
		
		//check wall
		if (newLoc.equals(loc.get("XX "))){
			return null;
		}
		
		//start from a start position
		if(newLoc.equals(preLoc) 
			|| (preLoc.equals(loc.get("S  ")) && newLoc.equals(loc.get(".  ")))){
			return newPoint;
		}
		
		//go in to a portal
		if (!preLoc.equals(loc.get(".  ")) 
				&& (newLoc.equals(loc.get("11 ")) || newLoc.equals(loc.get("22 ")))){
			newPoint = processPortal(newPoint);
			return newPoint;
		}
			
		//go out of a portal
		if ((preLoc.equals(loc.get("11 ")) || preLoc.equals(loc.get("22 "))) 
				&& !newLoc.equals(loc.get(".  "))){
			return newPoint;
		}
		return null;
	}

	/**
	 * pass a entrance of a portal position into this method will 
	 * return the exit position of that portal.
	 * @param newPoint
	 * @return
	 */
	private Point processPortal(Point newPoint) {
		// TODO Auto-generated method stub
		if (newPoint.getX() == 5 && newPoint.getY() ==1){
			newPoint.setLocation(23, 21);
			System.out.println("Go from kitchen to study through portal 1.");
			return newPoint;
		}
		if (newPoint.getX() == 23 && newPoint.getY() ==21){
			newPoint.setLocation(5, 1);
			System.out.println("Go from study to kitchen through portal 1.");
			return newPoint;
		}
		if (newPoint.getX() == 22 && newPoint.getY() ==5){
			newPoint.setLocation(0, 19);
			System.out.println("Go from conservatory to lounge through portal 2.");
			return newPoint;
		}
		if (newPoint.getX() == 0 && newPoint.getY() ==19){
			newPoint.setLocation(22, 5);
			System.out.println("Go from lounge to conservatory through portal 2.");
			return newPoint;
		}
		return newPoint;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return the name of the room in the given position in the board
	 */
	public String getRoom(int x, int y){
		return loc.get(board[y][x]).toString();
	}
	
	
	/**
	 * This method register every position to the board
	 */
	private void createBoard(){
		//draw ground
		for (int y = 0; y < 25; ++y){
			for (int x = 0; x < 24; ++x){
				board[y][x] = ".  ";
			}
		}

		//draw walls
		for (int y = 0; y < 25; ++y){
			board[y][0] = "XX ";
			board[y][23] = "XX ";
		}
		board[1][6] = "XX ";
		board[1][17] = "XX ";
		
		for (int x = 0; x < 24; ++x){
			board[0][x] = "XX ";
			board[24][x] = "XX ";
		}
		board[0][8] = "XX ";
		

		board[7][0] = ".  ";
		board[24][16] = ".  ";

		//draw kitchen
		for (int y = 1; y < 7; ++y){
			for (int x = 0; x < 6; ++x){
				board[y][x] = "K  ";
			}
		}
		board[6][0] = "XX ";

		//draw ballRoom
		for (int y = 2; y < 8; ++y){
			for (int x = 8; x < 16; ++x){
				board[y][x] = "B  ";
			}
		}
		for (int y = 0; y < 2; ++y){
			for (int x = 10; x < 14; ++x){
				board[y][x] = "B  ";
			}
		}

		//draw conservatory
		for (int y = 1; y < 6; ++y){
			for (int x = 18; x < 24; ++x){
				board[y][x] = "C  ";
			}
		}
		board[5][23] = "XX ";

		//draw billiardRoom
		for (int y = 8; y < 13; ++y){
			for (int x = 18; x < 24; ++x){
				board[y][x] = "Bi ";
			}
		}

		//draw library
		for (int y = 15; y < 18 ; ++y){
			for (int x = 17; x < 24; ++x){
				board[y][x] = "Li ";
			}
		}
		for (int x = 18; x < 23; ++x){
			board[14][x] = "Li ";
			board[18][x] = "Li ";
		}

		//draw study
		for (int y = 21; y < 25 ; ++y){
			for (int x = 17; x < 24; ++x){
				board[y][x] = "St ";
			}
		}
		board[24][17] = "XX ";

		//draw hall
		for (int y = 18; y < 25 ; ++y){
			for (int x = 9; x < 15; ++x){
				board[y][x] = "H  ";
			}
		}

		//draw lounge
		for (int y = 19; y < 25 ; ++y){
			for (int x = 0; x < 7; ++x){
				board[y][x] = "L  ";
			}
		}
		board[24][6] = "XX ";

		//draw diningRoom
		for (int y = 10; y < 16 ; ++y){
			for (int x = 0; x < 8; ++x){
				board[y][x] = "D  ";
			}
		}
		for (int x = 0; x < 5; ++x){
			board[9][x] = "D  ";
		}
		
		//draw center
		for (int y = 10; y < 17; ++y){
			for (int x = 10; x < 15; ++x){
				board[y][x] = "Ce ";
			}
		}

		//draw doors
		board[7][4] = "DD ";
		board[5][7] = "DD ";
		board[8][9] = "DD ";
		board[8][14] = "DD ";
		board[5][16] = "DD ";
		board[5][18] = "DD ";
		board[9][17] = "DD ";
		board[16][16] = "DD ";
		board[20][17] = "DD ";
		board[20][15] = "DD ";
		board[17][11] = "DD ";
		board[17][12] = "DD ";
		board[18][6] = "DD ";
		board[16][6] = "DD ";
		board[12][8] = "DD ";

		//draw start points
		board[0][9] = "S  ";
		board[0][14] = "S  ";
		board[6][23] = "S  ";
		board[19][23] = "S  ";
		board[24][7] = "S  ";
		board[17][0] = "S  ";

		//draw portals
		board[1][5] = "11 ";
		board[21][23] = "11 ";
		board[5][22] = "22 ";
		board[19][0] = "22 ";
	}
	
	/**
	 * draws the board to console
	 * @param players
	 */
	public void drawBoard(List<Player> players) {
		// TODO Auto-generated method stub
//		for (int i = 0; i < 25; ++i){
//			System.out.print("▄▄▄");
//		}
		System.out.println();
		for (int y = 0; y < 25; ++y){
//			System.out.print("▌▌");
			for (int x = 0; x < 24; ++x){
				boolean player = false;
				for (int i = 0; i < players.size(); ++i){
					if(players.get(i).getX() == x && players.get(i).getY() == y){
						System.out.print("P" + (i+1) + " ");
						player = true;
					}
				}
				if(!player){
					boolean weapon = false;
					for (int i = 0; i < weapons.size(); ++i){
						for(int j = 0; j < weapons.get(i).num(); ++j){
							if ((weapons.get(i).getX() + j == x) && weapons.get(i).getY() == y){
								System.out.print(weapons.get(i).getName(j));
								weapon = true;
							}
						}
					}
					if(!weapon){
						System.out.print(board[y][x]);
					}
				}
				player = false;
			}
//			System.out.print("▌▌");
			System.out.println();
		}
//		for (int i = 0; i < 25; ++i){
//			System.out.print("▀▀▀");
//		}
		System.out.println();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return whether the given position is inside a room or not
	 */
	public boolean inRoom(int x, int y) {
		// TODO Auto-generated method stub
		if (board[y][x] == "K  ")		//kitchen
			return true;
		else if (board[y][x] == "B  ")	//ballroom
			return true;
		else if (board[y][x] == "C  ")	//conservatory
			return true;
		else if (board[y][x] == "Bi ")	//billiardRoom
			return true;
		else if (board[y][x] == "Li ")	//library
			return true;
		else if (board[y][x] == "St ")	//study
			return true;
		else if (board[y][x] == "H  ")	//hall
			return true;
		else if (board[y][x] == "L  ")	//lounge
			return true;
		else if (board[y][x] == "D  ")	//diningRoom
			return true;
		
		return false;
	}
	
	/**
	 * read the weapons
	 * @param weaponToken
	 */
	public void placeWeapon(List<WeaponToken> weaponToken){
		this.weapons = weaponToken;
	}

	/**
	 * move the weapon to the room with the given name.
	 * @param weapon
	 * @param room
	 * @param startPoints
	 */
	public void moveWeapon(String weapon, String room,
			List<Point> startPoints) {
		Point p = getPoint(room, startPoints);
		System.out.println(p);
		if (p == null){
			System.out.println("Wrong input.");
		}
//		for (WeaponToken weap : weapons){
//			if(weap.toString() == weapon){
//				weap.setXpos(p.x);
//				weap.setYpos(p.y);
//			}
//		}
		for (int i = 0; i < weapons.size(); ++i){
//			System.out.println(weapon);
//			System.out.println(weapons.get(i).toString());
			if(weapons.get(i).toString().equalsIgnoreCase(weapon)){
				//if new y is not open then y=y+1
				int n = 0;
				for (WeaponToken w : weapons){
					if (w.getX() == p.x && w.getY() == p.y){
						n++;
					}
				}
				weapons.get(i).setXpos(p.x);
				weapons.get(i).setYpos(p.y+n);
			}
		}
	}
	
	/**
	 * get the position that the weapon should put in the room
	 * @param room
	 * @param p
	 * @return
	 */
	private Point getPoint(String room, List<Point> p){
		switch (room){
		case "kitchen":
			return new Point(1,3);
		case "ballRoom":
			return new Point(10,3);
		case "conservatory":
			return new Point(19,3);
		case "billiardRoom":
			return new Point(19,10);
		case "library":
			return new Point(19,16);
		case "study":
			return new Point(19,23);
		case "hall":
			return new Point(10,21);
		case "lounge":
			return new Point(2,22);
		case "diningRoom":
			return new Point(2,12);
		default:
			return null;
		}
	}

}
