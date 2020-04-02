import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Improvements:
 *      -Chance line chart colors
 *      -updateValues() makes a for each loop for all the persons that checks the status every second
 *       this can be improved by having counters in the class Person and get the them by a method.
 *      -Add a probability of infection so in the future it can be changed to different values. Note:
 *       when two people meet, the method meet gets called multiple times, this could affect the probability.
 *       When a person A meets person B, both can get infected, when person B meets person A in its object, both
 *       will get infected again, this will increase the probability and has to be changed.
 *       -When someone is infected it will stop his movement, but just from day 5 of infection.
 *       -Some people don't get the symptoms, therefore, as they are not aware, the will keep moving.
 *       -Make the possibility to set a lockdown, so a %of the population won't move at all.
 */

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

    //Lines in the line chart
    private XYChart.Series seriesInfected;
    private XYChart.Series seriesRecovered;
    private XYChart.Series seriesDeaths;


    @Override
    public void start(Stage stage) throws Exception {
        background = new Pane();
        vBox = new VBox();
        stage.setTitle("Coronavirus");
        background.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        //Axis of the line chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of people");
        xAxis.setLabel("Time");


        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Cases");
        lineChart.setCreateSymbols(false);

        //defining a series
        seriesInfected = new XYChart.Series();
        seriesInfected.setName("Infecteds");

        seriesInfected.getData().add(new XYChart.Data(0, 0)); //Set (0, 0) as initial value
        lineChart.getData().add(seriesInfected);

        //defining a series
        seriesRecovered = new XYChart.Series();
        seriesRecovered.setName("Recovered");

        seriesRecovered.getData().add(new XYChart.Data(0, 0)); //Set (0, 0) as initial value
        lineChart.getData().add(seriesRecovered);

        //defining a series
        seriesDeaths = new XYChart.Series();
        seriesDeaths.setName("Deaths");

        seriesDeaths.getData().add(new XYChart.Data(0, 0));  ///Set (0, 0) as initial value
        lineChart.getData().add(seriesDeaths);

        vBox.getChildren().addAll(background, lineChart);
        Scene scene = new Scene(vBox);

        stage.setScene(scene);
        stage.show();

        /**
         * Set the line of deaths to be black
         * The legend does not get updated
         */
        seriesDeaths.getNode().setStyle("-fx-stroke: #000000; -fx-text-fill: #000000");

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
                    updateValues();
                }
            }
        });
        thread.start();
    }

    /**
     * This method updates the values in the chart
     * Gets the state of each person individually, then it adds
     * them in the chart.
     */
    private void updateValues() {
        int dead = 0;
        int recovered = 0;
        int infected = 0;

        for (Person person : list){
            String state = person.getState();

            if(state.equals("recovered")) recovered++;
            else if(state.equals("infected")) infected++;
            else if (state.equals("dead")) dead++;
        }

        seriesDeaths.getData().add(new XYChart.Data(index, dead));
        seriesInfected.getData().add(new XYChart.Data(index, infected));
        seriesRecovered.getData().add(new XYChart.Data(index, recovered));

        //Increases the index of the x axis
        index++;
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
