package menus;

import frames.DrawingPanel;
import global.Constants;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class PopupMenu extends JPopupMenu {
    private PopupHandler popupHandler;
    private DrawingPanel drawingPanel;

    public PopupMenu(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.popupHandler = new PopupHandler();

        for (Constants.EPopupMenu ePopupMenu : Constants.EPopupMenu.values()) {
            JMenuItem menuItem = new JMenuItem(ePopupMenu.getTitle());
            menuItem.setActionCommand(ePopupMenu.getActionCommand());
            menuItem.addActionListener(this.popupHandler);
            this.add(menuItem);
        }
        this.addPopupMenuListener(new PopupPrintListener());
    }

    public void cut() {
        this.drawingPanel.cut();
    }

    public void shapeGoFront() {
        this.drawingPanel.shapeGoFront(true);
    }

    public void shapeGoBack() {
        this.drawingPanel.shapeGoFront(false);
    }

    private void invokeMethod(String methodName) {
        try {
            this.getClass().getMethod(methodName).invoke(this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private class PopupPrintListener implements PopupMenuListener {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

        }
        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

        }
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {

        }
    }

    private class PopupHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String methodName = e.getActionCommand();
            invokeMethod(methodName);
        }
    }
}
