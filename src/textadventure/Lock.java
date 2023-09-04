
package textadventure;

import static textadventure.TextAdventure.print;

public class Lock 
{
    String description;
    public int pickDC, breakDC;
    public Item key;
	public Selectable parent;

    public Lock(String description, int pickDC, int breakDC) {
        this.description = description;
        this.pickDC = pickDC;
        this.breakDC = breakDC;
        this.key = null;
    }

    public void assignKey(Item key) { this.key = key; }
    public boolean unlock(Creature creature) { 
	if (creature.inventory.contains(this.key))
		{
			parent.locked = false;
			print("You unlock it with the key!");
			return true;
		}
		return false;
	}
    public boolean pick(int check) {  
		if (check >= pickDC)
		{
			parent.locked = false;
			print("You've picked the lock!");
			return true;
		}
		return false;
	}	
    public boolean ruin(int check) { 
		if (check >= breakDC)
		{
			parent.locked = false;
			print("You've broke the lock!");
			return true;
		}
		return false; 
	}
}

class CombinationLock extends Lock 
{
    private String combination;
  
    public CombinationLock(String description, String combination) {
        super(description, 0, 0);
        this.combination = combination;
    }
   
    @Override
    public boolean unlock(Creature creature) { 
        return false;
    }
    
    public boolean unlockWithCombination(String inputCombination) {
        if (this.combination.equals(inputCombination)) {
            this.parent.locked = false;
            print("The lock clicks open!");
            return true;
        }
       
        print("The lock stays firmly shut.");
        return false;
    }
}
