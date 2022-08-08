package shapes;

import java.awt.*;

public class TTextBox extends TRectangle {

    private static final long serialVersionUID = 1L;

    private String text;
    private Font font;

    public TTextBox() {
        this.text = "Please enter it!";
        this.font = new Font("default font", Font.PLAIN, 16);
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public TShape clone() {
        return new TTextBox();
    }

    @Override
    public void draw(Graphics2D graphics2d) {
        graphics2d.setStroke(new BasicStroke(1));
        graphics2d.setFont(this.font);
        if (this.getBounds().getMaxX() - this.getBounds().getMinX() > 1) {
            graphics2d.drawString(this.text, (int) this.getBounds().getCenterX(), (int) this.getBounds().getCenterY());
            if (this.bSelected) this.anchors.draw(graphics2d, this.shape.getBounds());
        }
    }
}
