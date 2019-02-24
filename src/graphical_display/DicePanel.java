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
import user_interface.InformationPanel;

public class DicePanel extends JPanel{
	//Variables storing the first two rolls to see who goes first
	private int firstRoll;
	private int secondRoll;
	
	//The information panel which the panel can send text to
	InformationPanel infoPanel;
	
	//Timer to carry out rolls
	private Timer timer;
	
	private JLabel dice1;
	private JLabel dice2;
	private JLabel text;
	
	public DicePanel(InformationPanel infoPanel) {
		this.infoPanel = infoPanel;
		
		timer = new Timer();
		
		// Set up Dice
		dice1 = new JLabel(getIcon("dice1.png"));
		dice2 = new JLabel(getIcon("dice1.png"));
		JButton button = new JButton("Throw");
		button.setBounds(2, 2, 1, 1);
		dice1.setBounds(4, 4, 1, 1);
		dice2.setBounds(7, 7, 1, 1);
		text = new JLabel("Total: 2");
		
	    this.add(dice1);
	    this.add(dice2);
	    this.add(text);
		
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
		        timer.schedule(new ThrowDice(dice1, dice2, text), 0);
			}
			
		}.init(dice1, dice2, text));
	        
	    this.add(button);
	}
	
	/*
	 * Method which does intial game rolls to see who starts
	 */
	public void rollInitialThrows() {
	    ThrowDice initialThrow1 = new ThrowDice(dice1, dice2, text);
	    timer.schedule(initialThrow1, 0);
       	try{
       		Thread.sleep(20);
       	} catch (Exception e){
       		e.printStackTrace();
       	}
	    firstRoll = initialThrow1.lastRollResult;	
	    infoPanel.addText("Black rolled " + firstRoll + ".\n");
       	
	    ThrowDice initialThrow2 = new ThrowDice(dice1, dice2, text);
	    timer.schedule(initialThrow2, 0);
       	try{
       		Thread.sleep(20);
       	} catch (Exception e){
       		e.printStackTrace();
       	}
	    secondRoll = initialThrow2.lastRollResult;	
	    infoPanel.addText("Red rolled " + secondRoll + ".\n");
	    
	    //If the rolls were the same, print a message and redo the procedure
	    if(firstRoll == secondRoll) {
	    	infoPanel.addText("Same result for intial rolls. Rerolling.\n");
	    	this.rollInitialThrows();
	    }
	}
	
	public int getFirstRoll() {return firstRoll;}
	public int getSecondRoll() {return secondRoll;}
	
    private Icon getIcon(String name){
        return new ImageIcon(Game.class.getResource("/resources/" + name));
    }
    
    /**
     * Method which rolls the two dice & displays them, and returns the result
     */
    
    class ThrowDice extends TimerTask {
        private JLabel dice1;
        private JLabel dice2;
        private JLabel text;
        private Random number = new Random();
        private int count;
        int lastRollResult;
        
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
                lastRollResult = roll1+roll2+2;
                text.setText("Total: " + lastRollResult);
            }
            else{
                this.cancel();
            }
        }
    }
}