package uk.ac.hud.postroom.ui.editor;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.assembler.statement.*;

import uk.ac.hud.postroom.ui.*;
import uk.ac.hud.postroom.ui.textpane.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Popup menu which has options for generating and inserting code into a SourceFileEditor
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class QuickCodePopupMenu extends JPopupMenu {
    
    // The Editor which generated code will be added to 
    private SourceFileEditor editor;
    
    /**
     * Constructs a new QuickCodePopupMenu menu for the given editor
     * @param editor Editor which generated code will be added to 
     */
    public QuickCodePopupMenu(SourceFileEditor editor) {
        super("Quick Code");
        
        this.editor = editor;
        
        // Construct Menu items);
        JMenu opCodes = new JMenu("Insert OpCode");
            // OpCode menu items are auto-generated later on
        JMenu statements = new JMenu("Build");
            JMenuItem includeDirective = new JMenuItem("Include directive");
            JMenuItem macro = new JMenuItem("Macro");
            JMenuItem macroCall = new JMenuItem("Macro Call");
            JMenuItem label = new JMenuItem("Label");
            JMenuItem data = new JMenuItem("Data value");
            JMenuItem string = new JMenuItem("String value");
            JMenuItem comment = new JMenuItem("Comment");
                
        // Add menu items
        add(opCodes);
        
            // Loop through OpCodes and create menu items
            for(final OpCode opCode : OpCode.values()) {
                JMenuItem opCodeMenuItem = new JMenuItem(opCode.name());
                opCodeMenuItem.setToolTipText(opCode.getDescription());
                opCodeMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String statement = buildOpCodeStatement(opCode);
                        if(statement != null) {
                            insertTextAtCaret(statement);
                        }
                    }
                });
                
                opCodes.add(opCodeMenuItem);
            }

        add(statements);
            statements.add(includeDirective);
            statements.addSeparator();
            statements.add(macro);
            statements.add(macroCall);
            statements.addSeparator();
            statements.add(label);
            statements.add(data);
            statements.add(string);
            statements.addSeparator();
            statements.add(comment);

        // Add listeners
        
        includeDirective.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildIncludeDirective();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        macro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildMacro();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        macroCall.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildMacroCall();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        label.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildLabel();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        data.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildData();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        string.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildString();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
        
        comment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String statement = buildComment();
                if(statement != null) {
                    insertTextAtCaret(statement);
                }
            }
        });
    }
    
    /**
     * Builds and adds an opcode to the text pane at the current caret position
     * @param opCode OpCode to add
     * @returns Generated OpCode statement (or null if the user cancelled the operation)
     */
    private String buildOpCodeStatement(OpCode opCode) {      
        StringBuilder statement = new StringBuilder(opCode.name());
        
        // Loop to get input for the required operands
        for(int i = 0; i < opCode.getRequiredOperandCount(); i++) {
            String argumentValue = (String) JOptionPane.showInputDialog(
                    this, // owner
                    "Enter operand " + (i + 1), // Message
                    "Command Builder", // Title
                    JOptionPane.PLAIN_MESSAGE);
            
            // If the dialog was cancell / nothing entered return null
            if(argumentValue == null || argumentValue.equals("")) {
                return null;
            }
            
            statement.append(" " + argumentValue);
        }
        
        // Return the statement
        return statement.toString();
    }
    
    /**
     * Builds an include directive
     * @returns The generated statement (or null if the user cancelled the operation)
     */
    private String buildIncludeDirective() {
        SourceFile[] sourceFiles = editor.getSourceFile().getProject().getSourceFiles();
        
        if(sourceFiles.length > 1) {
            SourceFile selectedFile = (SourceFile) JOptionPane.showInputDialog(
                    this, // owner
                    "Select file to include", // Message
                    "Include Directive Builder", // Title
                    JOptionPane.QUESTION_MESSAGE, // Dialog type
                    null, // Icon
                    sourceFiles, // Selection values
                    sourceFiles[0]); // Initial selection
        
            if(selectedFile != null) {
                // Return generated statement
                return("!include " + selectedFile.getName());
            }
            
            return null;
        }else {
            UIUtilities.showErrorDialog(this, "No files available to include");
            
            return null;
        }
    }
    
    /**
     * Builds a Macro Statement (with empty body)
     * @returns Generated macro (or null if the user cancelled the operation)
     */
    private String buildMacro() {
        // Show MacroBuilderDialog
        MacroBuilderDialog macroBuilder = new MacroBuilderDialog();
        macroBuilder.setLocationRelativeTo(this);
        macroBuilder.setVisible(true);
        
        // If macrodialog was closed successfully by the user
        if(macroBuilder.getOption() == JOptionPane.OK_OPTION) {
            // Add the macro to the text pane
            return macroBuilder.getMacroDefinition() + "\n" + 
                    ";enter macro body here " + "\n%";
        }
        
        // Dialog was dismissed
        return null;
    }
    
    /**
     * Builds a MacroCall statement
     * @returns Generated MacroCall statement (or null if the user cancelled the operation)
     */
    private String buildMacroCall() {
        // Assemble the file to get the available macros
        MacroStatement[] macros = new SyntaxAnalyser(editor.getSourceFile()).getMacroStatements();
        
        if(macros.length == 0) {
            // No macros are available
            UIUtilities.showErrorDialog(this, "No macros found");
            
            return null;
        }
        
        // Show a dialog to allow the user to select a macro
        MacroStatement selectedMacro = (MacroStatement) JOptionPane.showInputDialog(
                this, // owner
                "Select macro to use", // Message
                "MacroCall Builder", // Title
                JOptionPane.QUESTION_MESSAGE, // Dialog type
                null, // Icon
                macros, // Selection values
                macros[0]); // Initial selection
            
        // Check the dialog wasn't cancelled
        if(selectedMacro == null) {
            return null;
        }
        
        // Build the macro call statement
        StringBuilder statement = new StringBuilder(selectedMacro.getName());   
                
        // Loop through arguments and show input dialog to get the operand
        for(String argumentLabel : selectedMacro.getArgumentLabels()) {
            String argumentValue = (String) JOptionPane.showInputDialog(
                    this, // owner
                    "Enter operand for argument '" + argumentLabel + "'", // Message
                    "Macro Builder", // Title
                     JOptionPane.PLAIN_MESSAGE);
            
            // If the dialog was cancell / nothing entered return null
            if(argumentValue == null || argumentValue.equals("")) {
                return null;
            }
            
            statement.append(" " + argumentValue);
        }
        
        return statement.toString();
    }
    
    /**
     * Generates a Label statement
     * @returns the generated label statement (or null if the user cancelled the operation)
     */
    private String buildLabel() {
        String label = (String) JOptionPane.showInputDialog(
                this, //owner
                "Enter unique label name", //message
                "Label Builder", //title
                JOptionPane.PLAIN_MESSAGE); // dialog type
        
        if(label == null) {
            // User cancelled the dialog
            return null;
        }
        
        label = label.trim();
        
        if(!label.matches("\\S+")) {
            // Label entered is not valid.  Show error dialog and restart proceedure
            UIUtilities.showErrorDialog(this, "Label names cannot contain spaces " +
                    "or non alphanumeric characters");
            return buildLabel();
        }
        
        return ":" + label + ":";
    }
    
    /**
     * Generates a data statement
     * @returns the generated statement (or null if the user cancelled the operation)
     */
    private String buildData() {        
        String label = buildLabel();
        
        // Check if label was sucessfully made
        if(label != null) {
            // Will show dialog until valid data has been entered
            while(true) {
                String data = (String) JOptionPane.showInputDialog(
                    this, //owner
                    "Enter (numeric) data value", //message
                    "Data Builder", //title
                    JOptionPane.PLAIN_MESSAGE); // dialog type
                
                if(data == null) {
                    // User cancelled dialog - return null
                    return null;
                }
                
                data = data.trim();
                
                if(data.matches("\\d+")) {
                    return label + " (" + data + ")";
                }else {
                    // User entered invalid data, show error message 
                    UIUtilities.showErrorDialog(this, "Please enter numerical values only");
                }
            }
        }
        
        // User cancelled new label dialog
        return null;
    }
    
    /**
     * Generates a new String statement
     * @returns Generated String statement (or null if the user cancelled the operation)
     */
    private String buildString() {        
        String label = buildLabel();
        
        // Check if label was sucessfully made
        if(label != null) {
            String string = (String) JOptionPane.showInputDialog(
                this, //owner
                "Enter the string value", //message
                "String Builder", //title
                JOptionPane.PLAIN_MESSAGE); // dialog type
                
            if(string == null) {
                // User cancelled the dialog
                return null;
            }
            
            return label + " \"" + string.trim() + "\"";
        }
        
        // User cancelled new label dialog
        return null;
    }
    
    /**
     * Builds a comment statement
     * @returns The generated comment (or null if the user cancels the operation)
     */
    private String buildComment() {
        String comment = (String) JOptionPane.showInputDialog(
                this, //owner
                "Enter comment", //message
                "Comment Builder", //title
                JOptionPane.PLAIN_MESSAGE); // dialog type
        
        if(comment != null) {
            return ";" + comment;
        }
        
        // User cancelled the dialog
        return null;
    }
    
    /**
     * Adds the given text to the text pane at the current position of the caret
     * @param string String to add to text pane
     */
    private void insertTextAtCaret(String string) {
        try {
            SourceFileTextPane textPane = editor.getTextPane();
            
            // Add text
            textPane.getDocument().insertString(textPane.getCaretPosition(), string, null);
        }catch (Exception e) {
            UIUtilities.showErrorDialog(this, e.getMessage());
        }
    }
}