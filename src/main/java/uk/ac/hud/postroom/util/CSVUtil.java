package uk.ac.hud.postroom.util;

import java.io.*;
import javax.swing.table.*;

/**
 * Utility class - saves a table model to a comma separated variable file
 * @author Richard Walton (c0410542@hud.ac.uk)
 */
public class CSVUtil {
    
    /** Singleton class - private constructor **/
    private CSVUtil() {}

    /**
     * Saves a tabel model to a comma separated variable file
     * @param file File to save data to
     * @param data Data to save
     * @throws java.io.IOException thrown if an IOException occurs
     */
    public static void saveCSVFile(File file, TableModel data) throws IOException {
        StringBuilder dataBuffer = new StringBuilder();
        
        // Gets the column headers
        for(int columnIndex = 0; columnIndex < data.getColumnCount(); columnIndex++) {
            dataBuffer.append(data.getColumnName(columnIndex) + ",");
        }
        
        dataBuffer.append("\n");
        
        // Gets the table data
        for(int row = 0; row < data.getRowCount(); row++) {
            for(int column = 0; column < data.getColumnCount(); column++) {
                dataBuffer.append(data.getValueAt(row, column) + ",");
            }
            
            dataBuffer.append("\n");
        }
        
        // Writes the data to the specified file
        FileOutputStream output = new FileOutputStream(file);
        output.write(dataBuffer.toString().getBytes());
        output.close();
    }
}