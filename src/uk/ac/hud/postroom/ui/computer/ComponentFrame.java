package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;

/**
 * Computer Component frame which displays activity log and functionality to 
 * save to comma separated variable (*.csv) files
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ComponentFrame extends JInternalFrame {
    
    // FileChooser for saving files
    private static JFileChooser fileChooser;
    
    static {
        // Constructs the file chooser
        fileChooser = new CSVFileChooser();
    }
    
    // Table which displays the log data
    protected JTable table;
    
    /**
     * Constructs a new Computer frame using the given title and a DefaultTableModel
     * @param title Title of the frame
     */
    public ComponentFrame(String title) {
        this(title, new DefaultTableModel());
    }
    
    /**
     * Constructs a new Computer frame using the given title and table model
     * @param title Title of the frame
     * @param tabelModel TableModel to set as the table model
     */
    public ComponentFrame(String title, TableModel tabelModel) {
        super(title, true, false, true, true); 
        
        setLayout(new BorderLayout());
        
        add(new JScrollPane(table = new JTable(tabelModel)));
        
        buildSouthPanel();
        
        pack();
        
        // Set reasonable size
        setSize(300, 300);
    }
    
    /**
     * Builds and adds the South Panel
     */
    private void buildSouthPanel() {
        // Construct Components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton save = new JButton("Save log");
            
        // Add components
        this.add(panel, BorderLayout.SOUTH);
            panel.add(save);
        
        // Add listener
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveLog();
            }
        });
    }
    
    /**
     * Sames the table model of this frame into a CSV file selected by the user
     */
    private void saveLog() {
        // Opens the file chooser dialog
        int action = fileChooser.showSaveDialog(this);
        
        if(action == JFileChooser.APPROVE_OPTION) { 
            // Get selected file
            File selectedFile = fileChooser.getSelectedFile();
            
            if(! selectedFile.getName().endsWith(".csv")) {
                // Ensure file ends with ".csv" 
                selectedFile = new File(selectedFile.getPath() + ".csv");
            }
            
            try {
                // Save the file
                CSVUtil.saveCSVFile(selectedFile, table.getModel());
                UIUtilities.showInformationDialog(this, selectedFile.getPath() + " saved");
            }catch (IOException e) {
                // Inform of error
                UIUtilities.showErrorDialog(this, e.getMessage());
            }
        }
    }
}