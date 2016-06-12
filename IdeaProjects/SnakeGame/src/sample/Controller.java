package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    public Canvas canvas;

    @FXML
    private Button btn_start;

    @FXML
    private Label game_info;

    public static int cellNum = 20;
    public static int cellD;
    private Random rn = new Random();
    Thread playerThread;
    Player player;
    static Pie pie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderField(canvas, cellNum);
    }

    private void renderField(Canvas canvas, int n){
        GraphicsContext gc = canvas.getGraphicsContext2D() ;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(1, 1, canvas.getWidth() , canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1.0);
        cellD = (int) (canvas.getHeight() / n);

        for (int i = 0; i < n + 1; i++) {
            canvas.getGraphicsContext2D().strokeLine(0, i * cellD, canvas.getHeight(), i * cellD);
            canvas.getGraphicsContext2D().strokeLine(i * cellD, 0, i * cellD, canvas.getHeight());
        }
        gc.stroke();
    }

    public void game_start(ActionEvent event) {
        if (!Player.game) {
            JOptionPane optionPane = new JOptionPane();
            JSlider slider = getSlider(optionPane);
            optionPane.setMessage(new Object[] { "Welcome" , "Control: A, W, S, D keys", "Select number of cells: ", slider });
            optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
            optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog = optionPane.createDialog("New Game Options");
            dialog.setVisible(true);
            if (optionPane.getValue().toString().equals("0")) {
                if (optionPane.getInputValue() == "uninitializedValue")
                    Controller.cellNum = 20;
                else
                    cellNum = (int) optionPane.getInputValue();
                renderField(canvas, cellNum);
                player = new Player(canvas, game_info);
                playerThread = new Thread(player);
                playerThread.start();
                pie = new Pie(cellNum - 1, cellNum - 1, canvas, Color.RED);
                pie.render();
            }
        }
    }


    static JSlider getSlider(final JOptionPane optionPane) {
        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(5);
        slider.setMinimum(5);
        slider.setMaximum(50);
        slider.setValue(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(theSlider.getValue());
                }
            }
        };
        slider.addChangeListener(changeListener);
        return slider;
    }


    public void handleKeyPressed(KeyEvent event) {
        if (playerThread != null){
            player.keyPress(event.getCode());
        }
    }

    public void highscore(ActionEvent actionEvent) {
        ArrayList<Score> obj= new ArrayList<>();
        File f = new File("score");
        ArrayList<String> score = new ArrayList<>();
        if(f.exists() && !f.isDirectory()) {
            ObjectInputStream ois;
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

    public void about(ActionEvent actionEvent) {
        Object[] objects = {
                "Game made by VABer",
                "version 1.0",
                "<html> contact : <a href=\\\"\\\">vaber93@mail.ru</a></html>"};
        JOptionPane.showMessageDialog(null, objects,
                "About",
                JOptionPane.PLAIN_MESSAGE);

    }

    public void bots(ActionEvent actionEvent) {
        Object[] objects = {
                "Sorry,",
                "option is not available in this version"};
        JOptionPane.showMessageDialog(null, objects,
                "Play with bots",
                JOptionPane.PLAIN_MESSAGE);
    }
}
