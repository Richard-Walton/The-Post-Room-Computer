package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.*;
import javax.swing.tree.*;

/**
 * JTree node for displaying SourceFiles
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceFileNode extends DefaultMutableTreeNode {
    
    // The SourceFile to display
    private SourceFile sourceFile;
    
    /**
     * Constructs a new SourceFileNode using the SourceFile name as display text
     * @param sourceFile SourceFile to display
     */
    public SourceFileNode(SourceFile sourceFile) {
        // Super-class (DefaultMutableTreeNode) constructor
        super(sourceFile.getName());
        
        this.sourceFile = sourceFile;
    }
    
    /**
     * Returns the SourceFile being displayed
     * @return SourceFile being displayed
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }
}