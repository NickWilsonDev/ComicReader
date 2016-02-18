import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TestImages {

   // *** your image path will be different *****
   private static final String IMG_PATH = "./Thanos Quest 1-00FC.jpg";

   public static void main(String[] args) {
      try {
         BufferedImage img = ImageIO.read(new File(IMG_PATH));
         ImageIcon icon = new ImageIcon(img);
         JLabel label = new JLabel(icon);
         JOptionPane.showMessageDialog(null, label);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
