 package user_interface;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EndFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndFrame(String player){	
		super("BackGammon");
	
		JLabel image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-03-19 at 19.25.41.png"));
		image.setBounds(0, 0, 1600, 1400);
		this.setSize(910, 450);
		this.add(image);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// Create label to say who won
		
		JLabel labelplayer1;

		labelplayer1 = new JLabel("Congrats " + player + ", you are the winner");
		labelplayer1.setBounds(300, 300, 500, 80);
		labelplayer1.setFont(new Font("Serif", Font.PLAIN, 25));
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
		

		this.setVisible(true);

	}
}