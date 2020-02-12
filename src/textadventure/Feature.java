package textadventure;

import java.util.ArrayList;

public class Feature extends Selectable
{
	int investigateDC;
	ArrayList<Item> loot;
	
	public Selectable secret;
	
	public Feature(String name, String description, String tag, String appearance) {
			super(name, description, tag, appearance);
            this.loot = new ArrayList();
	}
}
