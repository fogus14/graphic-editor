package transformer;

import shapes.TShape;

import java.awt.*;
import java.io.Serializable;

public abstract class Transformer implements Serializable {
    protected TShape shape;
    protected Point oldPoint;

    public Transformer(TShape shape) {
        this.shape = shape;
        this.oldPoint = new Point(0, 0);
    }

    public abstract void initTransforming(Graphics2D graphics2d, int x, int y);

    public abstract void keepTransforming(Graphics2D graphics2d, int x, int y);

    public abstract void finishTransforming(Graphics2D graphics2d, int x, int y);

    public abstract void continueTransforming(Graphics2D graphics2d, int x, int y);
}
