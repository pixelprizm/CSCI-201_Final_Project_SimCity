package gui.astar;

import  java.util.*;
import java.util.concurrent.Semaphore;

public class AStarNode implements Node
{
	Semaphore access;
	public static enum NodeType {none, people, vehicles};
	NodeType type;
	List<AStarNode> adjacencyList;
	int xPos;
	int yPos;
	int f;
	int g;
	int h;
	public Position gridLoc;
	AStarNode parentNode;
	
	public AStarNode(Position p, NodeType t)
	{
		access = new Semaphore(1, true);
		adjacencyList = new ArrayList<AStarNode>();
		type = t;
		gridLoc = p;
		if(type == NodeType.people)
		{
			xPos = gridLoc.x * 20 + 7;
			yPos = gridLoc.y * 20 + 7;
		}
		else if(type == NodeType.vehicles)
		{
			xPos = gridLoc.x * 20;
			yPos = gridLoc.y * 20;
		}
		else if(type == NodeType.none)
		{
			xPos = -1;
			yPos = -1;
		}
		int f = 0;
		int g = 0;
		int h = 0;
		parentNode = null;
	}
	
	public void ComputeAdjacencyList(AStarNode[][] grid)
	{
		if(gridLoc.x > 0)
		{
			if(grid[gridLoc.x-1][gridLoc.y].type == this.type)
			{
				adjacencyList.add(grid[gridLoc.x-1][gridLoc.y]);
			}
		}
		if(gridLoc.x < 34)
		{
			if(grid[gridLoc.x+1][gridLoc.y].type == this.type)
			{
				adjacencyList.add(grid[gridLoc.x+1][gridLoc.y]);
			}
		}
		if(gridLoc.y > 0)
		{
			if(grid[gridLoc.x][gridLoc.y-1].type == this.type)
			{
				adjacencyList.add(grid[gridLoc.x][gridLoc.y-1]);
			}
		}
		if(gridLoc.y > 18)
		{
			if(grid[gridLoc.x][gridLoc.y+1].type == this.type)
			{
				adjacencyList.add(grid[gridLoc.x][gridLoc.y+1]);
			}
		}
	}
	
	public int TestAdoptionCase(AStarNode n)
	{
		int newG = n.g+1;
		return newG;
	}
	
	public void ComputeG(AStarNode startNode)
	{
		if(this == startNode)
		{
			g = 0;
		}
		else
		{
			g = parentNode.g + 1;
		}
	}
	
	public void ComputeH(AStarNode endNode)
	{
		h = Math.abs(endNode.gridLoc.x - this.gridLoc.x) + Math.abs(endNode.gridLoc.y - this.gridLoc.y);
	}
	
	public void ComputeF()
	{
		f = h + g;
	}

	@Override
	public boolean goalTest(Position o) 
	{
		return gridLoc.equals(o);
	}

	@Override
	public void printNode() 
	{
		//Don't need this
	}
	
	/*private Position pos; //last position in path ; redundant
	private double distTravelled;
	private double approxTotalDist;
	private List<Position> path; 

	public AStarNode(Position pos){
		if (pos==null) System.out.println("AStarNode constructor, pos is null?");
		this.pos = pos;
	}
	public Position getPosition() {
		return pos;		
	}
	public String toString(){
		String t = "";
		for (Position p : path) t = t + p.toString();
		return "("+ approxTotalDist +","+distTravelled+",("+t+")";		    
	}
	public double getDistTravelled() {
		return distTravelled;
	}
	public void setDistTravelled(double distTravelled){
		this.distTravelled = distTravelled;
	}
	public double getApproxTotalDist() {
		return approxTotalDist;
	}
	public void setApproxTotalDist(double newApprox){
		//distCity is the straight line distance to B
		approxTotalDist =  newApprox;
	}
	public List<Position> getPath() {
		return path;
	}
	public void setPath(List<Position> path) {
		this.path=path;
	}
	public void updatePath(List<Position> x) {
		path = new ArrayList<Position>(x);
		path.add(pos);
	}
	public void printNode() {
		String ppos = printPath();
		System.out.print("("+pos);
		System.out.format(" %.2f,%.2f,",
				approxTotalDist, distTravelled);
		System.out.print(ppos + ")");
	}
	public String printPath() {
		String pp = "(";
		for (Position p:path) pp = pp + p;
		return pp+")";
	}

	public boolean goalTest(Object p){
		return pos.equals((Position)p); 
	}*/	
}