package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.table.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Custom table for displaying Statement information - it also uses custom cell
 * renderers in certain cells to provide syntax highlighting
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StatementInfoTable extends JTable {
    
    /**
     * Constructs a new StatementInfoTable backed by a StatementInfoTableModel
     * @param statement Statement to be displayed
     */
    public StatementInfoTable(Statement statement) {
        super(new StatementInfoTableModel(statement));
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        setPreferredScrollableViewportSize(getPreferredSize());
    }
    
    /** @inheritDoc **/
    @Override public TableCellRenderer getCellRenderer(int row, int column) {
        // Returns a SourceCode renderer for the 2 cells in this table which need it
        if(row > 2 && column == 1) {
            return new SourceCodeCellRenderer();
        }
        
        // Other cells are rendered using the cell default renderer
        return new DefaultTableCellRenderer();
    }
}