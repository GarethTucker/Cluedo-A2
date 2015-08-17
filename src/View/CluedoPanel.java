package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CluedoPanel extends JPanel {

	private Image img;
	private int width;
	private int height;

	public CluedoPanel(Image img, int w, int h) {
		this.img = img;
		width = w;
		height = h;
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, width, height, null);
	}

}