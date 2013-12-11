package city.restaurant.tanner.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import city.restaurant.tanner.TannerRestaurantCashierRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCashier;
import gui.Gui;

public class TannerRestaurantCashierRoleGui implements Gui {
	
	private TannerRestaurantCashierRole agent;
	boolean present;
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Cashier.png"));
    Image cashier = a.getImage();
    int xGap = 19;
    int yGap = 25;
    int xPos;
    int yPos;
    int xDest;
    int yDest;
    enum Command {noCommand, LeaveRestaurant}
    Command command;

	public TannerRestaurantCashierRoleGui(TannerRestaurantCashier cashier) 
	{
		agent = (TannerRestaurantCashierRole) cashier;
		present = true;
		xPos = 150;
		yPos = 25;
		xDest = 150;
		yDest = 25;
	}

	@Override
	public void updatePosition() 
	{
		if (xPos < xDest)
            xPos++;
        else if (xPos > xDest)
            xPos--;

        if (yPos < yDest)
            yPos++;
        else if (yPos > yDest)
            yPos--;
        
        if (xPos == xDest && yPos == yDest) {
			if (command==Command.LeaveRestaurant) {
				agent.active = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	@Override
	public void draw(Graphics2D g) 
	{
		if(agent.active)
		{
			g.drawImage(cashier, xPos, yPos, xGap, yGap, null);
		}
	}

	@Override
	public boolean isPresent() 
	{
		return present;
	}
	
	public void LeaveRestaurant()
	{
		xDest = -40;
		yDest = -40;
		command = Command.LeaveRestaurant;
	}

}
