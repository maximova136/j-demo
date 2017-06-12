package sample;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by vmaksimo on 12.06.2017.
 */
public class Eraser extends Tool {
    Eraser(GraphicsContext g) {
        super(g);
        size = 20;
    }

    @Override
    void setColor(Color c) {

    }

    @Override
    void setSize(double newSize) {
        size = newSize;
    }


    private Point2D point = null;

    private Image oldImage = null;
    void setImage(Image image){
        oldImage = image;
    }

    @Override
    void onPress(MouseEvent e) {
        gc.setStroke(Color.TRANSPARENT);
        gc.setFill(Color.TRANSPARENT);
        gc.setFillRule(FillRule.EVEN_ODD);

        gc.setLineWidth(15);
        gc.beginPath();

        int xFrom = (int) e.getX();
        int yFrom = (int) e.getY();
        point = new Point2D(xFrom, yFrom);

    }

    @Override
    void onDrag(MouseEvent e) {
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
        int xNow = (int) e.getX();
        int yNow = (int) e.getY();

        // Settings
        double x = e.getX() - size / 2;
        double y = e.getY() - size / 2;

        // Draw in position
        clear(x, y);

        // Draw line between the two points that the event catch
        if (point != null) {
            double x1 = x;
            double y1 = y;
            double x2 = point.getX();
            double y2 = point.getY();
            double m = (y1 - y2) / (x1 - x2);
            double q = y1 - m * x1;
            if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
                if (x1 < x2) {
                    for (int j = (int) Math.round(x1); j <= x2; j++) {
                        int k = (int) Math.round(m * j + q);
                        clear(j, k);
                    }
                } else {
                    for (int j = (int) Math.round(x2); j <= x1; j++) {
                        int k = (int) Math.round(m * j + q);
                        clear(j, k);
                    }
                }
            } else {
                if (y1 < y2) {
                    for (int j = (int) Math.round(y1); j <= y2; j++) {
                        int k;
                        if (x1 == x2) {
                            k = (int) Math.round(x1);
                        } else {
                            k = (int) Math.round((j - q) / m);
                        }
                        clear(k, j);
                    }
                } else {
                    for (int j = (int) Math.round(y2); j <= y1; j++) {
                        int k;
                        if (x1 == x2) {
                            k = (int) Math.round(x1);
                        } else {
                            k = (int) Math.round((j - q) / m);
                        }
                        clear(k, j);
                    }
                }

            }
        }
        point = new Point2D(x, y);
    }

    private void clear(double x, double y) {
        if (oldImage != null){
            gc.drawImage(oldImage,x,y,size,size,x,y,size,size);
        } else {
            gc.clearRect(x, y, size, size);
        }
    }

    @Override
    void onRelease(MouseEvent e) {
        gc.closePath();
    }

    @Override
    String getName() {
        return "Eraser";
    }
}
