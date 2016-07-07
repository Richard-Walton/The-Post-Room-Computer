package uk.ac.hud.postroom.ui.table;

import uk.ac.hud.postroom.*;
import uk.ac.hud.postroom.computer.*;
import uk.ac.hud.postroom.event.*;
import java.util.*;

/**
 * TabelModel which stores the state of the RegisterStore
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class RegisterLogTableModel extends ComputerLogTableModel {;
    
    // List of register history, regardless of model type
    private List<String[]> allData;
    
    private List<String> visibleItems;
    
    /**
     * Constructs a new RegisterLogTableModel for the given RegisterStore
     * @param registers RegisterStore to be logged
     */
    public RegisterLogTableModel(RegisterStore registers) {   
        super(new String[] { "IOType", "Register", "Value" }, registers.getComputer());
        
        // Default model type = all
        visibleItems = new ArrayList<String>();
        allData = new ArrayList<String[]>();
        
        displayAll(true);
        
        // listen for RegisterStore events and add to data model
        registers.addRegisterListener(new RegisterListener() {
            public void registerReadFrom(RegisterStore registers, Register register, String value) {
                String[] data = new String[] { "Read", register.getMnemonic(), value};
                allData.add(data);
                
                update(data);
            }

            public void registerWroteTo(RegisterStore registers, Register register, String value) {
                String[] data = new String[] { "Write", register.getMnemonic(), value};
                allData.add(data);
                
                update(data);
            }
        });
        
        registers.getComputer().addComputerListener(new ComputerAdapter() {
            public void computerReset(Computer computer) {
                allData.clear();
            }
        });
    }
        
    /**
     * Adds the data to the visible model if it matches the model type
     * @param data Data to add to visible model type
     */
    private void update(String[] data) {     
        if(visibleItems.contains(data[1])) {
            dataModel.add(data);
            
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
        }
    }
    
    public void displayAll(boolean displayAll) {
        visibleItems.clear();
        dataModel.clear();
        
        if(displayAll){
            for(Register register : Register.values()) {
                visibleItems.add(register.getMnemonic());
            }
        }
        
        ListIterator<String[]> list = allData.listIterator();
        while(list.hasNext()) {
            update(list.next());
        }
        
        fireTableDataChanged();
    }
    
    public void setRegisterVisible(String register, boolean visible) {
        if(visible) {
            visibleItems.add(register);
        }else {
            visibleItems.remove(register);
        }
        
        dataModel.clear();
        // Use list iterator to avoid concurrent modification errors
        ListIterator<String[]> list = allData.listIterator();
        while(list.hasNext()) {
            update(list.next());
        }
    }
}