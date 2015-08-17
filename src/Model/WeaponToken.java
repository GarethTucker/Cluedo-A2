package Model;

public class WeaponToken {
	private int Xpos, Ypos;
	private String name;
	
	public WeaponToken(int x, int y, String name){
		this.Xpos = x;
		this.Ypos = y;
		this.name = name;
	}
	
	public void setYpos(Integer integer) {
		Ypos = integer;
	}

	public void setXpos(Integer integer) {
		Xpos = integer;
	}
	
	public int getX(){
		return Xpos;
	}
	
	public int getY(){
		return Ypos;
	}
	
	/**
	 * This is called by board to draw the full name of the weapon across multiple squares
	 * @return
	 */
	public int num(){
		return name.length()/3;
	}
	
	public String getName(int i){
		return name.substring(i*3, 3+3*i);
	}
	
	public String toString(){
		return name;
	}
}
