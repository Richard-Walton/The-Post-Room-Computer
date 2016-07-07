package uk.ac.hud.postroom.ui.computer;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.ui.table.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
/**
 * Customized ComponentFrame for displaying Register information
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class RegisterFrame extends ComponentFrame {
    
    // Popup menu for changing the visible registers
    private JPopupMenu popupMenu;
    
    // Table Model used in the ComponentFrame table
    private RegisterLogTableModel tableModel;
    
    private ArrayList<JCheckBoxMenuItem> popupMenuItems;
    
    public RegisterFrame(RegisterStore registerStore) {
        super("Registers");
        
        // Sets the table model of the ComponentFrame table
        table.setModel(tableModel = new RegisterLogTableModel(registerStore));
        
        popupMenuItems = new ArrayList<JCheckBoxMenuItem>();
        
        buildPopupMenu();
        
        /* Add popup menu to table and the component under the table for when no data is 
        * in the table */
        table.setComponentPopupMenu(popupMenu);
        table.getParent().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(table, e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Builds the popupmenu which allows different registers to be set visible in
     * the table
     */
    private void buildPopupMenu() {        
        /* Listener which changes the model type using the menu item text
         * The text will always be a Register mnemoic */
        ActionListener changeModelType = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                tableModel.setRegisterVisible(item.getText(), item.isSelected());
                
            }
        };
        
        // All components
        popupMenu = new JPopupMenu("Show");
            final JMenuItem all = new JMenuItem("Show all");
            final JMenuItem none = new JMenuItem("Show none");
        popupMenu.add(all);
        popupMenu.add(none);
        popupMenu.addSeparator();
        
        // Loop adds registers by bank into the menu
        for(int i = 0; i < 5; i++) {
            // News submenu for the bank
            JMenu menu = new JMenu("Bank " + i);
            
            // Gets all registers in current bank i
            for(Register register : Register.getByBank(i)) {
                // Contructs menu item
                JCheckBoxMenuItem item = new JCheckBoxMenuItem(register.getMnemonic());
                item.addActionListener(changeModelType);
                
                menu.add(item);
                popupMenuItems.add(item);
            }
            
            // Adds submenu
            popupMenu.add(menu);
        }
        
        all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Null type = show all
                tableModel.displayAll(true);
                
                for(JCheckBoxMenuItem item : popupMenuItems) {
                    item.setSelected(true);
                }
            }
        });  
        
        none.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Null type = show all
                tableModel.displayAll(false);
                
                for(JCheckBoxMenuItem item : popupMenuItems) {
                    item.setSelected(false);
                }
            }
        });
    }
}