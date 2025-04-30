import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GameTable extends JTable {

	public GameTable() {
		 super();
	}

	public GameTable(DefaultTableModel tablemodel) {
		super(tablemodel);
		 this.setBackground(new Color(221,235,157));
	       this.setFillsViewportHeight(true);
	       this.getTableHeader().setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 30));
	       this.getTableHeader().setBackground(new Color(160,200,120));
	      this.setRowHeight(20);
	      for (int i = 0; i < this.getColumnCount(); i++) {
	          this.getColumnModel().getColumn(i).setPreferredWidth(100);
	      }
		}
}
