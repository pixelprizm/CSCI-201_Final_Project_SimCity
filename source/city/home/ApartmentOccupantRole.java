package city.home;

import city.PersonAgent;
import city.home.gui.ApartmentOccupantGui;

public class ApartmentOccupantRole extends HomeOccupantRole {
	
	// note: This class exists basically only so that we can use instanceof stuff in PersonAgent.

	public ApartmentOccupantRole(PersonAgent person, Home home)
	{
		super(person, home);
		_gui = new ApartmentOccupantGui(this);
	}
	
	// PROPERTIES
	public int apartmentNumber() { return ((Apartment)_home).number(); }

}
