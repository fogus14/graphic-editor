package shapes;

import java.awt.*;

public class TPolygon extends TShape {

    public TPolygon() {
        this.eDrawingStyle = EDrawingStyle.eNPoints;
        this.shape = new Polygon();
    }

    @Override
    public TShape clone() {
        return new TPolygon();
    }

    @Override
    public void setOrigin(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);
    }

    @Override
    public void resize(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.xpoints[polygon.npoints - 1] = x;
        polygon.ypoints[polygon.npoints - 1] = y;
    }

    @Override
    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.addPoint(x, y);
    }

}
