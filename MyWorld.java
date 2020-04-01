import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The world of the Virus Simulation
 * 
 * @author Sumeet Saini 
 * @version 1.00
 */
public class MyWorld extends World
{
    private static final int population = 300;
    /**
     * Create the world and populate it
     * 
     */
    public MyWorld(){    
        // Create a 1000x600 pixel world with 1 pixel cells
        super(1200, 750, 1);
        populate();
    }
    
    /**
     * Populate the world based on the screen size
     * and defined population
     */
    private void populate(){
        for(int i = 0; i<population; i++){
            int x = Greenfoot.getRandomNumber(getWidth());
            int y = Greenfoot.getRandomNumber(getHeight());
            addObject(new Person(), x,y);
        }
    }
}
