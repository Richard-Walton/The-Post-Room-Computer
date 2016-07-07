package uk.ac.hud.postroom;

import uk.ac.hud.postroom.ui.*;
import javax.swing.*;

/**
 * Main class to start the Post Room Computer <br>
 * Starts a new uk.ac.hud.postroom.ui.SetupDialog
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class Main {
    
    public static void main(String[] args) {
        
        if(args.length == 0) {
            UIUtilities.setLookAndFeel(null, UIManager.getSystemLookAndFeelClassName());
        
            SetupDialog.showDialog(null);
        }else {
            new CommandLineInstance(args);
        }  
    }
}