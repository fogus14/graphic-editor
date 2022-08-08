package frames;

import controller.MyStack;
import global.Constants;
import menus.PopupMenu;
import shapes.TAnchors;
import shapes.TSelection;
import shapes.TShape;
import shapes.TTextBox;
import transformer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

public class DrawingPanel extends JPanel implements Printable {
    // attribute : 속성은 어떤 관점의 값을 가진 것. 전체를 나타내기 위한 값.
    private static final long serialVersionUID = 1L;
    private boolean updated;       // 수정사항 확인용. 수정 <- true / 수정X <- false.
    private boolean drawing;
    private Color lineColor, fillColor;

    // components : 부품.
    private Vector<TShape> shapes;
    private Image bufferedImage;
    private Graphics2D bufferedGraphics2D;

    private boolean front;
    private Vector<TShape> goFront;

    private enum EDrawingState {eIdle, eDrawing, eTransforming;}

    private EDrawingState eDrawingState;
    private Constants.ETools selectedTool;
    private TShape currentShape;
    private TShape selectedShape;
    private Transformer transformer;
    private Cursor rotateCursor;

    private MyStack drawStack;
    private MyStack transformStack;
    private TShape copiedShape;
    private boolean grouping;
    private TShape beforeTransforming;

    private ToolBar toolBar;

    public DrawingPanel(ToolBar toolBar) {
        // attributes
        this.eDrawingState = EDrawingState.eIdle;   // 아무런 상태도 진행중이지 않음을 의미
        this.setBackground(Color.WHITE);
        this.updated = false;
        this.drawing = true;
        this.toolBar = toolBar;
        // components
        this.shapes = new Vector<TShape>();
        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);        // button 센서 감지
        this.addMouseMotionListener(mouseHandler);  // position 위치 감지
        this.addMouseWheelListener(mouseHandler);   // wheel 센서

        this.currentShape = null;
        this.transformer = null;
        this.lineColor = null;
        this.fillColor = null;
        this.drawStack = new MyStack();
        this.transformStack = new MyStack();
        this.copiedShape = null;
        this.grouping = false;
        this.goFront = new Vector<TShape>();

        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Image rotateImage = toolKit.getImage("image/rotate.png");
        this.rotateCursor = toolKit.createCustomCursor(rotateImage, new Point(10, 10), "rotateCursor");
    }

    public void initialize() {
        this.bufferedImage = super.createImage(super.getWidth(), super.getHeight());
        this.bufferedGraphics2D = (Graphics2D) this.bufferedImage.getGraphics();
    }

    public boolean isUpdated() {       // modified의 getter
        return this.updated;
    }
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Object getShapes() {     // saveObject 시 도형들을 write 해주기 위해
        return this.shapes;
    }
    @SuppressWarnings("unchecked")      // 컴파일러한테 의도적으로 형변환한 것임을 명시
    public void setShapes(Object shapes) {      // 파일에서 읽고 있을 때는 어떤 객체일지 모르므로 Object로 둔다.
        this.shapes = (Vector<TShape>) shapes;
        this.repaint();     // file로 부터 받아온 shapes를 순서에 맞춰 다시 paint해준다.
    }

    public void setSelectedTool(Constants.ETools selectedTool) {        // ToolBar로 부터 선택된 eTool을 받아온다.
        this.selectedTool = selectedTool;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        for (TShape shape : this.shapes) {
            if (shape.isSelected()) {
                shape.setLineColor(lineColor);
            }
        }
        this.repaint();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        for (TShape shape : this.shapes) {
            if (shape.isSelected()) {
                shape.setFillColor(fillColor);
            }
        }
        this.repaint();
    }

    public void setStroke(int index, float[] dash) {
        for (TShape shape : this.shapes) {
            if (shape.isSelected()) {
                shape.setStroke(index);
                shape.setStrokeDash(dash);
            }
        }
        this.repaint();
    }

    public void reset() {       // newWindow 수행 시 활용
        this.shapes.clear();
        this.setSelectedTool(selectedTool);
        this.currentShape = null;
        repaint();
    }

    public void undo() {
        if (this.shapes.size() != 0) this.shapes.remove(this.shapes.size() - 1);
        if (!drawing) this.shapes.add(this.transformStack.peek());
        this.repaint();
    }

    public void redo() {
        if (this.drawing) {
            this.shapes.add(this.drawStack.peek());
        } else {
            this.shapes.remove(this.shapes.size() - 1);
            this.shapes.add(this.currentShape);
        }
        this.repaint();
    }

    public void copy() {
        this.copiedShape = this.currentShape.clone();
    }

    public void cut() {
        this.copiedShape = this.currentShape.clone();
        this.shapes.remove(this.currentShape);
        this.repaint();
    }

    public void paste() {
        if (this.copiedShape != null) this.shapes.add((TShape) this.copiedShape.clone());
        repaint();
    }

    public void shapeGoFront(boolean front) {
        this.front = front;
        this.goFront.clear();
        if (this.front) {
            this.shapes.remove(this.currentShape);
            this.goFront.addAll(this.shapes);
            this.goFront.add(this.currentShape);
            this.shapes.clear();
            this.shapes.addAll(this.goFront);
        } else {
            this.goFront.add(this.currentShape);
            this.shapes.remove(this.currentShape);
            this.goFront.addAll(this.shapes);
            this.shapes.clear();
            this.shapes.addAll(this.goFront);
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        this.bufferedImage = super.createImage(super.getWidth(), super.getHeight());
        this.bufferedGraphics2D = (Graphics2D) this.bufferedImage.getGraphics();
        super.paint(this.bufferedGraphics2D);
        for (TShape shape : this.shapes) shape.draw(this.bufferedGraphics2D);
        graphics.drawImage(this.bufferedImage, 0, 0, this);
    }

    private void initTransforming(TShape shape, int x, int y) {
        if (shape == null) {
            this.drawing = true;
            // drawing
            for (TShape tShape : this.shapes) {
                tShape.setSelected(false);
            }
            this.currentShape = this.selectedTool.newShape();       // 새로운 객체를 계속해서 만들면서 가져온다 (vector에 저장할 수 있도록)
            this.currentShape.setStroke((int) this.toolBar.getStrokeSizeCombo().getSelectedItem());
            this.currentShape.setLineColor(lineColor);
            this.currentShape.setFillColor(fillColor);
            this.transformer = new Drawer(this.currentShape);
        } else {
            // transformation
            this.drawing = false;
            this.currentShape = shape;
            this.beforeTransforming = this.currentShape.clone();
            this.transformStack.push(this.beforeTransforming);
            switch (shape.getSelectedAnchor()) {
                case eMove:
                    this.transformer = new Mover(this.currentShape);
                    break;
                case eRR:   // rotate
                    this.transformer = new Rotator(this.currentShape);
                    break;
                default: // resize
                    this.transformer = new Resizer(this.currentShape);
                    break;
            }
        }
        Graphics2D graphics2d = (Graphics2D) this.getGraphics();
        this.transformer.initTransforming(graphics2d, x, y);
        super.repaint();
    }

    private void keepTransforming(int x, int y) {
        Graphics2D graphics2d = (Graphics2D) this.getGraphics();
        this.shapes.forEach(shape -> shape.setSelected(false));
        this.transformer.keepTransforming(this.bufferedGraphics2D, x, y);
        if (this.transformer instanceof Drawer) {
            graphics2d.drawImage(this.bufferedImage, 0, 0, this);
        } else {
            super.repaint();
        }
    }

    private void finishTransforming(int x, int y) {
        Graphics2D graphics2D = (Graphics2D) this.getGraphics();
        graphics2D.setXORMode(this.getBackground());
        this.transformer.finishTransforming(this.bufferedGraphics2D, x, y);
        if (!(this.currentShape instanceof TSelection) && !this.shapes.contains(this.currentShape)) {
            this.shapes.add(this.currentShape);     // 그린 object들을 저장
            System.out.println("hello");
            this.setUpdated(true);
            this.selectedShape = this.currentShape;
            this.selectedShape.setSelected(true);
            this.drawStack.push(this.currentShape);
        }
        this.currentShape.setSelected(true);
        this.repaint();
    }

    private void continueTransforming(int x, int y) {
        Graphics2D graphics = (Graphics2D) this.getGraphics();
        this.transformer.continueTransforming(this.bufferedGraphics2D, x, y);
    }

    private TShape onShape(int x, int y) {
        Vector<TShape> tShapes = this.shapes;
        for (int i = this.shapes.size() - 1; i > -1; i--) {
            TShape shape = tShapes.get(i);      // 각 도형들에게 x,y좌표가 해당 도형 위에 있는지 물어본다.
            if (shape.contains(x, y)) return shape;
        }
        return null;
    }

    private void changeSelection(int x, int y) {
        if (this.selectedShape != null) {
            this.selectedShape.setSelected(false);
        }
        this.repaint();     // 모든 걸 다지우고 새로 그린다.
        // draw anchors
        this.selectedShape = this.onShape(x, y);
        if (this.selectedShape != null) {
            this.selectedShape.setSelected(true);
            this.selectedShape.draw((Graphics2D) this.getGraphics());
        }
    }

    private void setSelected(TShape selectedShape) {        // changeSelection 개념 대용
        for (TShape shape : shapes) shape.setSelected(false);
        selectedShape.setSelected(true);
        this.repaint();
    }

    private void changeCursor(int x, int y) {
        Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        if (this.selectedTool == Constants.ETools.eSelection) {
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);

            this.currentShape = this.onShape(x, y);
            if (this.currentShape != null) {
                cursor = new Cursor(Cursor.MOVE_CURSOR);
                if (this.currentShape.isSelected()) {
                    TAnchors.EAnchors eAnchor = this.currentShape.getSelectedAnchor();
                    switch (eAnchor) {
                        case eRR:
                            cursor = this.rotateCursor;
                            break;
                        case eNW:
                            cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
                            break;
                        case eWW:
                            cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
                            break;
                        case eSW:
                            cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
                            break;
                        case eSS:
                            cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
                            break;
                        case eSE:
                            cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
                            break;
                        case eEE:
                            cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
                            break;
                        case eNE:
                            cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
                            break;
                        case eNN:
                            cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        this.setCursor(cursor);     // DrawingPanel(Component)이 커서를 바꾼다
    }


    private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getClickCount() == 1) {
                    lButtonClick(e);
                } else if (e.getClickCount() == 2) {
                    lButtonDoubleClick(e);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (e.getClickCount() == 1) {
                    rButtonClick(e);
                }
            }
        }

        private void rButtonClick(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            TShape shape = onShape(x, y);
            if (shape != null) {
                PopupMenu popupMenu = new PopupMenu(DrawingPanel.this);
                popupMenu.show(DrawingPanel.this, x, y);
            }
        }

        private void lButtonClick(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            TShape shape = onShape(x, y);
            if (shape == null) {
                if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.eNPoints && eDrawingState == EDrawingState.eIdle) {
                    initTransforming(null, x, y);
                    eDrawingState = EDrawingState.eDrawing;
                }
            } else {
                setSelected(shape);
            }
            if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.eNPoints && eDrawingState == EDrawingState.eDrawing) {
                continueTransforming(x, y);
            }
        }

        private void lButtonDoubleClick(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.eNPoints && eDrawingState == EDrawingState.eDrawing) {
                finishTransforming(x, y);
                eDrawingState = EDrawingState.eIdle;
            }
            if (currentShape instanceof TTextBox) {
                String text = JOptionPane.showInputDialog("텍스트를 입력하세요");
                if (text != null) {
                    ((TTextBox) currentShape).setText(text);
                }
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
//            System.out.println(currentShape);
            int x = e.getX();
            int y = e.getY();
            if (currentShape != null && currentShape.getEDrawingStyle() == TShape.EDrawingStyle.eNPoints && eDrawingState == EDrawingState.eDrawing) {
                keepTransforming(x, y);
            }
            changeCursor(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            TShape shape = onShape(x, y);
            if (eDrawingState == EDrawingState.eIdle) {
                if (shape == null) {
                    currentShape = selectedTool.newShape();
                    if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.e2Points) {
                        initTransforming(null, x, y);
                        eDrawingState = EDrawingState.eDrawing;
                    }
                } else {
                    initTransforming(shape, x, y);
                    eDrawingState = EDrawingState.eTransforming;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (eDrawingState == EDrawingState.eTransforming) {
                keepTransforming(x, y);
            } else if (eDrawingState == EDrawingState.eDrawing) {
                if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.e2Points) {
                    keepTransforming(x, y);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if (eDrawingState == EDrawingState.eTransforming) {
                finishTransforming(x, y);
                eDrawingState = EDrawingState.eIdle;
            } else if (eDrawingState == EDrawingState.eDrawing) {
                if (currentShape.getEDrawingStyle() == TShape.EDrawingStyle.e2Points) {
                    finishTransforming(x, y);
                    eDrawingState = EDrawingState.eIdle;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }
    }


    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex != 0) return Printable.NO_SUCH_PAGE;  // 1
        Graphics2D graphics2d = (Graphics2D) graphics;
        for (TShape shape : this.shapes) shape.draw(graphics2d);
        return Printable.PAGE_EXISTS;   // 0
    }

    public void doPrint() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();     // PrinterJob(인쇄 제어 클래스). PrinterJob 클래스의 Static 메소드를 통해 인스턴스를 얻음.
        printerJob.setPrintable((Printable) this);      // 프린트를 위한 렌더링 대상 setting
        boolean isPrinted = printerJob.printDialog();   // 프린트 대화 상자 띄우기
        if (isPrinted) {    // 사용자가 대회 상자를 취소하지 X -> true 조건
            try {
                printerJob.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "프린트 에러가 발생했습니다. 다시 시도해주세요.");
            }
        }
    }
}
