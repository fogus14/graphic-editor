package shapes;

import java.awt.*;

public class TTriangle extends TShape {

    private int shiftX, shiftY;

    public TTriangle() {
        this.eDrawingStyle = EDrawingStyle.e2Points;
        this.shape = new Polygon();
    }

    @Override
    public TShape clone() {
        return new TTriangle();
    }

    @Override
    public void setOrigin(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);
        this.shiftX = x;
        this.shiftY = y;
    }

    @Override
    public void resize(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.reset();
        double cx = (this.shiftX + x) / 2;
        double cy = (this.shiftY + y) / 2;
        double angle = Math.toRadians(120);
        polygon.addPoint((this.shiftX + x) / 2, this.shiftY);
        int x1 = this.newPointX((this.shiftX + x) / 2, this.shiftY, cx, cy, angle);
        int y1 = this.newPointY((this.shiftX + x) / 2, this.shiftY, cx, cy, angle);
        polygon.addPoint(x1, y1);
        int x2 = this.newPointX(x1, y1, cx, cy, angle);
        int y2 = this.newPointY(x1, y1, cx, cy, angle);
        polygon.addPoint(x2, y2);
    }

    private int newPointX(double x, double y, double cx, double cy, double angle) {
        int modifiedPoint = (int) ((x - cx) * Math.cos(angle) - (y - cy) * Math.sin(angle) + cx);
        return modifiedPoint;
    }

    private int newPointY(int x, int y, double cx, double cy, double angle) {
        int modifiedPoint = (int) ((x - cx) * Math.sin(angle) + (y - cy) * Math.cos(angle) + cy);
        return modifiedPoint;
    }
}
