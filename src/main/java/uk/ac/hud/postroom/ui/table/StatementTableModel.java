package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.assembler.*;
import javax.swing.table.*;

/**
 * Table model for an array of Statements
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StatementTableModel extends SimpleTableModel {
    
    // Statements being displayed
    private Statement[] statements;
    
    /**
     * Constructs a new StatementTableModel using the given statements
     * @param statements Statements to display
     */
    public StatementTableModel(Statement[] statements) {
        super(new String[] { "Address", "Source File", "Statement Type", "Statement Data"});
        this.statements = statements;      
    }
    
    /** @inheritDoc **/
    public int getRowCount() {
        return statements.length;
    }    
        
    /** @inheritDoc **/
    @Override public boolean isCellEditable(int row, int column){
        return false;
    }
    
    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        Statement statement = statements[row];
        switch(column) {
            case 0 : return statement.getMemoryLocation();
            case 1 : return statement.getSourceFile().getName();
            case 2 : return statement.getClass().getSimpleName();
            case 3 : return statement.toString();
            default : return null;
        }
    }
}