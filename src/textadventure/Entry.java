package textadventure;

import static textadventure.TextAdventure.print;
public class Entry extends Selectable
{
    public Location inside, outside;

    public Entry(String name, String tag, String description, String appearance, Location inside, Location outside) {
        super(name, tag, description, appearance);
        this.inside = inside;
        this.outside = outside;
        this.locked = false;
        this.trapped = false;
    }

    public boolean pass (Creature creature) {
		
		// Encounter Trap if Present
        if (this.trapped) if(!encounterTrap()) return false;
		
        // Encounter Lock if Present
        if (this.locked) if(!encounterLock())   
        {
            print("You failed to get the " + this.tag + " open.");
            return false;
        }
		print("You go through the " + this.tag + "...");

		// Traverse the Entry
		if (creature.locale.equals(this.inside)) creature.locale = this.outside;
		else if (creature.locale.equals(this.outside)) creature.locale = this.inside; 
		return true;		
    }
}
