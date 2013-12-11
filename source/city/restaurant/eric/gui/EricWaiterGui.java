package city.restaurant.eric.gui;


import city.restaurant.eric.EricWaiterRole;
import city.restaurant.eric.interfaces.EricCustomer;
import gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class EricWaiterGui implements Gui
{
	// ---------------------------------- DATA ----------------------------------

	// RestDims used for positions of stuff (unchanging):
	private ArrayList<RestDim> TABLES = new ArrayList<RestDim>();
	private RestDim FRONT_DESK;
	private RestDim OUTSIDE;
	private RestDim IDLE_LOCATION;
	private RestDim COOK;
	private RestDim REVOLVING_STAND;
	private RestDim CASHIER;
	// RestDims used for positions of stuff (variable):
	private RestDim _position;
	private RestDim _destination;
	private boolean _goingSomewhere = false; // implies that _agent will be expecting the msgReachedDestination() message and that the food being carried needs to be reset to none.
	
	// String for what food item to display.
	private String _carrying = null;
	
	// Correspondence:
    private EricWaiterRole _role = null;
    // private RestaurantGui _restaurantGui; //TODO implement a system to appropriately replace this
    
    // -------------------------------- CONSTRUCTOR -----------------------------
    public EricWaiterGui(EricWaiterRole role)
    {
        _role = role;
        
        // Initialize values:
        // Unchanging:
        TABLES.add(new RestDim(EricAnimationConstants.TABLE0_POSX + EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.TABLE0_POSY - EricAnimationConstants.PERSON_HEIGHT));
        TABLES.add(new RestDim(EricAnimationConstants.TABLE1_POSX + EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.TABLE1_POSY - EricAnimationConstants.PERSON_HEIGHT));
        TABLES.add(new RestDim(EricAnimationConstants.TABLE2_POSX + EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.TABLE2_POSY - EricAnimationConstants.PERSON_HEIGHT));
        FRONT_DESK = new RestDim(EricAnimationConstants.FRONTDESK_X + EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.FRONTDESK_Y - EricAnimationConstants.PERSON_HEIGHT);
        OUTSIDE = new RestDim(EricAnimationConstants.OUTSIDE_X, EricAnimationConstants.OUTSIDE_Y);
        IDLE_LOCATION = new RestDim(EricAnimationConstants.NEXT_WAITER_X, EricAnimationConstants.NEXT_WAITER_Y);
        EricAnimationConstants.updateNextWaiter();
        COOK = new RestDim(EricAnimationConstants.COOK_POSX + EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.COOK_POSY - EricAnimationConstants.PERSON_HEIGHT);
        REVOLVING_STAND = new RestDim(EricAnimationConstants.REVOLVING_STAND_POSX, EricAnimationConstants.REVOLVING_STAND_POSY);
        CASHIER = new RestDim(EricAnimationConstants.CASHIER_POSX, EricAnimationConstants.CASHIER_POSY - EricAnimationConstants.PERSON_HEIGHT);
        // Variable:
        _position = new RestDim(OUTSIDE);
        _destination = new RestDim(IDLE_LOCATION);
    }
    
    // --------------------------------- PROPERTIES & MESSAGES ----------------------------------------
    // public void setRestGui(RestaurantGui rg) { _restaurantGui = rg; }
    public EricWaiterRole agent() { return _role; }
    public int getXPosition() { return _position.x; }
    public int getYPosition() { return _position.y; }
    public boolean isPresent() { return true; }
    public boolean atDestination() { return _position.equals(_destination); }
    public boolean wantsBreak() { return _role.wantsBreak(); }
    public boolean onBreak() { return _role.onBreak(); }
    public void msgBreaksOver() // from RestaurantGui
    {
    	_role.msgBreaksOver();
    }
    public void msgWantsABreak() // from WaiterAgent
    {
    	_role.msgWantABreak();
    }
    
    
    
    // ---------------------------------- COMMANDS --------------------------------------------

    // Makes the waiter go to the front desk, but does not notify anyone when he gets there; i.e. this function assumes that no other code is waiting for him to get there, so this motion may be safely intercepted.
    public void doGoIdle()
    {
    	_goingSomewhere = false;
    	_destination.set(IDLE_LOCATION);
    }
    
    public void doBringCustomerToTable(EricCustomer customer, int tableNumber)
    {
    	doGoToTable(tableNumber);
    	
    	// Hack (sorta) to pass the coordinates of the table to the CustomerGui (there might be a better way to do this)
    	customer.gui().tableIsHere(
    			TABLES.get(tableNumber).x - EricAnimationConstants.PERSON_WIDTH ,
    			TABLES.get(tableNumber).y + EricAnimationConstants.PERSON_HEIGHT
    			);
    }
    
    public void doGoToTable(int tableNumber)
    {
    	_destination.set(TABLES.get(tableNumber));
    	_goingSomewhere = true;
    }

    public void doGoToFrontDesk()
    {
        _destination.set(FRONT_DESK);
    	_goingSomewhere = true;
    }
    
    public void doGoToCook()
    {
    	_destination.set(COOK);
    	_goingSomewhere = true;
    }
    
    public void doGoToRevolvingStand()
    {
    	_destination.set(REVOLVING_STAND);
    	_goingSomewhere = true;
    }
    
    public void doTakeOrderToCook(String choice)
    {
    	if(choice != null) {
    		_carrying = choice.substring(0, ((choice.length() > 1) ? 2 : 1) ) + "?";
    	}
    	doGoToCook();
    }
    
    public void doTakeOrderToRevolvingStand(String choice)
    {
    	if(choice != null) {
    		_carrying = choice.substring(0, ((choice.length() > 1) ? 2 : 1) ) + "?";
    	}
    	doGoToRevolvingStand();
    }
    
    public void doDeliverFood(int tableNumber, String choice)
    {
    	if(choice != null) {
    		_carrying = choice.substring(0, ((choice.length() > 1) ? 2 : 1) );
    	}
    	
    	doGoToTable(tableNumber);
    }
    
    public void doGoToCashier()
    {
    	_destination.set(CASHIER);
    	_goingSomewhere = true;
    }
    
    public void doBringCheckToCustomer(EricCustomer customer, int tableNumber)
    {
    	doGoToTable(tableNumber);
    	
    	// Hack (sorta) to pass the coordinates of the table to the CustomerGui (there might be a better way to do this)
    	customer.gui().cashierIsHere(
    			CASHIER.x ,
    			CASHIER.y - EricAnimationConstants.PERSON_HEIGHT
    			);
    }
    
    public void doCleanTable(int tableNumber)
    {
    	doGoToTable(tableNumber);
    	// will add cleaning animation here, possibly
    }
    
    public void doNoBreak()
    {
    	// _restaurantGui.notGoingOnBreak(this);
    	// don't need to go idle unless we replace the break location to be different from the idle location
    }
    
    public void doGoOnBreak()
    {
    	// _restaurantGui.goingOnBreak(this);
    	doGoIdle(); // might change to going to other coordinates
    }
    
    public void doBackToWork()
    {
    	// _restaurantGui.goingBackToWork(this);
    	doGoIdle();
    }
    
    
    
    // -------------------------------------- METHODS -------------------------------------

    public void updatePosition()
    {
        if (_position.x < _destination.x) _position.x++;
        else if (_position.x > _destination.x) _position.x--;

        if (_position.y < _destination.y) _position.y++;
        else if (_position.y > _destination.y) _position.y--;

        // Check to see of the position changed and the new position is equal to the destination.
        if (atDestination() && _goingSomewhere)
        {
           _role.msgReachedDestination();
           _goingSomewhere = false;
           _carrying = null;
        }
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.MAGENTA);
        g.fillRect(_position.x, _position.y, EricAnimationConstants.PERSON_WIDTH, EricAnimationConstants.PERSON_HEIGHT);
        
        if(_carrying != null)
        {
        	g.setFont(EricAnimationConstants.FONT);
        	g.setColor(Color.BLACK);
        	g.drawString(_carrying,
        			_position.x + EricAnimationConstants.PERSON_WIDTH,
        			_position.y + EricAnimationConstants.PERSON_HEIGHT
        			);
        }
    }
}
