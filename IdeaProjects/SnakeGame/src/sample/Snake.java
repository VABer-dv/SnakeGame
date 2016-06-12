package sample;

import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vlad1slav on 09.06.2016.
 */
public class Snake {

    private ArrayList<Dot> dots = new ArrayList<>();
    private MoveVector nextStep = MoveVector.NONE;
    private Color color;
    private int length = 3;
    public boolean isMoved = true;
    Canvas canvas;
    private int speed = 500;
    private int score = 0;

    public Snake(Canvas canvas, Color color) {
        this.canvas = canvas;
        this.color = color;
        dots.add(new Dot(1,1));
        dots.add(new Dot(1,2));
        dots.add(new Dot(1,3));
    }

    public void move() throws IOException {
        int xInc = 0; int yInc = 0;
        boolean move = true;
        switch(nextStep){
            case UP :
                yInc-- ; break;
            case DOWN :
                yInc++ ; break;
            case RIGHT :
                xInc++ ; break;
            case LEFT :
                xInc-- ; break;
            default:
                move = false;
                break;
        }

        if (move) {
            clearTail(dots.get(dots.size() - 1));
            dots.add(0, new Dot(dots.get(0).getX() + xInc, dots.get(0).getY() + yInc));

            if (dots.get(0).getX() >= Controller.cellNum)
                dots.set(0, new Dot(0, dots.get(0).getY()));


            if (dots.get(0).getY() >= Controller.cellNum)
                dots.set(0, new Dot(dots.get(0).getX(), 0));

            if (dots.get(0).getX() < 0)
                dots.set(0, new Dot(Controller.cellNum - 1, dots.get(0).getY()));

            if (dots.get(0).getY() < 0)
                dots.set(0, new Dot(dots.get(0).getX(), Controller.cellNum - 1));

//            for (Dot item : dots) {
//                System.out.println(item.getX() + " " + item.getY());
//            }
            if (collideItself()){
                Player.game = false;
                Player.showScore(getScore());
            }

            if (!collidePie()) {
                dots.remove(dots.size() - 1);
            }
            else {
                setScore(getScore() + 10);
                Random rn = new Random();
                Controller.pie.setX(rn.nextInt(Controller.cellNum));
                Controller.pie.setY(rn.nextInt(Controller.cellNum));
                while (collidePie()){
//                    Controller.pie = new Pie(rn.nextInt(Controller.cellNum), rn.nextInt(Controller.cellNum), canvas, Color.RED);
                    Controller.pie.setX(rn.nextInt(Controller.cellNum));
                    Controller.pie.setY(rn.nextInt(Controller.cellNum));
                }
                Controller.pie.render();
                if (getSpeed() > 100)
                    setSpeed(getSpeed() - 50);
            }

            render();
        }
    }

    private boolean collideItself() {
        for (int i = 1; i < dots.size() - 1; i++) {
            if (dots.get(0).getX() == dots.get(i).getX() && dots.get(0).getY() == dots.get(i).getY())
                return true;
        }
        return false;
    }

    private boolean collidePie() {
        for (Dot pos: dots) {
            if (pos.getX() == Controller.pie.getX() && pos.getY() == Controller.pie.getY())
                return true;
        }
        return false;
    }

    private void clearTail(Dot dot) {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.WHITE);
        ctx.fillRect(dot.getX()*Controller.cellD + 1, dot.getY()*Controller.cellD + 1, Controller.cellD - 2, Controller.cellD - 2);
    }

    public void setNextStep(MoveVector nextStep) {
        this.nextStep = nextStep;
    }

    public Color getColor() {
        return color;
    }

    public MoveVector getNextStep() {
        return nextStep;
    }

    public void render() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(getColor());
        for (Dot item : dots) {
            ctx.fillRect(item.getX()*Controller.cellD + 1, item.getY()*Controller.cellD + 1, Controller.cellD - 2, Controller.cellD - 2);
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
