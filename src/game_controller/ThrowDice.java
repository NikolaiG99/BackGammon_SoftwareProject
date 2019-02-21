package game_controller;


import java.util.Random;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.JLabel;

class ThrowDice extends TimerTask {
    private JLabel dice1;
    private JLabel dice2;
    private JLabel text;
    private Random number = new Random();
    private getDiceImage image;
    private int count;

    public ThrowDice(JLabel dice1, JLabel dice2, JLabel text) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.text = text;
        count = 25;
        image = new getDiceImage();
    }
    public void run(){
        if(count > 0){
            count --;
            int roll1 = number.nextInt(6);
            int roll2 = number.nextInt(6);
            Icon image1 = image.getIcon("dice" + (roll1 + 1) + ".png");
            Icon image2 = image.getIcon("dice" + (roll2 + 1) + ".png");
            dice1.setIcon(image1);
            dice2.setIcon(image2);
            text.setText("Total: " + (roll1 + roll2 + 2));
        }
        else{
            this.cancel();
        }
    }

}
