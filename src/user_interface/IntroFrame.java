package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IntroFrame extends JFrame{
	public IntroFrame(JFrame gameToDisplayOnClick, InformationPanel infoPanel){	
		super("BackGammon");
	
		JButton start = new JButton("Click to start game");
		JLabel image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-02-20 at 11.31.11.png"));
		image.setBounds(0, 0, 1600, 1400);
		this.setSize(910, 450);
		this.add(image);
		start.setBounds(400, 315, 150, 40);
		image.add(start);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// Create TextField to get player 1 name
		JTextField textplayer1;
		JLabel labelplayer1;

		labelplayer1 = new JLabel("Player 1 Name: ");
		labelplayer1.setBounds(100, 300, 150, 40);
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
		textplayer1 = new JTextField();
		textplayer1.setBounds(195, 310, 100, 20);
		image.add(textplayer1);

		// Create TextField to get player 2 name
		JTextField textplayer2;
		JLabel labelplayer2;

		labelplayer2 = new JLabel("Player 2 Name: ");
		labelplayer2.setBounds(100, 350, 150, 40);
		labelplayer2.setForeground(Color.white);
		image.add(labelplayer2);
		textplayer2 = new JTextField();
		textplayer2.setBounds(195, 360, 100, 20);
		image.add(textplayer2);
		this.setVisible(true);

		// Once you click button,game will start
		start.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String p1 = textplayer1.getText();
			String p2 = textplayer2.getText();
			close();
			infoPanel.addText(p1 + ", you are the Red Checker.");
			infoPanel.addText("\n");
			infoPanel.addText(p2 + ", you are the Black Checker.");
			infoPanel.addText("\n");
			gameToDisplayOnClick.setVisible(true);
			
			}
		});
	}
	
	/*
	 * Method to dispose of this JFrame
	 */
	private void close() {
		this.dispose();
	}
}