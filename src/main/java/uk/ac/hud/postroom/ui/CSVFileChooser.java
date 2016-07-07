package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.ui.filechooser.*;
import javax.swing.*;

/**
 * FileChooserDialog for CSV Files
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class CSVFileChooser extends JFileChooser {
    
    /**
     * Constructs a new CSVFileChooser
     */
    public CSVFileChooser() {
        setDialogTitle("CSV File Chooser");
        setFileHidingEnabled(true);
        setMultiSelectionEnabled(false);
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Set custom file filter
        setFileFilter(new CSVFileFilter());
    }
}