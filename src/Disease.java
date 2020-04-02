import java.util.Random;

/**
 * Creates the disease according to some fields
 *
 * @author Oussama Rakye
 * @version 1.1
 */
public class Disease {
    //The maximum longitude that the disease can last
    private static final int LONGITUDE = 18;
    //The mortality rate
    private static final double MORTALITY_RATE = 0.2;
    //The infection rate
    private static final double INFECTION_RATE = 0.8;
    //The days left for this disease
    private int left;

    private Random random;

    /**
     * Sets the longitude of the disease between the value set and 9 days less
     */
    public Disease(){
        random = new Random();
        left = random.nextInt((LONGITUDE - (LONGITUDE -9)) + 1) + (LONGITUDE -9)*20;    //There are 20 steps in one day
    }

    /**
     * @return the duration left for the disease
     */
    public int step(){
        return --left;
    }

    /**
     * @return true if the disease kills the person, false otherwise
     */
    public boolean getDead() {
        if(random.nextDouble()< MORTALITY_RATE)
            return true;
        else
            return false;
    }
}
