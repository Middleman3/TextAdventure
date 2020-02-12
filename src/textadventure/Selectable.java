/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import textadventure.TextAdventure.contingent;
import textadventure.TextAdventure.execution;
import static textadventure.TextAdventure.print;

/**
 *
 * @author the1jonbrown
 */
public abstract class Selectable 
{
	public String name, tag, description, appearance, hiddenDetail;
	public boolean locked, trapped, hidden;
	public int investigateDC;
	public Trap trap;
    public Lock lock;
	public Feature facade;
	
	public Selectable(String name, String tag, String description, String appearance) {
		this.name = name;
		this.tag = tag;
		this.description = description;
		this.appearance = appearance;
	}
	
	public void addHiddenDetail(String detail, int dc) { 
		this.hiddenDetail = detail;
		this.investigateDC = dc; 
	}
	
	public boolean investigate(int check) {
		print(appearance + ": " + description);
		if (check >= investigateDC)
		{
			if (this.hiddenDetail != null) print(this.hiddenDetail);
			if (this.trapped) if (check >= trap.findDC)
			{
			print(trap.findDescription);
			trap.hidden = false;
			}
			return true;
		}
		print("Nothing Interesting Here.");
		return false;
	}
	public void trap(Trap trap) {
        this.trapped = true;
        this.trap = trap;
		trap.parent = this;
    }
    public void lock(Lock lock) {
        this.locked = true;
        this.lock = lock;
		lock.parent = this;
    }
    public void hide(Feature facade) {
        this.hidden = true;
        this.facade = facade;
		facade.secret = this;
    }
	public void reveal()  {
        this.hidden = false;
        print("A hidden " + this.tag + " is revealed!");
    }
	
	// TO DO: incorporate Strategy pattern
	public boolean encounterLock() {
		Creature creature = TextAdventure.player;
		print("It is locked.");
			
		// Populate Choices List
		LinkedHashMap<String, contingent> choices  = new LinkedHashMap();
		choices.put("Pick lock", () -> this.lock.pick(creature.roll()));
		choices.put("Break lock", () -> this.lock.ruin(creature.roll()));
		choices.put("Use key", () -> this.lock.unlock(creature));
		
		return TextAdventure.determine("action", choices);
	}
	public boolean encounterTrap() {
		Creature creature = TextAdventure.player;
		
		// Trigger Trap if Hidden
		if(this.trap.hidden) return this.trap.trigger(creature);
		
		print(this.trap.findDescription);

		// Populate Choices List                
		LinkedHashMap<String, contingent> choices  = new LinkedHashMap();
		choices.put("Disarm", () ->  this.trap.disarm(creature.roll())); 
		choices.put("Trigger", () -> this.trap.trigger(creature));
		choices.put("Leave", () -> false );
		
		// Get Choice
		return TextAdventure.determine("action", choices);
	}
}
