package uk.ac.hud.postroom.ui.editor.tree.menu;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.editor.*;

import javax.swing.*;
import java.awt.event.*;

public class ProjectContextMenu extends JPopupMenu {
    
    public ProjectContextMenu(final ProjectTree owner) {
        final Project project = owner.getModel().getProject();
        
        JMenuItem newSourceFile = new JMenuItem("New source file");
        JMenuItem refreshProject = new JMenuItem("Refresh project");
        JMenuItem deleteFiles = new JMenuItem("Delete source files");
        JMenuItem deleteProject = new JMenuItem("Delete project");
        
        newSourceFile.setToolTipText("Creates a new source file in the Project");
        refreshProject.setToolTipText("Checks the Project directory for new source files");
        deleteFiles.setToolTipText("Deletes all the source files in the Project");
        deleteProject.setToolTipText("Deletes the Project (including all source files)");
            
        add(newSourceFile);
        add(refreshProject);
        addSeparator();
        add(deleteFiles);
        add(deleteProject);
        
        boolean enabled = project.getSourceFiles().length > 0;

        refreshProject.setEnabled(enabled);
        deleteFiles.setEnabled(enabled);
        
        newSourceFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog(owner, "Enter filename:");
                if(fileName != null) {
                    fileName = fileName.trim();
                    if(fileName.length() > 0){
                        // Dealing with no spaces file name is much easier for the Tokenizer
                        fileName = fileName.replace(' ', '_');
                        
                        // Append .pca if not already present
                        if(!fileName.endsWith(".pca")) { 
                            fileName = fileName + ".pca";
                        }
                        
                        // Append path to file
                        fileName = project.getPath() + "/" + fileName;
                        
                        try {
                            project.addNewSourceFile(fileName);
                            owner.getModel().refresh();
                        }catch (Exception ioError) {
                            UIUtilities.showErrorDialog(owner, ioError.getMessage());
                        }
                    }else{
                        UIUtilities.showErrorDialog(owner, "Invalid filename");
                    }
                }
            }
        });
        
        refreshProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    project.refreshProject();
                    owner.getModel().refresh();
                }catch (Exception ioError) {
                    UIUtilities.showErrorDialog(owner, ioError.getMessage());
                }
            }
        });
        
        deleteFiles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int action = UIUtilities.showConfirmDialog(
                        owner, "Are you sure you want to DELETE all the " +
                        "source files in this project?");
                if(action == JOptionPane.YES_OPTION) {
                    try{
                        project.deleteSourceFiles();
                        owner.getModel().refresh();
                    }catch (Exception ioError) {
                        UIUtilities.showErrorDialog(owner, ioError.getMessage());
                    }
                }
            }
        }); 
        
        deleteProject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int action = UIUtilities.showConfirmDialog(
                        owner, "Are you sure you want to DELETE this project? " +
                        "This action will cause The Post Room Computer to restart");
                if(action == JOptionPane.YES_OPTION) {
                    try{
                        project.deleteProject();
                        // Restart Post Room
                        
                        SwingUtilities.getWindowAncestor(owner).dispose();
                        SetupDialog.showDialog(owner);
                    }catch (Exception ioError) {
                        UIUtilities.showErrorDialog(owner, ioError.getMessage());
                    }
                }
            }
        });        
    }
}