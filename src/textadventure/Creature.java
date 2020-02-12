package textadventure;
import java.util.ArrayList;
import java.util.Random;
import static textadventure.TextAdventure.print;
public class Creature 
{
    static Random rand = new Random();
    String name, tag, description, appearance;
    int hitPoints, armorClass;
    boolean alive, hidden;
    ArrayList<Item> inventory, magicItems;
    Item weapon, armor, shield, magicItem;
    Location locale;

    public Creature(String name, String tag, String description, String appearance, int hitPoints, int armorClass) {
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.appearance = appearance;
        this.hitPoints = hitPoints;
        this.armorClass = armorClass;
		this.inventory = new ArrayList<Item>();
		this.magicItems = new ArrayList<Item>();
		this.alive = true;
        this.weapon = null;
        this.armor = null;
    }	

    public boolean take(Item item) {
        return inventory.add(item);
    }
    public boolean take(ArrayList<Item> items)  {
        return this.inventory.addAll(items);
    }
    public void drop(Item item)  {
        
		// Remove from Inventory
        if (!inventory.remove(item)) print("Warning, Attempted to remove missing item!");

        // Potential Unequip
        if (item.equals(this.weapon)) this.weapon = null;
        else if (item.equals(this.armor)) this.armor = null;
        
        // Add it to locale Loot
        boolean success = locale.loot.add(item);

        // Print Drop 
        if (success) print("You dropped the " + item.name + " in the " + locale.title);
        else print("A starry portal opened up and that " + item.name + " fell into it, lost forever...");
        updateAC();
    }
    public void equip(Item item) {
        if (!inventory.contains(item))
        {
            print("You don't have that item in your inventory!");
            return;
        }
        String itemTag = item.tag;
        if ("weapon".equals(itemTag))
        {
            if (this.weapon == null)
            {
                this.weapon = item;
                print(item.name + " equipped!");
            }
            else
            {
                // Guaruntee Previous Item is in Inventory
                if (!inventory.contains(this.weapon)) take(this.weapon);
                print("Swapped out "+this.weapon+" for your "+item.name+"!");
            }
        } 
        else if ("armor".equals(itemTag))
        {
            if (this.armor == null)
            {
                this.armor = item;
                print(item.name + " equipped.");
            }
            else
            {
                // Guaruntee Previous Item is in Inventory
                if (!inventory.contains(this.armor)) take(this.armor);
                print("Swapped out "+this.armor+" for your "+item.name+".");
            }
        }
        else if ("magicItem".equals(itemTag))
        {
            if (this.magicItem == null)
            {
                this.magicItem = item;
                print(item.name + " equipped!");
            }
            else
            {
                // Guaruntee Previous Item is in Inventory
                if (!inventory.contains(this.magicItem)) take(this.magicItem);
                print("Swapped out "+this.magicItem+" for your "+item.name+"!");
            }
        }
        else print("You can't equip a " + item.name + "."); 
        updateAC();
    }

    public void attack (Creature target) {
        int damage = 1;
        if (this.weapon != null) damage = this.weapon.damage;
        
        if (this.roll() >= target.armorClass)
        {
            print(this.name + " attacked " + target.name);
            target.takeDamage(rand.nextInt(damage) + 1);
        }
        else
        {
            print(this.name + " missed " + target.name + "!");
        }
        
    }

    public void takeDamage(int dmg) {
        this.hitPoints -= dmg;
        print(this.name + " took " + dmg + " damage!");
        if (this.hitPoints <= 0) die();
    }
    public void gainHealth(int hp) {
            this.hitPoints += hp;
            print(this.name + " gained " + hp + " health!");
    }

	public void die() {
		this.hitPoints = 0;
		print(this.name + " died!");
		alive = false;
	}
	
    public int roll() { return rand.nextInt(20)+1; }
    //public int roll(string bonus) { return rand.nextInt(20)+1+bonus; }


    public void updateAC() {
		int bonus = 0;
        if (this.armor != null)  bonus += this.armor.acBonus;
		if (this.shield != null) bonus += this.shield.acBonus;
		this.armorClass = 10 + bonus;
    }
}
