import javafx.scene.shape.Rectangle;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class CollisionChecker extends Thread {
    @Override
    public synchronized void run() {
        while (Person.isSimulating()) {
            long start = System.currentTimeMillis();
            LinkedHashMap<Rectangle, Person> linkedHashMap = new LinkedHashMap<>();
            for (Person person : Person.getPersons()) {
                //Bounds of the person in form of a rectangle
                Rectangle bounds = new Rectangle(person.getLayoutX(), person.getLayoutY(), person.getRadius() * 2, person.getRadius() * 2);
                linkedHashMap.put(bounds, person);
            }

            while (linkedHashMap.size()>=2) {
                Rectangle firstBounds = linkedHashMap.keySet().iterator().next();
                detectMeeting(linkedHashMap, firstBounds);
                linkedHashMap.remove(firstBounds);
            }
            System.out.println(System.currentTimeMillis()-start);
        }
    }


    /**
     * This class checks if has met someone during the movement.
     * If some of this two has the disease, this person will infect the other one
     */
    private void detectMeeting(LinkedHashMap<Rectangle, Person> linkedHashMap, Rectangle firstBounds) {
        Iterator iterator = linkedHashMap.keySet().iterator();
        //System.out.println(iterator.next());

        while (iterator.hasNext()) {
            Rectangle secondBounds = (Rectangle) iterator.next();

            if (secondBounds.intersects(firstBounds.getLayoutBounds())) {
                Person firstPerson = linkedHashMap.get(firstBounds);
                Person secondPerson = linkedHashMap.get(secondBounds);

                if ((firstPerson.getDisease() != null) ^ (secondPerson.getDisease() != null)) {
                    //If this person doesn't have the disease, this person gets it, otherwise the other person will.
                    if (firstPerson.getDisease() == null) {
                        if (firstPerson.isCanGetInfected())
                            firstPerson.infect();
                    } else {
                        if (secondPerson.isCanGetInfected())
                            secondPerson.infect();
                    }
                }
            }
        }
        linkedHashMap.remove(firstBounds);
    }
}