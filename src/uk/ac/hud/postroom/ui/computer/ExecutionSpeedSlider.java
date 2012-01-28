package uk.ac.hud.postroom.ui.computer;

import javax.swing.*;
import java.util.*;

/**
 * Slider to display different available speed settings for the computer
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ExecutionSpeedSlider extends JSlider {
    
    /**
     * Constructs a new ExecutionSpeedSlider
     */
    public ExecutionSpeedSlider() {
        super(0, 4);
        
        // Labels to be used at different points
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
            labelTable.put(0, new JLabel("Step"));
            labelTable.put(1, new JLabel("Slow"));
            labelTable.put(2, new JLabel("Medium"));
            labelTable.put(3, new JLabel("Fast"));
            labelTable.put(4, new JLabel("Full"));
            
        // Customize the slider display
        setLabelTable(labelTable);
        setPaintLabels(true);
        setPaintTicks(true);
        setMajorTickSpacing(1);
        setSnapToTicks(true);   
    }
}