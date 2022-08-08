package shapes;

import java.awt.*;
import java.io.Serializable;

abstract public class TShape implements Serializable {
    // attribute : 나의 특성 - 한번 정해지면 잘 안변해
    private static final long serialVersionUID = 1L;

    public enum EDrawingStyle {
        e2Points, eNPoints
    }

    protected Color lineColor, fillColor;
    protected EDrawingStyle eDrawingStyle;
    protected int stroke;
    protected float[] dash;

    // components
    protected Shape shape;
    protected TAnchors anchors;

    // working - 유기적으로 변하는 것
    protected boolean bSelected;
    private TAnchors.EAnchors eSelectedAnchors;

    public TShape() {
        this.bSelected = false;
        this.anchors = new TAnchors();

        this.lineColor = Color.black;
        this.fillColor = null;

        this.stroke = 0;
        this.dash = null;
    }

    public EDrawingStyle getEDrawingStyle() {
        return this.eDrawingStyle;
    }
    public void setEDrawingStyle(EDrawingStyle eDrawingStyle) {
        this.eDrawingStyle = eDrawingStyle;
    }

    public Shape getShape() {
        return shape;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public boolean isSelected() {
        return bSelected;
    }
    public void setSelected(boolean bSelected) {
        this.bSelected = bSelected;
    }

    public TAnchors.EAnchors getSelectedAnchor() {
        return eSelectedAnchors;
    }
    public void setSelectedAnchors(TAnchors.EAnchors eSelectedAnchors) {
        this.eSelectedAnchors = eSelectedAnchors;
    }

    public Rectangle getBounds() {
        return this.shape.getBounds();
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setStroke(int index) {
        this.stroke = index;
    }
    public void setStrokeDash(float[] dash) {
        this.dash = dash;
    }


    public abstract TShape clone();

    public abstract void setOrigin(int x, int y);

    public abstract void resize(int x, int y);

    public void addPoint(int x, int y) {
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (this.fillColor != null) {
            graphics2D.setColor(this.fillColor);
            graphics2D.fill(this.shape);
        }
        if (this.lineColor != null) {
            graphics2D.setColor(this.lineColor);
        } else {
            graphics2D.setColor(Color.BLACK);
        }
        if (this.stroke != 0) {
            graphics2D.setStroke(new BasicStroke(this.stroke));
        }
        if (this.dash != null) {
            graphics2D.setStroke(new BasicStroke(this.stroke, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1.0f, this.dash, 0.0f));         // 선이 끝나는 곳의 모양을 사각형으로 + 꼭짓점 모양 날카롭게 설정
        }

        graphics2D.draw(this.shape);
        if (this.bSelected) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.black);       // selected되었을 때 black로 설정해야, 앵커가 검은색으로 유지됨
            this.anchors.draw(graphics2D, this.shape.getBounds());
        }
    }

    public boolean contains(int x, int y) {
        if (this.bSelected) {
            this.eSelectedAnchors = this.anchors.contains(x, y);
            if (this.eSelectedAnchors != null) return true;        // 앵커위에 이미 올라간 상태
        }
        if (this.shape.contains(x, y)) {
            this.eSelectedAnchors = TAnchors.EAnchors.eMove;
            return true;
        }
        return false;
    }

}
