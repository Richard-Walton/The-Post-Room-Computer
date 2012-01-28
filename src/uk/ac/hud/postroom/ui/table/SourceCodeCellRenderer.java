package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.ui.textpane.*;
        
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Custom Table Cell Renderer which colour formats Post Room Computer source code
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceCodeCellRenderer extends SourceFileTextPane implements TableCellRenderer {
    
    /**
     * Creates a new SourceCodeCellRenderer
     */
    public SourceCodeCellRenderer() {
        setEditable(false);
        setMargin(new Insets(0,0,0,0));
        setBorder(null);     
        setOpaque(true);
    }
    
    /** @inheritDoc **/
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Sets the text to display
        setText(value.toString());
        
        // Changes the back/foreground depending on whether the cell is selected
        if(isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
         
        return this;
    }
}