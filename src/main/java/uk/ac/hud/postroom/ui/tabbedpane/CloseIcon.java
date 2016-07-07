package uk.ac.hud.postroom.ui.tabbedpane;

import java.awt.*;
import javax.swing.*;

/**
 * Custom Icon which displays an 'X' icon
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class CloseIcon implements Icon {
    private static final int WIDTH = 8;
    private static final int HEIGHT = 7;
    
    /** @inheritDoc **/
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        graphics.setColor(Color.BLACK);
        
        graphics.drawLine(x, y, x+6, y+6);
        graphics.drawLine(x+1, y, x+7, y+6);
        graphics.drawLine(x, y+6, x+6, y);
        graphics.drawLine(x+1, y+6, x+7, y);
    }
    
    /** @inheritDoc **/
    public int getIconWidth() {
        return WIDTH;
    }
    
    /** @inheritDoc **/
    public int getIconHeight() {
        return HEIGHT;
    }
}