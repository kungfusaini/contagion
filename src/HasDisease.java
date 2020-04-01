import java.util.Random;

public class HasDisease extends Disease{
    int left;
    Random random;
    boolean alreadyHadIt;

    public HasDisease(){
        random = new Random();
        left = random.nextInt((longitude - (longitude-9)) + 1) + (longitude-9);
    }

    public void step(){
        --left;
    }

    public boolean alreadyHadIt(){
        return alreadyHadIt;
    }
}
