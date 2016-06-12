package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

/**
 * Created by Vlad1slav on 09.06.2016.
 */
public class Player implements Runnable {
    Snake snake;
    Canvas canvas;

    @FXML
    Label game_info;

    public static void showScore(int score) throws IOException {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog("Player");
            dialog.setTitle("Game Over");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter your name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                ArrayList<Score> obj= new ArrayList<>();
                File f = new File("score");
                if(f.exists() && !f.isDirectory()) {
                    ObjectInputStream ois = null;
                    try {
                        ois = new ObjectInputStream(new FileInputStream("score"));
                        obj = (ArrayList<Score>) ois.readObject();
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }


                Score pl = new Score(result.get(), score, Controller.cellNum);
                obj.add(pl);

                Collections.sort(obj, (record1, record2) -> {
                    int cmpPoints = - (record1.getPoints() - record2.getPoints());
                    if (cmpPoints != 0) {
                        return cmpPoints;
                    } else {
                        int cmpCells = (record1.getCells() - record2.getCells());
                        return cmpCells;
                    }
                });

                while (obj.size()> 10){
                    obj.remove(obj.size() - 1);
                }

                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream("score");
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(obj);
                    oos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StringBuilder builder = new StringBuilder() ;
                builder.append("<html><table border=1><tr><td>Name</td><td>Score</td><td>Cells</td></tr>");
                for(Score item_ : obj) {
                    builder.append("<tr><td>");
                    builder.append(item_.getName());
                    builder.append("</td><td>");
                    builder.append(item_.getPoints());
                    builder.append("</td><td>");
                    builder.append(item_.getCells());
                    builder.append("</td></tr>");
                }
                builder.append("</table></html>");
                JOptionPane.showMessageDialog(null,
                        builder.toString(), "High Scores",JOptionPane.PLAIN_MESSAGE);
            }
        });

    }

    public Player(Canvas canvas, Label game_info) {
        snake = new Snake(canvas, Color.YELLOW);
        this.canvas = canvas;
        this.game_info = game_info;
    }
    static boolean game = false;

    @Override
    public void run() {
        game = true;
        while (game) {
            try {
                Thread.sleep(snake.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                snake.move();
            } catch (IOException e) {
                e.printStackTrace();
            }
            snake.render();
            Platform.runLater(() -> game_info.setText("Your score: " + snake.getScore()));
        }
    }

    public void keyPress(KeyCode key) {
        if (key == KeyCode.A) {
            if (snake.getNextStep() != MoveVector.RIGHT
                    && snake.getNextStep() != MoveVector.LEFT)
                snake.setNextStep(MoveVector.LEFT);
        }

        if (key == KeyCode.D) {
            if (snake.getNextStep() != MoveVector.RIGHT
                    && snake.getNextStep() != MoveVector.LEFT)
                snake.setNextStep(MoveVector.RIGHT);
        }

        if (key == KeyCode.W) {
            if (snake.getNextStep() != MoveVector.UP
                    && snake.getNextStep() != MoveVector.DOWN)
                snake.setNextStep(MoveVector.UP);
        }

        if (key == KeyCode.S) {
            if (snake.getNextStep() != MoveVector.UP
                    && snake.getNextStep() != MoveVector.DOWN)
                snake.setNextStep(MoveVector.DOWN);
        }
    }

}
