package uk.ac.hud.postroom.ui.table;

import javax.swing.table.*;

/**
 * Partial implementation of a AbstractTableModel
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public abstract class SimpleTableModel extends AbstractTableModel {
    
    // Column names of the table
    private String[] columnNames;
    
    /**
     * Constructs a new SimpleTableModel using the given columnNames
     * @param columnNames Column names in the table
     */
    public SimpleTableModel(String[] columnNames) {
        this.columnNames = columnNames;
    }
    
    /** @inheritDoc **/
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /** @inheritDoc **/
    public String getColumnName(int column) {
        return columnNames[column];
    }
}