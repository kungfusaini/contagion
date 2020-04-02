import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

/**
 * A person in a virus simulation
 *
 * @author Sumeet Saini
 * @version 1.00
 */
public class Person extends Circle {
    private static ArrayList<Person> persons = new ArrayList<>();
    private static boolean simulating = true;
    private static int counter;
    private Circle ball;
    private Pane background;
    private Random random = new Random();
    private double dx;
    private double dy;
    private double radius = 10;
    private Person person;
    private HasDisease hasDisease;
    private boolean canGetInfected = true;
    private boolean alive = true;
    private Timeline timeline;

    public Person(Pane background) {
        super(10, Color.CADETBLUE);
        this.relocate(random.nextInt(MyWorld.PANE_WIDTH + (int) radius * 3) - radius * 1.5, random.nextInt(MyWorld.PANE_HEIGHT + (int) radius * 3) - radius * 1.5);
        this.background = background;
        persons.add(this);
        person = this;
        counter++;

        dx = 6 * random.nextDouble(); //Step on x or velocity
        if (random.nextBoolean()) dx *= -1;
        dy = Math.sqrt(36 - Math.pow(dx, 2)); //Step on y
        if (random.nextBoolean()) dy *= -1;

        addMovement();
    }


    private void addMovement() {
        Bounds bounds = background.getBoundsInLocal();
        timeline = new Timeline(new KeyFrame(Duration.millis(20),
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        //move the ball
                        setLayoutX(getLayoutX() + dx);
                        setLayoutY(getLayoutY() + dy);

                        //If the ball reaches the left or right border make the step negative
                        if (getLayoutX() <= bounds.getMinX()) {

                            dx = 6 * random.nextDouble(); //Step on x or velocity
                            dy = Math.sqrt(36 - Math.pow(dx, 2)); //Step on y
                            if (random.nextBoolean()) dy *= -1;

                        } else if (getLayoutX() >= bounds.getMaxX()) {
                            dx = -6 * random.nextDouble(); //Step on x or velocity
                            dy = Math.sqrt(36 - Math.pow(dx, 2)); //Step on y
                            if (random.nextBoolean()) dy *= -1;

                        }

                        //If the ball reaches the bottom or top border make the step negative
                        if (getLayoutY() >= bounds.getMaxY()) {

                            dy = -6 * random.nextDouble(); //Step on y or velocity
                            dx = Math.sqrt(36 - Math.pow(dy, 2)); //Step on y
                            if (random.nextBoolean()) dx *= -1;

                        } else if (getLayoutY() <= bounds.getMinY()) {

                            dy = 6 * random.nextDouble(); //Step on y or velocity
                            dx = Math.sqrt(36 - Math.pow(dy, 2)); //Step on y
                            if (random.nextBoolean()) dx *= -1;
                        }
                        detectMeeting();
                        if (hasDisease != null) {
                            if (hasDisease.step() <= 0)
                                finalDisease();
                        }
                    }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void finalDisease() {
        setCantGetInfected();
        if (hasDisease.getDead()) {
            alive = false;
            timeline.stop();
            this.setFill(Color.BLACK);
        } else {
            this.setFill(Color.GREEN);
        }
        hasDisease = null;
        if(--counter<=0){
            stopAll();
        }
    }

    private void stopAll() {
        simulating = false;
        for(Person each : persons){
            each.timeline.stop();
        }
    }

    public static boolean isSimulating(){
        return simulating;
    }

    private void detectMeeting() {

        Rectangle bounds = new Rectangle(getLayoutX(), getLayoutY(), radius * 2, radius * 2);

        for (Person personC : persons) {
            if (personC != person) {
                Rectangle personCB = new Rectangle(personC.getLayoutX(), personC.getLayoutY(), radius * 2, radius * 2);
                if (personCB.intersects(bounds.getLayoutBounds())) {
                    if ((hasDisease != null) ^ (personC.getHasDisease() != null)) {
                        if (hasDisease == null) {
                            if (canGetInfected)
                                infect();
                        } else {
                            if (personC.isCanGetInfected())
                                personC.infect();
                        }
                    }
                }
            }
        }
    }

    public void infect() {
        hasDisease = new HasDisease();
        this.setFill(javafx.scene.paint.Color.RED);
    }

    public HasDisease getHasDisease() {
        return hasDisease;
    }

    public boolean isCanGetInfected() {
        return canGetInfected;
    }

    public void setCantGetInfected() {
        this.canGetInfected = false;
    }

    public String getState(){
        if(alive && !canGetInfected) return "recovered";
        else if(alive && hasDisease != null) return "infected";
        else if(!alive) return "dead";
        else return "";
    }
}
