package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.setup.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Post Room Computer setup dialog
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SetupDialog extends JDialog {
    
    /**
     * Constructs a new SetupDialog
     */
    public SetupDialog() {       
        setTitle("Post Room Computer Setup");
        setLayout(new BorderLayout());
        
        setJMenuBar(new SetupUIMenuBar(this));
        
        buildNorthPanel();
        buildCenterPanel();
        
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Builds and adds a panel to the North section of the dialog
     */
    private void buildNorthPanel() {
        // Construct components
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel header = new JLabel("Post Room Computer Setup");
          
        // add components to dialog
        this.add(panel, BorderLayout.NORTH);
            panel.add(header);
        
        header.setFont(new Font("Arial", Font.BOLD, 20));
    }
    
    /**
     * Builds and adds a panel to the Center section of the dialog
     */
    private void buildCenterPanel() {
        // Construct components
        JPanel panel = new JPanel(new GridLayout(1, 3, 5, 5));
            JButton absoluteAddress = new SetupMenuButton("Start Absolute<br>Address Machine");
            JButton registerAddress = new SetupMenuButton("Start Register<br>Address Machine");
            JButton exit = new SetupMenuButton("Exit");
        
        // add components to dialog
        this.add(panel, BorderLayout.CENTER);
            panel.add(absoluteAddress);
            panel.add(registerAddress);
            panel.add(exit);
        
        // Add action listeners to buttons
        absoluteAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Project project = getProjectDirectory();
                if(project != null) {
                    dispose();
                    new PostRoomUI(
                        project,
                        new AbsoluteAddressComputer(), 
                        new AbsoluteAddressAssembler()).setVisible(true);
                }
            }
        });
        
        registerAddress.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Project project = getProjectDirectory();
                if(project != null) {
                    dispose();
                    new PostRoomUI(
                        project,
                        new RegisterAddressComputer(),
                        new RegisterAddressAssembler()).setVisible(true);
                }
            }
        });
        
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Prompts the user to select a project directory
     * @return The selected project directory (null values = no selection)
     */
    private Project getProjectDirectory() {
        ProjectChooser projectChooser = new ProjectChooser();
        int action = projectChooser.showOpenDialog(this);
        if(action == JFileChooser.APPROVE_OPTION) {
            try {
                return new Project(projectChooser.getSelectedFile().getPath());
            }catch (Exception ioError) {
                UIUtilities.showErrorDialog(this, ioError.getMessage());
            }
        }
        
        return null;
    }
    
    public static void showDialog(Component parent) {
        SetupDialog dialog = new SetupDialog();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}