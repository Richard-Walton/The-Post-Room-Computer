package uk.ac.hud.postroom.ui.styleeditor;

import uk.ac.hud.postroom.ui.editor.*;
import javax.swing.*;
import java.awt.*;

/**
 * Font list combo box
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class FontListComboBox extends JComboBox {
    /**
     * Constructs a FontListComboBox
     */
    public FontListComboBox() {
        /* Calls the super-class (JComboBox) constructor with an array of fonts 
         installed on the local system */
        super(GraphicsEnvironment.
                getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        
        // Uses a custom renderer to show each font in its own syle
        setRenderer(new FontListRenderer());
    }
    
    /**
     * Returns the currently selected Font name as a String
     * @return Name of the currently selected Font
     */
    public String getSelectedItem() {
        return (String) super.getSelectedItem();
    }
}

/**
 * Custom cell renderer which renders each cell with the font which matches its value 
 */
class FontListRenderer extends JLabel implements ListCellRenderer {
        
    /**
     * Constructs a new FontListRenderer
     */
    public FontListRenderer() {
        // Makes the cell opaque (so background colours are shown)
        setOpaque(true);
            
        // Sets the preferred size
        setPreferredSize(new Dimension(125, 18));
    }
    
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean selected, boolean focus) {  
            
        // Sets the text of the cell to the font name
        setText((String) value);
            
        // Sets the font using the font name
        setFont(new Font(
                (String) value, 
                Font.PLAIN, 
                ((Font) UIManager.getDefaults().get("Label.font")).getSize()));
            
        /* Sets the correct foreground and background colours
           depending on whether the cell is currently selected */
        
        setForeground(selected ? list.getSelectionForeground() : list.getForeground());
        setBackground(selected ? list.getSelectionBackground() : list.getBackground());
        
        return this;
    }
}