package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by vmaksimo on 12.06.2017.
 */
public class Pen extends Tool {

    Pen(GraphicsContext g){
    super(g);
    }
    @Override
    public void setColor(Color c) {
        gc.setStroke(c);
    }

    @Override
    void setSize(double newSize) {
        gc.setLineWidth(newSize);
    }

    @Override
    void onPress(MouseEvent e) {
        gc.beginPath();
    }

    @Override
    void onDrag(MouseEvent e) {
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }

    @Override
    void onRelease(MouseEvent e) {
        gc.closePath();
    }

    @Override
    public String getName() {
        return "Pen";
    }
}
