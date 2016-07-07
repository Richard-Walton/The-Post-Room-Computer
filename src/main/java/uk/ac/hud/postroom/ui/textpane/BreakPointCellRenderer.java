/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.hud.postroom.ui.textpane;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Richard Walton (walton909@gmail.com)
 */
public class BreakPointCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean hasFocus) {
        Component cell = super.getListCellRendererComponent(list, value, index, selected, hasFocus);
        if(selected) cell.setBackground(Color.RED);
        
        return cell;
    }

}
