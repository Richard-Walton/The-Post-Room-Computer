package uk.ac.hud.postroom.ui.tabbedpane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A custom Button which displays a close icon
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class CloseButton extends JButton {
    // Close icon
    private static final CloseIcon closeIcon;
    
    // Listener which shows the button border on rollover
    private static final MouseListener rolloverListener;
    
    static {
        closeIcon = new CloseIcon();
        
        rolloverListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                
                button.setContentAreaFilled(true);
                button.setBorderPainted(true);
            }
            
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
            }
        };
    }
    
    /**
     * Constructs a new CloseButton
     */
    public CloseButton() {
        super(closeIcon);
              
        setToolTipText("Close this tab");
        setFocusable(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));
        
        addMouseListener(rolloverListener);
    }
}