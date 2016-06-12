package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Vlad1slav on 09.06.2016.
 */
public class Pie {
    public int x;
    public int y;
    Canvas canvas;
    Color color;

    public Pie(int x, int y, Canvas canvas, Color color) {
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        this.color = color;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void render() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(getColor());
        ctx.fillRect(getX()*Controller.cellD + 1, getY()*Controller.cellD + 1, Controller.cellD - 2, Controller.cellD - 2);
    }
}
