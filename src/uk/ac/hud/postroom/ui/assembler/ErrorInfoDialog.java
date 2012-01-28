package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog which displays a table view of error information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ErrorInfoDialog extends JDialog {
    
    // Error being displayed
    private AssemblyError error;
    
    /**
     * Constructs a new ErrorInfoDialog to display the given error
     * @param error Error to display
     */
    public ErrorInfoDialog(AssemblyError error) {
        this.error = error;
        
        buildUI();
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setTitle("Error Info");
        setLayout(new BorderLayout());
        
        add(new JScrollPane(new ErrorInfoTable(error)), BorderLayout.CENTER);
        buildSouthPanel();
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
    }
    
    private void buildSouthPanel() {
        // Construct components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton statementInfo = new JButton("View statement info");
            JButton sourceFileInfo = new JButton("View Source File info");
            JButton close = new JButton("Close");
            
        // add components
        add(panel, BorderLayout.SOUTH);
            panel.add(statementInfo);
            panel.add(sourceFileInfo);
            panel.add(close);
        
        // Add listeners
        statementInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StatementInfoDialog.showDialog(
                        ErrorInfoDialog.this, error.getStatement());
            }
        });
        
        sourceFileInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SyntaxInfoDialog.showDialog(ErrorInfoDialog.this, 
                        new SyntaxAnalyser(error.getStatement().getSourceFile()));
            }
        });
        
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Displays a ErrorInfoDialog for the given error relative to the given parent
     * @param parent Parent of the dialog
     * @param error Error to display
     */
    public static void showDialog(Component parent, AssemblyError error) {
        ErrorInfoDialog dialog = new ErrorInfoDialog(error);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}