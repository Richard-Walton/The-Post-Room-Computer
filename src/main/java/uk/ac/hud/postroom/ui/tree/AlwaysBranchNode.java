package uk.ac.hud.postroom.ui.tree;

import javax.swing.tree.*;

/**
 * JTree custom node which always claims to have children (Fixes UI issues)
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AlwaysBranchNode extends DefaultMutableTreeNode {
    
    /** 
     * Constructs a new AlwaysBranchNode which always reports it has children
     * @param userObject User object
     */
    public AlwaysBranchNode(Object userObject) {
        super(userObject);
    }
    
    /**
     * Always returns false - thus fixes some UI issues 
     * @return false
     */
    @Override public boolean isLeaf() {
        return false;
    }
}