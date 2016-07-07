package uk.ac.hud.postroom.ui.tree;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.assembler.*;
import uk.ac.hud.postroom.exception.*;
import uk.ac.hud.postroom.assembler.statement.*;

import javax.swing.tree.*;

/**
 * A Tree representation using the results of a SyntaxAnalsis on a SourceFile
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class SyntaxTreeModel extends DefaultTreeModel { 
    // Syntax Analyser which provides the results to display
    private SyntaxAnalyser syntaxAnalyser;
    
    /**
     * Constructs new Tree representation of the given TwoAddressSyntaxAnalyser
     * @param syntaxAnalyser to display in Tree format
     */
    public SyntaxTreeModel(SyntaxAnalyser syntaxAnalyser) {
        // Super-class (DefaultTreeModel) constructor
        super(new DefaultMutableTreeNode(syntaxAnalyser.getSourceFile().getName()));
        
        this.syntaxAnalyser = syntaxAnalyser;
        
        /* Builds the tree model:
         * GeneralInfo
         * -- Included Files
         * -- Lables
         * -- Macros
         * -- Errors
         */
        buildFileInfoSubTree();
        buildIncludedFileSubTree();
        buildLabelSubTree();
        buildMacroSubTree();
        buildErrorSubTree();                                                                                                                                                                     
    }
    
    /**
     * Builds and adds FileInfo to the tree
     */
    private void buildFileInfoSubTree() {
        SourceFile sourceFile = syntaxAnalyser.getSourceFile();
        
        DefaultMutableTreeNode rootNode = getRoot();
        
        rootNode.add(new DefaultMutableTreeNode(
                "Project: " + sourceFile.getParentFile().getName()));
        rootNode.add(new DefaultMutableTreeNode(
                "Line count: " + syntaxAnalyser.getLineCount()));  
    }
    
    /**
     * Builds and adds IncludeDirective to the tree
     */
    private void buildIncludedFileSubTree() {
        IncludeDirective[] statements = syntaxAnalyser.getIncludeStatements();
        
        AlwaysBranchNode includeNode = new AlwaysBranchNode(
                "Included files (" + statements.length + ")");
        for(IncludeDirective statement : statements) {
           includeNode.add(new IncludeNode(statement));
        }
        
        getRoot().add(includeNode);
    }
    
    /**
     * Builds and adds Labels to the tree
     */
    private void buildLabelSubTree() {
        LabelStatement[] statements = syntaxAnalyser.getLabelStatements();
        
        AlwaysBranchNode labelNode = new AlwaysBranchNode(
                "Labels (" + statements.length + ")");
        for(LabelStatement statement : statements) {
           labelNode.add(new LabelNode(statement));
        }
        
        getRoot().add(labelNode);
    }
    
    /**
     * Builds and adds Macros to the tree
     */
    private void buildMacroSubTree() {
        MacroStatement[] statements = syntaxAnalyser.getMacroStatements();
        
        AlwaysBranchNode macroNode = new AlwaysBranchNode(
                "Macros (" + statements.length + ")");
        for(MacroStatement statement : statements) {
           macroNode.add(new MacroNode(statement));
        }
        
        getRoot().add(macroNode);
    }
    
    /**
     * Builds and adds any Syntax errors to the tree
     */
    private void buildErrorSubTree() {
        SyntaxError[] errors = syntaxAnalyser.getErrors();
        
        AlwaysBranchNode errorNode = new AlwaysBranchNode(
                "Errors (" + errors.length + ")");
        for(SyntaxError error : errors) {
           errorNode.add(new ErrorNode(error));
        }
        
        getRoot().add(errorNode);
    }
    
    /** @inheritDoc **/
    @Override public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) super.getRoot();
    }
}