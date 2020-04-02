import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class Graph {

    //Lines in the line chart
    private XYChart.Series seriesInfected;
    private XYChart.Series seriesRecovered;
    private XYChart.Series seriesDeaths;


    //Axis of the line chart
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of people");
        xAxis.setLabel("Time");


    //creating the chart
    final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

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



    /**
     * Set the line of deaths to be black
     * The legend does not get updated
     */
        seriesDeaths.getNode().setStyle("-fx-stroke: #000000; -fx-text-fill: #000000");



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
}
