package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import aStar.AStarTraversal;
import city.Time;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.CommuterGui;
import city.transportation.gui.TruckAgentGui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

@SuppressWarnings("serial")
public class WorldView extends JPanel implements MouseListener, ActionListener 
{
	private static int WINDOWX = 1024 * 2 / 3;
	private static int WINDOWY = 720 / 2;
	
	static int gridX = 682;
	static int gridY = 360;
	Semaphore[][] grid = new Semaphore[gridX+1][gridY+1];

    private List<Gui> guis = new ArrayList<Gui>();
	
	ArrayList<WorldViewBuilding> buildings;
	
	public WorldView()
	{
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		this.setBorder(BorderFactory.createTitledBorder("World View"));
		 buildings = new ArrayList<WorldViewBuilding>();
         
		 for (int i=0; i<gridX+1 ; i++){
	         for (int j = 0; j<gridY+1; j++){
	             grid[i][j]=new Semaphore(1,true); //exclude buildings
	         }
		 }
		 
		 try{
		 for (int j=0; j<gridY+1; j++) {grid[0][0+j].acquire();} // cant go to row 0 or col 0
	     for (int k=1; k<gridX+1; k++) {grid[0+k][0].acquire();}
		 } catch (Exception e){
			 System.out.println("Unexpected Exception");
			 e.printStackTrace();
		 }
		 
		 for ( int i=0; i<buildings.size(); i++ ) {
             WorldViewBuilding b = buildings.get(i);
             for(int j = 0; j < b.height;j++){
            	 for(int k = 0; k < b.width; k++){
            		 try {
						grid[b.xPosition()+k][b.yPosition()+j].acquire();
					} catch (InterruptedException e) {
						System.out.println("Unexpected error");
						e.printStackTrace();
					}
            	 }
             }
     }	
	     
         addMouseListener( this );

     	Timer timer = new Timer(10, this);
     	timer.start();
	}
	
    public void addGui(CommuterGui gui)
    {
       guis.add(gui);
    }
	
	public WorldViewBuilding addBuilding(int x, int y, int dim)
	{
		 WorldViewBuilding b = new WorldViewBuilding( x, y, dim );
		 buildings.add( b );
		 return b;
	}
	
	public void addGui(BusAgentGui gui){
		guis.add(gui);
	}
	
	public void addGui(TruckAgentGui gui){
		guis.add(gui);
	}
	

	public void paintComponent( Graphics g ) {
		
	/*	 if ((int)(100*Time.getTime()) % 25 == 0) {
				for ( int i=0; i<lanes.size(); i++ ) {
					lanes.get(i).redLight();
				}
			}
			if ((int)(100*Time.getTime()) % 50 == 0){
				for ( int i=0; i<lanes.size(); i++ ) {
					lanes.get(i).greenLight();
				}
		} */
			
		super.paintComponent(g); // this prevents the building animation panel from being copied in the worldview (for some reason).
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor( Color.black );
		
		for ( int i=0; i<buildings.size(); i++ ) {
			WorldViewBuilding b = buildings.get(i);
		    g2.fill( b );
		}
		
        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.updatePosition();
            }
        }
        //TODO make guis a synchronized list
        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.draw(g2);
            }
        } 
        
	}
	
	public ArrayList<WorldViewBuilding> getBuildings() {
		return buildings;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse Clicked in WorldView");

        for ( int i=0; i<buildings.size(); i++ ) {
                WorldViewBuilding b = buildings.get(i);
                if ( b.contains( e.getX(), e.getY() ) ) {
                	if(b.myBuildingPanel != null){
                        b.displayBuilding();
                	}
                }
        }	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint(); // will call paintComponent
	}
	
	public AStarTraversal generateAStar(){
		return new AStarTraversal(grid);
	}
}