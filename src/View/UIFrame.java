package View;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;



public class UIFrame extends JFrame {


	private CluedoPanel board;
	private CluedoPanel dice;
	private CluedoPanel hand;
	private ButtonPanel buttons;

	public UIFrame() {
		super("Cluedo"); 
		JPanel top = new JPanel(new BorderLayout());
		JPanel bottom = new JPanel(new BorderLayout());
		board = new CluedoPanel(new ImageIcon("Board.jpg").getImage(), 600, 600);
		dice = new CluedoPanel(new ImageIcon("Dice.png").getImage(), 150, 150);
		hand = new CluedoPanel(new ImageIcon("Hand.jpg").getImage(), 450, 150);
		buttons = new ButtonPanel(null);
		JMenuBar menu = new JMenuBar();
		JMenuItem file = new JMenuItem("File");
		menu.add(file);
		
		top.add(board, BorderLayout.CENTER);
		top.add(menu, BorderLayout.NORTH);
		bottom.add(dice, BorderLayout.EAST);
		bottom.add(hand, BorderLayout.WEST);
		bottom.add(buttons, BorderLayout.SOUTH);
		
		add(top, BorderLayout.NORTH); // add canvas
		add(bottom, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack components tightly together
		setResizable(false); // prevent us from being resizeable
		setVisible(true); // make sure we are visible!	

	}

	public static void main(String[] args){
		new UIFrame();
	}
}