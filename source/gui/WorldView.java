package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.Time;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.CommuterGui;
import city.transportation.gui.TruckAgentGui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@SuppressWarnings("serial")
public class WorldView extends JPanel implements MouseListener, ActionListener 
{
	private static int WINDOWX = 1024 * 2 / 3;
	private static int WINDOWY = 720 / 2;

    private List<Gui> guis = new ArrayList<Gui>();
    private Image background;
	
	ArrayList<WorldViewBuilding> buildings;
	
	public WorldView()
	{
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		//this.setBorder(BorderFactory.createTitledBorder("World View"));
		 buildings = new ArrayList<WorldViewBuilding>();
		 try {
			background = ImageIO.read(new File("source/image/WorldView/WorldViewBG.png"));
		} catch (IOException e) {
			System.out.println("Failed to load image");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 this.setLayout(null);
         
         addMouseListener( this );

     	Timer timer = new Timer(10, this);
     	timer.start();
	}
	
    public void addGui(CommuterGui gui)
    {
       guis.add(gui);
    }
	
	public WorldViewBuilding addBuilding(String filePath, int x, int y)
	{
		 WorldViewBuilding b = new WorldViewBuilding(filePath, x, y);
		 buildings.add( b );
		 return b;
	}
	
	public void addGui(BusAgentGui gui){
		guis.add(gui);
	}
	
	public void addGui(TruckAgentGui gui){
		guis.add(gui);
	}
	
	public void addAllBuildingsToWorld()
	{
		for(int i = 0; i < buildings.size(); i++)
		{
			this.add(buildings.get(i));
			buildings.get(i).setBounds(buildings.get(i).positionX(), buildings.get(i).positionY(), 
					buildings.get(i).width(), buildings.get(i).height());
			buildings.get(i).addActionListener(this);
		}
		this.repaint();
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
		g2.drawImage(background, 0, 0, null);
		g2.setColor( Color.black );
		
		/*g.drawString("Bank 1", 530, 230);
		g.drawString("Bank 2", 330, 325);
		g.drawString("YixinRestaurant", 530, 130);
		g.drawString("Market 1", 530, 325);
		g.drawString("Market 2", 430, 325);
		g.drawString("OmarRestaurant", 380, 230);
		g.drawString("RyanRestaurant", 380, 125);
		g.drawString("EricRestaurant", 280, 125);
		g.drawString("TannerRestaurant", 280, 230);
		
		g.drawString("Bus Stop", 60, 40);
		g.drawString("Bus Stop", 560, 40);
		g.drawString("Bus Stop", 560, 340);
		g.drawString("Bus Stop", 60, 340);
		
		g.drawString("Houses", 60, 300);
		g.drawString("Apartments", 100, 60);*/
		
		/*for ( int i=0; i<buildings.size(); i++ ) {
			WorldViewBuilding b = buildings.get(i);
		    g2.fill( b );
		}*/
		
		try
		{
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
		} catch(ConcurrentModificationException e) { } // do nothing, because this function will get called again
        
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
		for(int i = 0; i < buildings.size(); i++)
		{
			if(buildings.get(i) == e.getSource())
			{
				if(buildings.get(i).myBuildingPanel != null)
				{
                    buildings.get(i).displayBuilding();
				}
			}
		}
	}
}