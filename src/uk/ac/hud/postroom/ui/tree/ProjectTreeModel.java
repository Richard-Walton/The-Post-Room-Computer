package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.*;
import javax.swing.tree.*;

/**
 * Tree model for displaying a Project (including the Projects Source Files)
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ProjectTreeModel extends DefaultTreeModel {
    
    // Project used to create this tree model
    private Project project;
    
    /**
     * Constructs a new ProjectTreeModel using the given project as the root node
     * and its SourceFile as children
     * @param project Project to display in Tree format
     */
    public ProjectTreeModel(Project project) {
        // Super-class (DefaultTreeModel) constructor
        super(new ProjectNode(project));
        
        this.project = project;
        
        // Builds the tree
        refresh();
    }
    
    public void refresh() {
        ProjectNode rootNode = getRoot();
        
        // Removes children ready to be readded
        rootNode.removeAllChildren();
        
        // Adds SourceFileNodes as children
        for(SourceFile sourceFile : rootNode.getProject().getSourceFiles()) {
            rootNode.add(new SourceFileNode(sourceFile));
        }
        
        // Notifies interested listeners
        reload();
    }
    
    /**
     * Returns the Project being displayed
     * @return Project being displayed
     */
    public Project getProject() {
        return project;
    }
    
    /** @inheritDoc **/
    @Override public ProjectNode getRoot() {
        return (ProjectNode) super.getRoot();
    }
}