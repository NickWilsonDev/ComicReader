/**
 * ImagePanel.java
 *
 * Class implements a panel with an image drawn on it.
 * idea came from stackoverflow link
 * http://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel?rq=1
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    // may pass image name and directory to contructor as parameters
    public ImagePanel() {
        try {
            image = ImageIO.read(new File("image name and path"));
        } catch (IOException ex) {
            //handle exception error message to user?
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
