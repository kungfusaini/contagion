import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A person in a virus simulation
 * 
 * @author Sumeet Saini
 * @version 1.00
 */
public class Person extends Actor
{
    //Chance person will turn randomly
    private int turnChance = 20;
    private int infection = 0;
    private static final int INFECTION_TIME = 200;  
    
    /**
     * Initialize a person with a random movement direction
     */
    public Person(){
        turn(Greenfoot.getRandomNumber(360));
    }
    
    /**
     * Act - do whatever the Person wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
      movement();
      if(isInfected()){
          spread();
      }
      heal();
    } 
    
    /**
     * The person's movement behaviour
     */
    private void movement(){
     move(3);
     if(isAtEdge()){
           turn(70);
     }
     //Randomly turn 30 degrees left or right
     if(Greenfoot.getRandomNumber(100)<turnChance){
         turn(Greenfoot.getRandomNumber(61)-30);
     }
        
     // //If the person is touching another person,turn
     // if(isTouching(other)){
           // turn(Greenfoot.getRandomNumber(360));
     // }
    }
    
    /**
     * Infect the actor and changes it's display image
     */
    public void infect(){
        infection = INFECTION_TIME;
        setImage("infected.png");
    }
    
    private boolean isInfected(){
        if (infection!= 0)
            return true;
        else
            return false;
    }
    
    /**
     * If touching another actor, infect them
     */
    private void spread(){
        Person other = (Person) getOneIntersectingObject(Person.class);
        if (other != null){
            other.infect();
        }
    }
    
    private void heal(){
        if (isInfected()){
            infection--;
        }
        
    }
}
