import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextPanel extends JFrame {
    private JButton InputButton;
    private JTextPane DisplayPanel;

    public TextPanel(){
        add(DisplayPanel);
        setTitle("Text Panel");
        setSize(400,500);
        InputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(InputButton,
                        "Do you wish to quit?,"
                                "Confirm Quit", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
    }
}
