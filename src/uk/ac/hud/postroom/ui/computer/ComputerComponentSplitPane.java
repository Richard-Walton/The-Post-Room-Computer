package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.ui.table.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Displays components of the computer in a SplitPane where the top component
 * is a DesktopPane for various loggers, and the bottom is a GIODevice
 * @author Richard Walton (c0410542@hud.ac.)
 */
public class ComputerComponentSplitPane extends JSplitPane {
    
    // Owner of the components being displayed
    private Computer computer;
    
    // Decoder activity logger frame
    private JInternalFrame decoderFrame;
    
    // Flag activity logger frame
    private JInternalFrame flagFrame;    
    
    // Memory activity logger frame
    private JInternalFrame memoryFrame;
    
    // Register activity logger frame
    private JInternalFrame registerFrame;
    
    // IO Device of the computer
    private GIODevice ioDevice;
    
    /**
     * Constructs a new ComputerComponentSplitPane for the given computer
     * @param computer Owner of the components being displayed
     */
    public ComputerComponentSplitPane(Computer computer) {
        super(JSplitPane.VERTICAL_SPLIT);
        
        this.computer = computer;
        
        // Init frames
        decoderFrame = new ComponentFrame("Instruction Decoder",
                new InstructionDecoderLogTableModel(computer.getInstructionDecoder()));
        
        flagFrame = new FlagFrame(computer.getRegisterStore());
                
        memoryFrame = new ComponentFrame("Memory", 
                new MemoryLogTableModel(computer.getMemory()));
        
        registerFrame = new RegisterFrame(computer.getRegisterStore());
        
        ioDevice = new GIODevice(computer.getIOModule());
        
        // Builds and adds the splitPane components
        buildTopComponent();
        buildBottomComponent();
        
        // Customize splitpane
        setOneTouchExpandable(true);
        setResizeWeight(1.0);
        setDividerSize(10);
        
        // Set all frames visible in the DesktopPane
        decoderFrame.setVisible(true);
        flagFrame.setVisible(true);
        memoryFrame.setVisible(true);
        registerFrame.setVisible(true);
    }
    
    /**
     * Builds and adds the top component to the splitpane
     */
    private void buildTopComponent() {
        // Construct components
        JPanel panel = new JPanel(new BorderLayout());
            JToolBar toolBar = new JToolBar();
                JCheckBox toggleDecoderFrame = new JCheckBox("Instruction Decoder", true);
                JCheckBox toggleFlagFrame = new JCheckBox("Flag Register", true);
                JCheckBox toggleMemoryFrame = new JCheckBox("Memory Frame", true);
                JCheckBox toggleRegisterFrame = new JCheckBox("Registers", true);
            JPanel desktopPanel = new JPanel(new BorderLayout());
                JDesktopPane desktopPane = new JDesktopPane();
          
        // Add components
        setTopComponent(panel);
            panel.add(toolBar, BorderLayout.NORTH);
                toolBar.add(toggleDecoderFrame);
                toolBar.add(toggleFlagFrame);
                toolBar.add(toggleMemoryFrame);
                toolBar.add(toggleRegisterFrame);
            panel.add(desktopPanel);
                desktopPanel.add(desktopPane);
                    desktopPane.add(decoderFrame);
                    desktopPane.add(flagFrame);
                    desktopPane.add(memoryFrame);
                    desktopPane.add(registerFrame);
                
        toolBar.setBorder(BorderFactory.createTitledBorder("Show:"));
        toolBar.setOpaque(false);
        desktopPanel.setBorder(BorderFactory.createTitledBorder("Computer Components"));
        
        //  Add listeners
        toggleDecoderFrame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decoderFrame.setVisible(((JCheckBox) e.getSource()).isSelected());
            }
        });
        
        toggleFlagFrame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flagFrame.setVisible(((JCheckBox) e.getSource()).isSelected());
            }
        });
        
        toggleMemoryFrame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                memoryFrame.setVisible(((JCheckBox) e.getSource()).isSelected());
            }
        });
        
        toggleRegisterFrame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerFrame.setVisible(((JCheckBox) e.getSource()).isSelected());
            }
        });
    }
    
    /**
     * Builds and adds the bottom component to the splitpane
     */
    private void buildBottomComponent() {
        // Construct components
        JPanel panel = new JPanel(new GridLayout(1, 1));
            JToolBar toolBar = new JToolBar("IO Module");
                JScrollPane scrollPane = new JScrollPane();
        
        // Add components
        setBottomComponent(panel);
            panel.add(toolBar);
                toolBar.add(scrollPane);
                    scrollPane.setViewportView(ioDevice);
            
        scrollPane.setBorder(BorderFactory.createTitledBorder("IO"));
    }
}