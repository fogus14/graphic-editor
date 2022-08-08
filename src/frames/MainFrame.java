package frames;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    // atrributes
    private static final long serialVersionUID = 1L;

    // components
    private DrawingPanel drawingPanel;
    private MenuBar menuBar;
    private ToolBar toolBar;

    public MainFrame() {
        this.setTitle("My Drawing Program_정래현");       // 프로그램 제목 설정
        ImageIcon img = new ImageIcon("image/title.png");       // 대표 아이콘 설정
        this.setIconImage(img.getImage());

        // attributes
        this.setSize(1200, 900);     // 자기의 속성은 자기 내부에서 정리해야 함. 외부로 빼서 시키면 안됨.
        this.setLocationRelativeTo(null);       // MainFrame 화면 중앙에 띄우기
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // components
        BorderLayout layoutManager = new BorderLayout();
        this.setLayout(layoutManager);   // 순서대로 레이아웃을 짠다. 일반적으로 글씨쓰는 방식.

        // 아래 두줄은 aggregation hierarchy의 기본 공식
        this.menuBar = new MenuBar();       // (메뉴바를) 만들어서
        this.setJMenuBar(this.menuBar);     // (메뉴바를) 같다붙인다.

        this.toolBar = new ToolBar();
        this.add(this.toolBar, BorderLayout.NORTH);

        this.drawingPanel = new DrawingPanel(this.toolBar);
        this.add(this.drawingPanel, BorderLayout.CENTER);

        // association : 자식과 자식과의 관계를 만들어주는 것. (자식-자식만 연결해주어야함에 유의!)
        this.menuBar.associate(this.drawingPanel);
        this.toolBar.associate(this.drawingPanel);
    }

    public void initialize() {
        this.menuBar.initialize();
        this.toolBar.initialize();
        this.drawingPanel.initialize();
    }

}
