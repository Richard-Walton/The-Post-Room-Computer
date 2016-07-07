package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;

import uk.ac.hud.postroom.ui.table.*;
import java.awt.*;
import javax.swing.*;

/**
 * Displays information capured by an AssemblySnapShot
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AssemblySnapShotPanel extends JSplitPane {
    
    /**
     * Constructs a new AssemblySnapShotPanel for the given AssemblySnapShot
     * @param snapshot SnapShot to display
     */
    public AssemblySnapShotPanel(AssemblySnapShot snapshot) {
        super(JSplitPane.VERTICAL_SPLIT);
        
        // Construct components
        JScrollPane statementScroller = new JScrollPane(new StatementTable(snapshot.getStatements()));
        JScrollPane errorScroller = new JScrollPane(new ErrorTable(snapshot.getErrors()));
           
        // Add components
        setTopComponent(statementScroller);
        setBottomComponent(errorScroller);
  
        // Customize components
        setResizeWeight(0.5);
        setDividerSize(10);
        setOneTouchExpandable(true);
            
        // Add borders
        statementScroller.setBorder(
                BorderFactory.createTitledBorder("Statements"));
        errorScroller.setBorder(
                BorderFactory.createTitledBorder(snapshot.hasErrors() ? "Errors" : "No errors"));
    }
}