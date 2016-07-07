package uk.ac.hud.postroom.ui;

import uk.ac.hud.postroom.ui.textpane.*;
import uk.ac.hud.postroom.ui.styleeditor.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Custom table for viewing / editting font options
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class StyleEditorDialog extends JDialog {
    // Font list
    private JComboBox fontList;
    
    // Size list
    private JComboBox sizeList;
    
    // Table of edittable options
    private JTable styleTable;
    
    // Sample output
    private SourceFileTextPane sampleEditor;
    
    /**
     * Constructs a new StyleEditorDialog
     */
    public StyleEditorDialog() {
        setTitle("Font and Colour chooser");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        buildFontPanel();
        buildOptionTable();
        buildSampleEditor();
        buildExitButton();
        
        // Updates the selected font
        ItemListener fontChangeListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e){
                Font font = new Font((String) fontList.getSelectedItem(),
                        Font.PLAIN,
                        (Integer) sizeList.getSelectedItem());
                                       
                EditorStyles.setFont(font);
                
                sampleEditor.getStyledDocument().recolour();
            }
        };
        
        // Add listener
        fontList.addItemListener(fontChangeListener);
        sizeList.addItemListener(fontChangeListener);
        
        pack();
        setModal(true);
        setResizable(false);
    }
    
    /**
     *  Builds and adds the font list / size comboboxs' to the dialog
     */
    private void buildFontPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        add(panel);
            panel.add(new JLabel("Font:"));
            panel.add(fontList = new FontListComboBox());
            panel.add(new JLabel("Size:"));
            panel.add(sizeList = new FontSizeComboBox());
            
        fontList.setSelectedItem(EditorStyles.getFont().getFamily());
        sizeList.setSelectedItem(EditorStyles.getFont().getSize());
            
        panel.setBorder(BorderFactory.createTitledBorder("General"));
    }
    
    /**
     * Builds and adds the option table to the dialog
     */
    private void buildOptionTable() {
        JScrollPane scrollPane = new JScrollPane(styleTable = new StyleTable());
        
        add(scrollPane);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Options"));
        
        styleTable.getModel().addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent e){
                sampleEditor.getStyledDocument().recolour();               
            }
        });
    }
    
    /**
     * Builds and adds the preview panel
     */
    private void buildSampleEditor() {
        JScrollPane scrollPane = new JScrollPane(
                sampleEditor = new SourceFileTextPane());
        
        add(scrollPane);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Preview"));
        
        sampleEditor.setEditable(false);
        
        // Add sample text
        sampleEditor.setText(
                "; Preview sample \n" +
                "!include file.pca\n" +
                "%macro a b\n" +
                "  add a b\n" +
                "  sub a b\n" + "%\n" +
                "inp :x:\n" +
                "macro :x: :x:\n" +
                "jmp nvr :x:\n" +
                "out :x:\n" + 
                "hlt\n" +
                ":x: (0)\n" +
                ":s: \"string\"");
        
        scrollPane.setPreferredSize(new Dimension(350, 200));
    }
    
    /**
     * Builds and adds the exit button to the dialog
     */
    private void buildExitButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton close = new JButton("Close");
            
        add(panel);
            panel.add(close);
            
        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
    }
    
    /**
     * Constructs and shows a new SytleEditorDialog 
     * @param parent Parent of the dialog
     */
    public static void showDialog(Component parent) {
        StyleEditorDialog editor = new StyleEditorDialog();
        editor.setLocationRelativeTo(parent);
        editor.setVisible(true);
    }
}