package uk.ac.hud.postroom.ui.table;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Custom Cell Renderer for displaying colours
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ColourCellRenderer extends JLabel implements TableCellRenderer {
    // Shared Border for all selected cells
    private static Border selectedBorder;
        
    // Shared Border for all un-selected cells
    private static Border unSelectedBorder;
    
    static {
        // Border to show when cell is selected
        selectedBorder = BorderFactory.createMatteBorder(
                    1,3,1,3, UIManager.getColor("Table.selectionBackground"));
            
        // Border to show when cell is un-selected
        unSelectedBorder = BorderFactory.createMatteBorder(
                    2,4,2,4, UIManager.getColor("Table.background"));
    }

    public ColourCellRenderer() {
        // Force the background to be shown
        setOpaque(true);  
    }
    
    /** @inheritDoc **/
    public Component getTableCellRendererComponent(
            JTable table, Object color, boolean selected, boolean focus, int row, int column) {
 
        setBackground((Color) color);
        setBorder(selected ? selectedBorder : unSelectedBorder);

        return this;
    }
}