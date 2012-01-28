package uk.ac.hud.postroom.ui.styleeditor;

import javax.swing.*;
import java.awt.event.*;

/**
 * Font size combobox
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class FontSizeComboBox extends JComboBox {
    
    /**
     * Creates a new FontSizeComboBox
     */
    public FontSizeComboBox() {
        /* calls the super-constructor (JComboBox) constructor with a set of 
         common font sizes */
        super(new Integer[] {8, 10, 12, 14, 16, 18, 24});
        
        // Sets the combobox to be editalbe
        setEditable(true);
        
        /* Add listener to the combobox editor component
         which consumes all non-digit input typed into the combobox */
        getEditor().getEditorComponent().addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                if(!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }
            }
        });
    }
    
    /**
     * Returns the currently selected Font size as an Integer
     * @return Currently selected Font size
     */
    @Override public Integer getSelectedItem() {
        return (Integer) super.getSelectedItem();
    }
}