package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by vmaksimo on 12.06.2017.
 */
public abstract class Tool {
    static Color color = null;
    abstract void setColor(Color c);
    static double size = 0;
    abstract void setSize(double newSize);
    static GraphicsContext gc = null;
    Tool(){

    }
    Tool(GraphicsContext g){
        gc = g;
    }
    public void setGc(GraphicsContext g){
        gc =g;
    }

    abstract void onPress(MouseEvent e);
    abstract void onDrag(MouseEvent e);
    abstract void onRelease(MouseEvent e);
    abstract String getName();
}
