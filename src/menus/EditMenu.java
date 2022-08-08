package menus;

import frames.DrawingPanel;
import global.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    private DrawingPanel drawingPanel;

    public EditMenu(String editLabel) {
        super(editLabel);       // super는 여기서 JMenu를 일컫는다.

        ActionHandler actionHandler = new ActionHandler();
        for (Constants.EEditMenu eEditMenuItem : Constants.EEditMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eEditMenuItem.getLabel());
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eEditMenuItem.name());
            menuItem.setAccelerator(eEditMenuItem.getKeyStroke());      // 단축키 지정
            menuItem.setToolTipText(eEditMenuItem.getText());
            this.add(menuItem);
        }
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public void undo() {
        this.drawingPanel.undo();
    }

    public void redo() {
        this.drawingPanel.redo();
    }

    public void cut() {
        this.drawingPanel.cut();
    }

    public void copy() {
        this.drawingPanel.copy();
    }

    public void paste() {
        this.drawingPanel.paste();
    }

    public void initialize() {

    }


    class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(Constants.EEditMenu.eUndo.name())) {
                undo();
            } else if (e.getActionCommand().equals(Constants.EEditMenu.eRedo.name())) {
                redo();
            } else if (e.getActionCommand().equals(Constants.EEditMenu.eCut.name())) {
                cut();
            } else if (e.getActionCommand().equals(Constants.EEditMenu.eCopy.name())) {
                copy();
            } else if (e.getActionCommand().equals(Constants.EEditMenu.ePaste.name())) {
                paste();
            }
        }
    }

}
