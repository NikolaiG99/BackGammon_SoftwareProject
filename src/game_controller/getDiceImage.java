package game_controller;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class getDiceImage {
    
    public Icon getIcon(String name){
        return new ImageIcon(Game.class.getResource("/images/" + name));
    }
}

