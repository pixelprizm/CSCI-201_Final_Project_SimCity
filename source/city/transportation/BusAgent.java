package city.transportation;

import java.awt.Dimension;

import gui.trace.AlertLog;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.Directory;
import city.Place;
import city.transportation.gui.BusAgentGui;
import city.transportation.interfaces.Bus;
import city.transportation.interfaces.Commuter;

//NOTES when taking bus, must decide who finds which busstop is nearest to destination (commuter role or busagent)

public class BusAgent extends Agent implements Bus{
	String _name;
	
	List<MyCommuter> _passengers = new ArrayList<MyCommuter>();
	BusAgentGui _gui;
	
	List<BusStopObject> _busStops = new ArrayList<BusStopObject>();
	
	
	public BusStopObject currentDestination;
	int _busStopNum;
	public List<Commuter> currentBusStopList = new ArrayList<Commuter>();
	Semaphore busSem = new Semaphore(0, true);
	
	static double _fare;
	double _register;
	private Timer _deathDelay = new Timer();
	static int capacity = 20;
	private int numPeople = 0;
	private int expectedPeople = 0;

	public enum BusState{moving, atDestination, droppingoff, pickingup, notmoving};
	public BusState bState = BusState.notmoving;
	
	enum PassengerState{onBus, offBus};

	class MyCommuter{
	    Commuter commuter;
	    Place destination;
	    double payment;
	    PassengerState pState = PassengerState.onBus;
	    
	    MyCommuter(Commuter person, Place destination){
	    	this.commuter = person;
	    	this.destination = destination;
	    }
	}
	
	public BusAgent(String name){
		_name = name;
		_fare = Directory.getBusFare();
		_busStopNum = 0;
		_busStops = Directory.busStops();
	}
	
	public void setBusAgentGui(BusAgentGui gui){
		_gui = gui;
	}
	
	//----------------------------------------------Messages----------------------------------------
	public void msgAtDestination(BusStopObject busstop){
	//	System.out.println("msgAtDestination");
	    currentDestination = busstop;
	    bState = BusState.atDestination;
	    stateChanged();
	}

	public synchronized void msgGotOff(Commuter passenger){
	    setNumPeople(getNumPeople() - 1);
	    stateChanged();
	}

	public synchronized void msgGettingOnBoard(Commuter person, Place destination, double payment){ //Check if payment is correct?
		_passengers.add(new MyCommuter(person, destination));
	    currentDestination.removeCommuterRole(person);
	    _register += payment;
	    setNumPeople(getNumPeople() + 1); //Fix this
		System.out.println("I am getting on board " + _passengers.size());
	    stateChanged();
	}
	
	
	//----------------------------------------------Scheduler----------------------------------------
	public boolean pickAndExecuteAnAction(){
		try{
			if(bState == BusState.notmoving){
				GoToFirstBusStop();
				return true;
			}
			
			if(bState == BusState.atDestination){
				DropOff();
				return true;
			}
			
			if(bState == BusState.droppingoff && getExpectedPeople() == getNumPeople()){
				PickUp();
				return true;
			}
			
			if(bState == BusState.pickingup && getExpectedPeople() == getNumPeople()){
				//System.out.println("Leaving");
				Leave();
				return true;
			}
		}
		catch(ConcurrentModificationException e){
			
		}
		
		return false;
	}
	
	//----------------------------------------------Actions----------------------------------------
	public void GoToFirstBusStop(){
		System.out.println("Start");
		bState = BusState.moving;
		_gui.goToBusStop(_busStops.get(_busStopNum));
		try {
			busSem.acquire();
	  } catch (InterruptedException e) {
			e.printStackTrace();
	  }
	}

	public void DropOff(){
		AlertLog.getInstance().logMessage(AlertTag.BUS, this.name() ,"Dropping off from " + currentDestination.name() + 
				", Number of Passengers: " + _passengers.size());
	    bState = BusState.droppingoff;
	    for(int i = 0; i < _passengers.size() ; i++){
	    	//System.out.println("The passenger is going to " + commuter.destination.getName());
	        if(_passengers.get(i).destination == currentDestination){
	        	_passengers.get(i).commuter.msgGetOffBus(currentDestination);
	            setExpectedPeople(getExpectedPeople() - 1);
	    	    _passengers.remove(_passengers.get(i)); //Fix this
	            i--;
	        }
	    }
    	AlertLog.getInstance().logMessage(AlertTag.BUS, this.name() ,"Finished dropping off " + getExpectedPeople() + " passengers");	    
	    stateChanged();
	}

	public void PickUp(){
		AlertLog.getInstance().logMessage(AlertTag.BUS, this.name() ,"Picking up from " + currentDestination.name());
		currentBusStopList = currentDestination.getList();
		bState = BusState.pickingup;
		setExpectedPeople(getExpectedPeople() + currentBusStopList.size());
		ArrayList<Commuter> waitingList = new ArrayList<Commuter>();
		for(Commuter comm: currentBusStopList){
	    	waitingList.add(comm);
        }
		//doing this because currentBusStopList is modified at the same time
		for(Commuter comm: waitingList){
    		if(getExpectedPeople() < capacity){
    			AlertLog.getInstance().logMessage(AlertTag.BUS, this.name() ,"Picked up");
    			comm.msgGetOnBus(_fare, this);
    		}
        }
    	AlertLog.getInstance().logMessage(AlertTag.BUS, this.name() ,"Finished Picking up " + getExpectedPeople() + " passengers");
	    stateChanged();
	}

	public void Leave(){
		//TODO change this to be better
		for (int i = 0; i < currentDestination.getSuicideList().size(); i++){
			final int j = i;
			_deathDelay.schedule(new TimerTask(){
				@Override
				public void run() {
					currentDestination.getSuicideList().get(j).msgYouAreAllowedToDie();
					if (j == currentDestination.getSuicideList().size() - 1)
						currentDestination.getSuicideList().clear();
				}
			}, 120 * i);
		}
	    bState = BusState.moving;
	    _busStopNum++;
		
		if(_busStopNum >= _busStops.size()){
			_busStopNum = 0;
		}
		_gui.goToBusStop(_busStops.get(_busStopNum));
	}
	
	//-----------------------------------------Utilities-----------------------------------------
	public MyCommuter findCommuter(Commuter commuter){
		synchronized(_passengers){
			for(MyCommuter passenger: _passengers){
				if(passenger.commuter == commuter){
					return passenger;
				}
			}
			return null;
		}
	}
	
	public String name(){
		return _name;
	}

	@Override
	public void setFare(int fare) {
		_fare = fare;
	}
	
	public void updateBusStopList(){
		_busStops = Directory.busStops();
	}
	
	public void releaseSem(){
		busSem.release();
	}

	public int getExpectedPeople() {
		return expectedPeople;
	}

	public void setExpectedPeople(int expectedPeople) {
		this.expectedPeople = expectedPeople;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}
	
	public BusStopObject getCurrentBusStop(){
		return currentDestination;
	}
	
	public void setCapacity(int num){
		capacity = num;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	
}
