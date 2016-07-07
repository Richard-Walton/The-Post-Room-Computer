package uk.ac.hud.postroom.ui.assembler;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.tree.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * A dialog displaying a SyntaxInfoTree
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SyntaxInfoDialog extends JDialog {

    /**
     * Constructs a new SyntaxInfoDialog for the given SourceFile
     * @param syntaxAnalyser TwoAddressSyntaxAnalyser results to be displayed
     */
    public SyntaxInfoDialog(SyntaxAnalyser syntaxAnalyser) {
        setTitle(syntaxAnalyser.getSourceFile().getName() + " information");
        setModal(true);
        setLayout(new BorderLayout());
        
        // HTML Centered message
        String message = 
                 "<html><center>The information shown here is based on " +
                "syntax only. <br> Compiling the file may result in futher" +
                " errors.</center></html>";
        
        // Results tree
        SyntaxInfoTree infoTree = new SyntaxInfoTree(syntaxAnalyser);
        infoTree.setVisibleRowCount(10);
        
        // South button panel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton closeButton = new JButton("Close");
            
        southPanel.add(closeButton);
        
        // Add components
        add(new JLabel(message, JLabel.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(infoTree), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        
        // Add listeners
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Displays a SyntaxInfoDialog for the given syntaxAnalyser relative to the given parent
     * @param parent Parent of the dialog
     * @param syntaxAnalyser syntaxAnalyser results to display
     */
    public static void showDialog(Component parent, SyntaxAnalyser syntaxAnalyser) {
        SyntaxInfoDialog dialog = new SyntaxInfoDialog(syntaxAnalyser);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
/**
     * Displays a SyntaxInfoDialog for the given syntaxAnalyser relative to the given parent
     * @param parent Parent of the dialog
     * @param sourceFile SourceFile to analyse and display results
     */
    public static void showDialog(Component parent, SourceFile sourceFile) {
        showDialog(parent, new SyntaxAnalyser(sourceFile));
    }
}