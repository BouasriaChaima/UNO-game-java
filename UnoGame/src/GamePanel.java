import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class GamePanel extends JPanel{

	GamePanel(){
		super();
		this.setBackground(new Color(0,162,96));
		this.setFocusable(true);
	}

	public GamePanel(BorderLayout borderLayout) {
		super( borderLayout);
		this.setBackground(new Color(0,162,96));
		this.setFocusable(true);
	
	}
}

