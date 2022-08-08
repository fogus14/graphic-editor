package shapes;


import java.awt.geom.Line2D;

public class TLine extends TShape {

    public TLine() {
        this.eDrawingStyle = EDrawingStyle.e2Points;
        this.shape = new Line2D.Float();
    }

    @Override
    public TShape clone() {
        return new TLine();
    }

    @Override
    public void setOrigin(int x, int y) {
        Line2D line = (Line2D) this.shape;
        line.setLine(x, y, x, y);
    }

    @Override
    public void resize(int x, int y) {
        Line2D line = (Line2D) this.shape;
        line.setLine(line.getX1(), line.getY1(), x, y);
    }

}
