package Main;
import Controller.UI;
import Model.Game;


public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		new UI(game);
	}
}
