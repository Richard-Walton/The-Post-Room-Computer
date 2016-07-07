package uk.ac.hud.postroom.ui.table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Custom Cell Editor for colours
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class ColourCellEditor extends AbstractCellEditor implements TableCellEditor {
    // Label which is displayed during editing
    private JLabel editor;
       
    // Value of the editor
    private Color editorValue;
        
    /**
     * Constructs a new ColourCellEditor
     */
    public ColourCellEditor() {
        editor = new JLabel();
        editor.setOpaque(true);
        editor.setBorder(BorderFactory.createMatteBorder(
                1,3,1,3, UIManager.getColor("Table.selectionBackground")));
        
        // Show the editor dialog as soon as the CellEditor is displayed in a table
        editor.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                showColourChooser();
                  
                fireEditingStopped();
            }
        });
    }
        
    /**
     * Shows a colour chooser dialog and sets the editor value to the 
     * colour selected in the dialog
     */
    private void showColourChooser() {
        Color color = JColorChooser.showDialog(editor, "Colour chooser", getCellEditorValue());
            
        if(color != null){
            // Colour was selected so change editor value
            editorValue = color;
        }   
    }
       
    /**
     * Returns the Colour selected in the editor
     * @return Color
     */
    public Color getCellEditorValue() {
        return editorValue;
    }

    /** @inheritDoc **/
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean selected, int row,int column) {
            
        editorValue = ((Color) value);
        editor.setBackground(getCellEditorValue());
        
        return editor;
    }
}