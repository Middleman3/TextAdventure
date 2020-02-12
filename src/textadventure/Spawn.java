package textadventure;

public abstract class Spawn 
{
    public static Creature Werewolf(String name, String description, String appearance) {
        return new Creature(name, "enemy", description, appearance, 10, 10);
    }
    public static Creature Wolf(String name, String description, String appearance) {
            return new Creature(name, "enemy", description, appearance, 5, 12);
	}
}
