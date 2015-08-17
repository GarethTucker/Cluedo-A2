package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {

	Dimension buttonSize = new Dimension(100, 30);

	public ButtonPanel(List JButton){
		for(int i= 0; i< 6; i++){
			String is = ""+i;
			JButton b1 = new JButton();
			b1.setPreferredSize(buttonSize);
			add(b1, BorderLayout.NORTH);
		}

		setVisible(true) ;
	}

	public Dimension getPreferredSize() {
		Dimension d = new Dimension(700, 40);
		return d;
    }

}
