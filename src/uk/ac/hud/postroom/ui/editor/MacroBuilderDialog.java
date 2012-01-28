package uk.ac.hud.postroom.ui.editor;

import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.table.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog which provides interface to create a Macro
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class MacroBuilderDialog extends JDialog {
    
    // TextField used to enter the name of the Macro
    private JTextField macroName;
    
    // Table to display the macro arguments
    private JTable argumentTable;
    
    // Option the user clicked to close the dialog
    private int option;
    
    /** 
     * Constructs a new MacroBuilderDialog
     */
    public MacroBuilderDialog() {
        // Default option value
        option = JOptionPane.CANCEL_OPTION;
        
        buildUI();
    }
    
    /**
     * Returns the option used to close the dialog
     * @return Option (Same as JOptionPane spec) used to close the dialog
     */
    public int getOption() {
        return option;
    }
    
    /**
     * Returns the macro definition using the values entered by the user
     * @return macro definition using the values entered by the user
     */
    public String getMacroDefinition() {
        // Add macro name
        StringBuilder definition = new StringBuilder("%" + macroName.getText());
        
        // Add macro arguments in order
        for(int i = 0; i < argumentTable.getRowCount(); i++) {
            definition.append(" " + argumentTable.getValueAt(i, 1));
        }
        
        return definition.toString();
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setTitle("Macro builder dialog");
        setLayout(new BorderLayout());
        
        buildNorthPanel();
        buildCenterPanel();
        buildSouthPanel();
        
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }
    
    /**
     * Builds and adds the north panel
     */
    private void buildNorthPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        add(panel, BorderLayout.NORTH);
            panel.add(new JLabel("Macro Name:"), BorderLayout.WEST);
            panel.add(macroName = new JTextField(10));
            
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Listener stops invalid characters being added to macro name
        macroName.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                
                if(key != KeyEvent.VK_BACK_SPACE && !Character.isLetter(key)) {
                    // Stops the typed character from being added to the text field
                    e.consume();
                    
                    // Causes a system beep
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }
    
    /** 
     * Builds and adds the center panel
     */
    private void buildCenterPanel() {
        JScrollPane scrollPane = new JScrollPane();
            argumentTable = new JTable(new MacroArgumentTableModel());
            
        add(scrollPane);
            scrollPane.setViewportView(argumentTable);
            
        scrollPane.setBorder(BorderFactory.createTitledBorder("Macro arguments"));
        argumentTable.setFillsViewportHeight(true);
    }
    
    /**
     * Builds and adds the south panel
     */
    private void buildSouthPanel() {
        // Construct components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton add = new JButton("Add / Insert argument");
            JButton remove = new JButton("Remove argument");
            JButton finish = new JButton("Finish");
            
        // Add components
        add(panel, BorderLayout.SOUTH);
            panel.add(add);
            panel.add(remove);
            panel.add(finish);
          
        // Add listeners
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = argumentTable.getSelectedRow();
                
                if(selectedRow == -1) {
                    selectedRow = argumentTable.getRowCount();
                }
                
                insertArgument(selectedRow + 1);
            }
        });
        
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = argumentTable.getSelectedRow();
                if(selectedRow > -1) {
                    ((MacroArgumentTableModel) argumentTable.getModel()).removeRow(selectedRow);
                }else {
                    UIUtilities.showErrorDialog(MacroBuilderDialog.this, 
                            "Select an argument from the argument table");
                }
            }
        });
        
        finish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(macroName.getText().equals("")) {
                    UIUtilities.showErrorDialog(MacroBuilderDialog.this, "No macro name entered");
                }else { 
                    option = JOptionPane.OK_OPTION;
                    dispose();
                }
            }
        });
    }
    
    /**
     * Inserts an argument into the given position in the argument table. 
     * @param row position to insert argument into the  argument table. 
     */
    private void insertArgument(int row) {
        // Get the argument name
        String argumentName = (String) JOptionPane.showInputDialog(
                this, // owner
                "Enter unique macro argument label", // Message
                "Macro Builder", // Title
                JOptionPane.PLAIN_MESSAGE);
        
        if(argumentName != null) {
            argumentName = argumentName.trim();
            
            // Check if valid - i.e one word made from characters
            if(!argumentName.matches("[a-z|A-Z]+")) {
                UIUtilities.showErrorDialog(this,
                        "Invalid argument label.  Use characters only");
                
                // Test failed - restart argument entry
                insertArgument(row);
                return;
            }
            
            // Check that the given argument is not already in the table
            for(int i = 0; i < argumentTable.getRowCount(); i++) {
                if(argumentName.equalsIgnoreCase((String) argumentTable.getValueAt(i, 1))) {
                    UIUtilities.showErrorDialog(this, "Duplicate argument label name entered");
                    
                    // Test failed - restart argument entry
                    insertArgument(row);
                    return;
                }
            }
            
            // All test passed - add the argument
            MacroArgumentTableModel tableModel = (MacroArgumentTableModel) argumentTable.getModel();
            
            // If the given row is bigger than the current table size, a new row is added
            if(row > argumentTable.getRowCount()) {
                tableModel.addRow(new String[] { "", argumentName});
            }else {
                tableModel.insertRow(row, new String[] { "", argumentName});
            }
        }     
    }
}