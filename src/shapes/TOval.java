package shapes;


import java.awt.geom.Ellipse2D;

public class TOval extends TShape {

    public TOval() {
        this.eDrawingStyle = EDrawingStyle.e2Points;
        this.shape = new Ellipse2D.Float();
    }

    @Override
    public TShape clone() {
        return new TOval();
    }

    @Override
    public void setOrigin(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.shape;
        ellipse.setFrame(x, y, 0, 0);
    }

    @Override
    public void resize(int x, int y) {
        Ellipse2D ellipse = (Ellipse2D) this.shape;
        ellipse.setFrame(ellipse.getX(), ellipse.getY(), x - ellipse.getX(), y - ellipse.getY());
    }

}
