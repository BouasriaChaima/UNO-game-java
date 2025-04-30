import java.awt.Color;

import javax.swing.JOptionPane;

public class GameOptionPane extends JOptionPane {
	GameOptionPane (){
		super();
		this.setFocusable(false);
		this.setForeground(Color.BLUE);
	this.setBackground(new Color(0,162,96));
	}
}
