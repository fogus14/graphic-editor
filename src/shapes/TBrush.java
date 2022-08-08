package shapes;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class TBrush extends TShape {

    private GeneralPath path;

    public TBrush() {
        this.eDrawingStyle = EDrawingStyle.eNPoints;
        this.shape = new GeneralPath();
        this.path = (GeneralPath) this.shape;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (super.lineColor != null) {
            graphics2D.setColor(super.lineColor);
        } else {
            graphics2D.setColor(Color.BLACK);
        }
        if (super.stroke != 0) {
            graphics2D.setStroke(new BasicStroke(super.stroke));
        }
        if (super.dash != null) {
            graphics2D.setStroke(new BasicStroke(super.stroke, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1.0f, super.dash, 0.0f));      // 선이 끝나는 곳의 모양을 사각형으로 + 꼭짓점 모양 날카롭게 설정
        }
        graphics2D.draw(super.shape);

        if (this.bSelected) {
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setColor(Color.black);       // selected되었을 때 black로 설정해야, 앵커가 검은색으로 유지됨
            this.anchors.draw(graphics2D, this.shape.getBounds());
        }
    }

    @Override
    public TShape clone() {
        return new TBrush();
    }

    @Override
    public void setOrigin(int x, int y) {
        this.path.moveTo(x, y);
    }

    @Override
    public void resize(int x, int y) {
        this.path.lineTo(x, y);
    }
}
