package transformer;

import shapes.TShape;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Rotator extends Transformer {

    private Point rotatePoint;
    private AffineTransform affineTransform;

    public Rotator(TShape shape) {
        super(shape);
        affineTransform = new AffineTransform();
    }

    @Override
    public void initTransforming(Graphics2D graphics2d, int x, int y) {
        this.oldPoint.x = x;
        this.oldPoint.y = y;
        this.rotatePoint = new Point((int) this.shape.getBounds().getCenterX(), (int) this.shape.getBounds().getCenterY());
    }

    @Override
    public void keepTransforming(Graphics2D graphics2d, int x, int y) {
        graphics2d.setXORMode(graphics2d.getBackground());
        this.shape.draw(graphics2d);
        double rotateAngle = Math.toRadians(computeAngle(this.rotatePoint, this.oldPoint, new Point(x, y)));
        affineTransform.setToRotation(rotateAngle, this.rotatePoint.getX(), this.rotatePoint.getY());       // 계산한 라디안 값을 affineTransform matrix에 적용시킴
        this.oldPoint.x = x;
        this.oldPoint.y = y;
        this.shape.setShape(affineTransform.createTransformedShape(this.shape.getShape()));
        this.shape.draw(graphics2d);
    }

    private double computeAngle(Point centerPoint, Point startPoint, Point endPoint) {      // 점 3개를 갖고, tan(높이/밑변)로 각도를 구한다.
        double startAngle = Math.toDegrees(Math.atan2(centerPoint.getX() - startPoint.getX(), centerPoint.getY() - startPoint.getY()));
        double endAngle = Math.toDegrees(Math.atan2(centerPoint.getX() - endPoint.getX(), centerPoint.getY() - endPoint.getY()));
        double calculatedAngle = startAngle - endAngle;
        if (calculatedAngle < 0) calculatedAngle += 360;
        return calculatedAngle;
    }

    @Override
    public void finishTransforming(Graphics2D graphics2d, int x, int y) {

    }

    @Override
    public void continueTransforming(Graphics2D graphics2d, int x, int y) {

    }
}
