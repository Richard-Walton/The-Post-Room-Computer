package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.assembler.statement.*;

import javax.swing.tree.*;

/**
 * JTree node for displaying IncludeDirectives
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class IncludeNode extends DefaultMutableTreeNode {
    
    // The TwoAddressIncludeDirective to display
    private IncludeDirective includeDirective;
    
    /**
     * Constructs a new IncludeNode using the filename as display text
     * @param includeDirective TwoAddressIncludeDirective to display
     */
    public IncludeNode(IncludeDirective includeDirective) {
        // Super-class (DefaultMutableTreeNode) constructor
        super(includeDirective.getFileName());
        
        this.includeDirective = includeDirective;
    }
    
    /**
     * Returns the includeDirective being displayed
     * @return TwoAddressIncludeDirective being displayed
     */
    public IncludeDirective getIncludeDirective() {
        return includeDirective;
    }
}