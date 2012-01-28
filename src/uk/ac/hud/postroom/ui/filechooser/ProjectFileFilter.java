package uk.ac.hud.postroom.ui.filechooser;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter for Post Room Computer .pca files
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ProjectFileFilter extends FileFilter {
    
    /**
     * Returns whether to show the specified file in a JFileChooser
     * @param file File to check
     * @return true if file is a directory or a .pca file
     */
    public boolean accept(File file) {
        if(file.isDirectory()) {
            return true;
        }
        
        if(file.getName().endsWith(".pca")) {
            return true;
        }
        
        if(file.getName().endsWith(".pco")) {
            return true;
        }
        
        return false;
    }
    
    /** @inheritDoc **/
    public String getDescription() {
        return "Post Room Computer files";
    }
}