import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class GameFrame extends JFrame {
	
	private JMenuItem exititem ;

 GameFrame(String g){
	 super(g);
	
	  this.setSize(800,800);
	  this.setLocationRelativeTo(null);
	  this.setResizable(true);
	  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  this.getContentPane().setBackground(new Color(0,162,96));

	 initializeMenu();
     	
}
 private void initializeMenu() {
	 JMenuBar menu = new JMenuBar();
	 JMenu ExitMenu = new JMenu("Exit");
	 exititem = new JMenuItem("Exit game");
	 ExitMenu.add(exititem);
	 exititem.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == exititem) {
				System.exit(0);
			}
		}
	 });
	 menu.add(ExitMenu);
	 this.setJMenuBar(menu);
	 
 }

}
