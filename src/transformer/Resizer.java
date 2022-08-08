package transformer;

import shapes.TAnchors;
import shapes.TShape;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Resizer extends Transformer {

    private TAnchors.EAnchors anchor;

    public Resizer(TShape shape) {
        super(shape);
    }

    private double getDX(double x, double width) {
        return (x - this.oldPoint.x) / width;
    }
    private double getDY(double y, double height) {
        return (y - this.oldPoint.y) / height;
    }

    @Override
    public void initTransforming(Graphics2D graphics2d, int x, int y) {
        this.oldPoint.x = x;
        this.oldPoint.y = y;
        this.anchor = this.shape.getSelectedAnchor();
    }

    @Override
    public void keepTransforming(Graphics2D graphics2d, int x, int y) {     // 기존도형에서 길이가 몇배 늘어났는 지 파악
        AffineTransform affineTransform = new AffineTransform();
        Rectangle bound = this.shape.getBounds();
        double dx = this.getDX(x, bound.getWidth());
        double dy = this.getDY(y, bound.getHeight());
        switch (this.anchor) {
            case eNW:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), bound.getMinY() + bound.getHeight());
                affineTransform.scale(1 - dx, 1 - dy);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), -(bound.getMinY() + bound.getHeight()));
                break;
            case eWW:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), 0);
                affineTransform.scale(1 - dx, 1);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), 0);
                break;
            case eSW:
                affineTransform.setToTranslation(bound.getMinX() + bound.getWidth(), bound.getMinY());
                affineTransform.scale(1 - dx, 1 + dy);
                affineTransform.translate(-(bound.getMinX() + bound.getWidth()), -(bound.getMinY()));
                break;
            case eSS:
                affineTransform.setToTranslation(0, bound.getMinY());
                affineTransform.scale(1, 1 + dy);
                affineTransform.translate(0, -(bound.getMinY()));
                break;
            case eSE:
                affineTransform.setToTranslation(bound.getMinX(), bound.getMinY());
                affineTransform.scale(1 + dx, 1 + dy);
                affineTransform.translate(-(bound.getMinX()), -(bound.getMinY()));
                break;
            case eEE:
                affineTransform.setToTranslation(bound.getMinX(), 0);
                affineTransform.scale(1 + dx, 1);
                affineTransform.translate(-(bound.getMinX()), 0);
                break;
            case eNE:
                affineTransform.setToTranslation(bound.getMinX(), bound.getMinY() + bound.getHeight());
                affineTransform.scale(1 + dx, 1 - dy);
                affineTransform.translate(-(bound.getMinX()), -(bound.getMinY() + bound.getHeight()));
                break;
            case eNN:
                affineTransform.setToTranslation(0, bound.getMinY() + bound.getHeight());
                affineTransform.scale(1, 1 - dy);
                affineTransform.translate(0, -(bound.getMinY() + bound.getHeight()));
                break;
            default:
                break;
        }
        this.shape.setShape(affineTransform.createTransformedShape(this.shape.getShape()));
        this.oldPoint.x = x;
        this.oldPoint.y = y;
    }

    @Override
    public void finishTransforming(Graphics2D graphics2d, int x, int y) {

    }

    @Override
    public void continueTransforming(Graphics2D graphics2d, int x, int y) {

    }
}
