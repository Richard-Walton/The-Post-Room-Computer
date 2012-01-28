package uk.ac.hud.postroom.ui.filechooser;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.textpane.*;

import java.awt.*;
import javax.swing.*;

/**
 * FileChooser accessory for displaying formatted .pca SourceCode
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceFileAccessory extends JPanel {

    // Header label
    private JLabel label;
    
    // Text pane which displays the source code
    private JTextPane textPane;
    
    /**
     * Constructs a new SourceFileAccessory panel
     */
    public SourceFileAccessory() {
        // Super-class (JPanel) constructor
        super(new BorderLayout());
        
        add(label = new JLabel("No preview", JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(textPane = new SourceFileTextPane()), BorderLayout.CENTER);
        
        textPane.setEditable(false);
        
        setPreferredSize(new Dimension(200, 200));
    }
    
    /**
     * Sets a new SourceFile to display
     * @param sourceFile SourceFile to display (null values result in "No Preview")
     */
    public void updatePreview(SourceFile sourceFile) { 
        if(sourceFile != null) {
            label.setText("Preview of " + sourceFile.getName());
            textPane.setText(sourceFile.getSourceCode());
        }else {
            label.setText("No preview");
            textPane.setText("");
        }
        
        // Display top of file
        textPane.setCaretPosition(0);
    }
}