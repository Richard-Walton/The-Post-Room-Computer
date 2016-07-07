package uk.ac.hud.postroom.ui.styleeditor;

import uk.ac.hud.postroom.ui.table.*;

import javax.swing.*;
import java.awt.*;

/**
 * JTable with custom cell renderer / editor for Colours
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class StyleTable extends JTable {
    
    /**
     * Constructs an OptionTable backed by an StyleTableModel
     */
    public StyleTable() {
        super(new StyleTableModel());
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        // Sets the default renderer for cells containing instances of the Color class
        setDefaultRenderer(Color.class, new ColourCellRenderer());
        
        // Sets the default editor for cells containing instances of the Color class
        setDefaultEditor(Color.class, new ColourCellEditor());
        
        // Sets the preferred size of the table when placed in a JScollPane
        setPreferredScrollableViewportSize(getPreferredSize());
    }
}