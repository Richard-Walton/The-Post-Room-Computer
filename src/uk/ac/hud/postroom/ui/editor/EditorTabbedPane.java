package uk.ac.hud.postroom.ui.editor;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.tabbedpane.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Customized JTabbedPane which displays each tab with a close button.
 * Any SourceFileEditor tabs show a 'Save Changes?' dialog before closing 
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class EditorTabbedPane extends JTabbedPane {
    
    /**
     * Constructs a new EditorTabbedPane
     */
    public EditorTabbedPane() {
        super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    /** @inheritDoc **/
    @Override public void insertTab(final String title, Icon icon, final Component component, String toolTip, int index) {
        super.insertTab(title, icon, component, toolTip, index);
        
        setTabComponentAt(index, new CloseableTabComponent(title, icon, toolTip, this));
    }
    
    /** @inheritDoc **/
    @Override public void removeTabAt(int index) {
        // Checks the component type being removed
        Component component = getComponentAt(index);
        if(component instanceof SourceFileEditor) {
            /* SourceFileEditor panels are checked to ensure their SourceFile is 
             * up to date */
            SourceFileEditor editor = (SourceFileEditor) component;
            if(editor.changesMade()) {
                // Show confirm dialog if need to save changed
                int action = UIUtilities.showConfirmDialog(
                        this, "Save changes to " + editor.getSourceFile().getName());
                switch(action) {
                    case JOptionPane.YES_OPTION :
                        editor.updateFileContents();
                        try {
                            SourceFile.save(editor.getSourceFile());
                        }catch (Exception e) {
                            UIUtilities.showErrorDialog(this, e.getMessage());
                        }
                        
                        break;
                    case JOptionPane.NO_OPTION :
                        break;
                    case JOptionPane.CANCEL_OPTION :
                        // Cancel - don't remove tab
                        return;
                }
            }
        }
        
        super.removeTabAt(index);
    }
}