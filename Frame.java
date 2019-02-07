import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.*;

public class Frame extends JComponent{
	
	class circle{
	public void paintComponent(Graphics g){
		// Recover Graphics2D 
	      Graphics2D g2 = (Graphics2D) g;
	      g2.setColor(Color.GREEN);
	      Ellipse2D.Double ellipse = new Ellipse2D.Double(10,20,5,10);
		  g2.draw(ellipse);

}
	}

	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setSize(600, 450);
		frame.setTitle("Backgammon");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(new JLabel(new ImageIcon("src/image/Screen Shot 2019-02-05 at 21.04.05.png")));
		
		frame.setVisible(true);
		Frame component = new Frame();
		frame.add(component);
		
		
		
		
		
        
		
	}

}
