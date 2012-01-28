package uk.ac.hud.postroom.ui.textpane;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

/**
 * LineNumberListModel keeps track of the line numbers in a given Document
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class LineNumberListModel extends AbstractListModel {
    
    // Root element in the document
    private Element documentRoot;
    
    // Last line count
    private int lineCount;
    
    /**
     * Constructs a new LineNumberListModel for the given document
     * @param document Document to keep line numbers of
     */
    public LineNumberListModel(Document document) {
        documentRoot = document.getDefaultRootElement();
        
        // Listener to deal with insert / delete of lines
        document.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                int newLineCount = getSize();
                if(lineCount < newLineCount) {
                    // New line(s) added - inform listeners
                    fireIntervalAdded(
                            LineNumberListModel.this, lineCount, newLineCount);
                    lineCount = newLineCount;
                }
            }
            
            public void removeUpdate(DocumentEvent e) {
                int newLineCount = getSize();
                if(lineCount > newLineCount) {
                    // Line(s) removed - informa listeners
                    fireIntervalRemoved(
                            LineNumberListModel.this, lineCount, newLineCount);
                    lineCount = newLineCount;
                }
            }
            
            public void changedUpdate(DocumentEvent e) {} //don't need to implement
        });
        
        lineCount = getSize();
    }
    
    /** @inheritDoc **/
    public Object getElementAt(int index) { 
        if(index > -1 && index < getSize()) {
            return index;
        }else{
            return "";
        }
    }
    
    /** @inheritDoc **/
    public int getSize() {
        return documentRoot.getElementCount();
    }
}