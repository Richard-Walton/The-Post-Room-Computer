package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.computer.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import uk.ac.hud.postroom.event.ComputerAdapter;

/**
 * Displays components to control a Computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ComputerControlPanel extends JPanel {
    
    /**
     * Constructs a new ComputerControlPanel for the given computer
     * @param computer computer to be controlled
     */
    public ComputerControlPanel(final Computer computer) {
        super(new FlowLayout(FlowLayout.CENTER));
        
        // Construct components
        final ExecutionSpeedSlider speedSlider = new ExecutionSpeedSlider();
        final JButton step = new JButton("Step once");
        JButton reset = new JButton("Reset computer");
        JButton cancel = new JButton("Cancel execution");
        JButton execute = new JButton("Execute program");
            
        // Adds components to the panel
        add(new JLabel("Execution Speed:"));
        add(speedSlider);
        add(step);
        add(reset);
        add(cancel);
        add(execute);
        
        step.setEnabled(false);
        
        step.setToolTipText("Invokes the computer to execute the next instruction");
        reset.setToolTipText("Resets the computer and reloads the program instructions into memory");
        cancel.setToolTipText("Forces the computer to stop executing the running program");
        execute.setToolTipText("Starts the computer executing the program instructions in memory");
        
        // Add listeners
        
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(!speedSlider.getValueIsAdjusting()) {
                    int value = speedSlider.getValue();
                    
                    step.setEnabled(value == 0);
                    
                    switch(value) {
                        case 0 : 
                            computer.setExecutionSpeed(ExecutionSpeed.STEP); 
                            break;
                        case 1 : 
                            computer.setExecutionSpeed(ExecutionSpeed.SLOW); 
                            break;
                        case 2 : 
                            computer.setExecutionSpeed(ExecutionSpeed.MEDIUM); 
                            break;
                        case 3 : 
                            computer.setExecutionSpeed(ExecutionSpeed.FAST); 
                            break;
                        case 4 :
                            computer.setExecutionSpeed(ExecutionSpeed.FULL); 
                            break;
                    }
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computer.reset();
            }
        });
        
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computer.forceStop();
            }
        });
        
        step.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computer.step();
            }
        });
        
        execute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                computer.execute();
            }
        });
        
        computer.addComputerListener(new ComputerAdapter() {
            public void breakPointHit(Computer computer, int breakpoint) {
                speedSlider.setValue(0);
                step.setEnabled(true);
            }
        });
    }
}