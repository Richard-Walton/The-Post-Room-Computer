package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.exception.*;
import javax.swing.table.*;

/**
 * Table Model of a list of Errors
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ErrorTableModel extends SimpleTableModel {

    // errors being displayed
    private AssemblyError[] errors;
    
    /**
     * Constructs a new ErrorTableModel using the given errors
     * @param errors Errors to be displayed
     */
    public ErrorTableModel(AssemblyError[] errors) {
        // Super-class (SimpleTableModel) constructor
        super(new String[] {"Source File", "Line Number", "Reason"});
        
        this.errors = errors;

    }
       /** @inheritDoc **/
    public int getRowCount() {
        return errors.length;
    }    
        
    /** @inheritDoc **/
    @Override public boolean isCellEditable(int row, int column){
        return false;
    }
    
    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        AssemblyError error = errors[row];
        switch(column) {
            case 0 : return error.getStatement().getSourceFile().getName();
            case 1 : return error.getLineNo();
            case 2 :
                String reason = error.getMessage();
                if(error instanceof SyntaxError) {
                    reason = reason +  " at or around '" + 
                            ((SyntaxError) error).getErrorText() + "'";
                }
                return reason;
            default : return null;
        }
    }
}