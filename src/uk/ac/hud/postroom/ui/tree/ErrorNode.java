package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.exception.*;

import javax.swing.tree.*;

/**
 * JTree node for displaying AssemblyErrors
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ErrorNode extends DefaultMutableTreeNode {
    // The error to display
    private AssemblyError error;
    
    /**
     * Constructs a new ErrorNode using the error message as display text
     * @param error Error to display
     */
    public ErrorNode(AssemblyError error) {
        // Super-class (DefaultMutableTreeNode) constructor
        super(error.getMessage());
        
        this.error = error;
    }
    
    /**
     * Returns the error being displayed
     * @return error being displayed
     */
    public AssemblyError getError() {
        return error;
    }
}