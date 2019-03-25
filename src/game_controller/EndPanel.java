package game_controller;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EndPanel extends JPanel{
	JLabel image;
	JLabel labelplayer1;
	
	public EndPanel(){	
		image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-03-19 at 19.25.41.png"));
		image.setBounds(0, 0, 1600, 1400);
		this.add(image);

		labelplayer1 = new JLabel("");
		labelplayer1.setBounds(300, 300, 500, 80);
		labelplayer1.setFont(new Font("Serif", Font.PLAIN, 25));
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
	}
	
	public void setText(String player) {
		labelplayer1.setText("Congrats " + player + ", you are the winner");
	}
}