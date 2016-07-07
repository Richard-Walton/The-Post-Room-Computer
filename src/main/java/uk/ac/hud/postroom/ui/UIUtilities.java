package uk.ac.hud.postroom.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Collection of utility methods used by various UI components
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class UIUtilities {
    
    // Singleton class - private constructor
    private UIUtilities(){}
    
    /**
     * Shows an information dialog
     * @param parent Parent of the dialog
     * @param message Message to display
     */
    public static void showInformationDialog(Component parent, String message) {
        showDialog(parent, message, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows a warning dialog
     * @param parent Parent of the dialog
     * @param message Message to display
     */
    public static void showWarningDialog(Component parent, String message) {
        showDialog(parent, message, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Shows an error dialog
     * @param parent Parent of the dialog
     * @param message Message to display
     */
    public static void showErrorDialog(Component parent, String message) {
        showDialog(parent, message, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a confirmation (YES / NO) dialog
     * @param owner Owner of the dialog
     * @param message Message to display
     * @return action taken by user (same as JOptionPane spec)
     */
    public static int showConfirmDialog(Component owner, String message) {
        return JOptionPane.showConfirmDialog(
                owner, message, "Post Room Computer", JOptionPane.YES_NO_CANCEL_OPTION);
    }
    
    /**
     * Shows the  Post Room Computer about dialog relative to the given parent
     * @param parent parent of the dialog
     */
    public static void showAboutDialog(Component parent) {
        String about = "<html><center>" + 
                "The Post Room Computer<br>" +
                "By Richard Walton (c0410542@hud.ac.uk) <br>" + 
                "Based on work by Dr. Hugh Osborne <br>" +
                "The University of Huddersfield <br>" +
                "Version 1.1 (July 2008)</center></html>";
        
        UIUtilities.showInformationDialog(parent, about);
    }

    /**
     * Updates the look and feel using the given class string
     * @param root root component to update (can be null)
     * @param lookAndFeelClassName LookAndFeel class name to use
     */
    public static void setLookAndFeel(Component root, String lookAndFeelClassName) {
        try {
            // Set look and feel for future objects
            UIManager.setLookAndFeel(lookAndFeelClassName);
            
            if(root != null) {
                // Update the LaF for onscreen objects
                SwingUtilities.updateComponentTreeUI(root);
            }
        }catch (Exception e) {
            UIUtilities.showErrorDialog(root,
                    "Unable to set look and feel:\n" + e.getMessage());
        }
    }
    
    /**
     * Shows a dialog 
     * @param parent Parent of the dialog
     * @param message Message to display
     * @param messageType Type of dialog (same as JOptionPane spec)
     */
    private static void showDialog(Component parent, String message, int messageType) {
        JOptionPane.showMessageDialog(parent, message, "Post Room Computer", messageType);
    }
}