package frames;

import menus.ColorMenu;
import menus.EditMenu;
import menus.FileMenu;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    // attributes
    private static final long serialVersionUID = 1L;

    // components
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private DrawingPanel drawingPanel;
    private ColorMenu colorMenu;

    public MenuBar() {
        // components
        this.fileMenu = new FileMenu("파일");
        this.add(this.fileMenu);

        this.editMenu = new EditMenu("수정");
        this.add(this.editMenu);

        this.colorMenu = new ColorMenu("색상");
        this.add(this.colorMenu);
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.fileMenu.associate(this.drawingPanel);
        this.editMenu.associate(this.drawingPanel);
        this.colorMenu.associate(this.drawingPanel);
    }

    public void initialize() {
        this.fileMenu.initialize();
        this.editMenu.initialize();
        this.colorMenu.initialize();
    }
}
