import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class GameLabel extends JLabel{
 GameLabel (){
	 super();
	 this.setForeground(Color.BLACK);
	 this.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 24));
 }

public GameLabel(String string) {
	// TODO Auto-generated constr
super(string);
this.setForeground(Color.BLACK);
this.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 24));
}
}