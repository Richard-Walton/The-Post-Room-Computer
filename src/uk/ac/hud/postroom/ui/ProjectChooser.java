package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.filechooser.*;

import java.io.*;
import java.beans.*;
import java.awt.*;
import javax.swing.*;

/**
 * FileChooser for Post Room Computer projects and .pca files
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ProjectChooser extends JFileChooser {
    // Accessory (preview) panel
    private SourceFileAccessory accessory;
    
    /**
     * Constructs a new ProjectChooser
     */
    public ProjectChooser() {
        // Construct FileChooser
        setDialogTitle("Select Project Directory");
        setFileHidingEnabled(true);
        setMultiSelectionEnabled(false);
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        // Set custom file filter
        setFileFilter(new ProjectFileFilter());
        
        // Add SourceFileAccessory
        setAccessory(accessory = new SourceFileAccessory());
        accessory.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        accessory.setPreferredSize(new Dimension(200, 200));
        
        // Listen for selection changes and update accessory as needed
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if(propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) ||
                        propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    
                    updateAccessory((File) e.getNewValue()); //perform update
                }
            }
        });
    }
    
    /** @inheritDoc **/
    @Override public File getSelectedFile() {
        File file = super.getSelectedFile();
        // Return the parent (directory) of a selected file
        if(file != null && !file.isDirectory()) {
            file = file.getParentFile();
        }
        
        return file;
    }
    
    /**
     * Updates the preview panel with the given File
     * @param file File to view using the accessory
     */
    private void updateAccessory(File file) {
        if(!file.isDirectory() && file.exists()) {
            try {
                accessory.updatePreview(SourceFile.load(file.getPath()));
                return;
                
            }catch (IOException e){ e.printStackTrace();}
        }
            
        accessory.updatePreview(null);
    }
}