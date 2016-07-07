package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.exception.*;

import javax.swing.table.*;

/**
 * Table representation of Error information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ErrorInfoTableModel extends SimpleTableModel {

    // The Error being displayed
    private AssemblyError error;
    
    // Row headings
    private String[] rowHeadings;
    
    /**
     * Constructs a new ErrorInfoTableModel using the given error
     * @param error Error to display in table format
     */
    public ErrorInfoTableModel(AssemblyError error) {
        super(new String[] { "Data", "Value" });
        this.error = error;
        
        rowHeadings = new String[] {
            "Error Type", "Source File", "Line Number", "Reason" };
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
                    case 0 : return error.getClass().getSimpleName();
                    case 1 : return error.getSourceFile().getName();
                    case 2 : return error.getLineNo();
                    case 3 : 
                        String reason = error.getMessage();
                        if(error instanceof SyntaxError) {
                            reason = reason +  " at or around '" + 
                                    ((SyntaxError) error).getErrorText() + "'";
                        }
                        
                        return reason;
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