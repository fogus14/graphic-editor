package shapes;

import java.awt.geom.RoundRectangle2D;

public class TRoundRectangle extends TShape {
    private final double ARCWIDTH = 40;
    private final double ARCHEIGHT = 40;

    public TRoundRectangle() {
        this.eDrawingStyle = EDrawingStyle.e2Points;
        this.shape = new RoundRectangle2D.Float();
    }

    @Override
    public TShape clone() {
        return new TRoundRectangle();
    }

    @Override
    public void setOrigin(int x, int y) {
        RoundRectangle2D roundRectangle = (RoundRectangle2D) this.shape;
        roundRectangle.setRoundRect(x, y, 0, 0, this.ARCWIDTH, this.ARCHEIGHT);
    }

    @Override
    public void resize(int x, int y) {
        RoundRectangle2D roundRectangle = (RoundRectangle2D) this.shape;
        roundRectangle.setRoundRect(roundRectangle.getX(), roundRectangle.getY(), x - roundRectangle.getX(), y - roundRectangle.getY(),
                this.ARCWIDTH, this.ARCHEIGHT);
    }
}
