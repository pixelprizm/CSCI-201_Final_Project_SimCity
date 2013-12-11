package city.restaurant.tanner.gui;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import city.restaurant.tanner.TannerRestaurantCustomerRole;
import city.restaurant.tanner.interfaces.TannerRestaurantCustomer;
import gui.Gui;

public class TannerRestaurantCustomerRoleGui implements Gui {

	boolean present;
	TannerRestaurantCustomerRole agent;
	int xPos;
	int yPos;
	int xDestination;
	int yDestination;
    private Timer thinkTimer = new Timer();
    private Timer eatTimer = new Timer();
	
    public enum Command {noCommand, GoToWait, GoToSeat, GoPay, LeaveRestaurant};
    private Command command=Command.noCommand;
    
    
	public TannerRestaurantCustomerRoleGui(TannerRestaurantCustomer customer) 
	{
		agent = (TannerRestaurantCustomerRole) customer;
		present = true;
		xPos = 0;
		yPos = 0;
		xDestination = 0;
		yDestination = 0;
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
	
			if (command==Command.GoToSeat)
			{
                agent.msgAnimationFinishedSeat();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishLeaving();
			}
			else if(command==Command.GoPay)
			{
				agent.msgReadyToPay();
			}
			else if(command==Command.GoToWait)
			{
				agent.msgAtWaitArea();
			}
			command=Command.noCommand;
		}

	}

	@Override
	public void draw(Graphics2D g) 
	{	
		g.drawImage(agent.getImage(), xPos, yPos, 20, 27, null);
	}

	@Override
	public boolean isPresent() 
	{
		return present;
	}

	public void DoGoToWaitArea() 
	{
		xDestination = 50;
		yDestination = 50;
		command = Command.GoToWait;
	}

	public void DoGoToSeat(int tableNumber) 
	{
		if(tableNumber == 1)
		{
			xDestination = 180;
			yDestination = 90;
		}
		
		if(tableNumber == 2)
		{
			xDestination = 300;
			yDestination = 90;	
		}
		
		if(tableNumber == 3)
		{
			xDestination = 400;
			yDestination = 90;	
		}
		command = Command.GoToSeat;
	}

	public void DoLookAtMenu() 
	{
        thinkTimer.schedule(new TimerTask() {
            public void run()
            {
            	agent.msgFiguredOutMyOrder();
            }
        }, 5000);
	}

	public void DoEatFood() 
	{
        eatTimer.schedule(new TimerTask() {
            public void run()
            {
            	agent.msgAnimationFinishEating();
            }
        }, 10000);
	}

	public void DoGoToFront() 
	{
		xDestination = 110;
		yDestination = 30;
		command = Command.GoPay;
	}

	public void DoExitRestaurant() 
	{
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

}
