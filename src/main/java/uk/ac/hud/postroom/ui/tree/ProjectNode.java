package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.*;

/**
 * JTree node for displaying Projects
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ProjectNode extends AlwaysBranchNode {
    
    // Project to display
    private Project project;
    
    /**
     * Constructs a new ProjectNode using the given project name as display text 
     * @param project Project to display
     */
    public ProjectNode(Project project) {
        // Super-class (AlwaysBranchNode) constructor
        super(project.getName());
        
        this.project = project;
    }
    
    /**
     * Returns the Project being displayed
     * @return Project being displayed
     */
    public Project getProject() {
        return project;
    }
}