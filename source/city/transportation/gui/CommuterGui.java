package city.transportation.gui;

import java.awt.Color;

import java.awt.Graphics2D;
import java.util.List;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

import gui.Gui;
import gui.astar.*;
import gui.Lane;
import city.Directory;
import city.Place;
import city.transportation.CommuterRole;
import city.transportation.BusStopObject;

public class CommuterGui implements Gui {

	private static final int NULL_POSITION_X = 300;
	private static final int NULL_POSITION_Y = 300;
	

	int _xPos, _yPos;
	int _currentBlockX = 0, _currentBlockY = 0;//TODO set the block positions used by cars
	int _destinationBlockX, _destinationBlockY; 
	List<Integer> route = new ArrayList<Integer>();
	List<Integer> intersections = new ArrayList<Integer>();
	Place _destination;
	int _xDestination, _yDestination;
	enum Command { none, walk, car}
	Command _transportationMethod = Command.none;
	boolean isPresent = true;
	private Semaphore _reachedDestination = new Semaphore(0, true);

	CommuterRole _commuter;
	AStarTraversal _aStarTraversal;
	
	Position currentPosition;
	
	//----------------------------------Constructor & Setters & Getters----------------------------------
	public CommuterGui(CommuterRole commuter, Place initialPlace) {
		System.out.println("Created CommuterGui");
		// Note: placeX and placeY can safely receive values of null
	/*	_xPos = placeX(initialPlace);
		_yPos = placeY(initialPlace); */
		_commuter = commuter;
		//currentPosition = convertPixelToGridSpace(placeX(initialPlace), placeY(initialPlace));
	}
	
	public double getManhattanDistanceToDestination(Place destination){
		double x = Math.abs(placeX(destination) - currentPosition.getX());
		double y = Math.abs(placeY(destination) - currentPosition.getY());
		
		return x+y;
	}
	
	public void releaseSemaphore(){ _reachedDestination.release(); };

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean present){
		this.isPresent = present;
	}
	
	public void setXY(int x, int y){
		_xPos = x;
		_yPos = y;
	}
	
	public int getX(){
		return currentPosition.getX();
	}
	
	public int getY(){
		return currentPosition.getY();
	}
	
	//Walking gui-------------------------------------------------------------------------------------------
	public void walkToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		setPresent(true);
		_transportationMethod = Command.walk;
		Position destinationP = convertPixelToGridSpace(placeX(destination), placeY(destination) - 10); // offset by 10
		guiMoveFromCurrentPositionTo(destinationP);
	}
	
	public void driveToLocation(Place destination){
		// set current x & y to _commuter.currrentPlace()
		// set visible to true
		route.clear();
		setPresent(true);
//		_xDestination = placeX(destination);
//		_yDestination = placeY(destination);
		_destinationBlockX = getBlockX(_xDestination);
		_destinationBlockY = getBlockY(_yDestination);
//		while (_currentBlockX != _destinationBlockX){
//			if (_destinationBlockX > _currentBlockX){
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockX++;
//			}
//			else {
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockX--;
//			}
//		}
//		while (_currentBlockY != _destinationBlockY){
//			if (_destinationBlockY > _currentBlockY){
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockY++;
//			}
//			else {
//				route.add(new Point(_currentBlockX, _currentBlockY));
//				_currentBlockY--;
//			}
//		}
		
		route.add(0);
		intersections.add(1);
		route.add(1);
		intersections.add(2);
		route.add(2);
		intersections.add(3);
		route.add(3);
		for (int i=0; i< route.size();i++){
			Lane lane = Directory.lanes().get(route.get(i));
			if (i!=0)
				Directory.intersections().get(i-1).release();
			for (int j=0; j< lane.permits.size();j++){//TODO change size to the ending position and starting position
				while(!lane.permits.get(j).tryAcquire());
				//TODO change waiting to timer based
				if (lane.isHorizontal){
					if (lane.xVelocity>0){
						_xDestination = lane.xOrigin + 10 * lane.xVelocity * j;
						_yDestination = lane.yOrigin;
					}
					else {
						_xDestination = lane.xOrigin + 10 * lane.permits.size() + 10 * lane.xVelocity * (j+1);
						_yDestination = lane.yOrigin;
					}
				}
				else {
					if (lane.yVelocity>0){
						_yDestination = lane.yOrigin + 10 * lane.yVelocity * j;
						_xDestination = lane.xOrigin;
					}
					else{
						_yDestination = lane.yOrigin + 10 * lane.permits.size() + 10 * lane.yVelocity * (j+1);
						_xDestination = lane.xOrigin;
					}
				}
				_transportationMethod = Command.car;
				waitForLaneToFinish();
				if (j!=0)
					lane.permits.get(j-1).release();
			}
			
			if (i<route.size() - 1){
				Lane next_lane = Directory.lanes().get(route.get(i+1));
				while(!Directory.intersections().get(i).tryAcquire());
				if (next_lane.isHorizontal){
					if (next_lane.xVelocity>0){
						_xDestination = next_lane.xOrigin - 10;
						_yDestination = next_lane.yOrigin;
					}
					else {
						_xDestination = next_lane.xOrigin + 10 * lane.permits.size();
						_yDestination = next_lane.yOrigin;
					}
				}
				else {
					if (lane.yVelocity>0){
						_yDestination = lane.yOrigin - 10;
						_xDestination = next_lane.xOrigin;
					}
					else{
						_yDestination = lane.yOrigin + 10 * lane.permits.size();
						_xDestination = next_lane.xOrigin;
					}
				}
				_transportationMethod = Command.car;
				waitForLaneToFinish();
			//TODO to get rid of deadlock acquire both intersection and the first spot in next lane
				lane.permits.get(lane.permits.size()-1).release();
			}
			lane.permits.get(lane.permits.size()-1).release();
		}
		setPresent(false);
	}
	
	//Bus gui
	public void goToBusStop(BusStopObject busstop){
		_transportationMethod = Command.walk;
		Position destinationP = convertPixelToGridSpace(busstop.positionX(), busstop.positionY() - 10);
		guiMoveFromCurrentPositionTo(destinationP);
		setPresent(true);
	}
/*	
	//Car gui
	public void goToCar(CarObject car, Place destination){
		_goingSomewhere = true;
		_xDestination = car.getXPosition();
		_yDestination = car.getYPosition();
		setPresent(true);
	}
	
	public void atCar(){
		setPresent(false);
		_commuter.msgAtCar();
	}
	
*/	
	public void getOnBus(){
		setPresent(false);
	}
	
	public void getOffBus(BusStopObject busstop){
	/*	currentPosition = busstop.positionX(); // setx
		currentPosition = busstop.positionY(); // sety */
		setPresent(true);
	}
	
	//------------------------------------------Animation---------------------------------------
	@Override
	public void updatePosition() {
		if (_xPos < _xDestination)
			_xPos++;
		else if (_xPos > _xDestination)
			_xPos--;

		if (_yPos < _yDestination)
			_yPos++;
		else if (_yPos > _yDestination)
			_yPos--; 
		
		
		
		if (_transportationMethod == Command.car){
			if (_xPos < _xDestination)
				_xPos++;
			else if (_xPos > _xDestination)
				_xPos--;

			if (_yPos < _yDestination)
				_yPos++;
			else if (_yPos > _yDestination)
				_yPos--;
		} 
		
		if(_xPos == _xDestination &&  _yPos == _yDestination &&
				(_transportationMethod == Command.car || _transportationMethod == Command.walk)){
			_transportationMethod = Command.none;
			//setPresent(false);
			releaseSemaphore();
			//_commuter.msgReachedDestination();
		}
	}
	
	public void move( int xv, int yv ) {
		_xPos+=xv;
		_yPos+=yv;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isPresent){
			if(_commuter.hasCar())
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);
			g.fillRect(_xPos, _yPos, 10, 10);
		}
	}
	
	// ----------------------------------------- UTILITIES --------------------------------------------
	/** This function returns the x value of the place; it can receive a value of null */
	private int placeX(Place place) {
		if(place != null) {
			return place.positionX();
		}
		else {
			return NULL_POSITION_X;
		}
	}
	/** This function returns the y value of the place; it can receive a value of null */
	private int placeY(Place place) {
		if(place != null) {
			return place.positionY();
		}
		else {
			return NULL_POSITION_Y;
		}
	}
	
/*	void move(int destx, int desty){
		_xDestination = destx;
		_yDestination = desty;
	} */
	
	void guiMoveFromCurrentPositionTo(Position to){
        AStarNode aStarNode = (AStarNode)_aStarTraversal.generalSearch(currentPosition, to);
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
            gotPermit       = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY()).moveInto(_aStarTraversal.getGrid());

            //Did not get lock. Lets make n attempts.
            while (!gotPermit && attempts < 3) {
                //System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

                //Wait for 1sec and try again to get lock.
                try { Thread.sleep(1000); }
                catch (Exception e){}

                gotPermit   = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY()).moveInto(_aStarTraversal.getGrid());
                attempts ++;
            }

            //Did not get lock after trying n attempts. So recalculating path.            
            if (!gotPermit) {
                guiMoveFromCurrentPositionTo(to);
                break;
            }

            //Got the required lock. Lets move.
            currentPosition.release(_aStarTraversal.getGrid());
            currentPosition = convertPixelToGridSpace(tmpPath.getX(), tmpPath.getY ());
          //  move(currentPosition.getX(), currentPosition.getY());
        }
        /*
        boolean pathTaken = false;
        while (!pathTaken) {
            pathTaken = true;
            //print("A* search from " + currentPosition + "to "+to);
            AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
            if (a == null) {//generally won't happen. A* will run out of space first.
                System.out.println("no path found. What should we do?");
                break; //dw for now
            }
            //dw coming. Get the table position for table 4 from the gui
            //now we have a path. We should try to move there
            List<Position> ps = a.getPath();
            Do("Moving to position " + to + " via " + ps);
            for (int i=1; i<ps.size();i++){//i=0 is where we are
                //we will try to move to each position from where we are.
                //this should work unless someone has moved into our way
                //during our calculation. This could easily happen. If it
                //does we need to recompute another A* on the fly.
                Position next = ps.get(i);
                if (next.moveInto(aStar.getGrid())){
                    //tell the layout gui
                    guiWaiter.move(next.getX(),next.getY());
                    currentPosition.release(aStar.getGrid());
                    currentPosition = next;
                }
                else {
                    System.out.println("going to break out path-moving");
                    pathTaken = false;
                    break;
                }
            }
        }
        */
    }
	
	Position convertPixelToGridSpace(int x, int y){
		return new Position((x-41)/10, (y-30)/10);
	}
	
	private int getBlockX(int xPos){
		if (xPos >= 41 + 8 * 10 && xPos < 41 + 16 * 10)
			return 0;
		if (xPos >= 41 + 24 * 10 && xPos < 41 + 36 * 10)
			return 1;
		if (xPos >= 41 + 44 * 10 && xPos < 41 + 52 * 10)
			return 2;
		return -1;
	}
	
	private int getBlockY(int yPos){
		if (yPos >= 30 + 5 * 10 && yPos < 41 + 9 * 10)
			return 0;
		if (yPos >= 41 + 19 * 10 && yPos < 41 + 25 * 10)
			return 1;
		return -1;
	}
	
	private void waitForLaneToFinish() {
		try {
			_reachedDestination.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
