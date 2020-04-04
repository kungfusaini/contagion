import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This class creates the pane where simulation will be hold
 *
 * @author Oussama Rakye
 * @author Sumeet Saini 
 * @version 2.00
 */

public class MyWorld extends Application {

    //Chart to display disease information
    private static DiseaseChart diseaseChart;

    //Size of the screen
    static final int PANE_WIDTH = 1500;
    static final int PANE_HEIGHT = 500;

    private Pane background;


    @Override
    public void start(Stage stage) {

        diseaseChart = createChart();

        background = new Pane();
        VBox vBox = new VBox();

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

    static void updateValues(){
        diseaseChart.updateValues(Person.getInfected(), Person.getRecovered(), Person.getDead());
    }


    /**
     * Populate the world based on the screen size
     * and defined population
     *
     * @return populationList a list of the population
     */
    private ArrayList<Person> populate() {
        double radius = Math.sqrt((PANE_WIDTH*PANE_HEIGHT*Constants.PANE_OCCUPATION)/(Constants.POPULATION*3.14));
        //List of all persons
        ArrayList<Person> populationList = new ArrayList<>();
        for (int i = 0; i < Constants.POPULATION; i++) {
            populationList.add(new Person(background, radius));
        }

        //Infects the 1st guy, the bat eater
        populationList.get(0).infect(true);
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


