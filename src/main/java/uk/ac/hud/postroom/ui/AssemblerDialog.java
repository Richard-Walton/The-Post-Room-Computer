package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.assembler.*;
import uk.ac.hud.postroom.ui.filechooser.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * Dialog for displaying the results of assembly
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class AssemblerDialog extends JDialog {
    
    // Assembler used
    private Assembler assembler;
    
    // Option the user clicked to close the dialog
    private int option;
    
    private String title;
    
    /**
     * Constructs a new AssemblerDialog for the given assembler
     * @param assembler Assembler used to provide information
     */
    public AssemblerDialog(Assembler assembler) {
        this.assembler = assembler;
        
        buildUI();
        
        // Default option is that the user cancelled the dialog
        option = JOptionPane.CANCEL_OPTION;
    }
    
    /**
     * Returns the option that was used to close the dialog (same spec as JOptionPane)
     * @return option that was used to close (e.g. JOptionPane.OK_OPTION)
     */ 
    public int getOption() {
        return option;
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setTitle("Post Room Assembler");
        setLayout(new BorderLayout());
       
        JLabel header = new JLabel("", JLabel.CENTER);
        JTable table = null;
        
        // If the assembler encountered errors display them
        if(assembler.hasErrors()) {
            header.setText("Errors were encountered during assembly");
            table = new ErrorTable(assembler.getErrors());
        }else if(! assembler.hasInstructions()) {
            header.setText("No instructions were assembled");
        }else{
            // otherwise display the instructions which were generated
            header.setText("Assembly completed");
            table = new InstructionTable(assembler.getInstructions());
        }
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton load = new JButton("Load into Computer");
            JButton moreInfo = new JButton("More Info");
            JButton save = new JButton("Save");
            JButton close = new JButton("Close");  

        // add components
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
            southPanel.add(load);
            southPanel.add(moreInfo);
            southPanel.add(save);
            southPanel.add(close);
            
        // Disable the start button if errors were found during assembly
        load.setEnabled(!assembler.hasErrors() && assembler.hasInstructions());
        moreInfo.setEnabled(assembler.hasInstructions());
        save.setEnabled(!assembler.hasErrors());
        
        pack();
        setModal(true);
        setSize(new Dimension(350, 400));
        
        // Add listeners
        
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                option = JOptionPane.YES_OPTION; // set to OK option
                dispose();
            }
        });
        
        moreInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssemblyInfoDialog.showDialog(AssemblerDialog.this, assembler);
            }
        });
        
        save.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new PCOFileFilter());
                // Opens the file chooser dialog
                int action = fileChooser.showSaveDialog(AssemblerDialog.this);
        
                if(action == JFileChooser.APPROVE_OPTION) { 
                    // Get selected file
                    String fileName = fileChooser.getSelectedFile().getPath();
            
                    if(! fileName.endsWith(".pco")) {
                        // Ensure file ends with ".pco" 
                        fileName = fileName + ".pco";
                    }
                    
                    StringBuilder machineCode = new StringBuilder();
                    for(Instruction instruction : assembler.getInstructions()) {
                        machineCode.append(instruction.getInstruction());
                        machineCode.append("\r\n");
                    }
            
                    try {
                        // Save the file
                        SourceFile.save(new SourceFile(fileName, machineCode.toString()));
                        UIUtilities.showInformationDialog(AssemblerDialog.this , fileName + " saved");
                    }catch (IOException ioError) {
                        // Inform of error
                        UIUtilities.showErrorDialog(AssemblerDialog.this, ioError.getMessage());
                    }
                }
            }
        });
        
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                dispose(); // no need to change option as default is CANCEL
            }
        });
    }
    
    /**
     * Constructs and shows a new AssemblerDialog using the given assembler
     * @param parent Parent of the dialog
     * @param assembler Assembler used
     * @return Option the user clicked to close the dialog (as JOptionPane spec)
     */
    public static int showDialog(Component parent, Assembler assembler) {
        AssemblerDialog dialog = new AssemblerDialog(assembler);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        
        return dialog.getOption();
    }
}