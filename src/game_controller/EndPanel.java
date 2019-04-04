package game_controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



@SuppressWarnings("serial")
public class EndPanel extends JPanel {
	JLabel image;
	JLabel labelplayer1;

	public EndPanel() {
		image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-03-19 at 19.25.41.png"));
		image.setBounds(0, 0, 1600, 1400);
		this.add(image);

		labelplayer1 = new JLabel("");
		labelplayer1.setBounds(300, 300, 500, 80);
		labelplayer1.setFont(new Font("Serif", Font.PLAIN, 25));
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);

		JTextField field;
		JOptionPane frame = null;

		field = new JTextField();
		field.setBounds(300, 375, 100, 20);
		image.add(field);

		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText().toLowerCase();
				System.out.println(text);
				field.setText(text);
				if (text.equals("yes")) {

					Game game = null;
					try {
						game = new Game();
					} catch (IOException e1) {

					}
				

				}

				if (text.equals("no")) {
					System.exit(0);
				}

				else {
					JOptionPane.showMessageDialog(frame, "Answer yes or no");

				}

			}

		});

		this.setVisible(true);

	}

	
	
	public void setText(String player) {
		labelplayer1.setText("Congrats " + player + ", you are the winner");
	}
	
	
}