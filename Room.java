import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;
    private boolean lootFound = false;
    private boolean hasEnemy = false;
    ArrayList<Item> loot = new ArrayList<Item>();

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Sets a boolean variable to determine if a room has a item in it
     * @param a boolean to tell if a room has an item in it
     */
    public void setLootFound(Boolean lootFound)
    {
        this.lootFound = lootFound;
    }
    
    /**
     * The getLootFound method acts as a retreiver for a room to see 
     * whether or not 
     */
    public Boolean getLootFound()
    {
        return lootFound;
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * There is a if else statment that acts a mask for a secret room.
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) 
        {
            if(exit == "secret")
            {
                returnString += "";
            }
            else
            {
                returnString += " " + exit;
            }
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    /**
     * Returns the loot at the index location in the ArrayList for 
     * loot in the mansion
     * @param the index which the item is at
     * @return the item at the selected index
     */
    public Item getLoot(int index)
    {
        return loot.get(index);
    }
    
    /**
     * Sets an item into a set room 
     * @param teh name of the item being placed in the room
     */
    public void setLoot(Item newItem)
    {
        loot.add(newItem);
    }
}

