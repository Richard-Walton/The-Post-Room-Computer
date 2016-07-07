package uk.ac.hud.postroom.ui.setup;

import javax.swing.*;
import java.awt.event.*;

/**
 * Custom JButton which does not display any borders and centers the button text
 * using HTML functionality
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SetupMenuButton extends JButton {
    
    // Static rollover listener to paint border on mouse over
    private static MouseListener rolloverListener;
    
    // Static constructor
    static {
        rolloverListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Paint button border on mouse enter
                JButton button = ((JButton) e.getSource());
                button.setBorderPainted(true);
                button.setContentAreaFilled(true);
            }
            
            public void mouseExited(MouseEvent e) {
                // Stop paining button border on mouse exit
                JButton button = ((JButton) e.getSource());
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);
            }
        };
    }
    
    /**
     * Constructs a new SetupMenuButton using the given text
     * @param buttonText Text to display on the button
     */
    public SetupMenuButton(String buttonText) {
        // HTML code centers the button text
        super("<html><center>" + buttonText + "</center></html>");
        
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);
        addMouseListener(rolloverListener);
    }
}