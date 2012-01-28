package uk.ac.hud.postroom.ui.editor;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.textpane.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.Position;

/**
 * Panel displaying editing components for a SourceFile
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceFileEditor extends JPanel {
    
    // The SourceFile being edittied
    private SourceFile sourceFile;
    
    // TextPane displaying the source code of the SourceFile
    private SourceFileTextPane textPane;
    
    private LineNumberList lineNumbers;
    
    // Quick Code Popup
    private QuickCodePopupMenu quickCode;
    
    /**
     * Constructs a new SourceFileEditor for the given SourceFile
     * @param sourceFile SourceFile to edit
     */
    public SourceFileEditor(SourceFile sourceFile) {
        super(new BorderLayout()); 
        
        this.sourceFile = sourceFile;
        
        JScrollPane scrollPane = new JScrollPane();
            textPane = new SourceFileTextPane();
            lineNumbers = new LineNumberList(textPane);
            
        add(scrollPane);
            scrollPane.setViewportView(textPane);
            scrollPane.setRowHeaderView(lineNumbers);
        
        textPane.setText(sourceFile.getSourceCode());
        
        // Moves the text pane caret on a right click and then shows the QuickCode menu
        textPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    int position = textPane.getUI().viewToModel(
                            textPane, e.getPoint(), new Position.Bias[1]);
                    if(position > -1) {
                        textPane.setCaretPosition(position);
                        quickCode.show(textPane, e.getX(), e.getY());
                    }
                }
            }
        });
        
        quickCode = new QuickCodePopupMenu(this);
    }
    
    /**
     * Returns the source file being editted
     * @return source file being editted
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }
    
    /**
     * Returns the TextPane used to edit the SourceFile
     * @return TextPane used to edit the SourceFile
     */
    public SourceFileTextPane getTextPane() {
        return textPane;
    }
    
    public int[] getBreakPoints() {
        return lineNumbers.getSelectedIndices();
    }
    
    /**
     * Returns whether there are any unsaved changes
     * @return true if there are any unsaved changes
     */
    public boolean changesMade() {
        return ! sourceFile.getSourceCode().equals(textPane.getText());
    }
    
    /**
     * Updates the SourceFile sourceCode with the text in the textPane
     */
    public void updateFileContents() {
        getSourceFile().setSourceCode(getTextPane().getText());
    }
}