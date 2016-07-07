package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Displays detailed information about each stage of assembly
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AssemblyInfoDialog extends JDialog {
    
    // Assembler used
    private Assembler assembler;
    
    // Tabbed pane displaying each assembly snapshot
    private JTabbedPane tabbedPane;
    
    // Listener to keep divider location in sync between tabs
    private PropertyChangeListener dividerListener;
    
    /**
     * Constructs a new AssemblyInfoDialog using the given assembler
     * @param assembler Assembly to provide information
     */
    public AssemblyInfoDialog(Assembler assembler) {
        this.assembler = assembler;
        
        // Listener to keep divider location in sync between tabs
        dividerListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                for(int i = 0; i < tabbedPane.getComponentCount(); i++) {
                    Component component = tabbedPane.getComponent(i);
                    if(component instanceof JSplitPane) {
                        ((JSplitPane) component).setDividerLocation((Integer) e.getNewValue());
                    }
                }
            }  
        };
        
        buildUI();
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setTitle(assembler.getMainSourceFile().getName() + " assembly info");
        setLayout(new BorderLayout());
        
        buildCenterPanel();
        buildSouthPanel();
        
        setPreferredSize(new Dimension(600, 400));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
    }
    
    /**
     * Builds and adds the center panel
     */
    private void buildCenterPanel() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        
        // Adds all the snapshots to the tabbed pane
        for(AssemblySnapShot snapShot : assembler.getSnapShots()) {
            String tabName = snapShot.getSnapShotName();
            
            // Snapshots with errors have a '*' appended to their tab name
            if(snapShot.hasErrors()) {
                tabName = tabName + " *";
            }
            
            AssemblySnapShotPanel panel = new AssemblySnapShotPanel(snapShot);
            // Add listener to keep divider location in sync
            panel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
            
            tabbedPane.addTab(tabName, panel);
        }
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Builds and adds the south panel
     */
    private void buildSouthPanel() {
        // Construct componenets
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton previous = new JButton("Previous");
            JButton next = new JButton("Next");
            JButton close = new JButton("Close");
         
        // Add components
        add(panel, BorderLayout.SOUTH);
            panel.add(previous);
            panel.add(next);
            panel.add(close);
        
        // Add listeners
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nextIndex = tabbedPane.getSelectedIndex() - 1;
                if(nextIndex < 0) {
                    nextIndex = tabbedPane.getTabCount() - 1;
                }
                
                tabbedPane.setSelectedIndex(nextIndex);
            }
        });
        
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nextIndex = tabbedPane.getSelectedIndex() + 1;
                if(nextIndex >= tabbedPane.getTabCount()) {
                    nextIndex = 0;
                }
                
                tabbedPane.setSelectedIndex(nextIndex);
            }
        });
        
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Constructs and displays a new AssemblyInfoDialog for the given assembler
     * @param parent Parent of the dialog
     * @param assembler Assembler used
     */
    public static void showDialog(Component parent, Assembler assembler) {
        AssemblyInfoDialog dialog = new AssemblyInfoDialog(assembler);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}