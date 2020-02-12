package textadventure;

import java.util.ArrayList;
import static textadventure.TextAdventure.print;

public class Adventure 
{
	String title;
	Location startingLocation;
	ArrayList<Location> locations;
	
        // Constructor 
	public Adventure(String title, Location initial, ArrayList<Location> locations)  {
            this.title = title;
			this.startingLocation = initial;
            this.locations = locations;
	}

	public static Adventure WolfsDen() {		
		String title = "Wolf's Den";
		
		ArrayList<Location> locations = new ArrayList();
		ArrayList<Selectable> secrets = new ArrayList();
		ArrayList<Feature> facades = new ArrayList();

		String startingAppearance = 
		"The open jaws of the wolf's head form a fifteen-foot-high canopy of\n"+
		"rock over the cave mouth, held up by natural pillars of rock.";

		//Create Locations
		Location startingLocation = new Location("Outside", startingAppearance);

		locations.add(new Location( "Cave Mouth", 
	"The ceiling rises to a height of twenty feet inside the cave. Torches in iron brackets\n"+
	"line the walls. From somewhere deep inside, you hear the echoing sounds of a flute. Some\n"+
	"of the notes are discordant... painfully so." + " Here the cave splits to the left and\n"+
	"right. Standing on a five-foot-high ledge between the divide are two feral-looking women\n"+
	"wearing shredded clothing and clutching spears."));
		
		locations.add(new Location("Guard Post", 
	"This five-foot ledge looks over the entrance to the cave. There are a set of stairs on each side."));
		
		locations.add(new Location("Wolf Den", 
	"A five-foot-high stone ledge overlooks this large cave, which has a smoldering campfire\n"+
	"on the far west end. The floor is covered with gnawed bones."));
		
		locations.add(new Location("Underground Spring", 
	"A gash in the rocky ceiling allows the gray light and cold drizzle of the outdoors to seep\n"+
	"into this dank, torchlit cave, where an underground spring forms a pool of water roughly \n"+
	"forty feet across and ten feet deep. A five-foot-high ledge to the north overlooks the pool.\n"+
	"A similar ledge spans the eastern wall, with a rough-hewn staircase leading up to it. A \n"+
	"few crates sit atop the eastern ledge."));
		
		locations.add(new Location("Cavern Storage Area", 
	"Several rotting wooden crates sit against the cave walls."));
		
		locations.add(new Location("Deep Caves", 
	"A maze of torchlit tunnels and caves expands in front of you. Bones lie strewn about the floor."));
		
		locations.add(new Location("South Cave", 
	"Between the scattered bones and torches stuck in the ground, a bedroll lies in the \n"+
	"middle of the room."));
		
		locations.add(new Location("North Cave", 
	"Between the scattered bones and torches stuck in the ground, a bedroll lies in the\n"+
	"middle of the room. You also see a few dolls, ripped and filthy."));
		
		locations.add(new Location("The Alpha's Den", 
	"The walls around the small cave area are covered from top to bottom with curtains\n"+
	"and tapestries made of animal hide and human skin. Some are marked with blood."));
		locations.add(new Location("Shrine Room", 
	"Rough-hewn stairs lead down to a torchlit cave and a bizarre sight: wide-eyed children\n"+
	"stand behind wooden bars and stare at you in terrified silence. The cave holds six\n"+
	"wooden cages, their lids held shut with heavy rocks. Two of the cages are empty, and\n"+
	"each of the others holds a pair of frightened children. A crude wooden statue stands\n"+
	"between the cages. It bears the rough likeness of a wolf-headed woman draped in garlands\n"+
	"of vines and night flowers. Piled around the statue's base is an incredible amount \n"+
	"of treasure. A woman in shredded clothes kneels before the statue. Behind the statue,\n"+
	"two maggot-ridden corpses hang from iron shackles bolted to the wall."));
		locations.add(new Location("Ring of Stone", 
	"A twenty-foot-diameter ring of stones dominates a rocky ledge on the mountainside. Within\n"+
	"the ring, you see spattered blood and small, gnawed bones. Lying on the ground outside\n"+
	"the circle are several spears stained with dry blood."));

		String passageAppearance = "a tunnel that leads to another area in the cave";
		String stairsAppearance = "a set of jagged stairs lead to another area in the cave";
		
		// Creating Pathways		
		addEntry("Cave Entrance", "passage", passageAppearance, locations.get(0), startingLocation);
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(2), locations.get(0));
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(3), locations.get(0));
		addEntry("Stone Staircase", "staircase", stairsAppearance, locations.get(2), locations.get(1));
		addEntry("Stone Staircase", "staircase", stairsAppearance, locations.get(3), locations.get(1));
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(5), locations.get(2));
		addEntry("Stone Staircase", "staircase", stairsAppearance, locations.get(4), locations.get(3));
		addEntry("Cave Ledge", "ledge", "This ledge rests five feet above the pond.", locations.get(5), locations.get(4));
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(6), locations.get(5));
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(7), locations.get(5));
		addEntry("Hide Curtains", "curtain", "Curtains made of animal hide hang from rough twine affixed to the rock walls.", locations.get(8), locations.get(5));
		addEntry("Stone Staircase", "staircase", stairsAppearance, locations.get(9), locations.get(5));
		addEntry("Cave Passage", "passage", passageAppearance, locations.get(8), locations.get(5));
		secrets.add(addEntry("Stone Staircase", "staircase", stairsAppearance, locations.get(10), locations.get(8)));
		
		// Adding Features
		facades.add(addFeature(locations.get(8), new Feature("Hide Curtains", "Hide Curtains", "curtain", "Curtains made of animal hide hang from rough twine affixed to the rock walls.")));
		
		// Hiding Secrets
		secrets.get(0).hide(facades.get(0));
		
		// Adding Enemies

			// Guard Post
		addEnemy(locations.get(1), Spawn.Werewolf("Davanka", "Davanka is a werewolf under Kiril's pack. She would die to defend her den.", "This woman is tall, muscular, filthy and frightening."));
		addEnemy(locations.get(1),Spawn.Werewolf("Aziana", "Aziana is a werewolf under Kiril's pack. She would die to defend her den.", "This woman is tall, muscular, filthy and frightening."));

			// Wolf Den
		addEnemy(locations.get(2), Spawn.Werewolf("Skennis", "Skennis is a werewolf under Kiril's pack. He would die to defend his den.", "This man is tall, muscular, filthy and frightening."));
			
			// To be continued...

		// Create and return Adventure
		return new Adventure(title, startingLocation, locations);
	}
	
	public static Entry addEntry (String description, String tag, String appearance, Location inside, Location outside) {
		Entry entry = new Entry(description, tag, description, appearance,  inside, outside);
		if (!(inside.entries.add(entry) && outside.entries.add(entry)))
			print("Error: Failed to add entry!");	
		return entry;
	}
	public static Creature addEnemy(Location location, Creature enemy)  { 
		if (!location.creatures.add(enemy)) print("Error: Failed to add enemy!");
		return enemy;
	}	
	public static Feature addFeature(Location location, Feature feature) {
		if (!location.features.add(feature)) print("Error: Failed to add feature!");
		return feature;
	}	
	public static Item AddItem(Location location, Item item) {
		if (!location.loot.add(item)) print("Error: Failed to add item!");
		return item;
	}
	
}
