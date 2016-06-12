package sample;

import java.io.Serializable;

/**
 * Created by Vlad1slav on 12.06.2016.
 */
public class Score implements Serializable {
    String name = "Player";
    int cells = 20;
    int points = 0;

    public Score(String name, int score, int cellNum) {
        this.name = name;
        this.points = score;
        this.cells = cellNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCells(int cells) {
        this.cells = cells;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {

        return name;
    }

    public int getCells() {
        return cells;
    }

    public int getPoints() {
        return points;
    }
}
