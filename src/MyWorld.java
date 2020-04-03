import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
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

public class MyWorld extends Application {

    //Population of the simulation.
    //Note: if you increase the number of the population you might have to increase TIME_STEP_MILLISECONDS, so
    //your computer can handle it
    private static final int POPULATION = 10000;

    //List of all persons
    private ArrayList<Person> populationList;

    //Chart to display disease information
    private static DiseaseChart diseaseChart;

    //Size of the screen
    public static final int PANE_WIDTH = 1500;
    public static final int PANE_HEIGHT = 500;

    //Percentage of pane occupied by people
    public static final double PANE_OCCUPATION = 0.1;

    private Pane background;
    private VBox vBox;


    @Override
    public void start(Stage stage) throws Exception {

        diseaseChart = createChart();

        background = new Pane();
        vBox = new VBox();

        stage.setTitle("Coronavirus");
        background.setPrefSize(PANE_WIDTH, PANE_HEIGHT);


        vBox.getChildren().addAll(background, diseaseChart);
        Scene scene = new Scene(vBox);

        stage.setScene(scene);
        stage.show();

        //Add all persons in the simulation
        background.getChildren().addAll(populate());

        //Create a new thread that updates the chart every 1 second
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //Run while there is someone still infected
//                while (Person.isSimulating()) {
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    diseaseChart.updateValues(Person.getInfected(), Person.getRecovered(), Person.getDead());
//                }
//            }
//        });
//        thread.start();

        CollisionChecker collisionChecker = new CollisionChecker();
        collisionChecker.start();
    }

    public static void updateValues(){
        diseaseChart.updateValues(Person.getInfected(), Person.getRecovered(), Person.getDead());
    }


    /**
     * Populate the world based on the screen size
     * and defined population
     *
     * @return populationList a list of the population
     */
    private ArrayList<Person> populate() {
        double radius = Math.sqrt((PANE_WIDTH*PANE_HEIGHT*PANE_OCCUPATION)/(POPULATION*3.14));
        populationList = new ArrayList<>();
        for (int i = 0; i < POPULATION; i++) {
            populationList.add(new Person(background, radius));
        }

        //Infects the 1st guy, the bat eater
        populationList.get(0).infect();
        return populationList;
    }

    /**
     * This method creates the Chart that will be displayed in the window
     * @return DiseaseChart the chart of disease information
     */
    private static DiseaseChart createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of people");
        xAxis.setLabel("Time");

        return new DiseaseChart(xAxis, yAxis);

    }
}


