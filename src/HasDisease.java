import java.util.Random;

public class HasDisease extends Disease{
    int left;
    Random random;

    public HasDisease(){
        random = new Random();
        left = random.nextInt((longitude - (longitude-9)) + 1) + (longitude-9)*20;
    }

    public int step(){
        return --left;
    }


    public boolean getDead() {
        if(random.nextDouble()<mortalityRate)
            return true;
        else
            return false;
    }
}
