package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.ui.textpane.*;

import javax.swing.table.*;
import java.awt.*;

/**
 * Custom table model backed by the StyleStyles class
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class StyleTableModel extends SimpleTableModel {
    
    /* Editable styles in the SyntaxStyles class */
    private TokenType[] editableStyles;
    
    /**
     * Constructs a new OptionTableModel
     */
    public StyleTableModel() {
        super(new String[] {"Data Type", "Colour", "Bold", "Italic", "Underline" });
        
        // Editable styles
        editableStyles = EditorStyles.getEditableStyleTypes();
    }
    
    /** @inheritDoc **/
    public int getRowCount() {
        return editableStyles.length;
    }
    
    /** @inheritDoc **/
    public Object getValueAt(int row, int column) {
        TokenType type = editableStyles[row];
        switch(column) {
            case 0 : 
                String title = type.toString();
                return title.charAt(0) + title.substring(1).toLowerCase();
            case 1 : 
                return EditorStyles.getForeground(type); 
            case 2 : 
                return EditorStyles.isBold(type); 
            case 3 :
                return EditorStyles.isItalic(type); 
            case 4 :
                return EditorStyles.isUnderline(type);
            default :
                return null;
        }
    }
    
    /** @inheritDoc **/
    public void setValueAt(Object value, int row, int column) {
        TokenType type = editableStyles[row];
        
        switch(column) {
            case 1 : 
                EditorStyles.setForeground(type, (Color) value); 
                break;
            case 2 : 
                EditorStyles.setBold(type, (Boolean) value); 
                break;
            case 3 :
                EditorStyles.setItalic(type, (Boolean) value); 
                break;
            case 4 :
                EditorStyles.setUnderline(type, (Boolean) value); 
                break;
        }
        
        // Notifies any listeners
        fireTableCellUpdated(row, column);
    }    
    
    /** @inheritDoc **/
    public boolean isCellEditable(int row, int column){
        if(column == 0) {
            return false;
        }
        
        return true;
    }
    
    /** @inheritDoc **/
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}