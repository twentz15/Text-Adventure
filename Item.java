/**
 * Creates items to put in the mansion when the player plays and creates the mansion to play
 * has only a name for the item
 * 
 * @author Tom Wentz 
 * @version April 4th 2016
 */
public class Item
{
    private String itemName;

    /**
     * Constructor for objects of class Item
     * Creates a simple item with a name
     * @param name of the item
     */
    public Item(String itemName)
    {
        this.itemName = itemName;
    }
    
    /**
     * Returns the name of the item
     * @param name of the item
     */
    public String getItemName()
    {
        return itemName;
    }
       

}

   