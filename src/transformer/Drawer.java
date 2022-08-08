package transformer;

import shapes.TShape;

import java.awt.*;

public class Drawer extends Transformer {
    public Drawer(TShape shape) {
        super(shape);
    }

    @Override
    public void initTransforming(Graphics2D graphics2d, int x, int y) {
        this.shape.setOrigin(x, y);
        this.shape.draw(graphics2d);
    }

    @Override
    public void keepTransforming(Graphics2D graphics2d, int x, int y) {
        graphics2d.setXORMode(graphics2d.getBackground());
        this.shape.draw(graphics2d);
        this.shape.resize(x, y);
        this.shape.draw(graphics2d);
    }

    @Override
    public void finishTransforming(Graphics2D graphics2d, int x, int y) {
        this.shape.setShape(this.shape.getShape());
    }

    @Override
    public void continueTransforming(Graphics2D graphics2d, int x, int y) {
        this.shape.addPoint(x, y);
    }
}
