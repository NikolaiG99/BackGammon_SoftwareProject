package game_controller;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IntroPanel extends JPanel{
	public JButton start;
	public JTextField p1;
	public JTextField p2;
	
	public IntroPanel() throws IOException{	
		start = new JButton("Click to start game");
		Image titleBackground = ImageIO.read(new File("src/resources/Screen Shot 2019-02-20 at 11.31.11.png"));
		titleBackground = titleBackground.getScaledInstance(910, 680, Image.SCALE_SMOOTH);
		JLabel image = new JLabel(new ImageIcon(titleBackground));
		image.setBounds(0, 0, 1600, 1400);
		this.add(image);
		start.setBounds(400, 515, 150, 40);
		image.add(start);

		// Create TextField to get player 1 name
		JTextField textplayer1;
		JLabel labelplayer1;

		labelplayer1 = new JLabel("Player 1 Name: ");
		labelplayer1.setBounds(100, 500, 150, 40);
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
		textplayer1 = new JTextField();
		textplayer1.setBounds(195, 510, 100, 20);
		image.add(textplayer1);
		p1 = textplayer1;

		// Create TextField to get player 2 name
		JTextField textplayer2;
		JLabel labelplayer2;

		labelplayer2 = new JLabel("Player 2 Name: ");
		labelplayer2.setBounds(100, 550, 150, 40);
		labelplayer2.setForeground(Color.white);
		image.add(labelplayer2);
		textplayer2 = new JTextField();
		textplayer2.setBounds(195, 560, 100, 20);
		image.add(textplayer2);
		p2 = textplayer2;
	}
}