package city.restaurant.tanner.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import city.restaurant.tanner.TannerRestaurantCookRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCook;
import gui.Gui;

public class TannerRestaurantCookRoleGui implements Gui {
	
	boolean present;
	TannerRestaurantCookRole agent;
	int xPos;
	int yPos;
	int xDestination;
	int yDestination;
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Chef.png"));
    Image cook = a.getImage();
    int xGap = 18;
    int yGap = 32;
	

	public TannerRestaurantCookRoleGui(TannerRestaurantCook cook) 
	{
		agent = (TannerRestaurantCookRole) cook;
		present = true;
		xPos = 565;
		yPos = 100;
		xDestination = 565;
		yDestination = 100;
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
        	
        	agent.msgAnimationComplete();
		}
	}

	@Override
	public void draw(Graphics2D g) 
	{
		if(agent.active)
			g.drawImage(cook, xPos, yPos, xGap, yGap, null);
	}

	@Override
	public boolean isPresent() 
	{
		return present;
	}

	public void DoGoToRevolvingStand() 
	{
		xDestination = 570;
		yDestination = 200;
	}

	public void DoGoToIngredients() 
	{
		xDestination = 610;
		yDestination = 260;
	}

	public void DoGoToGrills() 
	{
		xDestination = 610;
		yDestination = 60;
	}

	public void DoGoToHeatLamp() 
	{
		xDestination = 565;
		yDestination = 100;
	}

}
