package textadventure;

// Things that can be picked up by creatures and stored inside of containers
public class Item extends Selectable
{
    int  damage, acBonus;

	// Constructor
    public Item(String name, String tag, String description, String appearance, int damage, int acBonus) {
		super(name, tag, description, appearance);
        this.acBonus = acBonus;
        this.damage = damage;
    }

	// Type Constructors
    public static Item createWeapon(String name, String description, String appearance, int damage)  {	
        return new Item(name, "weapon", description, appearance, damage, 0);
    }
    public static Item createArmor(String name, String description, String appearance, int ac) {	
        return new Item(name, "armor", description, appearance, 0, ac);
    }
    public static Item createShield(String name, String description, String appearance, int ac) {	
        return new Item(name, "shield", description, appearance, 0, ac);
    }
}
