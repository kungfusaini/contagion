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
 * The world of the Virus Simulation
 * 
 * @author Sumeet Saini 
 * @version 1.00
 */
public class MyWorld extends Application
{
    private static final int POPULATION = 200;

    ArrayList<Person> list;

    public static final int PANE_WIDTH = 1500;
    public static final int PANE_HEIGHT = 500;

    private Pane background;
    private VBox vBox;

    private int index = 1;

    XYChart.Series seriesInfected;
    XYChart.Series seriesRecovered;
    XYChart.Series seriesDeads;


    @Override
    public void start(Stage stage) throws Exception {
        background = new Pane();
        vBox = new VBox();
        stage.setTitle("Coronavirus");
        background.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of people");
        xAxis.setLabel("Time");


        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Cases");

        //defining a series
        seriesInfected = new XYChart.Series();
        seriesInfected.setName("Infecteds");
        //populating the series with data
        seriesInfected.getData().add(new XYChart.Data(0, 0));

        lineChart.getData().add(seriesInfected);

        seriesRecovered = new XYChart.Series();
        seriesRecovered.setName("Recovered");
        //populating the series with data
        seriesRecovered.getData().add(new XYChart.Data(0, 0));

        lineChart.getData().add(seriesRecovered);

        seriesDeads = new XYChart.Series();
        seriesDeads.setName("Deads");
        //populating the series with data
        seriesDeads.getData().add(new XYChart.Data(0, 0));

        //background.getChildren().add(lineChart);
        lineChart.getData().add(seriesDeads);

        vBox.getChildren().addAll(background, lineChart);
        Scene scene = new Scene(vBox);

        stage.setScene(scene);
        stage.show();

        background.getChildren().addAll(populate());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

        seriesDeads.getData().add(new XYChart.Data(index, dead));
        seriesInfected.getData().add(new XYChart.Data(index, infected));
        seriesRecovered.getData().add(new XYChart.Data(index, recovered));

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
        list.get(0).infect();
        return list;
    }
}
