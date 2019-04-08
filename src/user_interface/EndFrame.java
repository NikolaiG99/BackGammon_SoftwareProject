package user_interface;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game_controller.Game;
import game_controller.GameMethods;
import logic.AvailablePlayAnalyser;
import logic.GameState;

public class EndFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Game game;
	JFrame end;

	public EndFrame(String player){
		JFrame end = new JFrame();
		end.setTitle("BackGammon");

		JLabel image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-03-19 at 19.25.41.png"));
		image.setBounds(0, 0, 1600, 1400);
		
		// Create label to say who won

		JLabel labelplayer1;

		labelplayer1 = new JLabel("Congrats " + player + ", you are the winner, do you want to play again?");
		labelplayer1.setBounds(100, 300, 800, 80);
		labelplayer1.setFont(new Font("Serif", Font.PLAIN, 25));
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
		
		JTextField field;

		field = new JTextField();
		field.setBounds(250, 375, 100, 20);
		image.add(field);

		
		end.setSize(910, 450);
		end.add(image);

		end.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		end.setResizable(false);
		end.setVisible(true);

		JOptionPane frame = null;

		field.addActionListener(new ActionListener() {
			@SuppressWarnings({ "null", "deprecation" })
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText().toLowerCase();
				
				field.setText(text);
				if (text.equals("yes")) {
					end.dispose();
					
					try {
						Game.BScore = 0;
						Game.RScore = 0;
						game.main(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				} 		

				else if (text.equals("no")) {
					System.exit(0);
				}

				else {
					JOptionPane.showMessageDialog(frame, "Answer yes or no");

				}

			}

		});

		

	}
}
