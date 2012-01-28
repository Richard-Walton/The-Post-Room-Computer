package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.computer.*;

import uk.ac.hud.postroom.ui.editor.*;
import uk.ac.hud.postroom.ui.assembler.*;
import uk.ac.hud.postroom.ui.tree.*;
import uk.ac.hud.postroom.ui.tabbedpane.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * EditorUI panel component of the PostRoomUI
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class EditorUI extends JPanel {
    
    // Tabbed Pane for SourceFileEditors
    private JTabbedPane editorTabbedPane;
    
    // Listener which updates editorTabbedPane when an Editor is updated
    private KeyListener saveListener;
    
    /**
     * Constructs a new EditorUI for the given PostRoomUI
     * @param ui Owner of the EditorUI
     */
    public EditorUI(final PostRoomUI ui) {
        super(new BorderLayout());
        
        // Construct Components
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            JScrollPane projectScroller = new JScrollPane();
            final ProjectTree projectTree = new ProjectTree(ui.getProject());
             editorTabbedPane = new EditorTabbedPane();
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                final JButton save = new JButton("Save");
                final JButton information = new JButton("File information");
                final JButton assemble = new JButton("Assemble file");
          
        // Add Components
        add(splitPane);
            splitPane.setLeftComponent(projectScroller);
                projectScroller.setViewportView(projectTree);
            splitPane.setRightComponent(editorTabbedPane);
        add(controls, BorderLayout.SOUTH);
            controls.add(save);
            controls.add(information);
            controls.add(assemble);
          
        // Customize components
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        
        projectScroller.setBorder(BorderFactory.createTitledBorder("Files"));
        projectTree.setBorder(null);
        
        save.setEnabled(false);
        information.setEnabled(false);
        assemble.setEnabled(false);
        
        save.setToolTipText("Saves the file");
        information.setToolTipText("View syntax information about the selected file");
        assemble.setToolTipText("Save and assemble the selected file");
        
        // Add listeners
        
        // Adds a "*" to the EditorTabbedPane when changes are made to a SourceFile
        saveListener = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                JLabel tab = ((CloseableTabComponent) editorTabbedPane.getTabComponentAt(
                        editorTabbedPane.getSelectedIndex())).getTitleLabel();
                
                String tabText = tab.getText();
                if(! tabText.endsWith("*")) { 
                    tab.setText(tabText + " *");
                }
                
                /* After the update the listener is removed to avoid overhead
                 * as we know the source file has been changed */
                ((Component) e.getSource()).removeKeyListener(this);
            }
        };
        
        /* Closes SourceFileEditor (if one is open) for a SourceFile when it is deleted
         * via the project tree */
        projectTree.getModel().addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent e) {}
            public void treeNodesInserted(TreeModelEvent e) {}
            public void treeNodesRemoved(TreeModelEvent e) {}
            public void treeStructureChanged(TreeModelEvent e) {
                 for(Component component : editorTabbedPane.getComponents()) {
                     if(component instanceof SourceFileEditor) {
                         boolean fileExists = false;
                         
                         SourceFileEditor editor = (SourceFileEditor) component;
                         SourceFile panelSourceFile = editor.getSourceFile();
                         for(SourceFile sourceFile : ui.getProject().getSourceFiles()) {
                             if(sourceFile.equals(panelSourceFile)) {
                                 fileExists = true;
                             }
                         }
                         
                         if(!fileExists) {
                             // Stop save dialog showing on tab remove
                             editor.updateFileContents();
                             
                             editorTabbedPane.remove(component);
                         }
                     }
                 }
            }
        });
        
        // Enables / Disables control buttons depending on the amount of tabs
        editorTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                boolean enabled = editorTabbedPane.getTabCount() > 0;
                
                save.setEnabled(enabled);
                information.setEnabled(enabled);
                assemble.setEnabled(enabled);
                
                SourceFileEditor editor = getSelectedEditorPanel();
                if(editor != null) {
                    if(editor.getSourceFile().getName().endsWith(".pco")) {
                        assemble.setText("Validate");
                    }else {
                        assemble.setText("Assemble");
                    }
                }
            }
        });
        
        // Opens selected SourceFile in the Project tree on a double click
        projectTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() > 1) {
                    TreePath selection =  projectTree.getSelectionPath();
                    if(selection != null) {
                        TreeNode selectedNode = 
                                (TreeNode) selection.getLastPathComponent();
                        if(selectedNode instanceof SourceFileNode) {
                            openEditorFor(((SourceFileNode) selectedNode).getSourceFile());
                        }
                    }
                }
            }
        });
        
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SourceFileEditor selectedPanel = getSelectedEditorPanel();
                if(selectedPanel != null) {
                    saveSelectedSourceFile();
                }
            }
        });
        
        // Shows SyntaxInformation about the selected SourceFile
        information.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SourceFileEditor selectedPanel = getSelectedEditorPanel();
                if(selectedPanel != null) {
                    if(selectedPanel.changesMade()) {
                        int action = UIUtilities.showConfirmDialog(
                                    selectedPanel, 
                                    "Do you want to save the changes before " +
                                    "viewing the file information?");
                        switch(action) {
                            case JOptionPane.OK_OPTION :
                                saveSelectedSourceFile();
                                break;
                            case JOptionPane.CANCEL_OPTION :
                                return;
                        }
                    }

                    SyntaxInfoDialog.showDialog(selectedPanel,
                            new SyntaxAnalyser(selectedPanel.getSourceFile()));
                }
            }
        });
        
        // Assembles the selected SourceFile
        assemble.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SourceFileEditor selectedPanel = getSelectedEditorPanel();
                if(selectedPanel != null) {
                    if(selectedPanel.changesMade()) {
                        
                        int action = UIUtilities.showConfirmDialog(
                                selectedPanel, 
                                "Do you want to save the changes before assembly?");
                        switch(action) {
                            case JOptionPane.OK_OPTION : 
                                saveSelectedSourceFile();
                                break;
                            case JOptionPane.CANCEL_OPTION :
                                return;
                        }
                    }
                    
                    Computer computer = ui.getComputer();
                    Assembler assembler = ui.getAssembler();
                    if(selectedPanel.getSourceFile().getName().endsWith(".pco")) {
                        assembler = new MachineCodeAssembler();
                    }
                    assembler.assemble(selectedPanel.getSourceFile());
                    
                    int action = AssemblerDialog.showDialog(selectedPanel, assembler);
                    
                    if(action == JOptionPane.YES_OPTION) {
                        ui.getTabbedPane().setSelectedIndex(1);

                        ui.getComputer().setInstructions(assembler.getInstructions());
                        ui.getComputer().setBreakPoints(selectedPanel.getBreakPoints());
                        ui.getComputer().reset();
                    }
                }
            }
        });       
    }
    
    /**
     * Saves the selected sourcefile to disk using the most recent update
     */
    private void saveSelectedSourceFile() {
        SourceFileEditor selectedPanel = getSelectedEditorPanel();
        if(selectedPanel != null) {
            // If changes have been made by the editor, update the file
            if(selectedPanel.changesMade()) {
                selectedPanel.updateFileContents();
            }
            
            // Save File
            try {
                SourceFile.save(selectedPanel.getSourceFile());
                
                // Update tab name to remove *
                ((CloseableTabComponent) editorTabbedPane.getTabComponentAt(
                    editorTabbedPane.getSelectedIndex())).setTitle(
                    selectedPanel.getSourceFile().getName());
                
                // Add listener to add * when changes are made
                selectedPanel.getTextPane().addKeyListener(saveListener);
                
            }catch (Exception ioError) { 
                UIUtilities.showErrorDialog(selectedPanel, ioError.getMessage());
            }
        }
    }
    
    /**
     * Returns the selected SourceFileEditor in the editor tabbed pane
     * @return selected SourceFileEditor in the editor tabbed pane or null if
     * nothing is selected or the component is not a SourceFileEditor
     */
    private SourceFileEditor getSelectedEditorPanel() {
        Component component = editorTabbedPane.getSelectedComponent();
        
        // Check if not null and is a SourceFileEditor
        if(component != null && component instanceof SourceFileEditor) {
            return (SourceFileEditor) component;
        }
        
        return null;
    }
    
    /**
     * Selects the current editor for a given source file in the editor tabbed 
     * pane - if one does not exist, it it created and selected
     * @param sourceFile The SourceFile of an editor which needs to be selected
     * or created
     */
    private void openEditorFor(SourceFile sourceFile) {
        for(int i = 0; i < editorTabbedPane.getTabCount(); i++) {
            Component component = editorTabbedPane.getComponentAt(i);
            if(component instanceof SourceFileEditor) {
                if(sourceFile.equals(((SourceFileEditor) component).getSourceFile())) {
                    editorTabbedPane.setSelectedIndex(i);
                    return;
                }
            }
        }
        
        // SourceFileEditor does not exist - create one and then select it
        
        SourceFileEditor editor = new SourceFileEditor(sourceFile);
        
        editorTabbedPane.addTab(sourceFile.getName(), null, editor, sourceFile.getPath());
        editorTabbedPane.setSelectedIndex(editorTabbedPane.getTabCount()-1);
        
        // Listener which shows a * in the editor tab if changes have been made
        editor.getTextPane().addKeyListener(saveListener);
    }
    
    public void closeAll() {
        for(Component c : editorTabbedPane.getComponents()) {
            editorTabbedPane.remove(c);
            
        }
    }
}