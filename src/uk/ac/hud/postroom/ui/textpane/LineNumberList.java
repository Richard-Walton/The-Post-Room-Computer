package uk.ac.hud.postroom.ui.textpane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import uk.ac.hud.postroom.event.*;

/**
 * Custom list which displays line numbers of a document
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class LineNumberList extends JList {
    
    /** 
     * Constructs a new LineNumberList for the given textComponent
     * @param textComponent TextComponent which needs a numbered list
     */
    public LineNumberList(final JTextComponent textComponent) {
        // Super-class (JList) constructor
        super(new LineNumberListModel(textComponent.getDocument()));
        
        // Set list ui
        setFocusable(false);
        setFont(EditorStyles.getFont());
        setFixedCellWidth(25);
        setFixedCellHeight(
                        textComponent.getFontMetrics(EditorStyles.getFont()).getHeight());
        
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        
        // Listener to move the caret to the end of line when the list is selected
        addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = getSelectedIndex();
                if(selectedIndex > -1) {
                    // Set caret position to end of selected line
                    textComponent.setCaretPosition(textComponent.getDocument().
                            getDefaultRootElement().getElement(selectedIndex).getEndOffset() - 1);
                }
            }
        });
        
        setCellRenderer(new BreakPointCellRenderer());
        
        EditorStyles.addEditorStylesListener(new EditorStylesListener() {
            public void fontChanged() {
                setFixedCellHeight(
                        textComponent.getFontMetrics(EditorStyles.getFont()).getHeight());
                setFont(EditorStyles.getFont());
            }
        });
        
        for(MouseListener l : getMouseListeners()) {
            removeMouseListener(l);
        }
        
        for(MouseMotionListener l : getMouseMotionListeners()) {
            removeMouseMotionListener(l);
        }
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) {
                    return;
                }
                int selected = locationToIndex(e.getPoint());
                if(selected > -1) {
                    if(getSelectionModel().isSelectedIndex(selected)){
                        getSelectionModel().removeSelectionInterval(selected, selected);
                    }else{
                        getSelectionModel().addSelectionInterval(selected, selected);
                    }
                } 
            }
        });
    }
}