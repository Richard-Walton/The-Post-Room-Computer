package uk.ac.hud.postroom.ui.tabbedpane;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Custom JTabbedPane tab component which can close itself
 * @author Richard Walton (c0410542@uk.ac.hud)
 */
public class CloseableTabComponent extends JPanel {
    // The TabbedPane which this component is added to
    private JTabbedPane owner;
    
    // The label which shows the title and/or icon
    private JLabel titleLabel;
    
    // The button which closes the tab
    private CloseButton closeButton;
    
    // Popupmenu which contains additional functionality
    private JPopupMenu popupMenu;
    
    // ActionListener which closes the tab
    private ActionListener closeAction;
    
    /**
     * Constructs a new CloseableTabComponent 
     * @param title The title of the tab component (can be null)
     * @param icon The icon to show in the tab component (can be null)
     * @param toolTip The tooltip to show (can be null)
     * @param owner The JTabbedPane this component will is added to
     */
    public CloseableTabComponent(String title, Icon icon, String toolTip, JTabbedPane owner) {
        super(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        this.owner = owner;
        
        titleLabel = new JLabel();
        titleLabel.setText(title);
        titleLabel.setIcon(icon);
        titleLabel.setToolTipText(toolTip);
        closeButton = new CloseButton();    
        popupMenu = new JPopupMenu();
        
        closeAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTabbedPane owner = getOwner();
                owner.remove(owner.indexOfTabComponent(CloseableTabComponent.this));
            }
        };
        
        buildPopupMenu();
        buildUI();
    }
    
    /**
     * Returns the TabbedPane which this tabComponent is contained in
     * @return the TabbedPane which this tabComponent is contained in
     */
    public JTabbedPane getOwner() {
        return owner;
    }
    
    /**
     * Returns the title label which shows the title and/or icon of this tab component
     * @return the title label
     */
    public JLabel getTitleLabel() {
        return titleLabel;
    }
    
    /**
     * Returns the button which closes the tab
     * @return the button which closes the tab
     */
    public JButton getCloseButton() {
        return closeButton;
    }
    
    /**
     * Returns the tab component right click menu
     * @return the tab component right click menu
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
    
    /**
     * Sets the titleLabel text
     * @param title Text to display
     */
    public void setTitle(String title) {
        getTitleLabel().setText(title);
    }
    
    /**
     * Builds the tab component
     */
    private void buildUI() {
        setFocusable(false);
        setOpaque(false);
        
        add(titleLabel);
        add(Box.createHorizontalStrut(7));
        add(closeButton);
        
        titleLabel.setComponentPopupMenu(popupMenu);
        
        // change tab on mouse click
        titleLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!SwingUtilities.isRightMouseButton(e)) {
                    JTabbedPane owner = getOwner();
                    owner.setSelectedIndex(owner.indexOfTabComponent(
                            CloseableTabComponent.this));
                }
            }
        });
        
        closeButton.addActionListener(closeAction);
    }
    
    /**
     * Builds right click menu
     */
    private void buildPopupMenu() {
        // Construct components
        popupMenu = new JPopupMenu();
            JMenuItem closeTab = new JMenuItem("Close tab");
            JMenuItem closeAllTabs = new JMenuItem("Close all tabs");
            JMenuItem closeOtherTabs = new JMenuItem("Close other tabs");
            
        // Add components to menu
        popupMenu.add(closeTab);
        popupMenu.add(closeAllTabs);
        popupMenu.addSeparator();
        popupMenu.add(closeOtherTabs);
        
        // Add action listeners
        closeTab.addActionListener(closeAction);
        
        closeAllTabs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                getOwner().removeAll();
            }
        });
        
        closeOtherTabs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JTabbedPane owner = getOwner();
                int tabCount = owner.getTabCount();
                for(int i = 0;i < tabCount; i++){
                    if(owner.getTabComponentAt(i) != CloseableTabComponent.this) {
                        owner.remove(i);
                        i--;
                        tabCount--;
                    }
                }
            }
        });
    }
}