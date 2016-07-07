package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.assembler.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog which displays a table view of statement information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StatementInfoDialog extends JDialog {
    
    // Statement being displayed
    private Statement statement;
    
    /**
     * Constructs a new StatementInfoDialog to display the given statement
     * @param statement Statement to display
     */
    public StatementInfoDialog(Statement statement) {
        this.statement = statement;
                 
        buildUI();
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setTitle("Statement Info");
        setLayout(new BorderLayout());
        
        add(new JScrollPane(new StatementInfoTable(statement)), BorderLayout.CENTER);
        buildSouthPanel();
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
    }
    
    /**
     * Builds and adds a panel to the dialog
     */
    private void buildSouthPanel() {
        // Construct components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton sourceFileInfo = new JButton("View Source File info");
            JButton close = new JButton("Close");
          
        // Add components
        add(panel, BorderLayout.SOUTH);
            panel.add(sourceFileInfo);
            panel.add(close);
        
        // Add listeners
        sourceFileInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SyntaxInfoDialog.showDialog(StatementInfoDialog.this, 
                        new SyntaxAnalyser(statement.getSourceFile()));
            }
        });
        
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Displays a StatementInfoDialog for the given statement relative to the given parent
     * @param parent Parent of the dialog
     * @param statement Statement to display
     */
    public static void showDialog(Component parent, Statement statement) {
        StatementInfoDialog dialog = new StatementInfoDialog(statement);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}