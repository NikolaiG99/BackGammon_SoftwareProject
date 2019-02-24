package graphical_display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game_controller.Game;

public class DicePanel extends JPanel{
	public DicePanel() {
		// Set up Dice
		JLabel dice1 = new JLabel(getIcon("dice1.png"));
		JLabel dice2 = new JLabel(getIcon("dice1.png"));
		JButton button = new JButton("Throw");
		button.setBounds(2, 2, 1, 1);
		dice1.setBounds(4, 4, 1, 1);
		dice2.setBounds(7, 7, 1, 1);
		JLabel text = new JLabel("Total: 2");
		
		// add dice to panel and the panel to the game board	
		this.setSize(20, 20);
		button.addActionListener(new ActionListener() {
		    JLabel dice1;
		    JLabel dice2;
		    JLabel text;
		    
		    public ActionListener init(JLabel d1, JLabel d2, JLabel txt) {
		    	this.dice1 = d1;
		    	this.dice2 = d2;
		    	this.text = txt;
		    	return this;
		    }
		    
			public void actionPerformed(ActionEvent e) {
		        Timer timer = new Timer();
		        timer.schedule(new ThrowDice(dice1, dice2, text), 0);
			}
			
		}.init(dice1, dice2, text));
	        
	    this.add(button);
	    this.add(dice1);
	    this.add(dice2);
	    this.add(text);
	}
	
    private Icon getIcon(String name){
        return new ImageIcon(Game.class.getResource("/resources/" + name));
    }
    
    class ThrowDice extends TimerTask {
        private JLabel dice1;
        private JLabel dice2;
        private JLabel text;
        private Random number = new Random();
        private int count;

        public ThrowDice(JLabel dice1, JLabel dice2, JLabel text) {
            this.dice1 = dice1;
            this.dice2 = dice2;
            this.text = text;
            count = 25;
        }
        public void run(){
            if(count > 0){
                count --;
                int roll1 = number.nextInt(6);
                int roll2 = number.nextInt(6);
                Icon image1 = getIcon("dice" + (roll1 + 1) + ".png");
                Icon image2 = getIcon("dice" + (roll2 + 1) + ".png");
                dice1.setIcon(image1);
                dice2.setIcon(image2);
                text.setText("Total: " + (roll1 + roll2 + 2));
            }
            else{
                this.cancel();
            }
        }
    }
}