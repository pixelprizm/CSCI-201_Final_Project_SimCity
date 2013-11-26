package city.transportation;

import java.util.Random;

import aStar.AStarTraversal;
import agent.Role;
import city.Directory;
import city.PersonAgent;
import city.Place;
import city.transportation.gui.CommuterGui;
import city.transportation.interfaces.Bus;
import city.transportation.interfaces.Commuter;

/** There is one CommuterRole per person, and the CommuterRole is the one that gets the person to different places in the world view. */

//NOTES when taking bus, must decide who finds which busstop is nearest to destination (commuter role or busagent)
//		Connect to directory to find fare

public class CommuterRole extends Role implements Commuter{
	// This is set by PersonAgent, and it is CommuterRole's responsibility to get to that location, then set its active to false.
	public PersonAgent _person;
	public Place _destination;
	public Place _currentPlace;
	BusStopObject _busStop;
//	Semaphore commuterSem = new Semaphore(0, true);

	public CarObject _car = new CarObject();
	public Bus _bus;
	public double _fare;
	CommuterGui _gui;
	
	public enum TravelState{choosing, 
		choseCar, goToCar, atCar, driving, 
		choseWalking, walking, 
		choseBus, goingToBusStop, atBusStop, waitingAtBusStop, busIsHere, ridingBus, busIsAtDestination, gettingOffBus,
			atDestination, done, none};
	public TravelState _tState = TravelState.none;
	public AStarTraversal _traversal;
	Random _generator = new Random();
	
	//Transportation Hacks
	enum PrefTransport{none, legs, bus, car};
	PrefTransport pTransport = PrefTransport.none;
	
	//Probably won't need -> not 100% sure though
	enum CarState{noCar, hasCar, usingCar};
	CarState _cState = CarState.noCar; 
	
	//----------------------------------------------CONSTRUCTOR & PROPERTIES----------------------------------------
	public CommuterRole(PersonAgent person, Place initialPlace, AStarTraversal traversal){
		super(person);
		_traversal = traversal;
		_person = person;
		_currentPlace = initialPlace;
		_destination = null;
		_car = null;
		_gui = new CommuterGui(this, initialPlace, _traversal);
	}
	
	public CommuterGui gui() { return _gui; }
	public void setGui(CommuterGui gui) { _gui = gui; }
	
	public void setCar(CarObject car){_car = car;}
	
	public Place destination() { return _destination; }
	public void setDestination(Place place) { cmdGoToDestination(_destination); }

	public Place place() { return currentPlace(); } // could replace this to return a home-like location that the CommuterRole defaults back to when PersonAgent sets its _nextRole to its _commuterRole.  This would of course require more changes to work correctly.
	public Place currentPlace() { return _currentPlace; }
	public void setCurrentPlace(Place place) { _currentPlace = place; }

	public void setBusStop(BusStopObject busStop){_busStop = busStop;}
	
	//----------------------------------------------Command---------------------------------------------
	public void cmdGoToDestination(Place destination){ //Command to go to destination
		if(destination != null) {
			print("Will go to " + destination.getName() + " at (" + destination.xPosition() + "," + destination.yPosition() + ").");
		}
		else {
			print("Will go to null destination.");
		}
		_tState = TravelState.choosing;
		_destination = destination;
		//System.out.println(_destination.xPosition() + " " + _destination.yPosition());
		stateChanged();
	}
	@Override
	public void cmdFinishAndLeave() {
		//active = true; // shouldn't need this
		stateChanged();
	}
	
	//----------------------------------------------Messages------------------------------------------
	//Bus Transportation messages
	public void msgAtBusStop(BusStopObject busstop){ //GUI message
		_tState = TravelState.atBusStop;
		_currentPlace = busstop;
		stateChanged();
		print("Going to bus stop " + busstop.getName());
	}
	public void msgGetOnBus(double fare, Bus bus){
		_tState = TravelState.busIsHere;
		_bus = bus;
		_fare = fare;
		stateChanged();
		print("Getting on bus " + bus.getName());
	}
	@Override
	public void msgGetOffBus(Place busstop){
		_currentPlace = busstop;
		_tState = TravelState.busIsAtDestination;
		_busStop = (BusStopObject)busstop;
		stateChanged();
		print("Getting off bus " + _bus.getName());
	}
	
	//Car Messages
	public void msgAtCar(){
		_tState = TravelState.atCar;
		stateChanged();
		print("At car");
	}
	
	//Msg At Destination from GUI
	public void msgAtDestination(Place place){
		_tState = TravelState.atDestination;
		_currentPlace = place;
		//active = false;
		stateChanged();
	}
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction() {
		//At Destination
		if(_tState == TravelState.atDestination){
			actAtDestination();
			return true;
		}
	
		//Choosing
		if(_tState == TravelState.choosing){
			actChooseTransportation();
			return true;
		}
		
		//Walking
		if(_tState == TravelState.choseWalking){
			actWalking();
			return true;
		}
		
		//Riding Bus
		if(_tState == TravelState.choseBus){
			actGoToBusStop();
			return true;
		}
		if(_tState == TravelState.atBusStop){
			actAtBusStop();
			return true;
		}
		if(_tState == TravelState.busIsHere && _bus != null && _person._money >= _fare){
			actGetOnBus();
			return true;
		}
		if(_tState == TravelState.busIsHere && _bus != null && _person._money <= _fare){
			actWalking();
			return true;
		}
		if(_tState == TravelState.busIsAtDestination){
			actGetOffBus();
			return true;
		}
		
		//Driving
		if(_tState == TravelState.choseCar){
			actGoToCar();
			return true;
		}
		if(_tState == TravelState.atCar){
			actDriving();
			return true;
		}
		

		
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------Actions----------------------------------------
	//Choosing
	public void actChooseTransportation(){
		print("Choosing mode of transport");
		
		if(_gui.getManhattanDistanceToDestination(_destination) > 300){
			if(_car != null){
				_tState = TravelState.choseCar;
			}
			else if(_person._money >= 10){
				_tState = TravelState.choseBus;
			}
			else{
				_tState = TravelState.walking;
			}
		}
		else{
			_tState = TravelState.choseWalking;
		}
		
		if(pTransport == PrefTransport.legs){
			_tState = TravelState.choseWalking;
		}
		if(pTransport == PrefTransport.bus){
			_tState = TravelState.choseBus;
		}
		if(pTransport == PrefTransport.car){
			_tState = TravelState.choseCar;
		}
		
//		_tState = TravelState.choseWalking;
//		pTransport = PrefTransport.legs;
		stateChanged();
	}
	public void actChooseNewTransportation(){ //Choosing when previous form of transportation doesn't work (Mostly for bus)
		_tState = TravelState.none;
	}
	
	//Walking
	public void actWalking(){
		_tState = TravelState.walking;
		_gui.walkToLocation(_destination);
	}
	
	//Bus
	public void actGoToBusStop(){
		_tState = TravelState.goingToBusStop;
		_busStop = Directory.getNearestBusStop(_gui.getX(), _gui.getY()); //Unit Testing will skip this for now
		_gui.goToBusStop(_busStop);
	}
	public void actAtBusStop(){
		_tState = TravelState.waitingAtBusStop;
		_busStop = Directory.getNearestBusStopToDestination(_destination);
		//_busStop.addCommuterRole(this);
	}
	public void actGetOnBus(){
		_tState = TravelState.ridingBus;
		_person._money -= _fare;
		_gui.getOnBus();
		_bus.msgGettingOnBoard(this, _busStop, _fare);
		stateChanged();
	}
	public void actGetOffBus(){
		_tState = TravelState.gettingOffBus;
		_gui.getOffBus(_busStop);
		_bus.msgGotOff(this);
		_bus = null;
		actWalking(); //Calls this function here because after you get off of the bus stop you walk to the destination
	}

	//Driving
	public void actGoToCar(){
		_tState = TravelState.goToCar;
		_gui.goToCar(_car, _destination);
	}
	
	public void actDriving(){
		_tState = TravelState.driving;
		_car.goToDestination(_destination);
	}
	
	public void actAtDestination(){
		_tState = TravelState.done;
		active = false;
	}
	
	//----------------------------------------------Hacks----------------------------------------
	public void chooseTransportation(int choice){
		if(choice == 0){
			pTransport = PrefTransport.legs;
		}
		else if(choice == 1){
			pTransport = PrefTransport.bus;
		}
		else if(choice == 2){
			pTransport = PrefTransport.car;
		}
		else{
			pTransport = PrefTransport.none;
		}
		
		
	}

}
