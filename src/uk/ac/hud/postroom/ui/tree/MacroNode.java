package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.assembler.statement.*;

import javax.swing.tree.*;

/**
 * JTree node for displaying Macros
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class MacroNode extends DefaultMutableTreeNode {
    // The macro to display
    private MacroStatement macro;
    
    /**
     * Constructs a new MacroName using the macro name and arity as display text
     * @param macro Macro to display
     */
    public MacroNode(MacroStatement macro) {
        // Super-class (DefaultMutableTreeNode) constructor
        super(macro.getName() + " (arity " + macro.argumentCount() + ")");
        
        this.macro = macro;
    }
    
    /**
     * Returns the macro being displayed
     * @return macro being displayed
     */
    public MacroStatement getMacro() {
        return macro;
    }
}