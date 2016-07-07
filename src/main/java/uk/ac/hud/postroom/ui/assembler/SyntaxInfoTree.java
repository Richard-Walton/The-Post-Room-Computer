package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.tree.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Custom JTree which displays the results of a Syntax Analsis
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SyntaxInfoTree extends JTree {
    
    /**
     * Constructs a new SyntaxInfoTree backed by a SyntaxTreeModel
     * @param syntaxAnalyser TwoAddressSyntaxAnalyser results to display
     */
    public SyntaxInfoTree(SyntaxAnalyser syntaxAnalyser) {
        // Super-class (JTree) constructor
        super(new SyntaxTreeModel(syntaxAnalyser));
        
        // Mouse Listener to view more information about a tree node
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                 if(e.getClickCount() > 1) {
                    showStatementInfo(getPathForLocation(e.getX(), e.getY()));
                }
            }
        });
    }
    
    /**
     * Shows a dialog displaying information about a node 
     * @param treePath Path with selected node on
     */
    private void showStatementInfo(TreePath treePath) {
        if(treePath != null) {
            TreeNode selectedNode = (TreeNode) treePath.getLastPathComponent();
            
            /* Get statement from the node which has been selected and show a 
             * relevent dialog */
            if(selectedNode instanceof IncludeNode) { // Include node
                StatementInfoDialog.showDialog(
                        this, ((IncludeNode) selectedNode).getIncludeDirective());
                
            }else if(selectedNode instanceof LabelNode) { // Label node
                StatementInfoDialog.showDialog(
                        this, ((LabelNode) selectedNode).getLabel());
                
            }else if(selectedNode instanceof MacroNode) { // Macro node
                StatementInfoDialog.showDialog(
                        this, ((MacroNode) selectedNode).getMacro());
                
            }else if(selectedNode instanceof ErrorNode) { // Error node
                // Displays a different dialog for errors
                ErrorInfoDialog.showDialog(
                        this, ((ErrorNode) selectedNode).getError());
            }
        }
    }
}