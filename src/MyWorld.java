import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * The world of the Virus Simulation
 * 
 * @author Sumeet Saini 
 * @version 1.00
 */
public class MyWorld extends Application
{
    private static final int POPULATION = 20;

    public static final int PANE_WIDTH = 600;
    public static final int PANE_HEIGHT = 400;

    private Pane background;


    @Override
    public void start(Stage stage) throws Exception {
        background = new Pane();
        stage.setTitle("Coronavirus");
        background.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        Scene scene = new Scene(background);

        stage.setScene(scene);
        stage.show();

        background.getChildren().addAll(populate());
    }

    /**
     * Populate the world based on the screen size
     * and defined population
     */
    private ArrayList<Person> populate(){
        ArrayList<Person> list = new ArrayList<>();
        for(int i = 0; i<POPULATION; i++){
            list.add(new Person(background));
        }
        list.get(0).infect();
        return list;
    }
}
