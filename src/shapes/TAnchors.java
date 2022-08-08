package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class TAnchors implements Serializable {

    private final static int WIDTH = 10;
    private final static int HEIGHT = 10;
    private final static int RRHEIGHT = 40;

    public enum EAnchors {
        eNW, eWW, eSW, eSS, eSE, eEE, eNE, eNN, eRR, eMove;
    }

    private Ellipse2D anchors[];

    public TAnchors() {
        this.anchors = new Ellipse2D[EAnchors.values().length - 1];
        for (int i = 0; i < EAnchors.values().length - 1; i++) {
            this.anchors[i] = new Ellipse2D.Double();
        }
    }

    public EAnchors contains(int x, int y) {
        for (int i = 0; i < EAnchors.values().length - 1; i++) {
            if (this.anchors[i].contains(x, y)) {
                return EAnchors.values()[i];        // 앵커 위에 있으면 앵커를 리턴시켜줘야지
            }
        }
        return null;
    }

    public void draw(Graphics2D graphics2D, Rectangle BoundingRectangle) {
        for (int i = 0; i < EAnchors.values().length - 1; i++) {
            EAnchors eAnchors = EAnchors.values()[i];       // 앵커를 찾아왔다
            // 네모의 위치 세팅. 이것을 for문 바깥으로 빼면 계산이 누적되므로 매번 초기화될 수 있게 for문 안에 넣어준다.
            int x = BoundingRectangle.x - WIDTH / 2;
            int y = BoundingRectangle.y - HEIGHT / 2;
            int w = BoundingRectangle.width;
            int h = BoundingRectangle.height;
            // 각 anchor의 위치 세팅
            switch (eAnchors) {
                case eNW:
                    break;
                case eWW:
                    y = y + h / 2;
                    break;
                case eSW:
                    y = y + h;
                    break;
                case eSS:
                    x = x + w / 2;
                    y = y + h;
                    break;
                case eSE:
                    x = x + w;
                    y = y + h;
                    break;
                case eEE:
                    x = x + w;
                    y = y + h / 2;
                    break;
                case eNE:
                    x = x + w;
                    break;
                case eNN:
                    x = x + w / 2;
                    break;
                case eRR:
                    x = x + w / 2;
                    y = y - RRHEIGHT;
                    break;
                default:
                    break;
            }
            this.anchors[eAnchors.ordinal()].setFrame(x, y, WIDTH, HEIGHT);     // 좌표를 세팅
            Color foreground = graphics2D.getColor();
            graphics2D.setColor(graphics2D.getBackground());
            graphics2D.fill(this.anchors[eAnchors.ordinal()]);
            graphics2D.setColor(foreground);
            graphics2D.draw(this.anchors[eAnchors.ordinal()]);
        }
    }

}
