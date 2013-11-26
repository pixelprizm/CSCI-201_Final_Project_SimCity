package city.home;

import city.PersonAgent;
import city.home.gui.HouseOccupantGui;

public class HouseOccupantRole extends HomeOccupantRole {
	
	// note: This class exists basically only so that we can use instanceof stuff in PersonAgent.

	public HouseOccupantRole(PersonAgent person, Home home)
	{
		super(person, home);
		_gui = new HouseOccupantGui(this);
	}

}
