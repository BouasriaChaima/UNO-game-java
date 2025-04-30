import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import java.awt.Component;

public class GameScrollPane extends JScrollPane {
    public GameScrollPane(java.awt.Component view) {
        super(view);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}