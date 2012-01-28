package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.assembler.statement.*;

import javax.swing.tree.*;

/**
 * JTree node for displaying Labels
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class LabelNode extends DefaultMutableTreeNode {
    // The label to display
    private LabelStatement label;
    
    /**
     * Constructs a new LabelNode using the label name as display text
     * @param label Label to display
     */
    public LabelNode(LabelStatement label) {
        super(label.getLabelName());
        
        this.label = label;
    }
    
    /**
     * Returns the label being displayed
     * @return label being displayed
     */
    public LabelStatement getLabel() {
        return label;
    }
}