package city.restaurant.tanner.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import city.restaurant.tanner.TannerRestaurantBaseWaiterRole;
import city.restaurant.tanner.TannerRestaurantSharedDataWaiterRole;
import city.restaurant.tanner.interfaces.TannerRestaurantWaiter;
import gui.Gui;

public class TannerRestaurantWaiterRoleGui implements Gui {
	
	boolean present;
	TannerRestaurantBaseWaiterRole agent;
	int xPos;
	int yPos;
	int xDestination;
	int yDestination;
    ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/NormalWaiter.png"));
    Image normal = a.getImage();
    ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/SharedDataWaiter.png"));
    Image shared = b.getImage();
    int xGap = 17;
    int yGap = 27;

	public TannerRestaurantWaiterRoleGui(TannerRestaurantWaiter newWaiter, int waiter_count) 
	{
		present = true;
		agent = (TannerRestaurantBaseWaiterRole) newWaiter;
		xPos = 80;
		yPos = 80;
		xDestination = 80;
		yDestination = 80;
	}

	@Override
	public void updatePosition() 
	{
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if (xPos == xDestination && yPos == yDestination) {
        	agent.msgAnimationFinished();
		}
	}

	@Override
	public void draw(Graphics2D g) 
	{
		if(agent.active)
		{
			if(agent instanceof TannerRestaurantSharedDataWaiterRole)
			{
				g.drawImage(shared, xPos, yPos, xGap, yGap, null);
			}
			else
			{
				g.drawImage(normal, xPos, yPos, xGap, yGap, null);
			}
		}
	}

	@Override
	public boolean isPresent() 
	{
		return present;
	}

	public void DoGoToFront() 
	{
		xDestination = 80;
		yDestination = 80;
	}

	public void DoGoToTable(int tableNumber) 
	{
		if(tableNumber == 1)
		{
			xDestination = 150;
			yDestination = 120;
		}
		
		if(tableNumber == 2)
		{
			xDestination = 270;
			yDestination = 120;
		}
		
		if(tableNumber == 3)
		{
			xDestination = 390;
			yDestination = 120;
		}
	}

	public void DoGoToCook(Point position) 
	{
		xDestination = 515;
		yDestination = 100;
	}

	public void DoGoToRevolvingStand() 
	{
		xDestination = 515;
		yDestination = 200;
	}

}
