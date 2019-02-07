import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel {

	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public Frame(String fileName) throws IOException {
		    backgroundImage = ImageIO.read(new File(fileName));
		  }

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 5, 10, this);
	}


public static void main(String[] args) throws IOException{
	JFrame frame = new JFrame();
    frame.setSize(600, 450);
    frame.setTitle("Backgammon");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Frame component = new Frame("src/Screen Shot 2019-02-05 at 21.04.05.png");
    frame.add(component);
    frame.setVisible(true);
}
}



