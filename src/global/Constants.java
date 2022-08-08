package global;

import shapes.*;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Constants {
    public enum ETools {        // 순서를 바꾸면 toolbar(상위...container)에 누적적으로 저장되므로 출력되는 순서가 달라진다. 똑같은 것 하나 추가 시 하나 더 생기겟지...
        eSelection("선택", "Drawing panel의 도형 선택하기", "image/select.png", "image/selected.png", new TSelection()),
        eRectangle("네모", "Rectangle 선택하기", "image/rectangle.png", "image/rectangle_selected.png", new TRectangle()),
        eOval("동그라미", "Oval 선택하기", "image/ellipse.png", "image/ellipse_selected.png", new TOval()),
        eLine("라인", "Line 선택하기", "image/line.png", "image/line_selected.png", new TLine()),
        ePolygon("폴리곤", "Polygon 선택하기", "image/polygon.png", "image/polygon_selected.png", new TPolygon()),
        eRoundRectangle("둥근 네모", "RoundRectangle 선택하기", "image/roundRectangle.png", "image/roundRectangle_selected.png", new TRoundRectangle()),
        eBrush("펜 그리기", "Pen 선택하기", "image/pen.png", "image/pen_selected.png", new TBrush()),
        eTextBox("글상자", "TextBox 선택하기", "image/text.png", "image/text_selected.png", new TTextBox()),
        eTriangle("세모", "Triangle 선택하기", "image/triangle.png", "image/triangle_selected.png", new TTriangle());

        private String label;
        private String text;        // 툴팁에 활용
        private String image;       // 선택전 기본 이미지
        private String selectedImage;       // 선택되었을 때 이미지
        private TShape tool;

        private ETools(String label, String text, String image, String selectedImage, TShape tool) {
            this.label = label;
            this.text = text;
            this.image = image;
            this.selectedImage = selectedImage;
            this.tool = tool;
        }

        public String getLabel() {
            return this.label;
        }

        public String getText() {
            return this.text;
        }

        public String getImage() {
            return image;
        }

        public String getSelectedImage() {
            return selectedImage;
        }

        public TShape newShape() {
            return this.tool.clone();
        }
    }


    public enum EFileMenu {
        eNew("새로만들기", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "새 그림을 만듭니다."),
        eOpen("열기", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "기존 그림을 엽니다."),
        eSave("저장하기", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "현재 그림을 저장합니다."),
        eSaveAs("다른이름으로저장하기", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "현재 그림을 새 파일로 저장합니다."),
        //        eClose("닫기"),
        ePrint("프린트하기", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), "현재 그림을 인쇄합니다."),
        eQuit("종료하기", KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK), "프로그램을 종료합니다.");

        private String label;
        private KeyStroke keyStroke;
        private String text;

        private EFileMenu(String label, KeyStroke keyStroke, String text) {
            this.label = label;
            this.keyStroke = keyStroke;
            this.text = text;
        }

        public String getLabel() {
            return this.label;
        }

        public KeyStroke getKeyStroke() {
            return keyStroke;
        }

        public String getText() {
            return text;
        }
    }


    public enum EEditMenu {
        eUndo("undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "되돌리기"),
        eRedo("redo", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "다시 실행"),
        eCut("cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "오려두기"),
        eCopy("copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "복사하기"),
        ePaste("paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "붙이기"),
        eGroup("group", KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), "그룹 설정"),
        eUnGroup("ungroup", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "그룹 해제");

        private String label;
        private KeyStroke keyStroke;
        private String text;

        private EEditMenu(String label, KeyStroke keyStroke, String text) {
            this.label = label;
            this.keyStroke = keyStroke;
            this.text = text;
        }

        public String getLabel() {
            return label;
        }

        public KeyStroke getKeyStroke() {
            return keyStroke;
        }

        public String getText() {
            return text;
        }
    }


    public enum EFileConstant {
        eFolderPath("myDrawingResult");

        private String constant;

        private EFileConstant(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public enum EColorMenu {
        eLineColor("라인 색", KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "라인 색 설정"),
        eFillColor("색 채우기", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), "색 체우기"),
        eColorReset("색상 초기화", KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_DOWN_MASK), "색상 초기화");

        private String label;
        private KeyStroke keyStroke;
        private String text;

        private EColorMenu(String title, KeyStroke keyStroke, String text) {
            this.label = title;
            this.keyStroke = keyStroke;
            this.text = text;
        }

        public String getLabel() {
            return this.label;
        }

        public KeyStroke getKeyStroke() {
            return keyStroke;
        }

        public String getText() {
            return this.text;
        }
    }

    public enum EPopupMenu {
        eCut("자르기", "cut"),
        eFront("도형을 맨 앞으로 이동", "shapeGoFront"),
        eBack("도형을 맨 뒤로 이동", "shapeGoBack");

        private String title;
        private String actionCommand;

        EPopupMenu(String title, String actionCommand) {
            this.title = title;
            this.actionCommand = actionCommand;
        }

        public String getTitle() {
            return title;
        }

        public String getActionCommand() {
            return actionCommand;
        }
    }


}

