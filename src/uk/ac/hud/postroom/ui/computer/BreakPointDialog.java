/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.hud.postroom.ui.computer;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import uk.ac.hud.postroom.Instruction;
import uk.ac.hud.postroom.computer.Computer;
import uk.ac.hud.postroom.ui.editor.SourceFileEditor;
import uk.ac.hud.postroom.ui.textpane.SourceFileTextPane;

/**
 *
 * @author Richard Walton (walton909@gmail.com)
 */
public class BreakPointDialog extends JDialog {

    private SourceFileTextPane sourceCode;

    public BreakPointDialog(Computer computer) {
        setLayout(new BorderLayout());

        sourceCode = new SourceFileTextPane();
        
        for(Instruction instruction : computer.getInstructions()) {
            sourceCode.setText(sourceCode.getText() + "\n" + instruction.getSourceCode());
        }

        add(sourceCode);
        pack();
    }

}
