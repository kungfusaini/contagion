import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class CollisionChecker extends Thread {
    @Override
    public synchronized void run() {
        while (Person.isSimulating()) {
            long start = System.currentTimeMillis();
            LinkedHashMap<Rectangle, Person> linkedHashMap = new LinkedHashMap<>();
            LinkedHashMap<Rectangle, Person> linkedHashMapCan = new LinkedHashMap<>();
            ArrayList<Person> personsWithIt = new ArrayList<>(Person.getPersonsWithDisease());
                for (Person person : personsWithIt) {
                    //Bounds of the person in form of a rectangle
                    if(person != null) {
                        Rectangle bounds = new Rectangle(person.getLayoutX(), person.getLayoutY(), person.getRadius() * 2, person.getRadius() * 2);
                        linkedHashMap.put(bounds, person);
                    }
                }


            for (Person person : Person.getPersonsCanGetDisease()) {
                //Bounds of the person in form of a rectangle
                Rectangle bounds = new Rectangle(person.getLayoutX(), person.getLayoutY(), person.getRadius() * 2, person.getRadius() * 2);
                linkedHashMapCan.put(bounds, person);
            }

            while (linkedHashMap.size()>=1) {
                Rectangle firstBounds = linkedHashMap.keySet().iterator().next();
                Person firstPerson = linkedHashMapCan.get(firstBounds);
                detectMeeting(linkedHashMapCan, firstBounds, firstPerson);
                linkedHashMap.remove(firstBounds);
            }
            MyWorld.updateValues();
            long waitedTime = System.currentTimeMillis()-start;

            if (waitedTime<800){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000-waitedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * This class checks if has met someone during the movement.
     * If some of this two has the disease, this person will infect the other one
     */
    private void detectMeeting(LinkedHashMap<Rectangle, Person> linkedHashMapCan, Rectangle firstBounds, Person firstPerson) {
        Iterator iterator = linkedHashMapCan.keySet().iterator();
        //System.out.println(iterator.next());

        while (iterator.hasNext()) {
            Rectangle secondBounds = (Rectangle) iterator.next();

            if (secondBounds.intersects(firstBounds.getLayoutBounds())) {
                Person secondPerson = linkedHashMapCan.get(secondBounds);
                secondPerson.infect();
            }
        }
    }
}