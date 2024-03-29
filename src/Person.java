import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

/**
 * A person in a virus simulation represented by a Circle
 *
 * @author Oussama Rakye
 * @author Sumeet Saini
 * @version 2.1
 */

public class Person extends Circle {
    //Array that contains all persons
    private static ArrayList<Person> persons = new ArrayList<>();
    //Array that contains all persons with the disease
    private static ArrayList<Person> personsWithDisease = new ArrayList<>();
    //Array that contains all persons with the disease
    private static ArrayList<Person> personsCanGetDisease = new ArrayList<>();
    //Boolean that's true while simulating
    private static boolean simulating = true;
    //Counters
    private static int counterInfectedPeople;
    private static int counterRecoveredPeople;
    private static int counterDeadPeople;
    private static int counterHealthyPeople;

    //Pane where it will be added to
    private Pane background;

    private Random random = new Random();
    //Speed of the person in the map in both axis
    private double dx;
    private double dy;
    //Module of the speed of the person
    private static final double SPEED = 6;
    /**
     * Radius of the circle
     * You also have to change it in the super() in the constructor, it doesn't let reference this one
     */
    private double radius;

    /**
     * This object
     * Has to be added here, otherwise it wouldn/t be possible to check collision
     */
    private Person person;
    //The disease, will be null if the person is healthy/recovered
    private Disease disease;
    //Once has passes the disease won't b able to get it again
    private boolean canGetInfected = true;
    //Boolean to mark if this person is alive
    private boolean alive = true;
    //Animation, it adds the movement to the persons
    private Timeline timeline;
    //Time that each person will move
    public static final int TIME_STEP_MILLISECONDS = 100;
    //Quarantined population%
    public static final double QUARANTINE = 0.9;
    //The person doesn't move if quarantined
    private boolean quarantined;

    /**
     * Creates the person, gets colocated in a random position of the background
     *
     * @param background where the person will be added to
     */
    public Person(Pane background, double radius) {
        super(radius, Color.CADETBLUE);
        this.radius=radius;
        this.relocate(random.nextInt(MyWorld.PANE_WIDTH + (int) radius * 3) - radius * 1.5, random.nextInt(MyWorld.PANE_HEIGHT + (int) radius * 3) - radius * 1.5);
        this.background = background;
        persons.add(this);
        personsCanGetDisease.add(this);
        person = this;

        quarantined = (random.nextDouble()<QUARANTINE) ? true : false;

        counterHealthyPeople++;

        //Sets the speed. The module will be equal to 6, with a random it can be positive or negative
        dx = SPEED * random.nextDouble(); //Step on x or velocity
        if (random.nextBoolean()) dx *= -1;
        dy = Math.sqrt(Math.pow(SPEED, 2) - Math.pow(dx, 2)); //Step on y
        if (random.nextBoolean()) dy *= -1;

        //Starts the movement
        addMovement();
    }

    //Empty constructor for test Person
    public Person()
    {
        //do nothing
    }

    /**
     * Makes the person move/act
     */
    private void addMovement() {
        //Limits of the background
        Bounds bounds = background.getBoundsInLocal();

        //Start of the animation with the predefined time
        timeline = new Timeline(new KeyFrame(Duration.millis(TIME_STEP_MILLISECONDS),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        if(!quarantined) {
                            //move the person
                            setLayoutX(getLayoutX() + dx);
                            setLayoutY(getLayoutY() + dy);

                            //If the person reaches the left or right border make the step negative changing the speed
                            if (getLayoutX() <= bounds.getMinX()) {

                                dx = SPEED * random.nextDouble(); //Step on x or velocity
                                dy = Math.sqrt(Math.pow(SPEED, 2) - Math.pow(dx, 2)); //Step on y
                                if (random.nextBoolean()) dy *= -1;

                            } else if (getLayoutX() >= bounds.getMaxX()) {
                                dx = -SPEED * random.nextDouble(); //Step on x or velocity
                                dy = Math.sqrt(Math.pow(SPEED, 2) - Math.pow(dx, 2)); //Step on y
                                if (random.nextBoolean()) dy *= -1;

                            }

                            //If the ball reaches the bottom or top border make the step negative changing the speed
                            if (getLayoutY() >= bounds.getMaxY()) {

                                dy = -SPEED * random.nextDouble(); //Step on y or velocity
                                dx = Math.sqrt(Math.pow(SPEED, 2) - Math.pow(dy, 2)); //Step on x
                                if (random.nextBoolean()) dx *= -1;

                            } else if (getLayoutY() <= bounds.getMinY()) {

                                dy = SPEED * random.nextDouble(); //Step on y or velocity
                                dx = Math.sqrt(Math.pow(SPEED, 2) - Math.pow(dy, 2)); //Step on x
                                if (random.nextBoolean()) dx *= -1;
                            }
                        }

                        //If this person has a disease, increase by one the longitude of it
                        if (disease != null) {
                            //If it has finished, call method finalDisease();
                            if (disease.step() <= 0)
                                finalDisease();
                        }
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);        //Make the animation run without a limit number of times
        timeline.play();
    }

    /**
     * Decides what will happen once the disease has arrived to its last day
     */
    private void finalDisease() {
        personsWithDisease.remove(this);
        //Checks if this person survives according to the disease class
        if (disease.getIsDead()) {
            alive = false;
            //Stop moving
            timeline.stop();
            //Set the color of the person to black
            setFill(Color.BLACK);
            counterDeadPeople++;
        } else {
            //If survives set the color to green.
            setFill(Color.rgb(20,240,0));
            counterRecoveredPeople++;
        }
        //No longer has a disease
        disease = null;

        //Check if there is people still infected, otherwise stop everything.
        if(--counterInfectedPeople <=0){
            stopAll();
        }
    }

    /**
     * Stop moving all the persons
     */
    private void stopAll() {
        simulating = false;
        for(Person each : persons){
            each.timeline.stop();
        }
    }

    /**
     * @return true if it is still simulating, false otherwise.
     */
    public static boolean isSimulating(){
        return simulating;
    }

    /**
     * Creates a new disease for this person and increases the counter of infected people
     */
    public void infect() {
        if(canGetInfected) {
            //This person won't be able to get infected gain
            setCantGetInfected();
            counterHealthyPeople--;
            personsWithDisease.add(this);
            personsCanGetDisease.remove(this);
            counterInfectedPeople++;
            disease = new Disease();
            //Set the person color to red
            this.setFill(javafx.scene.paint.Color.rgb(245, 6, 0));
        }
    }

    /**
     * @return the disease of this person
     */
    public Disease getDisease() {
        return disease;
    }

    /**
     * @return true if this person can get infected, false otherwise
     */
    public boolean isCanGetInfected() {
        return canGetInfected;
    }

    /**
     * Set false if this person can't get infected again
     */
    public void setCantGetInfected() {
        this.canGetInfected = false;
    }

    /**
     * Checks the state of this person
     * @return state state
     */
    public String getState(){
        if(alive && !canGetInfected) return "recovered";
        else if(alive && disease != null) return "infected";
        else if(!alive) return "dead";
        //Returns an empty string if this person is healthy and has never got the disease
        else return "";
    }

    public static int getInfected()
        {
            return counterInfectedPeople;
        }

    public static int getRecovered() {
        return counterRecoveredPeople;
    }

    public static int getDead() {
        return counterDeadPeople;
    }

    public static ArrayList<Person> getPersons() {
        return persons;
    }

    public static ArrayList<Person> getPersonsWithDisease() {
        return personsWithDisease;
    }

    public static ArrayList<Person> getPersonsCanGetDisease() {
        return personsCanGetDisease;
    }

    public static String getTotalPeople(){

        return "D: " + counterDeadPeople + "   H: " + counterHealthyPeople + "   I: " + counterInfectedPeople + "   R: " + counterRecoveredPeople + "   T: " +
                (counterRecoveredPeople+counterInfectedPeople+counterHealthyPeople+counterDeadPeople);
    }
}
