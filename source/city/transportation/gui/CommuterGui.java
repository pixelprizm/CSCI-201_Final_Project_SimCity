package city.transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import aStar.AStarNode;
import aStar.AStarTraversal;
import aStar.Position;
import gui.Gui;
import city.Place;
import city.transportation.BusStopObject;
import city.transportation.CarObject;
import city.transportation.CommuterRole;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	
	int _xPos, _yPos;
	Place _destination;
	int _xDestination, _yDestination;
	boolean tFlag = false;
	boolean isPresent = true;
	
	CommuterRole _commuter;
	AStarTraversal _traversal;
	Position currentPosition;
	
	enum TransportationType{goToCar, atCar, walking, driving, ridingBus, none};
	TransportationType _transportationType = TransportationType.none;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace, AStarTraversal traversal) {
		System.out.println("Created CommuterGui");
		// Note: placeX and placeY can safely receive values of null
		_xPos = placeX(initialPlace);
		_yPos = placeY(initialPlace);
		traversal = _traversal;
		_commuter = commuter;
		currentPosition = new Position(_xPos, _yPos);
		//currentPosition.moveInto(_traversal.getGrid());
	}
	
	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - _xPos);
		double y = Math.abs(placeY(destination) - _yPos);
		
		return x+y;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		tFlag = true;
		System.out.println("destination X: " + placeX(destination));
		System.out.println("destination Y: " + placeY(destination));
		
	//	currentPosition = new Position(placeX(destination), placeY(destination));
	//	currentPosition.moveInto(_traversal.getGrid());
	//	guiMoveFromCurrentPositionTo(new Position(placeX(destination), placeY(destination)));
		
		_xDestination = placeX(destination);
		_yDestination = placeY(destination);
		_transportationType = TransportationType.walking;
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationType = TransportationType.ridingBus;
		tFlag = true;
	}
	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		tFlag = true;
		_transportationType = TransportationType.goToCar;
		_xDestination = car.getXPosition();
		_yDestination = car.getYPosition();
	}
	
	public void atCar(){
		_transportationType = TransportationType.atCar;
		setPresent(false);
		_commuter.msgAtCar();
	}
	
	
	//at destination message
	public void atDestination(){
		_commuter.msgAtDestination(_destination);
		_transportationType = TransportationType.none;
		tFlag = false;
		setPresent(false);
	}
	
	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
		_xPos = busstop.xPosition();
		_yPos = busstop.yPosition();
		setPresent(true);
	}
	
	public int getX(){
		return _xPos;
	}
	
	public int getY(){
		return _yPos;
	}
	
	
	//------------------------------------------Animation---------------------------------------
	@Override
	public void updatePosition() {
		/*if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--; */
		
		if(_xPos == _xDestination && _yPos == _yDestination && tFlag){
			atDestination();
		}
		if(_xPos == _xDestination && _yPos == _yDestination && _transportationType == TransportationType.goToCar){
			isPresent = false;
			atCar();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 5, 5);
		}
	}
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.xPosition();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.yPosition();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
	
	void guiMoveFromCurrentPositionTo(Position to){
        AStarNode aStarNode = (AStarNode)_traversal.generalSearch(currentPosition, to);
        List<Position> path = aStarNode.getPath();
        Boolean firstStep   = true;
        Boolean gotPermit   = true;

        for (Position tmpPath: path) {
            //The first node in the path is the current node. So skip it.
            if (firstStep) {
                firstStep   = false;
                continue;
            }

            //Try and get lock for the next step.
            int attempts    = 1;
            gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(_traversal.getGrid());

            //Did not get lock. Lets make n attempts.
            while (!gotPermit && attempts < 3) {
                //Wait for 1sec and try again to get lock.
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(_traversal.getGrid());
                attempts ++;
            }

            //Did not get lock after trying n attempts. So recalculating path.            
            if (!gotPermit) {
                guiMoveFromCurrentPositionTo(to);
                break;
            }
            currentPosition.release(_traversal.getGrid());
            currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
            move(currentPosition.getX(), currentPosition.getY());
        }
	}
        
        public void move(int x, int y){
        	_xPos = x;
        	_yPos = y;
        	
        	updatePosition();
        }
}
