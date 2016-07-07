package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.ui.table.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * Custom table for displaying Error information - it also uses custom cell
 * renderers in certain cells to provide syntax highlighting
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ErrorInfoTable extends JTable {
    
    /**
     * Constructs a new ErrorInfoTable backed by a ErrorInfoTableModel
     * @param error Error to be displayed
     */
    public ErrorInfoTable(AssemblyError error) {
        super(new ErrorInfoTableModel(error));
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        setPreferredScrollableViewportSize(getPreferredSize());
    }
}