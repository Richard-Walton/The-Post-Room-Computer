package uk.ac.hud.postroom.ui;

import java.awt.*;
import javax.swing.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.ComputerAdapter;
import uk.ac.hud.postroom.ui.computer.*;

/**
 * Computer user interface panel
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ComputerUI extends JPanel {
    
    /**
     * Constructs a new ComputerUI for the given computer
     * @param computer
     */
    public ComputerUI(Computer computer) {
        super(new BorderLayout());
        
        // Construct Components
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            JScrollPane scrollPane = new JScrollPane();
        
        // Add Components
        add(splitPane);
            splitPane.setLeftComponent(scrollPane);
                scrollPane.setViewportView(new MemoryDisplayTable(computer.getMemory()));
            splitPane.setRightComponent(new ComputerComponentSplitPane(computer));
        add(new ComputerControlPanel(computer), BorderLayout.SOUTH);
            
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerSize(10);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Memory"));
        
        computer.addComputerListener(new ComputerAdapter() {
            public void breakPointHit(Computer computer, int breakPoint) {
                BreakPointDialog dialog = new BreakPointDialog(computer);
                dialog.setLocationRelativeTo(ComputerUI.this);
                dialog.setVisible(true);
            }
        });
    }
}