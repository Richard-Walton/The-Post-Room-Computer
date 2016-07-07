package uk.ac.hud.postroom.ui.textpane;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Custom JTextPane with disabled line wrapping and backed by a SourceFileDocument
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SourceFileTextPane extends JTextPane {
    
    /**
     * Constructs a new SourceFileTextPane
     */
    public SourceFileTextPane() {
        super(new SourceFileDocument());

        setBackground(Color.WHITE);
        setMargin(new Insets(0,0,0,0));
        setBorder(null);  
        
        // Registers the textPane with the ToolTipManager to display tooltips
        ToolTipManager.sharedInstance().registerComponent(this);  
    }
    
    /** @inheritDoc **/
    @Override public SourceFileDocument getStyledDocument() {
        return (SourceFileDocument) super.getStyledDocument();
    }
    
    /** @inheritDoc **/
    @Override public boolean getScrollableTracksViewportWidth() {
        /* This method disables line wrapping */
        Component parent = getParent();

        if(parent != null){
            return getUI().getPreferredSize(this).getWidth() <= parent.getWidth();
        }else{
            return true;
        }
    }
    
    /** @inheritDoc **/
    @Override public String getToolTipText(MouseEvent event) {
        // Gets the position index which the mouse pointer is currently at
        int position = getUI().viewToModel(this, event.getPoint(), new Position.Bias[1]);
        if(position > -1) {
            // Gets the token at the index
            Token token = getStyledDocument().getTokenizer().getTokenAtOffset(position);
            
            if(token != null) {
        
                TokenType tokenType = token.getType();
        
                switch(tokenType) {
                    case OPCODE :
                        // Returns specific opcode information
                        OpCode opCode = OpCode.getByMnemonic(token.getValue());
                        return opCode.name() + ": " + opCode.getDescription();
                    default :
                        // Returns the tokenType description
                        return tokenType.name() + ": " + tokenType.getDescription();
                }
            }
        }
        
        // Invalid index - try super method
        return super.getToolTipText(event);
    }
}