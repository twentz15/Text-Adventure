import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes modified by Tom Wentz
 * @version 2011.08.10
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    ArrayList<Item> inventory = new ArrayList<Item>();
    ArrayList<Item> loot = new ArrayList<Item>();
    private Boolean lootFound = false;
    private Boolean keyFound = false;
    private boolean soulFound = false;
    private int stepCount = 0;
    Random itemRoll = new Random();
    Random skeleRoll = new Random();
    private boolean win = false;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }
    

    /**
     * Create all the rooms and link their exits together.
     * Also create the items and sets items to rooms
     */
    private void createRooms()
    {
        Room foyer, mainHall1, mainHall2, mainHall3, mainHall4, endCorridor, library, secretRoom, study;
        Room diningRoom, supplyCloset, kitchen, hallCloset, stairWay, bedroomHall, bedRoom, balcony, freedom;
      
        // create the rooms
        
        foyer = new Room("by the front door of a locked mansion");
        mainHall1 = new Room("in the main hall of the dimly lit mansion");
        mainHall2 = new Room("still in the main hall, but next to a Dining Room");
        mainHall3 = new Room("nearing the end of the main hall");
        endCorridor = new Room("in-between two rooms on either side of the hall");
        study = new Room("in the main study with a trunk behind the desk");
        library = new Room("in the main library, you feel a draft \ncoming from the shelfs, there may be a secret");
        secretRoom = new Room("in a secret room with a chest");
        diningRoom = new Room("in a nice room with a table set for dinner");
        kitchen = new Room("in the Kitchen, where blood is all over the cutting board");
        supplyCloset = new Room("in the pantry that also holds other household items");
        hallCloset = new Room("in the coat closet, seems as though party was going on");
        mainHall4 = new Room("under an arched segment of hallway with a bear carpet");
        stairWay = new Room("finally at the stair way to the upper levels of the house");
        bedroomHall = new Room("at the door of the lone bedroom door left ajar");
        bedRoom = new Room("in the master bedroom, which time has forgot to touch");
        balcony = new Room("on the balcony, jump for freedom");
        freedom = new Room("free");
        
        
        // initialise room exits
        foyer.setExit("east", mainHall1);
        
        mainHall1.setExit("west", foyer);
        mainHall1.setExit("east", hallCloset);
        mainHall1.setExit("north", mainHall4);
        mainHall1.setExit("south", mainHall2);
        
        mainHall2.setExit("north", mainHall1);
        mainHall2.setExit("south", mainHall3);
        mainHall2.setExit("east", diningRoom);

        mainHall3.setExit("north", mainHall2);
        mainHall3.setExit("south", endCorridor);
        
        endCorridor.setExit("north", mainHall3);
        endCorridor.setExit("east", study);
        endCorridor.setExit("west", library);
        
       library.setExit("east", endCorridor);
       library.setExit("secret", secretRoom);
       
       secretRoom.setExit("south", library);
       
       study.setExit("west", endCorridor);
       
       diningRoom.setExit("west", mainHall2);
       diningRoom.setExit("east", kitchen);
       
       kitchen.setExit("west", diningRoom);
       kitchen.setExit("north", supplyCloset);
       
       supplyCloset.setExit("south", kitchen);
       supplyCloset.setExit("West", hallCloset);
       
       hallCloset.setExit("east", supplyCloset);
       hallCloset.setExit("west", mainHall4);
       
       mainHall4.setExit("south", mainHall1);
       mainHall4.setExit("east", hallCloset);
       mainHall4.setExit("north", stairWay);
       
       stairWay.setExit("south", mainHall4);
       stairWay.setExit("east", bedroomHall);
       
       bedroomHall.setExit("west", stairWay);
       bedroomHall.setExit("north", bedRoom);
       
       bedRoom.setExit("south", bedroomHall);
       bedRoom.setExit("west", balcony);
       
       balcony.setExit("west", freedom);
       
       loot.add(new Item("key"));
       loot.add(new Item("jar of life"));
       
       if(itemRoll.nextInt(20) <= 15)
       {
           secretRoom.setLootFound(true);
       }
       else
       {
           study.setLootFound(true);
       }
       
       if(itemRoll.nextInt(30) <= 15)
       {
           supplyCloset.setLootFound(true);
       }
       else
       {
           supplyCloset.setLootFound(false);
       }
       

        currentRoom = foyer;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while(! finished)
        {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if(win == true)
            {
                finished = true;
            }
        }
        System.out.println("Thanks for Playing. Good Bye.");
        System.out.println("You took " + stepCount + " steps");
    }
   

   /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome the Haunted Mansion");
        System.out.println("You made the bad choice of walking ");
        System.out.println("into an abandon mansion. Now you must escape. To escape ");
        System.out.println("you must find your war to the balcony to jump to safty.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case EXAMINE:
               openLoot();
               break;
               
            case INVENTORY:
                viewInven();
                break;
        }
        return wantToQuit;
    }
    /**
     * Used to open a loot chest if lootFound boolean is true
     * Also searches the loot ArrayList for a set item to make sure the right message is displayed
     * display a default message if no item it found
     */
    private void openLoot()
    {
        if(currentRoom.getLootFound() == true )
        {
            for(int i = 0; i < loot.size(); i++)
            if(loot.get(i).getItemName().equals("key"))
            {
                System.out.println("You found a Key for a locked door");
                keyFound = true;
                inventory.add( new Item(loot.get(i).getItemName()));
                loot.remove(i);
                i = loot.size();
                
                
                currentRoom.setLootFound(false);
            }
            else if(loot.get(i).getItemName().equals("jar of life"))
            {
                System.out.println("You found a Jar with a Soul in it");
                soulFound = true;
                
                inventory.add( new Item(loot.get(i).getItemName()));
                i = loot.size();
                
                currentRoom.setLootFound(false);
            }
        }
        else
        {
            System.out.println("There isnt anything to take");
        }
    }
    /**
     * The method is used to display the player's inventory
     * 
     */
    private void viewInven()
    {
        String inven = "";
        System.out.print("\nYou Have: ");
        for(int i = 0; i < inventory.size(); i++)
        {
            System.out.print(inventory.get(i).getItemName() + ", ");
        }
    }
    

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around the mansion but find yourself where.");
        System.out.println("you left off");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * There is also a if else checker that acts as a locked door for the ending.
     * In order to enter a boolean type needs to be true and in a certain room to unlock
     * 
     * Also adds a int variable to create a step counter
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room tempRoom = currentRoom;
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            if(currentRoom.getShortDescription().equals("on the balcony, jump for freedom") && keyFound == false)
            {
                currentRoom = tempRoom;
                System.out.println("You can't leave till you find the key");
                System.out.println(currentRoom.getLongDescription());
            }
            else if(currentRoom.getShortDescription().equals("free"))
            {
                System.out.println(currentRoom.getLongDescription());
                //System.out.println("You have WON!!! Type 'quit' or sit here for enternity");
                win = true;
            }
            else
            {
                System.out.println(currentRoom.getLongDescription());
                stepCount++;
            }
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Main method for the game
     * 
     */
    public static void main(String[] args) 
    {
        Game adventure = new Game();
        adventure.play();
    }
    
}
