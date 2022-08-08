package frames;

import global.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ToolBar extends JToolBar {
    // attributes
    private static final long serialVersionUID = 1L;

    // components
    private JComboBox<Integer> strokeSizeCombo;
    private JComboBox<String> strokePatternCombo;
    private String[] strokePatternName = {"기본", "점선1", "점선2", "점선3"};
    private List<Float[]> strokePatternList = new ArrayList<>();

    // associations : 같은 레벨의 다른 것과의 연결을 의미
    private DrawingPanel drawingPanel;

    public ToolBar() {
        // components
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionHandler actionHandler = new ActionHandler();
        StrokeChooseHandler strokeChooseHandler = new StrokeChooseHandler();

        for (Constants.ETools eTool : Constants.ETools.values()) {
            JRadioButton toolButton = new JRadioButton(eTool.getLabel());       // 버튼 옆 label 부여
            toolButton.addActionListener(actionHandler);
            toolButton.setActionCommand(eTool.name());      // 해당 버튼에 부여할 액션명령에 대한 명칭을 부여한다.
            toolButton.setIcon(new ImageIcon(eTool.getImage()));        // 기본 아이콘 부여
            toolButton.setSelectedIcon(new ImageIcon(eTool.getSelectedImage()));        // 선택시 반영 아이콘 부여
            toolButton.setToolTipText(eTool.getText());         // 툴팁부여
            toolButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));       // 툴바에 올려두었을 때 커서 변경
            this.add(toolButton);       // toolbar(Container)의 vector에 하나씩 누적 저장된다.
            buttonGroup.add(toolButton);
        }

        JLabel strokeSizeLabel = new JLabel("테두리 크기");
        strokeSizeLabel.setFont(new Font("고딕", Font.BOLD, 13));
        strokeSizeLabel.setToolTipText("도형의 테두리 크기를 1~10으로 설정합니다");
        this.add(strokeSizeLabel);
        this.strokeSizeCombo = new JComboBox<>();
        for(int i=1;i<11; i++) this.strokeSizeCombo.addItem(i);
        this.strokeSizeCombo.addActionListener(strokeChooseHandler);
        this.strokeSizeCombo.setActionCommand("StorkeSize");
        this.add(this.strokeSizeCombo);

        JLabel strokePatternLabel = new JLabel("테두리 유형");
        strokePatternLabel.setFont(new Font("고딕", Font.BOLD, 13));
        strokePatternLabel.setToolTipText("도형의 테두리 유형을 선택합니다");
        this.add(strokePatternLabel);
        this.strokePatternCombo = new JComboBox<>();
        for (String strokeName : this.strokePatternName) this.strokePatternCombo.addItem(strokeName);

        this.strokePatternList.add(null);
        this.strokePatternList.add(new Float[] {4f, 3f});
        this.strokePatternList.add(new Float[] {10f, 4f});
        this.strokePatternList.add(new Float[] {10f, 10f, 1f, 10f});
        this.strokePatternCombo.addActionListener(strokeChooseHandler);
        this.strokePatternCombo.setActionCommand("StrokePattern");
        this.add(this.strokePatternCombo);
    }

    public void associate(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        JRadioButton defaultButton = (JRadioButton) this.getComponent(Constants.ETools.eSelection.ordinal());
        defaultButton.doClick();
    }

    public void initialize() {

    }

    public JComboBox<Integer> getStrokeSizeCombo() {
        return strokeSizeCombo;
    }

    public JComboBox<String> getStrokePatternCombo() {
        return strokePatternCombo;
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            drawingPanel.setSelectedTool(Constants.ETools.valueOf(e.getActionCommand()));       // setSelectedTool은 열거체를 넘겨준다
        }
    }

    class StrokeChooseHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            int index = (int) strokeSizeCombo.getSelectedItem();
            Float[] dash = strokePatternList.get(strokePatternCombo.getSelectedIndex());
            if (dash == null) {
                drawingPanel.setStroke(index, null);
                return;
            }
            float[] floats = new float[dash.length];
            for (int i = 0; i < dash.length; i++) {
                floats[i] = dash[i];
            }
            drawingPanel.setStroke(index, floats);
        }
    }

}
