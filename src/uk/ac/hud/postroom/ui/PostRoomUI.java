package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.computer.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The Post Room Computer User Interface
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class PostRoomUI extends JFrame {
    
    // Project that is being used
    private Project project; 
    
    // Computer that is being used
    private Computer computer;
    
    // Assembler that is being used
    private Assembler assembler;
    
    // TabbedPane which has the Editor and Computer components
    private JTabbedPane tabbedPane;
    
    private EditorUI editorUI;
    /**
     * Constructs a new PostRoomUI
     * @param project The Project being used / editted
     * @param computer The Computer used to execute programs
     */
    public PostRoomUI(Project project, Computer computer, Assembler assembler) {
        super("Post Room Computer v1.0 " + "[" + project.getName() + "]");
         
        this.project = project;
        this.computer = computer;
        this.assembler = assembler;
        
        buildUI();
    }
    
    /**
     * Returns the Project being used
     * @return Project used / editted
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Returns the Computer
     * @return Computer used to execute programs
     */
    public Computer getComputer() {
        return computer;
    }
    
    /**
     * Returns the Assembler
     * @return Assembler used to translate assembly language programs into machine code
     */
    public Assembler getAssembler() {
        return assembler;
    }
    
    /**
     * Returns the tabbedPane used to hold the Editor and Computer UI components
     * @return tabbedPane used to hold the Editor and Computer UI components
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    
    /**
     * Builds the UI
     */
    private void buildUI() {
        setLayout(new BorderLayout());
        
        buildMenuBar();
        buildCenterPanel();
        buildSouthPanel();
        
        pack();
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Centers the frame on screen
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }
    
    /**
     * Builds and adds the MenuBar
     */
    private void buildMenuBar() {
        // Contruct components
        JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
                JMenuItem openProject = new JMenuItem("New/Open Project");
                JMenuItem exit = new JMenuItem("Exit");
            JMenu optionsMenu = new JMenu("Options");
                JMenuItem fontAndColourChooser = new JMenuItem("Font and Colour chooser");
                JCheckBoxMenuItem tooltips = new JCheckBoxMenuItem("Show tooltips", true);
            JMenu helpMenu = new JMenu("Help");
                JMenuItem about = new JMenuItem("About");
          
        // Add components
        setJMenuBar(menuBar);
            menuBar.add(fileMenu);
                fileMenu.add(openProject);
                fileMenu.addSeparator();
                fileMenu.add(exit);
            menuBar.add(optionsMenu);
                optionsMenu.add(fontAndColourChooser);
                optionsMenu.add(tooltips);
            menuBar.add(helpMenu);
                helpMenu.add(about);
        
        // Add listeners
        openProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SetupDialog().setVisible(true);
            }
        });
        
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        fontAndColourChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StyleEditorDialog.showDialog(PostRoomUI.this);
            }
        });
        
        tooltips.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToolTipManager.sharedInstance().setEnabled(
                    ((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIUtilities.showAboutDialog(PostRoomUI.this);
            }
        });
    }
    
    /**
     * Builds and adds the center panel
     */
    private void buildCenterPanel() {            
        add(tabbedPane = new JTabbedPane(), BorderLayout.CENTER);
            tabbedPane.addTab("Editor", editorUI = new EditorUI(this));
            tabbedPane.addTab("Computer", new ComputerUI(computer));
    }
    
    /**
     * Builds and adds the south panel
     */
    private void buildSouthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            JLabel label = new JLabel("[Computer: " + computer.getClass().getSimpleName() + "]" + 
                " [Assembler: " + assembler.getClass().getSimpleName() + "]");
            
        this.add(panel, BorderLayout.SOUTH);
            panel.add(label);
            
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    /**
     * Stops any computer execution and closes this instance of the Post Room Computer
     */
    private void exit() {
        int action = UIUtilities.showConfirmDialog(rootPane, "Are you sure you want to exit?");
        if(action == JOptionPane.YES_OPTION) {
            getComputer().forceStop();
            editorUI.closeAll();
            dispose();
        }
    }
}