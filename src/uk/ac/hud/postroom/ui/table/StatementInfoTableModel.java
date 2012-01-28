package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.assembler.*;

import javax.swing.table.*;

/**
 * Table representation of Statement information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StatementInfoTableModel extends SimpleTableModel {

    // The statement being displayed
    private Statement statement;
    
    private String[] rowHeadings;
    
    /**
     * Constructs a new StatementInfoTableModel using the given statement
     * @param statement Statement to display in table format
     */
    public StatementInfoTableModel(Statement statement) {
        super(new String[] {"Data", "Value" });
        this.statement = statement;
        
        rowHeadings = new String[] {
            "Source File", "Line Number", "Statement Type", "Statement Data", "Source Code" };
    }
    
    /** @inheritDoc **/
    public int getRowCount() {
        return rowHeadings.length;
    }

    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0 : return rowHeadings[row];
            case 1 :
                switch (row) {
                    case 0 : return statement.getSourceFile().getName();
                    case 1 : return statement.getLineNo();
                    case 2 : return statement.getClass().getSimpleName();
                    case 3 : return statement.toString();
                    case 4 : return statement.getSourceCode();
                    default : return null;
                }
            default : return null;
        }
    }
    
    /** @inheritDoc **/
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}