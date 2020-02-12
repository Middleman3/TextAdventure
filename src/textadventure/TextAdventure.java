
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
public class TextAdventure 
{
	private static final boolean test = true;
    static Scanner userInput = new Scanner(System.in);
    static Random rand = new Random();
    public static Creature player;
	static boolean gameOver = false;
    
	
	// Setup Functions
    public static void main(String[] args)  {
		Adventure adventure = Adventure.WolfsDen();
		
        player = createPC();
		player.locale = adventure.startingLocation;
        while (true)
        {//Wrong location
            explore(player.locale);
			gameOver = !player.alive;
            if (gameOver) break;
        }
    }
	public static Creature createPC() {
        // Create Weapon Options
        LinkedHashMap<String, execution> options = new LinkedHashMap();
		
        // Prompt Player for Character Creation (test sets default values to avoid User Input
        String gender =			(test)? "male": prompt("What is your gender?");
        String race =			(test)? "elf": prompt("What is your race (human, dwarf, elf, etc.)");
        String appearance =		(test)? "Tall, with white hair and green eyes. In a gray cloak.": prompt("Describe your appearance");
        String name =			(test)? "Zanos Moonseer": prompt("What is your name?");

        // Create Character
        String pcDescription = "This "+gender.toLowerCase()+" "+race.toLowerCase()+" is the hero of this tale.";
        Creature pc = new Creature(name, "player", pcDescription, appearance, 20, 10);
		
		// Give Equipment
		options.put("Sword and Shield", ()-> {
			Item weapon = Item.createWeapon("Sword", "This is the classic weapon of choice.", "a sword with a sharp steel blade and hilt of gilded silver.", 8);
			Item shield = Item.createArmor("Shield", "No warrior leaves home without a trusty shield.", "a shield with shining steel engraved with the emblem of your forefathers.", 2);
			pc.take(shield);
			pc.equip(shield);
			pc.take(weapon);
			pc.equip(weapon);
		});
		options.put("Bow and Arrow", () -> {
			Item weapon = Item.createWeapon("Bow and Arrow", "This weapon is silent, swift, and deadly at range.", "A birchwood shortbow with a satisfying pull to the string.", 8);
			pc.take(weapon);
			pc.equip(weapon);
		});
		options.put("Double Handaxes", () -> {
			Item weapon = Item.createWeapon("Double Handaxes", "Two weapons is always better than one.", "a set of twin axes with a crude design, but they have razor-sharp edges.", 12);
			pc.take(weapon);
			pc.equip(weapon);
		});
		options.put("Rock", () -> {
			Item weapon = Item.createWeapon("rock", "You got a rock.", "It's kinda dirty.", 2);
			pc.take(weapon);
			pc.equip(weapon);
		});
        choose("weapon", options, "n");
        return pc;
    }	
	
	// Gameplay Functions
    
        // Exploration
    public static void explore(Location location) {
        location.display();
		
		// Call explore every time.
		if (!location.creatures.isEmpty()) 
        {
            combat(location.creatures);
            return;
        }
        
		LinkedHashMap<String, execution> choices = new LinkedHashMap();

		choices.put("Investigate Something", () -> location.Investigate());
		choices.put("Move on from this location", () -> {
			location.Leave();
		});
		if (!location.loot.isEmpty()) 
			choices.put("Loot the room", () -> location.AquireLoot());
		choices.put("Other options", () -> options());

		choose("action", choices);
    }
    
        // Combat Functions
    public static void combat(ArrayList<Creature> creatures) {
        
        // Create Collections to Keep Track of Creatures in Battle
        TreeMap<Integer, Creature> initiative = new TreeMap(Collections.reverseOrder());
        ArrayList<Creature> allies = new ArrayList();
        ArrayList<Creature> enemies = new ArrayList();
        
        // Populate Lists
        initiative.put(player.roll(), player);
        allies.add(player);
        for (Creature creature : creatures)
        {
            initiative.put(creature.roll(), creature);
            if (creature.tag.equals("enemy"))
                enemies.add(creature);
            else
                allies.add(creature);
        }
        // Check False Alarm
        if (enemies.size() == 0) return;
        print(  "\n========= Combat! =========");
        boolean inCombat = true;
        while (inCombat) // One Round
        {
            // One Turn
            for (Map.Entry<Integer, Creature> pair : initiative.entrySet())
            {
                Creature creature = pair.getValue();
                if (creature.alive) print(  "========= " + creature.name + "'s Turn =========");
                if (creature.tag.equals("enemy")) // Enemy AI
                {
                    if (!creature.alive) 
                    {
                        initiative.remove(pair.getKey());
                        enemies.remove(creature);
                        if (enemies.isEmpty()) inCombat = false;
                        break;
                    }
                        Creature target = allies.get(rand.nextInt(allies.size()));
                        creature.attack(target);
                }
                else if (creature.tag.equals("player")) // Player's Turn
                {
                    Location tmp = player.locale;
                    playerTurn(enemies);
                    if (!player.locale.equals(tmp)) return;
                } 
                else // Ally AI
                {
                    if (!creature.alive) 
                    {
                        initiative.remove(pair.getKey());
                        allies.remove(creature);
                        break;
                    }
                    Creature target = enemies.get(rand.nextInt(enemies.size()));
                    creature.attack(target);
                }
            }
        }
        //.Remove enemies, add corpses, and handle loots.
    }
    public static void playerTurn(ArrayList<Creature> enemies) {
        if (!player.alive) 
        {
            gameOver = true;
            return;
        }
        // Populate Choices List
        LinkedHashMap<String, execution> options = new LinkedHashMap();
        options.put("Attack", ()-> attackEnemy(enemies)); 
        options.put("Flee", ()-> flee());
        options.put("Other Options", () -> { options(); playerTurn(enemies); });
        choose("action", options, "n");
    }
	public static void attackEnemy(ArrayList<Creature> enemies) {
		// Populate Option List
		LinkedHashMap<String, execution> entryOptions = new LinkedHashMap();

		int optionCount = 0;
		for (Creature enemy : enemies)
		{
			if (enemy.hidden) 
				continue; // Enemy hidden.

			// Label and Add String for Selection
			String label = ++optionCount + ": " + enemy.name;
			entryOptions.put(label, () -> player.attack(enemy));
		}
		TextAdventure.choose("target of attack", entryOptions, "n");
	}
	public static void flee() {
		if (player.roll() >= 10)
			player.locale.Leave();
		else
			print("You attempt to flee, but you are blocked by the enemy!");
	}
	
	// Menu Functions
    public static void options() {
        print(  "========= Menu =========");
        // Ask for action
        LinkedHashMap<String, execution> choices = new LinkedHashMap();
		choices.put("Inventory", () -> { Inventory(); options(); });
		choices.put("Character Info", () -> { CharacterInfo(); options(); });
        choices.put("Quit Game", () -> System.exit(0));
        choose("action", choices);
    }
	public static void Inventory() {
        print(  "========= Inventory =========");
        // Create Option List
        LinkedHashMap<String, execution> itemOptions = new LinkedHashMap();

        // Populate Option List
        for (Item item : player.inventory)
        {
            itemOptions.put(item.name, () -> 
            {
                print(  "========= " + item.name + " =========");
                print(item.appearance);
                print(item.description);
                ItemActions(item);
                Inventory();
            });
        }
        TextAdventure.choose("item to select", itemOptions, "nes");
    }
    public static void ItemActions(Item item) {
        // Populate Choices List
        LinkedHashMap<String, execution> choices = new LinkedHashMap();
		choices.put("Drop", () -> player.drop(item));

		// Get Choice
        choose("action", choices, "nes");
    }
    public static void CharacterInfo() {
        print(  "========= Character Info =========");
        print(player.name);
        print(player.appearance);
        print(player.description);
        print("Hit Points: " + player.hitPoints);
        print("Armor Class: " + player.armorClass);
        print("Location:" + player.locale.title);
        if (player.weapon != null) print("Equipped Weapon: " + player.weapon.name);
        if (player.armor != null) print("Equipped Armor: " + player.armor.name + "\n");
    }
	
	// Input and Output Functions
	public interface execution  {
		void execute();
	} // Functional Interface
	public interface contingent  {
		boolean determine();
	} // Functional Interface
    public interface selection {
        execution getExecution(Selectable selection);
    } // Functional Interface
	public static void choose(String label, LinkedHashMap<String, execution> options) {
        
        if (options.size() == 0)
        {
            print("You have no options!");
            return;
        }
        
        // Prepare List 
        options.put("Exit", () -> {});
        options = numberOptions(options);
		ArrayList<String> optionList = new ArrayList();
		
		// Prompt for item
        print("Choose your " + label + ":");
		
		// Print items
        for (Map.Entry<String, execution> pair : options.entrySet()) 
        {	// For each item in choices
			String option = pair.getKey();
			print(option);
			optionList.add(option);
        }
        int fakeIndex = promptInt("Choose Wisely...", optionList.size()); // Get Choice from actually visible items
		String option = optionList.get(fakeIndex-1);
		
		options.get(option).execute();
    }
    public static void choose(String label, LinkedHashMap<String, execution> options, String args) {
        
        if (options.size() == 0)
        {
            print("You have no options!");
            return;
        }
        
        // If choosing between one option and Exit is necessary
        if (!args.contains("s"))  if (options.size() == 1) 
        {
            for (Map.Entry<String, execution> option : options.entrySet())
            {
                String choice = option.getKey();
                print("There's only one choice: " + choice);
                option.getValue().execute();
                return;
            }
        } 

        // If calls for Exit Option
        if (args.contains("e")) options.put("Exit", () -> {}); 
        
        // If calls for Numbering
        if (args.contains("n")) options = numberOptions(options);
            
        ArrayList<String> optionList = new ArrayList();
		
		// Prompt for item
        print("Choose your " + label + ":");
		
		// Print items
        for (Map.Entry<String, execution> pair : options.entrySet()) 
        {	// For each item in choices
			String option = pair.getKey();
			print(option);
			optionList.add(option);
        }
        int fakeIndex = promptInt("Choose Wisely...", optionList.size()); // Get Choice from actually visible items
		String option = optionList.get(fakeIndex-1);
		
		options.get(option).execute();
    }
    public static boolean determine(String label, LinkedHashMap<String, contingent> choices) {
        
		ArrayList<String> choiceList = new ArrayList();
		choices.put("Exit", () -> {return false;}); // Allow Exit
		
		// Prompt for item
        print("\nChoose your " + label + ":");
		
		// Print items
        for (Map.Entry<String, contingent> pair : choices.entrySet()) 
        {	// For each item in choices
			String choice = pair.getKey();
			print(choice);
			choiceList.add(choice);
        }
        int fakeIndex = promptInt("Choose Wisely...", choiceList.size()); // Get Choice from actually visible items
		String choice = choiceList.get(fakeIndex-1);
		
		return choices.get(choice).determine();
    }
	public static LinkedHashMap<String, execution> getOptions(ArrayList<? extends Selectable> list, selection selection) {
        // Populate Option List
        LinkedHashMap<String, execution> options = new LinkedHashMap();
		
        for (Selectable selectable : list)
        {
            if (selectable.hidden)
				continue; // Entry hidden.
            
            // Label and Add String for Selection
            int duplicate = 0;
            String label = selectable.tag;
            while (options.containsKey(label)) 
            { 
                label = String.format(selectable.tag + " (%d)", ++duplicate);
            } 

			options.put(label, selection.getExecution(selectable));
        }
        return options;
    }
    private static LinkedHashMap<String, execution> numberOptions (LinkedHashMap<String, execution> options) {
        LinkedHashMap<String, execution> numbered = new LinkedHashMap();
        int optionCount = 0;
        // Number Options
        for (Map.Entry<String, execution> option : options.entrySet())
        {
            String labeled = ++optionCount + ": " + option.getKey();
            numbered.put(labeled, option.getValue());
        }
        return numbered;
    }
	public static String prompt(String Message) {
			print(Message);
			String input = userInput.nextLine();
			while (input.equals(""))
			{
				print("Please Type something in.");
				input = userInput.nextLine();
			}
            return input;
    }
    public static int promptInt(String Message, int max) {
        print(Message);
        int input = -1;
        while (true)
        {
            print("Please enter an integer between 1 and " + max);
            try  { input = userInput.nextInt(); }
            catch (Exception e)  { print("Please type your choice as a number."); }
            
            if (1 <= input && input <= max) return input;
        }
    }
    public static int promptInt(String Message, int min, int max) { 
            print(Message);
            int input = -1;
            while (true)
            {
                print("Please enter an integer between " + min + " and " + max);
                try {  input = userInput.nextInt(); }
                catch (Exception e) { print("Please type your choice as a number.");  }
                if (min <= input && input <= max) return input;
            }
    }
    public static void print (String message) { 
        System.out.println(message);
    }
}