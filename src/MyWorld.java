import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class creates the pane where simulation will be hold
 *
 * @author Oussama Rakye
 * @author Sumeet Saini 
 * @version 2.00
 */
public class MyWorld extends Application
{
    /**
     * Population of the simulation.
     * Note: if you increase the number of the population you might have to increase TIME_STEP_MILLISECONDS, so your computer can handle it
     */
    private static final int POPULATION = 400;

    //List of all persons
    private ArrayList<Person> list;

    //Size of the screen
    public static final int PANE_WIDTH = 1500;
    public static final int PANE_HEIGHT = 500;

    private Pane background;
    private VBox vBox;

    //Index of the x-axis in the line chart
    private int index = 1;



    @Override
    public void start(Stage stage) throws Exception {
        background = new Pane();
        vBox = new VBox();
        stage.setTitle("Coronavirus");
        background.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        //Add all persons in the simulation
        background.getChildren().addAll(populate());

        //Create a new thread that updates the chart every 1 second
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Run while there is someone still infected
                while (Person.isSimulating()){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   // updateValues();
                }
            }
        });
        thread.start();
    }



    /**
     * Populate the world based on the screen size
     * and defined population
     */
    private ArrayList<Person> populate(){
        list = new ArrayList<>();
        for(int i = 0; i<POPULATION; i++){
            list.add(new Person(background));
        }

        //Infects the 1st guy, the bat eater
        list.get(0).infect();
        return list;
    }
}
