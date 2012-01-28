package uk.ac.hud.postroom.ui.editor;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.tree.*;
import uk.ac.hud.postroom.ui.editor.tree.menu.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;

/**
 * A custom tree for displaying and manipulating Post Room Computer projects
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ProjectTree extends JTree {
    
    /**
     * Constructs a new Project Tree for the given Project
     * @param project project to be displayed / manipulated
     */
    public ProjectTree(Project project) {
        super(new ProjectTreeModel(project));
        
        setToggleClickCount(1);
        
        // Right click listener - shows popup menus
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(e.getX(), e.getY());
                }
            }
        });
    }
    
    /** @inheritDoc **/
    @Override public ProjectTreeModel getModel() {
        return (ProjectTreeModel) super.getModel();
    }
    
    /**
     * Shows a relevent JPopup menu depending on where the right click occured
     * @param x X coordinate where the click occured
     * @param y Y coordinate where the click occured
     */
    private void showPopupMenu(int x, int y) {
        // Get the selection made by the mouse click
        TreePath selection = getPathForLocation(x, y);
        
        if(selection == null) {
            // Nothing was selected - display general project menu
            new ProjectContextMenu(this).show(this, x, y);
        }else{
            // Highlight the selected path
            setSelectionPath(selection);
            
            // Gets the selected node
            TreeNode selectedNode = (TreeNode) selection.getLastPathComponent();
            
            // If selected node is the Project Node
            if(selectedNode instanceof ProjectNode) {
                // display general project menu
                new ProjectContextMenu(this).show(this, x, y);
            }else{
                // Else - must be a SourceFile node - display custom popup menu
                new SourceFileContextMenu(
                        this, ((SourceFileNode) selectedNode)).show(this, x, y);
            }
        }
    }
}