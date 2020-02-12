package textadventure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import textadventure.TextAdventure;
import textadventure.TextAdventure.execution;
import static textadventure.TextAdventure.choose;
import static textadventure.TextAdventure.print;

public class Location 
{
    String title, description;
    ArrayList<Entry> entries;
    ArrayList<Item> loot;
    ArrayList<Feature>  features;
    ArrayList<Creature>  creatures;

    public Location(String title, String description) {
        this.title = title;
        this.description = description;
		this.entries = new ArrayList();
		this.loot = new ArrayList();
		this.features = new ArrayList();
		this.creatures = new ArrayList();
		
    }
    
    public void display() {
		print("\nYour location: " + title);
		print(description);
		// Creatures
        if (!creatures.isEmpty())
        {
            print("In here you see...");
            for (int i = 0; i < creatures.size(); i++)
            {
                if (creatures.get(i) != null) print(creatures.get(i).appearance);
            }
        }
		// Features
		if (!features.isEmpty())
        {
			print("There is...");
            displayHelp(this.features);
        }
		// Loot 
        if (!loot.isEmpty())
        {
            print("Looking around you find...");
            displayHelp(this.loot);
        }
		//Entries
		print("Leading elsewhere is... ");
        displayHelp(this.entries, true);
    }
	public void displayHelp (ArrayList<? extends Selectable> list) {
		int optionCount = 0;
        for (Selectable option : list)
        {
            if (!option.hidden) print(++optionCount + ": " + option.appearance);
        }
	}
    public void displayHelp (ArrayList<Entry> list, boolean dummy) {
		int optionCount = 0;
        for (Entry option : list)
        {
            if (!option.hidden) print(++optionCount + ": " + labelEntry(option));
        }
	}
    public void Investigate() {
		// Populate Choices List
		LinkedHashMap<String, execution> options = new LinkedHashMap();
		
		options.put("Investigate an Entryway", ()-> investigateHelper(this.entries));
		if (hasVisibleOptions(this.loot)) 
			options.put("Investigate an Item", ()-> investigateHelper(this.loot));
		if (hasVisibleOptions(this.features)) 
			options.put("Investigate something else", () -> investigateHelper(this.features));
		
        choose("action", options, "ne");
    }
	private void investigateHelper(ArrayList<? extends Selectable> list) {
        // Populate Option List
        LinkedHashMap<String, execution> options = TextAdventure.getOptions(list,(selectable) ->
        {
            return () -> selectable.investigate(TextAdventure.player.roll());
        });
		
        TextAdventure.choose("subject to investigate", options, "nes");
    }
    public void AquireLoot() {
        Creature player = TextAdventure.player;
        
        // Create Item List 
        LinkedHashMap<String, execution> options = TextAdventure.getOptions(this.loot, (Selectable selectable) ->
        {
            return () -> 
            {
                Item item = (Item) selectable;
                if (!player.take(item)) print("Your inventory is full.");
                else player.locale.loot.remove(item);
            };
        });
		// Add option to take all. 
		options.put("Take all items", () -> 
        {
            ArrayList<Item> items = new ArrayList();
			for (Selectable selectable : this.loot) // For each item in loot.
			{
				Item item = (Item) selectable;
                items.add(item);
			}
            player.take(items);
            player.locale.loot.clear();
		});
        TextAdventure.choose("item to pick up", options);
    }
    public void Leave() {
		Creature player = TextAdventure.player;
        // Populate Option List
        LinkedHashMap<String, execution> options = TextAdventure.getOptions(this.entries, (Selectable selectable) -> 
        {
            return () -> 
            {
                Entry entry = (Entry) selectable;
                entry.pass(player);
            };
        });
        TextAdventure.choose("next path", options, "ne");
    }
	
	private String labelEntry (Entry entry) {
		Creature player = TextAdventure.player;
		String label = entry.appearance;
		if (player.locale.equals(entry.outside))
			label += ": " + entry.inside.title;
		else if (player.locale.equals(entry.inside))
			label += ": " + entry.outside.title;
		return label;
	}
	private boolean hasVisibleOptions(ArrayList<? extends Selectable> list) {
		for (Selectable option : list)
		{ 
			if (!option.hidden) return true; 
		}
		return false;
	}
}