package city.restaurant.tanner.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class TannerRestaurantAnimationPanel extends JPanel implements ActionListener{
	
	int WINDOWX = 680;
	int WINDOWY = 360;
	int agentWidth = 20;
	int agentHeight = 20;
	
	ImageIcon a = new ImageIcon(this.getClass().getResource("/image/restaurant/Fridge.png"));
	Image fridge = a.getImage();
	Dimension fridgePos = new Dimension(580, 260);
	int fridgeWidth = 25;
	int fridgeHeight = 48;
	
	ImageIcon b = new ImageIcon(this.getClass().getResource("/image/restaurant/Grill.png"));
	Image grill = b.getImage();
	Dimension grillPos = new Dimension(580, 1);
	int grillWidth = 28;
	int grillHeight = 88;
	
	ImageIcon c = new ImageIcon(this.getClass().getResource("/image/restaurant/Plating.png"));
	Image platingArea = c.getImage();
	Dimension platingPos = new Dimension(540, 80);
	int platingWidth = 20;
	int platingHeight = 80;
	
	ImageIcon d = new ImageIcon(this.getClass().getResource("/image/restaurant/RevolvingStand.png"));
	Image rStand = d.getImage();
	Dimension rsPos = new Dimension(540,200);
	int rsWidth = 24;
	int rsHeight = 32;
	
	ImageIcon e = new ImageIcon(this.getClass().getResource("/image/restaurant/Table.png"));
	Image table = e.getImage();
	Dimension table1Pos = new Dimension(180, 120);
	Dimension table2Pos = new Dimension(300, 120);
	Dimension table3Pos = new Dimension(420, 120);
	int tableWidth = 50;
	int tableHeight = 53;
	
	ImageIcon f = new ImageIcon(this.getClass().getResource("/image/restaurant/CashierCounter.png"));
	Image cashierCounter = f.getImage();
	Dimension cashierCounterPos = new Dimension(140, 20);
	int ccWidth = 63;
	int ccHeight = 51;
	
	ImageIcon g = new ImageIcon(this.getClass().getResource("/image/restaurant/Counter.png"));
	Image counter = g.getImage();
	Dimension counterPos = new Dimension(540, 0);
	int coutnerWidth = 20;
	int counterHeight = 350;
	
	ImageIcon h = new ImageIcon(this.getClass().getResource("/image/restaurant/WoodFloor.png"));
	Image diningRoom = h.getImage();
	Dimension drPos = new Dimension(0, 0);
	int drWidth = 540;
	int drHeight = 360;
	
	ImageIcon i = new ImageIcon(this.getClass().getResource("/image/restaurant/KitchenFloor.png"));
	Image kitchen = i.getImage();
	Dimension kitchenPos = new Dimension(540, 0);
	int kitchenWidth = 140;
	int kitchenHeight = 350;

	public void addGui(TannerRestaurantWaiterRoleGui tannerWaiterGui) {
		
		// TODO Auto-generated method stub
		
	}

	public void addGui(TannerRestaurantCustomerRoleGui tannerCustomerGui) {
		
		// TODO Auto-generated method stub
		
	}

	public void addGui(TannerRestaurantCashierRoleGui tannerCashierGui) {
		// TODO Auto-generated method stub
		
		
	}

	public void addGui(TannerRestaurantCookRoleGui tannerCookGui) {
		// TODO Auto-generated method stub
		
		
	}

	public void addGui(TannerRestaurantHostRoleGui tannerHostGui) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
