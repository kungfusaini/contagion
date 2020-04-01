import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The world of the Virus Simulation
 * 
 * @author Sumeet Saini 
 * @version 1.00
 */
public class MyWorld extends World
{
    private static final int worldWidth = 1200;
    private static final int worldHeight = 750;
    private static final int population = 300;
    /**
     * Create the world and populate it
     * 
     */
    public MyWorld(){    
        //Create a 1200x750 pixel world with 1 pixel cells
        super(worldWidth, worldHeight, 1);
        populate();
    }
    
    /**
     * Populate the world based on the screen size
     * and defined population
     */
    private void populate(){
        //Create patient zero at centre of world
        Person patient0 = new Person();
        patient0.infect();
        addObject(patient0,worldWidth/2, worldHeight/2);
        //Add healthy population
        for(int i = 0; i<population; i++){
            int x = Greenfoot.getRandomNumber(getWidth());
            int y = Greenfoot.getRandomNumber(getHeight());
            addObject(new Person(), x,y);
        }
    }
}
