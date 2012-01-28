package uk.ac.hud.postroom.ui.editor.tree.menu;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.editor.*;
import uk.ac.hud.postroom.ui.assembler.*;
import uk.ac.hud.postroom.ui.tree.*;

import javax.swing.*;
import java.awt.event.*;

public class SourceFileContextMenu extends JPopupMenu {
    
    public SourceFileContextMenu(final ProjectTree owner, final SourceFileNode node) {        
        JMenuItem information = new JMenuItem("Information");
        JMenuItem deleteFile = new JMenuItem("Delete file");
        
        information.setToolTipText("Shows information about the source file");
        deleteFile.setToolTipText("Deletes the source file");
            
        add(information);
        addSeparator();
        add(deleteFile);
        
        information.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SyntaxInfoDialog.showDialog(owner, node.getSourceFile());
            }
        });
        
        deleteFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int action = UIUtilities.showConfirmDialog(
                        owner, "Are you sure you want to DELETE this file?");
                if(action == JOptionPane.YES_OPTION) {
                    try {
                        Project parent = ((ProjectNode) node.getParent()).getProject();
                        parent.deleteSourceFile(node.getSourceFile());
                    
                        owner.getModel().refresh();
                    }catch (Exception ioError) {
                        UIUtilities.showErrorDialog(owner, ioError.getMessage());
                    }
                }
            }
        });
    }
}