package textadventure;
import java.util.Random;
import static textadventure.TextAdventure.print;

public class Trap 
{
	public String findDescription, triggerEvent;
	public int  findDC, trapDC, damage;
	boolean hidden;
	Selectable parent;
	
	public Trap(String find, String trigger, int findDC, int trapDC, int damage) {
            this.findDescription = find;
            this.triggerEvent = trigger;
            this.findDC = findDC;
            this.trapDC = trapDC;
            this.damage = damage;
            this.hidden = true;
	}

	public boolean disarm(int check) {
		if (check >= trapDC)
		{
			parent.trapped = false;
			print("You've disarmed the trap!");
			return true;
		}
		return false;
	}
	public boolean trigger(Creature creature) {
		print(this.triggerEvent);
        attack(creature);
		return false;
	}
	public boolean find(int check) {
            return check >= findDC;
	}
	
	public void attack (Creature target) {
            Random rand = new Random();
            int attackDamage = rand.nextInt(this.damage) + 1;
            target.takeDamage(attackDamage);
	}
}
