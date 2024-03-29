package city;

import gui.ImageAtlas;
import gui.PersonInfoRefreshable;
import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.awt.Image;
import java.lang.reflect.Type;
import java.util.*;

import javax.swing.ImageIcon;

import city.interfaces.Person;
import city.home.*;
import city.bank.*;
import city.market.*;
import city.restaurant.*;
import city.restaurant.eric.*;
import city.restaurant.omar.*;
import city.restaurant.ryan.*;
import city.restaurant.yixin.*;
import city.transportation.CommuterRole;
import agent.*;

public class PersonAgent extends Agent implements Person
{
	// Constants:
	public static final int RICH_LEVEL = 250;
	public static final int POOR_LEVEL = 10;
	private static final int MONEY_LOW_LEVEL = 20;
	private static final int MONEY_MID_LEVEL = 50;
	private static final int MONEY_HIGH_LEVEL = 80;
	private static final double NOURISHMENT_PER_MEAL = 5;
	private static final double HOURLY_NOURISHMENT_DECREASE = 0.5;
	private static final double INITIAL_NOURISHMENT = 5;
	private static final double NOURISHMENT_HUNGRY_MAX = 2;
	private static final double NOURISHMENT_FULL_MIN = 6;
	private static final double WEEKLY_RENT = 10;
	
	// --------------------------------------- DATA -------------------------------------------
	// Personal data:
	private String _name;
	
	// Role data:
	private List<Role> _customerRoles = new ArrayList<Role>(); // these are the customer roles that are dormant e.g. EricCustomerRole, MarketCustomerRole
	private Role _currentRole; // this should never be null
	private boolean _sentCmdFinishAndLeave = false;
	private Role _nextRole; // this is the Role that will become active once the current transportation finishes.
	private CommuterRole _commuterRole;
	public Role _occupation = null;
	// private boolean _weekday_notWeekend;
	private HomeOccupantRole _homeOccupantRole;
	// private HomeBuyingRole _homeBuyingRole; // Will handle buying an apartment or house
	private BankCustomerRole _bankCustomerRole = null;
	private boolean _dontCheckPlaceOpen = false;
	
	// Commands for scenarios
	private List<String> _actionsToDo = new ArrayList<String>();
	
	// State data:
	private double _money;
	private boolean _paidRentThisWeek = false;
	enum WealthState { RICH, NORMAL, BROKE, POOR }
	enum NourishmentState { HUNGRY, NORMAL, FULL }
	/** Contains state data about this person; this data can change (some parts, like wealth, don't change often). */
	class State
	{
		// Constructor
		public State() {
			nourishmentTimer.schedule(new TimerTask() { public void run() { hourlyHungerChange(); }}, Time.getRealTime(1));
		}
		
		// Nourishment/hunger
		double nourishmentLevel = INITIAL_NOURISHMENT;
		Timer nourishmentTimer = new Timer();
		NourishmentState nourishment() {
			if(nourishmentLevel < NOURISHMENT_HUNGRY_MAX) {
				return NourishmentState.HUNGRY;
			}
			else if(nourishmentLevel < NOURISHMENT_FULL_MIN) {
				return NourishmentState.NORMAL;
			}
			else {
				return NourishmentState.FULL;
			}
		}
		private void hourlyHungerChange() {
			if(nourishmentLevel >= HOURLY_NOURISHMENT_DECREASE) {
				nourishmentLevel -= HOURLY_NOURISHMENT_DECREASE;
			}
			else {
				nourishmentLevel = 0;
			}
			stateChanged();
			nourishmentTimer.schedule(new TimerTask() { public void run() { hourlyHungerChange(); }}, Time.getRealTime(1));
		}
		
		// Wealth
		/** Get the current wealth state, based on money and occupation status. */
		WealthState wealth()
		{
			if(totalMoney() < POOR_LEVEL) {
				return (_occupation != null) ? WealthState.BROKE : WealthState.POOR;
			}
			else if(totalMoney() < RICH_LEVEL) {
				return WealthState.NORMAL;
			}
			else {
				return WealthState.RICH;
			}
		}
		
		// Pocket Money
		boolean haveLotsOfMoney() {
			return _money > MONEY_HIGH_LEVEL;
		}
		boolean haveLowMoney() {
			return _money < MONEY_LOW_LEVEL;
		}
		double amountToWithdrawOrDeposit() {
			return Math.abs(_money - MONEY_MID_LEVEL);
		}
		
		// Time
		double time()
		{
			return Time.currentTime();
		}
		Time.Day today()
		{
			return Time.today();
		}
	}
	State _state = new State();
	
	// Notify when refreshing info:
	private PersonInfoRefreshable _personInfoPanel;
	
	//Images
	ImageIcon a;
	ImageIcon b;
	Image _picture;
	
	
	// ------------------------------------------- CONSTRUCTORS & SETUP --------------------------------------------
	public PersonAgent(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType, PersonInfoRefreshable personInfoPanel, List<String> actionsToDo)
	{
		this(name, money, occupationType, weekday_notWeekend, housingType, personInfoPanel);
		_actionsToDo.addAll(actionsToDo);
	}
	// This constructor is for unit testing
	public PersonAgent(String name) { _name = name; }
	/**
	 * Constructor
	 * @param name Name
	 * @param money Initial amount of money
	 * @param occupationType I.e. Restaurant Cashier or Restaurant Host or Bank Teller etc.
	 * @param housingType House or Apartment
	 */
	public PersonAgent(String name, double money, String occupationType, boolean weekday_notWeekend, String housingType, PersonInfoRefreshable personInfoPanel)
	{
		_name = name; 
		_money = money;
		_personInfoPanel = personInfoPanel;
		// setWorkDays(weekday_notWeekend);
		acquireOccupation(occupationType);
		if(_occupation != null) { AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(), "Acquired occupation " + _occupation.typeToString() + "."); }
		else { AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(),"Acquired null occupation."); }
		acquireHome(housingType);
		
		generateAndSetCommuterRole();
		setNextRole(_homeOccupantRole);
		
		// Randomly choose between the two images to display in the city's view.
		a = new ImageIcon(ImageAtlas.mapAtlas.get("MPerson"));
		b = new ImageIcon(ImageAtlas.mapAtlas.get("FPerson"));
		Random generator = new Random();
		int i = generator.nextInt(2);
		if(i == 1) {
			_picture = a.getImage();
		}
		else {
			_picture = b.getImage();
		}

		if(_personInfoPanel != null) _personInfoPanel.refreshInfo(this);
	}
	/** Sets _commuterRole to a new CommuterRole */
	public void generateAndSetCommuterRole()
	{
		_commuterRole = new CommuterRole(this, _homeOccupantRole.place()); // may replace null with _homeOccupantRole.place() to set the person's starting position
	}
	/** Acquires an available house or apartment and sets the _homeOccupantRole and _homeBuyingRole appropriately.
	 * @param homeType Either "house" or "apartment" */
	public void acquireHome(String homeType)
	{
		while(true)
		{
			if(!homeType.equals("house"))
			{
				List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
				for(ApartmentBuilding b : apartmentBuildings)
				{
					List<Apartment> apartments = b.apartments();
					for(Apartment a : apartments)
					{
						HomeOccupantRole newHomeOccupantRole = a.tryGenerateHomeOccupantRole(this);
						if(newHomeOccupantRole != null)
						{
							_homeOccupantRole = newHomeOccupantRole;
							// _homeBuyingRole = a.generateHomeBuyingRole(this);
							return;
						}
					}
				}
			}
			if(!homeType.equalsIgnoreCase("apartment"))
			{
				List<House> houses = Directory.houses();
				for(House h : houses)
				{
					HomeOccupantRole newHomeOccupantRole = h.tryGenerateHomeOccupantRole(this);
					if(newHomeOccupantRole != null)
					{
						_homeOccupantRole = newHomeOccupantRole;
						// _homeBuyingRole = null; // will eventually change this to HomeOwnerRole
						return;
					}
				}

				List<ApartmentBuilding> apartmentBuildings = Directory.apartmentBuildings();
				for(ApartmentBuilding b : apartmentBuildings)
				{
					List<Apartment> apartments = b.apartments();
					for(Apartment a : apartments)
					{
						HomeOccupantRole newHomeOccupantRole = a.tryGenerateHomeOccupantRole(this);
						if(newHomeOccupantRole != null)
						{
							_homeOccupantRole = newHomeOccupantRole;
							// _homeBuyingRole = a.generateHomeBuyingRole(this);
							return;
						}
					}
				}
			}
			
			if(!homeType.equals("house") && !homeType.equals("apartment")) // i.e. if you tried to get both an apartment and a house and it still failed
			{
				break;
			}
			else
			{
				homeType = "";
			}
		}
		
		AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name(),"Failed to acquire a(n) " + homeType + ".");
		// _homeOccupantRole = new HomelessRole(this);
		// _homeBuyingRole = null;
	}
	/** Sets the value of _occupation to a role that is requested by occupationType if possible; else it sets _occupation to null. */
	public void acquireOccupation(String occupationType) 
	{
		if(occupationType.contains("Restaurant"))
		{
			List<Restaurant> restaurants = Directory.restaurants();
			List<Restaurant> chosenRestaurants = new ArrayList<Restaurant>(); // we will fill this list up with the restaurants that match the type (i.e. Eric, Omar, etc); if no type is specified, this list will be set to all restaurants
			if(occupationType.contains("Eric")) {
				for(Restaurant r : restaurants) {
					if(r instanceof EricRestaurant) {
						chosenRestaurants.add(r);
					}
				}
			}
			else if(occupationType.contains("Omar")) {
				for(Restaurant r : restaurants) {
					if(r instanceof OmarRestaurant) {
						chosenRestaurants.add(r);
					}
				}
			}
			else if(occupationType.contains("Ryan")) {
				for(Restaurant r : restaurants) {
					if(r instanceof RyanRestaurant) {
						chosenRestaurants.add(r);
					}
				}
			}
			//	else if(occupationType.contains("Tanner")) {
			//		for(Restaurant r : restaurants) {
			//			if(r instanceof TannerRestaurant) {
			//				restaurantsChosen.add(r);
			//			}
			//		}
			//	}
			else if(occupationType.contains("Yixin")) {
				for(Restaurant r : restaurants) {
					if(r instanceof YixinRestaurant) {
						chosenRestaurants.add(r);
					}
				}
			}
			else // i.e. if we didn't specify a specific type of restaurant
			{
				chosenRestaurants = restaurants;
			}
			
			if(occupationType.contains("Waiter"))
			{
				if(chosenRestaurants.size() != 0)
				{
					int index = new Random().nextInt(chosenRestaurants.size());
					// Hack for testing:
					index = 0;
					_occupation = chosenRestaurants.get(index).generateWaiterRole(this, occupationType.contains("SharedData"));
					return;
				}
			}
			else if(occupationType.contains("Cashier"))
			{
				for(Restaurant r : chosenRestaurants) {
					_occupation = r.tryAcquireCashierRole(this);
					if(_occupation != null) return;
				}
			}
			else if(occupationType.contains("Host"))
			{
				for(Restaurant r : chosenRestaurants) {
					_occupation = r.tryAcquireHostRole(this);
					if(_occupation != null) return;
				}
			}
			else if(occupationType.contains("Cook"))
			{
				for(Restaurant r : chosenRestaurants) {
					_occupation = r.tryAcquireCookRole(this);
					if(_occupation != null) return;
				}
			}
			// If unable to get a restaurant role, _occupation will remain null. 
		}
		else if(occupationType.contains("Market"))
		{
			List<Market> markets = Directory.markets();
			if(occupationType.contains("Cashier"))
			{
				for(Market m : markets)
				{
					_occupation = m.tryAcquireCashierRole(this);
					if(_occupation != null) return;
				}
			}
			else if(occupationType.contains("Employee"))
			{
				for(Market m : markets)
				{
					_occupation = m.tryAcquireEmployeeRole(this);
					if(_occupation != null) return;
				}
			}
		}
		else if(occupationType.contains("Bank"))
		{
			List<Bank> banks = Directory.banks();
			if(occupationType.contains("Teller"))
			{
				for(Bank b : banks)
				{
					_occupation = b.tryAcquireTellerRole(this);
					if(_occupation != null) return;
				}
			}
			else if(occupationType.contains("Host"))
			{
				for(Bank b : banks)
				{
					_occupation = b.tryAcquireHostRole(this);
					if(_occupation != null) return;
				}
			}
		}
		
		// note: control reaches here because no jobs were found. _occupation will be null.
	}
	
	
	
	// ------------------------------------------- PROPERTIES --------------------------------------------------
	public String name() { return _name; }
	public double money() { return _money; }
	public double bankAccountFunds() { return _bankCustomerRole != null ? _bankCustomerRole.accountFunds() : 0.0; }
	public double bankAmountOwed() { return _bankCustomerRole != null ? _bankCustomerRole.amountOwed() : 0.0; }
	public double totalMoney() { return _money + bankAccountFunds() + bankAmountOwed(); }
	// /** Sets the days the person works. @param weekday_notWeekend True if working weekdays, false if working weekends. */
	// public void setWorkDays(boolean weekday_notWeekend) { _weekday_notWeekend = weekday_notWeekend; }
	public Role currentRole() { return _currentRole; }
	public String occupationTypeToString() { return (_occupation != null) ? _occupation.typeToString() : "None"; }
	public String nextRoleTypeToString() { return (_nextRole != null && _nextRole != _currentRole) ? _nextRole.typeToString() : "None"; }
	public HomeOccupantRole homeOccupantRole() { return _homeOccupantRole; }
	public CommuterRole commuterRole() { return _commuterRole; }
	
	// Actions to do:
	/** Adds an action to do to the back of the list of actions to do */
	public void addActionToDo(String actionToDo) { _actionsToDo.add(actionToDo); stateChanged(); }
	/** Adds a list of actions to do to the back of the list of actions to do */
	public void addActionsToDo(List<String> actionsToDo) { _actionsToDo.addAll(actionsToDo); stateChanged(); }
	/** Inserts an action to do at the beginning of the list */
	public void insertFirstActionToDo(String actionToDo) { _actionsToDo.add(0, actionToDo); stateChanged(); }
	public boolean removeActionToDo(String actionToDo) {  stateChanged(); return _actionsToDo.remove(actionToDo); }
	/** Removes and returns the first action in the list */
	private String popFirstActionToDo() { stateChanged(); return _actionsToDo.remove(0); }
	/** Returns the current _actionsToDo list and resets it to a new, empty list. */
	public List<String> clearActionsToDo() {
		List<String> oldActionsToDo = _actionsToDo;
		_actionsToDo = new ArrayList<String>();
		stateChanged();
		return oldActionsToDo;
	}
	
	
	
	// ------------------------------------------------ COMMANDS & MESSAGES -----------------------------------------------------------
	public void cmdChangeMoney(double delta) { 
		_money += delta;
		if(_personInfoPanel != null){
			_personInfoPanel.refreshInfo(this); 
		}
		stateChanged();
	}
	public void cmdNoLongerHungry() { _state.nourishmentLevel += NOURISHMENT_PER_MEAL; stateChanged(); }
	public void msgWeekStarted() { _paidRentThisWeek = false; stateChanged(); }
	
	
	
	// =========================================================================================================================
	// -------------------------------------------------- SCHEDULER ------------------------------------------------------------
	// =========================================================================================================================
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(_currentRole != null && _currentRole.active)
		{
			if(!_sentCmdFinishAndLeave)
			{
				if(!_actionsToDo.isEmpty())
				{
					if(_currentRole == _homeOccupantRole) {
						finishAndLeaveCurrentRole();
					}
				}
				// Finish current role because you have to get to work:
				if(workingToday())
				{
					if(_occupation != null)
					{
						if(_currentRole == _occupation)
						{
							// note: you're currently at you job.
							// If your shift just finished, leave.
							if(!timeToBeAtWork())
							{
								finishAndLeaveCurrentRole();
								return true;
							}
						}
						else
						{
							// note: you're not currently at your job.
							// If you need to go to work, finish your current role.
							if(timeToBeAtWork())
							{
								finishAndLeaveCurrentRole();
								return true;
							}
						}
					}
				}
			}
			/*
			if(_currentRole == _homeOccupantRole && (_state.time() > 20 || _state.time() < 7))
			{
				if(_state.nourishment() == NourishmentState.HUNGRY)
				{
					if(!_homeOccupantRole.cooking())
					{
						_homeOccupantRole.cmdCookAndEatFood();
						return true;
					}
				}
				else
				{
					if(!_homeOccupantRole.sleeping())
					{
						_homeOccupantRole.cmdGoToBed();
						return true;
					}
				}
			}
			*/
			
			// ---------------------------------------------- Call current role's scheduler -------------------------------------------
			// print("About to call _currentRole (" + _currentRole.toString() + ") scheduler.");
			if(_currentRole.pickAndExecuteAnAction())
			{ 
				return true;
			}
		}
		else // i.e. _currentRole is null or _currentRole.active == false
		{
			// note: if we get here, a role just finished leaving.
			_sentCmdFinishAndLeave = false;
			
			if(_currentRole == _commuterRole)
			{
				// Commuter role must have just finished travelling to the destination.
				// If the next role is a customer role and an actionToDo is not active (i.e. _dontCheckPlaceOpen = false), check if the place is open.
				// If the next role is a customer role and the place is open, send msgIAmComing() to the place.
				
				// First, check if it's a workplace and do appropriate actions (check if it's closed, add yourself to the list)
				if(_nextRole != null && _nextRole.place() instanceof Workplace)
				{
					if(isCustomerRole(_nextRole))
					{
						if( _dontCheckPlaceOpen || ((Workplace)_nextRole.place()).isOpen())
						{
							// Only if _nextRole is a customer role and nextRole's place is open
							((Workplace)_nextRole.place()).msgIAmComing();
						}
						else
						{
							// if my next role is a customer role but the place isn't open
							_currentRole = null;
							// Note: if we call setNextRole(_homeOccupantRole), the person will return home after going to a closed place.
							// setNextRole(_homeOccupantRole);
							return true;
						}
					}
					
				}
				
				// Shift the current role from the commuter role to whatever next role is, and start the next role.
				_currentRole = _nextRole;
				if(_currentRole != null) _currentRole.active = true;
				return true;
			}
			else if(_currentRole != null && (_currentRole.place() instanceof Workplace))
			{
				// we just left a workplace because _currentRole just finished
				if(isCustomerRole(_currentRole))
				{
					// we just left a workplace and we were a customer
					((Workplace)_currentRole.place()).msgIAmLeaving();
				}
			}
			
			
			
			// We just finished a role that was NOT commuterRole.
			// Whether it was an actionToDo or not, reset _dontCheckPlaceOpen to false.
			// If another actionToDo is chosen, it will set _dontCheckPlaceOpen to true.
			_dontCheckPlaceOpen = false;
			
			
			
			// Now, choose a new role and call setNextRole on it.
			
			
			
			// First, check if there are actions to do in the list.
			if(!_actionsToDo.isEmpty())
			{
				String nextAction = popFirstActionToDo();
				_dontCheckPlaceOpen = true;
				if(nextAction.contains("Restaurant"))
				{
					if(nextAction.contains("Eric")) {
						if(actGoToRestaurantOfType("Eric")) return true;
					}
					if(nextAction.contains("Omar")) {
						if(actGoToRestaurantOfType("Omar")) return true;
					}
					if(nextAction.contains("Ryan")) {
						if(actGoToRestaurantOfType("Ryan")) return true;
					}
				//	if(nextAction.contains("Tanner")) {
				//		if(goToRestaurantOfType("Tanner")) return true;
				//	}
					if(nextAction.contains("Yixin")) {
						if(actGoToRestaurantOfType("Yixin")) return true;
					}
				}
				else if(nextAction.contains("Bank"))
				{
					int preferredBankNumber = -1;
					if(nextAction.contains("First")) {
						preferredBankNumber = 0;
					}
					else if(nextAction.contains("Second")) {
						preferredBankNumber = 1;
					}
					if(nextAction.contains("Withdraw")) {
						if(actGoToBank("Withdraw", 20, preferredBankNumber)) return true;
					}
					else if(nextAction.contains("Deposit")) {
						if(actGoToBank("Deposit", 20, preferredBankNumber)) return true;
					}
					else if(nextAction.contains("Robber")) {
						if(actGoToBank("Robber", 500, preferredBankNumber)) return true;
					}
				}
				else if(nextAction.contains("Market"))
				{
					int preferredMarketNumber = -1;
					if(nextAction.contains("First")) {
						preferredMarketNumber = 0;
					}
					else if(nextAction.contains("Second")) {
						preferredMarketNumber = 1;
					}
					// Buy 3 meals from the market
					if(actBuyMealsFromMarket(3, preferredMarketNumber)) return true;
				}
				else if(nextAction.contains("Home"))
				{
					if(nextAction.contains("Sleep")) {
						_homeOccupantRole.cmdGoToBed();
					}
					else if(nextAction.contains("Eat") || nextAction.contains("Cook")) {
						_homeOccupantRole.cmdCookAndEatFood();
					}
					else if(nextAction.contains("TV")) {
						_homeOccupantRole.cmdWatchTv();
					}
					setNextRole(_homeOccupantRole);
					return true;
				}
				
				// If we get here, no actionsToDo were called, so we go back to free-run mode and we do want to check if a place is open.  Usually won't get here.
				_dontCheckPlaceOpen = false;
			}
			
			
			
			// Now, choose the next behavior based on free-running choices.
			if(_occupation != null && workingToday() && timeToBeAtWork())
			{
				if(setNextRole(_occupation)) return true;
			}
			if(_state.nourishment() == NourishmentState.HUNGRY)
			{
				if(_state.wealth() == WealthState.RICH)
				{
					if(actGoToAnyRestaurant()) return true;
				}
				else
				{
					if(new Random().nextInt(4) == 0)
					{
						if(actGoToAnyRestaurant()) return true;
					}
					if(actEatAtHome()) return true;
				}
			}
			
			// Bank stuff
			// If I have lots of money and I owe money, repay it; if I have lots of money and don't owe money, deposit.
			if(_state.haveLotsOfMoney())
			{
				if(_bankCustomerRole != null && _bankCustomerRole.amountOwed() > 0)
				{
					if(actGoToBank("Pay Loan", _bankCustomerRole.amountOwed() > _money ? _money : _bankCustomerRole.amountOwed(), -1)) return true;
				}
				else
				{
					if(actGoToBank("Deposit", _state.amountToWithdrawOrDeposit(), -1)) return true;
				}
			}
			// If I have low money and I have funds, withdraw
			else if(_state.haveLowMoney() && _bankCustomerRole != null)
			{
				if(_bankCustomerRole.accountFunds() > 0) actGoToBank("Withdraw", _state.amountToWithdrawOrDeposit(), -1);
				else actGoToBank("Withdraw Loan", _state.amountToWithdrawOrDeposit(), -1);
			}
			else if(totalMoney() == 0)
			{
				if(_bankCustomerRole != null && _bankCustomerRole.accountFunds() <= 5 && _bankCustomerRole.amountOwed() > 0)
				{
					if(actGoToBank("Robber", 1000, -1)) return true;
				}
			}
			
			if(_state.time() > Directory.CLOSING_TIME || _state.time() < Directory.OPENING_TIME - .5) //could replace with variables for sleepTime and wakeTime
			{
				_homeOccupantRole.cmdGoToBed();
				setNextRole(_homeOccupantRole);
				return true;
			}
			if(!_paidRentThisWeek && _money != 0)
			{
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name(), "Paying " + WEEKLY_RENT + " in rent.");
				if(money() >= WEEKLY_RENT)
				{
					cmdChangeMoney(-WEEKLY_RENT);
				}
				else
				{
					cmdChangeMoney(-money());
				}
				stateChanged();
			}
			_homeOccupantRole.cmdWatchTv();
			setNextRole(_homeOccupantRole);
			return true;
		}
		
		// note: The thread will get to this point if a role is active and its scheduler returned false.
		// If the current role is inactive, we cannot get here.
		
		// note: Here is where we would check for and do non-important actions, like check your phone, before returning false
		
		return false;
	}
	
	
	
	// ---------------------------------------- ACTIONS ----------------------------------------
	// note: These really just set the next role.
	
	// ----------------- HOME -----------------
	/** Eats at home if you have food, otherwise goes to market first then tries to eat at home. */
	private boolean actEatAtHome()
	{
		if(_homeOccupantRole.haveFood())
		{
			_homeOccupantRole.cmdCookAndEatFood();
			setNextRole(_homeOccupantRole); // always returns true for _homeOccupantRole
			return true;
		}
		else
		{
			if(actBuyMealsFromMarket(3, -1)) // hard-coded 3 meals for now
			{
				insertFirstActionToDo("HomeEat");
				return true;
			}
		}
		return false;
	}
	
	
	
	// ----------------- Market -----------------
	private boolean actBuyMealsFromMarket(int meals, int preferredMarketNumber)
	{
		// Search for a MarketCustomerRole in _customerRoles, use that;
		// if no MarketCustomerRole in _customerRoles, choose a Market from the Directory, and get a new MarketCustomerRole from it
		for(Role r : _customerRoles)
		{
			if(r instanceof MarketCustomerRole)
			{
				MarketCustomerRole mcr = (MarketCustomerRole)r;
				mcr.cmdBuyFood(meals);
				if(setNextRole(mcr)) return true;
			}
		}
		// note: we only get here if no MarketCustomerRole was found in _customerRoles
		List<Market> markets = Directory.markets();
		
		// Check for preferred market
		if(preferredMarketNumber != -1)
		{
			if(markets.size() > preferredMarketNumber)
			{
				MarketCustomerRole mcr = markets.get(preferredMarketNumber).generateCustomerRole(this);
				mcr.cmdBuyFood(meals);
				if(setNextRole(mcr)) return true;
			}
		}
		
		for(Market m : markets)
		{
			MarketCustomerRole mcr = m.generateCustomerRole(this);
			_customerRoles.add(mcr);
			mcr.cmdBuyFood(meals);
			if(setNextRole(mcr)) return true;
		}
		return false;
	}
	
	
	
	// ------------------- Bank ---------------------
	private boolean actGoToBank(String request, double amount, int preferredBankNumber)
	{
		// If _bankCustomerRole is nonexistant, try to generate a new role.
		if(_bankCustomerRole == null)
		{
			if(!getNewBankCustomerRole(preferredBankNumber)) {
				return false;
			}
		}
		// note: _bankCustomerRole will not be null here.
		
		_bankCustomerRole.cmdRequest(request, amount);
		if(setNextRole(_bankCustomerRole)) return true;
		return false;
	}
	private boolean getNewBankCustomerRole(int preferredBankNumber)
	{
		List<Bank> banks = Directory.banks();
		
		if(preferredBankNumber != -1)
		{
			if(banks.size() > preferredBankNumber)
			{
				_bankCustomerRole = banks.get(preferredBankNumber).generateCustomerRole(this);
				return true;
			}
		}
		
		if(!banks.isEmpty()) {
			_bankCustomerRole = banks.get(new Random().nextInt(banks.size())).generateCustomerRole(this);
			return true;
		}
		return false;
	}
	
	
	
	// ----------------- Restaurant -----------------
	/** If a RestaurantCustomerRole exists in _customerRoles, set that to the next role.  Else, get a new customer role from a randomly chosen restaurant */
	private boolean actGoToAnyRestaurant()
	{
		RestaurantCustomerRole rcr = (RestaurantCustomerRole)findCustomerRoleOfType(RestaurantCustomerRole.class);
		if(rcr != null)
		{
			rcr.cmdGotHungry();
			if(setNextRole(rcr)) return true;
		}
		
		// note: we only get here if no RestaurantCustomerRole was found in _customerRoles
		List<Restaurant> restaurants = Directory.restaurants();
		if(restaurants.size() != 0)
		{
			rcr = restaurants.get(new Random().nextInt(restaurants.size())).generateCustomerRole(this);
			rcr.cmdGotHungry();
			_customerRoles.add(rcr);
			if(setNextRole(rcr)) return true;
		}
		
		return false;
	}
	/**
	 * Searches for a RestaurantCustomerRole of the correct type in _customerRoles and then in Directory.restaurants() and calls setNextRole on it
	 * @param type "Eric", "Omar", "Ryan", or "Yixin".  Case-sensitive and must match exactly.
	 * @return true if a restaurant of the passed-in type was chosen and setNextRole was called
	 */
	private boolean actGoToRestaurantOfType(String type)
	{
		RestaurantCustomerRole rcr = null;
		switch(type)
		{
		case "Eric":
			rcr = (RestaurantCustomerRole)findCustomerRoleOfType(EricCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(EricRestaurant.class);
				if(rcr != null) _customerRoles.add(rcr);
			}
			break;
		case "Omar":
			rcr = (RestaurantCustomerRole)findCustomerRoleOfType(OmarCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(OmarRestaurant.class);
				if(rcr != null) _customerRoles.add(rcr);
			}
			break;
		case "Ryan":
			rcr = (RestaurantCustomerRole)findCustomerRoleOfType(RyanCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(RyanRestaurant.class);
				if(rcr != null) _customerRoles.add(rcr);
			}
			break;
		//case "Tanner":
		//	rcr = (RestaurantCustomerRole)getRoleOfType(TannerCustomerRole.class);
		//	if(rcr == null)
		//	{
		//		rcr = getNewCustomerRoleFromRestaurantOfType(TannerRestaurant.class);
		//		if(rcr != null) _customerRoles.add(rcr);
		//	}
		//	break;
		case "Yixin":
			rcr = (RestaurantCustomerRole)findCustomerRoleOfType(YixinCustomerRole.class);
			if(rcr == null) {
				rcr = getNewCustomerRoleFromRestaurantOfType(YixinRestaurant.class);
				if(rcr != null) _customerRoles.add(rcr);
			}
			break;
		}
		if(rcr != null)
		{
			rcr.cmdGotHungry();
			if(setNextRole(rcr)) return true;
		}
		
		return false;
	}
	
	
	
	// ------------------------------------------ UTILITIES -------------------------------------
	private boolean workingToday()
	{
		if(_occupation != null)
		{
			if(
				_occupation instanceof BankHostRole ||
				_occupation instanceof BankTellerRole
			)
			{
				// if I work at a bank, return true if not the weekend
				return !(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY);
			}
		}
		// Commenting this out because we're currently not taking account of weekends
		// return ((_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && !_weekday_notWeekend) ||
		//		(!(_state.today() == Time.Day.SATURDAY || _state.today() == Time.Day.SUNDAY) && _weekday_notWeekend);
		
		return true;
	}
	private boolean timeToBeAtWork()
	{
		return _state.time() > Directory.OPENING_TIME - .5 && // .5 is half an hour
				_state.time() < Directory.CLOSING_TIME + .5;
	}
	private void finishAndLeaveCurrentRole()
	{
		//if(_currentRole == _commuterRole) setNextRole(_occupation); // why is that here?
		_sentCmdFinishAndLeave = true;
		_currentRole.cmdFinishAndLeave();
		stateChanged();
	}
	private boolean setNextRole(Role nextRole)
	{
		if(!_dontCheckPlaceOpen && isCustomerRole(nextRole))
		{
			if(!((Workplace)nextRole.place()).isOpen()) {
				return false;
			}
		}
		if(nextRole != null && nextRole.place() instanceof Workplace && !((Workplace)nextRole.place()).allowedToOpen())
		{
			return false;
		}
		_nextRole = nextRole;
		_commuterRole.setDestination(nextRole.place());
		_currentRole = _commuterRole;
		_currentRole.active = true;
		_personInfoPanel.refreshInfo(this);
		return true;
	}
	// Uncomment if we want to be able to make people go to a place but not enter
	//	private void setNextPlace(Place place)
	//	{
	//		_nextRole = null;
	//		_commuterRole.setDestination(place);
	//		_currentRole = _commuterRole;
	//		_currentRole.active = true;
	//		_personInfoPanel.refreshInfo(this);
	//	}
	private boolean isCustomerRole(Role r)
	{
		return (
				r instanceof RestaurantCustomerRole ||
				r instanceof BankCustomerRole ||
				r instanceof MarketCustomerRole
		);
	}
	/** This method searches for & returns a role in _customerRoles that is of the passed-in type; if none is found, it returns null. */
	private Role findCustomerRoleOfType(Type type)
	{
		for(Role r : _customerRoles)
		{
			if(r.getClass().equals(type)) return r;
		}
		return null;
	}
	/** This method returns a newly generated customer role from the restaurant type specified.
	 * It does not take into account whether or not the restaurant's customer role already exists in _customerRoles.
	 */
	private RestaurantCustomerRole getNewCustomerRoleFromRestaurantOfType(Type type)
	{
		List<Restaurant> restaurants = Directory.restaurants();
		for(Restaurant r : restaurants)
		{
			if(r.getClass().equals(type)) return r.generateCustomerRole(this);
		}
		return null;
	}
	
	//Getter for images
    public Image getImage(){
    	return _picture;
    }
}
