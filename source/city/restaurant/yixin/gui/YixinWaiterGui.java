package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinWaiterRole;

public class YixinWaiterGui extends JPanel implements Gui {

	private YixinWaiterRole agent = null;
	private boolean show_choice = false;
	private String food;
	public static final int TableX = 500;
	public static final int TableY1 = 60, TableY2 = 130, TableY3 = 200;
	
	public static final int xPlate=200, xCook = 300, yCook = 250;
	private static int xPlace, yPlace = 90;//default waiter position
	private int xPos, yPos = 90;//waiter current position
	private int xDestination, yDestination = 90;//destination
	public static int xTable = 500;
	public static int yTable = 150;
	public static int xGap = 30;
	public static int yGap = 30;
	int count;
	private ImageIcon i = new ImageIcon(this.getClass().getResource("/image/waiter.png"));
	private Image image = i.getImage();

	private enum Command {noCommand, GoToSeat};
	private Command command=Command.noCommand;

	public YixinWaiterGui(YixinWaiterRole  agent, int count) {
		this.agent = agent;
		this.count = count;
		xPlace = 100 + count*50;
		xPos = xPlace;
		xDestination = xPos;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;
		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		if (yPos == yDestination && xPos == xDestination && command == Command.GoToSeat){
			command = Command.noCommand;
			show_choice = false;
			agent.releaseSemaphore();
		}
	}

	public void draw(Graphics2D g) {
		if(agent.active){
			g.setColor(Color.RED);
			//g.fillRect(xPos, yPos, xGap, yGap);    	
			g.drawImage(image, xPos, yPos, xGap, yGap, this);
			if (show_choice)
				g.drawString(food, xPos, yPos - 10);
		}
	}

	public boolean isPresent() {
		return true;
	}

	public void DoGoToTable(int table_number) {
		if (table_number == 1){
			yTable = TableY1;
		}
		else if(table_number == 2){
			yTable = TableY2;
		}
		else if (table_number == 3){
			yTable = TableY3;
		}
		xDestination = xTable - xGap;
		yDestination = yTable - yGap;
		command = Command.GoToSeat;
	}

	public void DoBringFood(int table_number, String food) {

		if (table_number == 1){
			yTable = TableY1;
		}
		else if(table_number == 2){
			yTable = TableY2;
		}
		else if (table_number == 3){
			yTable = TableY3;
		}
		this.food = food;
		show_choice = true;
		xDestination = xTable - xGap;
		yDestination = yTable - yGap;
		command = Command.GoToSeat;
	}

	public void DoGoToCook() {
		xDestination = xCook;
		yDestination = yCook - yGap;
		command = Command.GoToSeat;
	}

	public void DoFetchDish() {
		xDestination = xPlate;
		yDestination = yCook - yGap;
		command = Command.GoToSeat;
	}

	public void DoGoToRevolvingStand() {
		xDestination = xPlate - 60;
		yDestination = yCook - yGap;
		command = Command.GoToSeat;
	}

	public void DoLeaveCustomer() {
		xDestination = 100+count*50;
		yDestination = yPlace;
	}

	public void DoFetchCustomer(int count) {
		xDestination = 30 + xGap;
		yDestination = count*30+15 + yGap;
		command = Command.GoToSeat;
	}

	public void LeaveRestaurant(){
		xDestination = 0;
		yDestination = 0;
		command = Command.GoToSeat;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

}
