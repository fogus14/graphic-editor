package shapes;

import java.awt.*;

public class TRectangle extends TShape {

    public TRectangle() {
        this.eDrawingStyle = EDrawingStyle.e2Points;
        this.shape = new Rectangle();
    }

    @Override
    public TShape clone() {
        return new TRectangle();
    }

    @Override
    public void setOrigin(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;       // shape를 rectangle로 다운 캐스팅
        rectangle.setFrame(x, y, 0, 0);
    }

    @Override
    public void resize(int x, int y) {
        Rectangle rectangle = (Rectangle) this.shape;
        rectangle.setSize(x - rectangle.x, y - rectangle.y);
    }

}
