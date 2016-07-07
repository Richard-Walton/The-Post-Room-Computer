package uk.ac.hud.postroom.ui.setup;

import uk.ac.hud.postroom.ui.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * SetupDialog Menu Bar
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SetupUIMenuBar extends JMenuBar {
    
    // Owner of the MenuBar
    private SetupDialog owner;
    
    /**
     * Constructs a new SetupIUMenuBar owned by the given SetupDialog
     * @param owner Owner of the MenuBar
     */
    public SetupUIMenuBar(final SetupDialog owner) {
        this.owner = owner;
        
        buildUI();
    }
    
    /**
     * Builds the MenuBar
     */
    private void buildUI() {
        // Construct menu items
        JMenu lookAndFeel = new JMenu("Look and feel");
            JRadioButtonMenuItem nativeLaF = new JRadioButtonMenuItem("Native", true);
            JRadioButtonMenuItem javaLaF = new JRadioButtonMenuItem("Java (Metal)");
            JRadioButtonMenuItem motifLaF = new JRadioButtonMenuItem("Motif");
        JMenu help = new JMenu("Help");
            JMenuItem about = new JMenuItem("About");
            
        // Add menu items to menu bar
        add(lookAndFeel);
            lookAndFeel.add(nativeLaF);
            lookAndFeel.add(javaLaF);
            lookAndFeel.add(motifLaF);
        add(Box.createHorizontalGlue());
        add(help);
            help.add(about);
            
        // Add buttons to button group
        ButtonGroup laFGroup = new ButtonGroup();
            laFGroup.add(nativeLaF);
            laFGroup.add(javaLaF);
            laFGroup.add(motifLaF);
            
        // Set action listeners
        nativeLaF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIUtilities.setLookAndFeel(owner, 
                        UIManager.getSystemLookAndFeelClassName());
                owner.pack();
            }
        });

        javaLaF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIUtilities.setLookAndFeel(owner, 
                        UIManager.getCrossPlatformLookAndFeelClassName());
                owner.pack();
            }
        });
        
        motifLaF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIUtilities.setLookAndFeel(owner,
                        "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                owner.pack();
            }
        });
        
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIUtilities.showAboutDialog(owner);
            }
        });
    }
}