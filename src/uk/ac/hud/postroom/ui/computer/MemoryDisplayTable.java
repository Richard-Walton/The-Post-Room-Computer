package uk.ac.hud.postroom.ui.computer;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;

import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.table.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import uk.ac.hud.postroom.Instruction;
import uk.ac.hud.postroom.assembler.statement.InvalidStatement;
import uk.ac.hud.postroom.exception.AssemblyError;

/** 
 * Table to display computer memory in a variety of formats
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class MemoryDisplayTable extends JTable {
    
    // The memory being displayed
    private Memory memory;
    
    // Popup menu to change display type
    private JPopupMenu popupMenu;
    
    // Row selected on right click
    private int selectedRow;
    
    /**
     * Constructs a new MemoryDisplayTable for the given memory
     * @param memory Memory to be displayed
     */
    public MemoryDisplayTable(Memory memory) {
        super(new MemoryDisplayTableModel(memory));
        
        this.memory = memory;
        
        // Allows only one row to be selected at any one time
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable re-ordering of columns
        getTableHeader().setReorderingAllowed(false);
        
        // Sets the preferred size of the table when placed in a JScollPane
        setPreferredScrollableViewportSize(getPreferredSize());
        
        
        // Listener to highlight memory address rows which are being accessed
        memory.addMemoryListener(new MemoryAdapter() {
            public void memoryReadFrom(Memory memory, int address, String value) {
                // Highlight the address row
                setRowSelectionInterval(address, address);
            }

            public void memoryWroteTo(Memory memory, int address, String value) {
                // Highlight the address row
                setRowSelectionInterval(address, address);
            }         
        });
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    selectedRow = rowAtPoint(e.getPoint());
                    popupMenu.show(MemoryDisplayTable.this, e.getX(), e.getY());
                }
            }
        });
        
        buildPopupMenu();
    }
    
    /**
     * Builds the popup menu
     */
    private void buildPopupMenu() {
        // Construct components
        popupMenu = new JPopupMenu();
            JMenu viewAs = new JMenu("View as");
                JRadioButtonMenuItem raw = new JRadioButtonMenuItem("Raw", true);
                JRadioButtonMenuItem decimal = new JRadioButtonMenuItem("Decimal");
                JRadioButtonMenuItem ascii = new JRadioButtonMenuItem("Ascii");
                JRadioButtonMenuItem hexadecimal = new JRadioButtonMenuItem("Hexadecimal");
                JRadioButtonMenuItem octal = new JRadioButtonMenuItem("Octal");
                JRadioButtonMenuItem binary = new JRadioButtonMenuItem("Binary");
            JMenuItem edit = new JMenuItem("Edit data");
         
        // Add components
        popupMenu.add(viewAs);
            viewAs.add(raw);
            viewAs.add(decimal);
            viewAs.add(ascii);
            viewAs.addSeparator();
            viewAs.add(hexadecimal);
            viewAs.add(octal);
            viewAs.add(binary);
        popupMenu.addSeparator();
        popupMenu.add(edit);
        
        // Add radiobuttons to button group
        ButtonGroup viewGroup = new ButtonGroup();
            viewGroup.add(raw);
            viewGroup.add(binary);
            viewGroup.add(octal);
            viewGroup.add(decimal);
            viewGroup.add(hexadecimal);
            viewGroup.add(ascii);
           
        // Add listeners to change display model
        binary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.BINARY);
            }
        });
        
        octal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.OCTAL);
            }
        });
        
        decimal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.DECIMAL);
            }
        });
        
        hexadecimal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.HEXADECIMAL);
            }
        });
        
        ascii.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.ACSII);
            }
        });
        
        raw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getModel().setModelType(MemoryDisplayTableModel.RAW);
            }
        });
        
        edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {              
                String input = 
                        JOptionPane.showInputDialog(
                        getParent(), "Input data for address " + selectedRow);
                if(input != null) {
                    input = input.trim();
        
                    if(! input.matches("\\d+")) {
                        // Invalid input - show error and try again
                        UIUtilities.showErrorDialog(getParent(), "Expected integer input");
                    }else {
                        if(selectedRow > -1) {
                            memory.put(selectedRow, input);
                            
                            // Update computer instruction cache
                            Instruction[] instructions = new Instruction[memory.size()];
                            
                            try {
                                for(int i = 0; i < memory.size(); i++) {
                                    // Hack to create new instructions without original source code statement
                                    instructions[i] = new Instruction(new InvalidStatement(null, 0, null), memory.get(i));
                                }
                                
                                // Set updated instructions
                                memory.getComputer().setInstructions(instructions);
                            }catch (AssemblyError ie) {
                                // Invalid instruction data
                                UIUtilities.showErrorDialog(getParent(), "Invalid instruction data");
                            }
                            
                        }
                    }
                }
            }
        });
    }
    
    /** @inheritDoc **/
    @Override public MemoryDisplayTableModel getModel() {
        return (MemoryDisplayTableModel) super.getModel();
    }
}