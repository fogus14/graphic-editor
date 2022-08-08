package menus;

import frames.DrawingPanel;
import global.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorMenu extends JMenu {

    private DrawingPanel drawingPanel;

    public ColorMenu(String colorLabel) {
        super(colorLabel);       // super는 여기서 JMenu를 일컫는다.

        ActionHandler actionHandler = new ActionHandler();
        for (Constants.EColorMenu eColorMenu : Constants.EColorMenu.values()) {
            JMenuItem menuItem = new JMenuItem(eColorMenu.getLabel());
            menuItem.addActionListener(actionHandler);
            menuItem.setActionCommand(eColorMenu.name());   // 이 버튼에 의해 발생하는 이벤트에 대한 명령 이름을 설정
            menuItem.setAccelerator(eColorMenu.getKeyStroke());
            menuItem.setToolTipText(eColorMenu.getText());
            this.add(menuItem);
        }
    }

    public void setLineColor() {
        Color selectedColor = JColorChooser.showDialog(drawingPanel, Constants.EColorMenu.eLineColor.getLabel(), this.drawingPanel.getForeground());
        this.drawingPanel.setLineColor(selectedColor);
    }

    public void setFillColor() {
        Color selectedColor = JColorChooser.showDialog(drawingPanel, Constants.EColorMenu.eFillColor.getLabel(), this.drawingPanel.getForeground());
        this.drawingPanel.setFillColor(selectedColor);
    }

    public void setColorReset() {
        this.drawingPanel.setLineColor(Color.black);
        this.drawingPanel.setFillColor(new Color(0f, 0f, 0f, 0f));
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public void initialize() {

    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(Constants.EColorMenu.eLineColor.name())) {
                setLineColor();
            } else if (e.getActionCommand().equals(Constants.EColorMenu.eFillColor.name())) {
                setFillColor();
            } else if (e.getActionCommand().equals(Constants.EColorMenu.eColorReset.name())) {
                setColorReset();
            }
        }
    }
}
