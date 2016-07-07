package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.ui.table.*;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Frame to display the Flag register
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class FlagFrame extends ComponentFrame {
    
    // Memory checkbox
    private JCheckBox memory;
    
    // Overflow checkbox
    private JCheckBox overflow;
    
    // Not checkbox
    private JCheckBox not;
    
    // Carry checkbox
    private JCheckBox carry;
    
    // Negative checkbox
    private JCheckBox negative;
    
    // Zero checkbox
    private JCheckBox zero;
    
    public FlagFrame(RegisterStore registerStore) {
        super("Flags", new FlagLogTableModel(registerStore));
        
        buildUI();
        
        // Listen for flag updates and update display
        registerStore.addRegisterListener(new RegisterAdapter() {
            public void registerWroteTo(RegisterStore registers, Register register, String value) {
                if(register == Register.FLG) {
                    Flag flag  = new Flag(value);
                    // Update check boxes
                    memory.setSelected(flag.isMemory());
                    overflow.setSelected(flag.isOverflow());
                    not.setSelected(flag.isNot());
                    carry.setSelected(flag.isCarry());
                    negative.setSelected(flag.isNegative());
                    zero.setSelected(flag.isZero());
                }
            }
        });
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {        
        // Construct components
        JPanel northPanel = new JPanel(new GridLayout(2, 3));
            // Construct and add components
            northPanel.add(memory = new JCheckBox("Memory"));
            northPanel.add(overflow = new JCheckBox("Overflow"));
            northPanel.add(not = new JCheckBox("Not"));
            northPanel.add(carry = new JCheckBox("Carry"));
            northPanel.add(negative = new JCheckBox("Negative"));
            northPanel.add(zero = new JCheckBox("Zero"));
            
        add(northPanel, BorderLayout.NORTH);
        
        northPanel.setBorder(BorderFactory.createTitledBorder("Current flag bits"));
        
        // Listener which negates any selection made on a JCheckBox
        ActionListener noSelect = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox source = (JCheckBox) e.getSource();
                source.setSelected(!source.isSelected());
            }
        };
        
        // Stop any selections being made (setEnabled(false) causes UI problems)
        memory.addActionListener(noSelect);
        overflow.addActionListener(noSelect);
        not.addActionListener(noSelect);
        carry.addActionListener(noSelect);
        negative.addActionListener(noSelect);
        zero.addActionListener(noSelect);     
    }
}