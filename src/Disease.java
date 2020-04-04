import java.util.Random;

/**
 * Creates the disease according to some fields
 *
 * @author Oussama Rakye
 * @version 1.1
 */

public class Disease {
    //The maximum time that the disease can last
    private static final int INFECTION_LEGNTH = 18;
    //The mortality rate
    private static final double MORTALITY_RATE = 0.2;
    //The infection rate
    public static final double INFECTION_RATE = 0.2;
    //The steps left for this disease
    private int infectionRemaining;

    private Random random;

    /**
     * Sets the remaining infection time of the disease between the value set and 9 days less
     */
    public Disease(){
        random = new Random();
        infectionRemaining = random.nextInt((INFECTION_LEGNTH - (INFECTION_LEGNTH -9)) + 1) + (INFECTION_LEGNTH -9)*20;    //There are 20 steps in one day
    }

    /**
     * @return the duration left for the disease
     */
    public int step(){
        return infectionRemaining;
    }

    public int getInfectionRemaining(){
        return infectionRemaining;
    }

    /**
     * @return true if the disease kills the person, false otherwise
     */
    public boolean getIsDead(double multiplier) {
        --infectionRemaining;
        if (infectionRemaining / 20 < 7) {
            if (random.nextDouble() < multiplier*MORTALITY_RATE/(7*20))
                return true;
        }
        return false;
    }

}
