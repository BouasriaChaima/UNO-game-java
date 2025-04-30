import java.awt.Image;

import javax.swing.ImageIcon;

class GameImageIcon extends ImageIcon {
    public GameImageIcon(String filename) {
        super(filename);
        if (this.getIconWidth() <= 0) {
            System.err.println("Warning: Could not load image file: " + filename);
            // Set a default image or handle the error as needed
        }
    }
        public GameImageIcon(Image image) {
            super(image);

    }
}

