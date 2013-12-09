package gui;

/**
 * This is the class is the main window for the project.  This is also where the main function is.
 * @author Tanner Zigrang
 */

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import city.Directory;
import city.Time;
import city.bank.Bank;
import city.home.*;
import city.market.Market;
import city.restaurant.omar.OmarRestaurant;
import city.restaurant.ryan.RyanRestaurant;
import city.restaurant.yixin.YixinRestaurant;
import city.transportation.*;
import city.transportation.gui.BusAgentGui;

public class MainGui extends JFrame 
{
	private static int FRAMEX = 1024;
	private static int FRAMEY = 720;
	Semaphore[][] grid;
	
	BuildingCardLayoutPanel _buildingCardLayoutPanel;
	ControlPanel cPanel;
	
	List<BuildingInteriorAnimationPanel> _buildingInteriorAnimationPanels = new ArrayList<BuildingInteriorAnimationPanel>();
	
	WorldView _worldView;
	/**
	 * Constructor for the MainGui window
	 */
	public MainGui()
	{
		//The code below is for setting up the default window settings
		this.setSize(FRAMEX, FRAMEY);
		this.setLocationRelativeTo(null);
		this.setTitle("SimCity201 - Team 18");
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		
		//Building View
		_buildingCardLayoutPanel = new BuildingCardLayoutPanel();
		
		//World View
		_worldView = new WorldView();
		
		//Control Panel
		cPanel = new ControlPanel(this);
		    
		//The code below will add an area for the two gui areas to go. BuildingView + WorldView
		JPanel animationArea = new JPanel();
		animationArea.setLayout(new BoxLayout(animationArea, BoxLayout.Y_AXIS));
		animationArea.setPreferredSize(new Dimension(2048/3, 720));
		animationArea.add(_worldView);
		animationArea.add(_buildingCardLayoutPanel);
		this.add(animationArea, Component.LEFT_ALIGNMENT);
		
		
		//TODO add all the buildings
		//WorldViewBuilding test = _worldView.addBuilding("source/image/host.png", 100, 100);
		
		//Yixin's Restaurant
		WorldViewBuilding b = _worldView.addBuilding("source/image/WorldView/Restaurants/YixinRestaurant.png", 520, 280);
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.name());
		_buildingInteriorAnimationPanels.add(bp);
		
		//Omar's Restaurant
		WorldViewBuilding b9 = _worldView.addBuilding("source/image/WorldView/Restaurants/OmarRestaurant.png", 120, 40);
		BuildingInteriorAnimationPanel bp9 = new BuildingInteriorAnimationPanel(this, "Omar's Restaurant", new city.restaurant.omar.gui.OmarRestaurantAnimationPanel());
		b9.setBuildingPanel(bp9);
		OmarRestaurant or = new OmarRestaurant("Omar's Restaurant", b9, bp9);
		Directory.addPlace(or);
		_buildingCardLayoutPanel.add( bp9, bp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(or.name());
		_buildingInteriorAnimationPanels.add(bp9);
		
		//Ryan's Restaurant
		WorldViewBuilding bR = _worldView.addBuilding("source/image/WorldView/Restaurants/RyanRestaurant.png", 520, 40);
		BuildingInteriorAnimationPanel bpR = new BuildingInteriorAnimationPanel(this, "Ryan's Restaurant", new city.restaurant.ryan.gui.RyanAnimationPanel());
		bR.setBuildingPanel(bpR);
		RyanRestaurant rr = new RyanRestaurant("Ryan's Restaurant", bR, bpR);
		Directory.addPlace(rr);
		_buildingCardLayoutPanel.add( bpR, bpR.getName() );
		cPanel.currentBuildingPanel.addBuilding(rr.name());
		_buildingInteriorAnimationPanels.add(bpR);
		
		//TODO Change to Eric's Restaurant
		WorldViewBuilding restaurantBuilding4 = _worldView.addBuilding("source/image/WorldView/Restaurants/EricRestaurant.png", 320, 40);
		BuildingInteriorAnimationPanel restaurantBuildingPanel4 = new BuildingInteriorAnimationPanel(this, "Eric's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding4.setBuildingPanel(restaurantBuildingPanel4);
		YixinRestaurant er = new YixinRestaurant("Eric's Restaurant", restaurantBuilding4, restaurantBuildingPanel4);
		Directory.addPlace(er);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel4, restaurantBuildingPanel4.getName() );
		cPanel.currentBuildingPanel.addBuilding(er.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel4);
		
		//TODO Change to Tanner's Restaurant
		WorldViewBuilding restaurantBuilding5 = _worldView.addBuilding("source/image/WorldView/Restaurants/TannerRestaurant.png", 120, 280);
		BuildingInteriorAnimationPanel restaurantBuildingPanel5 = new BuildingInteriorAnimationPanel(this, "Tanner's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding5.setBuildingPanel(restaurantBuildingPanel5);
		YixinRestaurant tr = new YixinRestaurant("Tanner's Restaurant", restaurantBuilding5, restaurantBuildingPanel5);
		Directory.addPlace(tr);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel5, restaurantBuildingPanel5.getName() );
		cPanel.currentBuildingPanel.addBuilding(tr.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel5);
		
		//Market 1
		WorldViewBuilding b3 = _worldView.addBuilding("source/image/WorldView/Market/Market.png", 40, 140);
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market 1", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market 1", b3, bp3, _worldView);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.name());
		_buildingInteriorAnimationPanels.add(bp3);
		
		//Market 2
		WorldViewBuilding marketBuilding2 = _worldView.addBuilding("source/image/WorldView/Market/Market.png", 600, 140);
		BuildingInteriorAnimationPanel marketBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Market 2", new city.market.gui.MarketAnimationPanel());
		marketBuilding2.setBuildingPanel(marketBuildingPanel2);
		Market market2 = new Market("Market 2", marketBuilding2, marketBuildingPanel2, _worldView);
		Directory.addPlace(market2);
		_buildingCardLayoutPanel.add( marketBuildingPanel2, marketBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(market2.name());
		_buildingInteriorAnimationPanels.add(marketBuildingPanel2);
		
		//Bank 1
		//Bank
		WorldViewBuilding b2 = _worldView.addBuilding("source/image/WorldView/Bank/Bank.png", 600, 40);
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.name());
		_buildingInteriorAnimationPanels.add(bp2);
		
		//Bank 2
		WorldViewBuilding bankBuilding2 = _worldView.addBuilding("source/image/WorldView/Bank/Bank.png", 40, 280);
		BuildingInteriorAnimationPanel bankBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Bank 2", new city.bank.gui.BankAnimationPanel());
		bankBuilding2.setBuildingPanel(bankBuildingPanel2);
		Bank bank2 = new Bank("Bank 2", bankBuilding2, bankBuildingPanel2);
		Directory.addPlace(bank2);
		_buildingCardLayoutPanel.add( bankBuildingPanel2, bankBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank2.name());
		_buildingInteriorAnimationPanels.add(bankBuildingPanel2);
		
		//House1
	    WorldViewBuilding h1 = _worldView.addBuilding("source/image/WorldView/Housing/House1.png", 200, 280);
		BuildingInteriorAnimationPanel hp1 = new BuildingInteriorAnimationPanel(this, "House 1", new city.home.gui.HouseAnimationPanel());
		h1.setBuildingPanel(hp1);
		House house1 = new House("House 1", h1, hp1);
		Directory.addPlace(house1);
		_buildingCardLayoutPanel.add( hp1, hp1.getName() );
		cPanel.currentBuildingPanel.addBuilding(house1.name());
		_buildingInteriorAnimationPanels.add(hp1);
		
		//House 2
	    WorldViewBuilding h2 = _worldView.addBuilding("source/image/WorldView/Housing/House2.png", 260, 280);
		BuildingInteriorAnimationPanel hp2 = new BuildingInteriorAnimationPanel(this, "House 2", new city.home.gui.HouseAnimationPanel());
		h2.setBuildingPanel(hp2);
		House house2 = new House("House 2", h2, hp2);
		Directory.addPlace(house2);
		_buildingCardLayoutPanel.add( hp2, hp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(house2.name());
		_buildingInteriorAnimationPanels.add(hp2);
		
		//House 3
	    WorldViewBuilding h3 = _worldView.addBuilding("source/image/WorldView/Housing/House1.png", 320, 280);
		BuildingInteriorAnimationPanel hp3 = new BuildingInteriorAnimationPanel(this, "House 3", new city.home.gui.HouseAnimationPanel());
		h3.setBuildingPanel(hp3);
		House house3 = new House("House 3", h3, hp3);
		Directory.addPlace(house3);
		_buildingCardLayoutPanel.add( hp3, hp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(house3.name());
		_buildingInteriorAnimationPanels.add(hp3);
		
		//House 4
	    WorldViewBuilding h4 = _worldView.addBuilding("source/image/WorldView/Housing/House2.png", 380, 280);
		BuildingInteriorAnimationPanel hp4 = new BuildingInteriorAnimationPanel(this, "House 4", new city.home.gui.HouseAnimationPanel());
		h4.setBuildingPanel(hp4);
		House house4 = new House("House 4", h4, hp4);
		Directory.addPlace(house4);
		_buildingCardLayoutPanel.add( hp4, hp4.getName() );
		cPanel.currentBuildingPanel.addBuilding(house4.name());
		_buildingInteriorAnimationPanels.add(hp4);
		
		//House 5
	    WorldViewBuilding h5 = _worldView.addBuilding("source/image/WorldView/Housing/House1.png", 440, 280);
		BuildingInteriorAnimationPanel hp5 = new BuildingInteriorAnimationPanel(this, "House 5", new city.home.gui.HouseAnimationPanel());
		h5.setBuildingPanel(hp5);
		House house5 = new House("House 5", h5, hp5);
		Directory.addPlace(house5);
		_buildingCardLayoutPanel.add( hp5, hp5.getName() );
		cPanel.currentBuildingPanel.addBuilding(house5.name());
		_buildingInteriorAnimationPanels.add(hp5);
		
		//House 6
	    WorldViewBuilding h6 = _worldView.addBuilding("source/image/WorldView/Housing/House2.png", 500, 120);
		BuildingInteriorAnimationPanel hp6 = new BuildingInteriorAnimationPanel(this, "House 6", new city.home.gui.HouseAnimationPanel());
		h6.setBuildingPanel(hp6);
		House house6 = new House("House 6", h6, hp6);
		Directory.addPlace(house6);
		_buildingCardLayoutPanel.add( hp6, hp6.getName() );
		cPanel.currentBuildingPanel.addBuilding(house6.name());
		_buildingInteriorAnimationPanels.add(hp6);
		
		//House 7
	    WorldViewBuilding h7 = _worldView.addBuilding("source/image/WorldView/Housing/House1.png", 200, 80);
		BuildingInteriorAnimationPanel hp7 = new BuildingInteriorAnimationPanel(this, "House 7", new city.home.gui.HouseAnimationPanel());
		h7.setBuildingPanel(hp7);
		House house7 = new House("House 7", h7, hp7);
		Directory.addPlace(house7);
		_buildingCardLayoutPanel.add( hp7, hp7.getName() );
		cPanel.currentBuildingPanel.addBuilding(house7.name());
		_buildingInteriorAnimationPanels.add(hp7);
		
		//House 8
	    WorldViewBuilding h8 = _worldView.addBuilding("source/image/WorldView/Housing/House2.png", 440, 80);
		BuildingInteriorAnimationPanel hp8 = new BuildingInteriorAnimationPanel(this, "House 8", new city.home.gui.HouseAnimationPanel());
		h8.setBuildingPanel(hp8);
		House house8 = new House("House 8", h8, hp8);
		Directory.addPlace(house8);
		_buildingCardLayoutPanel.add( hp8, hp8.getName() );
		cPanel.currentBuildingPanel.addBuilding(house8.name());
		_buildingInteriorAnimationPanels.add(hp8);
		
		//House 9
	    WorldViewBuilding h9 = _worldView.addBuilding("source/image/WorldView/Housing/House1.png", 80, 220);
		BuildingInteriorAnimationPanel hp9 = new BuildingInteriorAnimationPanel(this, "House 9", new city.home.gui.HouseAnimationPanel());
		h9.setBuildingPanel(hp9);
		House house9 = new House("House 9", h9, hp9);
		Directory.addPlace(house9);
		_buildingCardLayoutPanel.add( hp9, hp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(house9.name());
		_buildingInteriorAnimationPanels.add(hp9);
		
		//House 10
	    WorldViewBuilding h10 = _worldView.addBuilding("source/image/WorldView/Housing/House2.png", 560, 220);
		BuildingInteriorAnimationPanel hp10 = new BuildingInteriorAnimationPanel(this, "House 10", new city.home.gui.HouseAnimationPanel());
		h10.setBuildingPanel(hp10);
		House house10 = new House("House 10", h10, hp10);
		Directory.addPlace(house10);
		_buildingCardLayoutPanel.add( hp10, hp10.getName() );
		cPanel.currentBuildingPanel.addBuilding(house10.name());
		_buildingInteriorAnimationPanels.add(hp10);
		
		//Apartment 1
		WorldViewBuilding a1 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment1.png", 140, 120);
		BuildingInteriorAnimationPanel ap1 = new BuildingInteriorAnimationPanel(this, "Apartment 1", new city.home.gui.ApartmentAnimationPanel());
		a1.setBuildingPanel(ap1);
		ApartmentBuilding apartment1 = new ApartmentBuilding("Apartment 1", a1, ap1);
		Directory.addPlace(apartment1);
		_buildingCardLayoutPanel.add( ap1, ap1.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment1.name());
		_buildingInteriorAnimationPanels.add(ap1);
		
		//Apartment 2
		WorldViewBuilding a2 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment2.png", 140, 220);
		BuildingInteriorAnimationPanel ap2 = new BuildingInteriorAnimationPanel(this, "Apartment 2", new city.home.gui.ApartmentAnimationPanel());
		a2.setBuildingPanel(ap2);
		ApartmentBuilding apartment2 = new ApartmentBuilding("Apartment 2", a2, ap2);
		Directory.addPlace(apartment2);
		_buildingCardLayoutPanel.add( ap2, ap2.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment2.name());
		_buildingInteriorAnimationPanels.add(ap2);
		
		//Apartment 3
		WorldViewBuilding a3 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment1.png", 200, 180);
		BuildingInteriorAnimationPanel ap3 = new BuildingInteriorAnimationPanel(this, "Apartment 3", new city.home.gui.ApartmentAnimationPanel());
		a3.setBuildingPanel(ap3);
		ApartmentBuilding apartment3 = new ApartmentBuilding("Apartment 3", a3, ap3);
		Directory.addPlace(apartment3);
		_buildingCardLayoutPanel.add( ap3, ap3.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment3.name());
		_buildingInteriorAnimationPanels.add(ap3);
		
		//Apartment 4
		WorldViewBuilding a4 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment2.png", 260, 220);
		BuildingInteriorAnimationPanel ap4 = new BuildingInteriorAnimationPanel(this, "Apartment 4", new city.home.gui.ApartmentAnimationPanel());
		a4.setBuildingPanel(ap4);
		ApartmentBuilding apartment4 = new ApartmentBuilding("Apartment 4", a4, ap4);
		Directory.addPlace(apartment4);
		_buildingCardLayoutPanel.add( ap4, ap4.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment4.name());
		_buildingInteriorAnimationPanels.add(ap4);
		
		//Apartment 5
		WorldViewBuilding a5 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment1.png", 260, 120);
		BuildingInteriorAnimationPanel ap5 = new BuildingInteriorAnimationPanel(this, "Apartment 5", new city.home.gui.ApartmentAnimationPanel());
		a5.setBuildingPanel(ap5);
		ApartmentBuilding apartment5 = new ApartmentBuilding("Apartment 5", a5, ap5);
		Directory.addPlace(apartment5);
		_buildingCardLayoutPanel.add( ap5, ap5.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment5.name());
		_buildingInteriorAnimationPanels.add(ap5);
		
		//Apartment 6
		WorldViewBuilding a6 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment2.png", 320, 160);
		BuildingInteriorAnimationPanel ap6 = new BuildingInteriorAnimationPanel(this, "Apartment 6", new city.home.gui.ApartmentAnimationPanel());
		a6.setBuildingPanel(ap6);
		ApartmentBuilding apartment6 = new ApartmentBuilding("Apartment 6", a6, ap6);
		Directory.addPlace(apartment6);
		_buildingCardLayoutPanel.add( ap6, ap6.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment6.name());
		_buildingInteriorAnimationPanels.add(ap6);
		
		//Apartment 7
		WorldViewBuilding a7 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment1.png", 380, 220);
		BuildingInteriorAnimationPanel ap7 = new BuildingInteriorAnimationPanel(this, "Apartment 7", new city.home.gui.ApartmentAnimationPanel());
		a7.setBuildingPanel(ap7);
		ApartmentBuilding apartment7 = new ApartmentBuilding("Apartment 7", a7, ap7);
		Directory.addPlace(apartment7);
		_buildingCardLayoutPanel.add( ap7, ap7.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment7.name());
		_buildingInteriorAnimationPanels.add(ap7);
		
		//Apartment 8
		WorldViewBuilding a8 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment2.png", 380, 120);
		BuildingInteriorAnimationPanel ap8 = new BuildingInteriorAnimationPanel(this, "Apartment 8", new city.home.gui.ApartmentAnimationPanel());
		a8.setBuildingPanel(ap8);
		ApartmentBuilding apartment8 = new ApartmentBuilding("Apartment 8", a8, ap8);
		Directory.addPlace(apartment8);
		_buildingCardLayoutPanel.add( ap8, ap8.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment8.name());
		_buildingInteriorAnimationPanels.add(ap8);
		
		//Apartment 9
		WorldViewBuilding a9 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment1.png", 440, 180);
		BuildingInteriorAnimationPanel ap9 = new BuildingInteriorAnimationPanel(this, "Apartment 9", new city.home.gui.ApartmentAnimationPanel());
		a9.setBuildingPanel(ap9);
		ApartmentBuilding apartment9 = new ApartmentBuilding("Apartment 9", a9, ap9);
		Directory.addPlace(apartment9);
		_buildingCardLayoutPanel.add( ap9, ap9.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment9.name());
		_buildingInteriorAnimationPanels.add(ap9);
		
		//Apartment 10
		WorldViewBuilding a10 = _worldView.addBuilding("source/image/WorldView/Housing/Apartment2.png", 500, 220);
		BuildingInteriorAnimationPanel ap10 = new BuildingInteriorAnimationPanel(this, "Apartment 10", new city.home.gui.ApartmentAnimationPanel());
		a10.setBuildingPanel(ap10);
		ApartmentBuilding apartment10 = new ApartmentBuilding("Apartment 10", a10, ap10);
		Directory.addPlace(apartment10);
		_buildingCardLayoutPanel.add( ap10, ap10.getName() );
		cPanel.currentBuildingPanel.addBuilding(apartment10.name());
		_buildingInteriorAnimationPanels.add(ap10);
		
		
		
		_worldView.addAllBuildingsToWorld();		
		
		/*//Bus Stops
		WorldViewBuilding b5 = _worldView.addBuilding(0, 0, 20);
		BusStopObject busStop0 = new BusStopObject("Bus Stop " + 0, b5);
		Directory.addPlace(busStop0);//
		WorldViewBuilding b6 = _worldView.addBuilding(58, 0, 20);
		BusStopObject busStop1 = new BusStopObject("Bus Stop " + 1, b6);
		Directory.addPlace(busStop1);
		WorldViewBuilding b7 = _worldView.addBuilding(58, 28, 20);
		BusStopObject busStop2 = new BusStopObject("Bus Stop " + 2, b7);
		Directory.addPlace(busStop2);
		WorldViewBuilding b8 = _worldView.addBuilding(0, 28, 20);
		BusStopObject busStop3 = new BusStopObject("Bus Stop " + 3, b8);
		Directory.addPlace(busStop3);
		
		BusAgent bus = new BusAgent("Bus");
		BusAgentGui busGui = new BusAgentGui(bus, null);
		bus.setBusAgentGui(busGui);
		busGui.setPresent(true);
		_worldView.addGui(busGui);
		bus.startThread();
		
		// Hard-coded instantiation of all the buildings in the city:
		//Market
		WorldViewBuilding b3 = _worldView.addBuilding(24, 5, 40);
		BuildingInteriorAnimationPanel bp3 = new BuildingInteriorAnimationPanel(this, "Market 1", new city.market.gui.MarketAnimationPanel());
		b3.setBuildingPanel(bp3);
		Market market = new Market("Market 1", b3, bp3, _worldView);
		Directory.addPlace(market);
		_buildingCardLayoutPanel.add( bp3, bp3.getName() );
		cPanel.currentBuildingPanel.addBuilding(market.name());
		_buildingInteriorAnimationPanels.add(bp3);
		
		WorldViewBuilding marketBuilding2 = _worldView.addBuilding(28, 5, 40);
		BuildingInteriorAnimationPanel marketBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Market 2", new city.market.gui.MarketAnimationPanel());
		marketBuilding2.setBuildingPanel(marketBuildingPanel2);
		Market market2 = new Market("Market 2", marketBuilding2, marketBuildingPanel2, _worldView);
		Directory.addPlace(market2);
		_buildingCardLayoutPanel.add( marketBuildingPanel2, marketBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(market2.name());
		_buildingInteriorAnimationPanels.add(marketBuildingPanel2);
		
		// Yixin's Restaurant:
		WorldViewBuilding b = _worldView.addBuilding(32, 5, 40);
		BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this, "Yixin's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		b.setBuildingPanel(bp);
		YixinRestaurant yr = new YixinRestaurant("Yixin's Restaurant", b, bp);
		Directory.addPlace(yr);
		_buildingCardLayoutPanel.add( bp, bp.getName() );
		cPanel.currentBuildingPanel.addBuilding(yr.name());
		_buildingInteriorAnimationPanels.add(bp);
		
		//Omar's Restaurant
		WorldViewBuilding b9 = _worldView.addBuilding(24, 19, 40);
		BuildingInteriorAnimationPanel bp9 = new BuildingInteriorAnimationPanel(this, "Omar's Restaurant", new city.restaurant.omar.gui.OmarRestaurantAnimationPanel());
		b9.setBuildingPanel(bp9);
		OmarRestaurant or = new OmarRestaurant("Omar's Restaurant", b9, bp9);
		Directory.addPlace(or);
		_buildingCardLayoutPanel.add( bp9, bp9.getName() );
		cPanel.currentBuildingPanel.addBuilding(or.name());
		_buildingInteriorAnimationPanels.add(bp9);
		
		//Ryan Restaurant
		WorldViewBuilding bR = _worldView.addBuilding(28, 19, 40);
		BuildingInteriorAnimationPanel bpR = new BuildingInteriorAnimationPanel(this, "Ryan's Restaurant", new city.restaurant.ryan.gui.RyanAnimationPanel());
		bR.setBuildingPanel(bpR);
		RyanRestaurant rr = new RyanRestaurant("Ryan's Restaurant", bR, bpR);
		Directory.addPlace(rr);
		_buildingCardLayoutPanel.add( bpR, bpR.getName() );
		cPanel.currentBuildingPanel.addBuilding(rr.name());
		_buildingInteriorAnimationPanels.add(bpR);

		//TODO change to Eric's restaurant
		WorldViewBuilding restaurantBuilding4 = _worldView.addBuilding(32, 19, 40);
		BuildingInteriorAnimationPanel restaurantBuildingPanel4 = new BuildingInteriorAnimationPanel(this, "Eric's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding4.setBuildingPanel(restaurantBuildingPanel4);
		YixinRestaurant er = new YixinRestaurant("Eric's Restaurant", restaurantBuilding4, restaurantBuildingPanel4);
		Directory.addPlace(er);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel4, restaurantBuildingPanel4.getName() );
		cPanel.currentBuildingPanel.addBuilding(er.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel4);
		
		//TODO change to Tanner's restaurant
		WorldViewBuilding restaurantBuilding5 = _worldView.addBuilding(44, 5, 40);
		BuildingInteriorAnimationPanel restaurantBuildingPanel5 = new BuildingInteriorAnimationPanel(this, "Tanner's Restaurant", new city.restaurant.yixin.gui.YixinAnimationPanel());
		restaurantBuilding5.setBuildingPanel(restaurantBuildingPanel5);
		YixinRestaurant tr = new YixinRestaurant("Tanner's Restaurant", restaurantBuilding5, restaurantBuildingPanel5);
		Directory.addPlace(tr);
		_buildingCardLayoutPanel.add( restaurantBuildingPanel5, restaurantBuildingPanel5.getName() );
		cPanel.currentBuildingPanel.addBuilding(tr.name());
		_buildingInteriorAnimationPanels.add(restaurantBuildingPanel5);
		
		//Bank
		WorldViewBuilding b2 = _worldView.addBuilding(48, 5, 40);
		BuildingInteriorAnimationPanel bp2 = new BuildingInteriorAnimationPanel(this, "Bank", new city.bank.gui.BankAnimationPanel());
		b2.setBuildingPanel(bp2);
		Bank bank = new Bank("Bank", b2, bp2);
		Directory.addPlace(bank);
		_buildingCardLayoutPanel.add( bp2, bp2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank.name());
		_buildingInteriorAnimationPanels.add(bp2);
		
		WorldViewBuilding bankBuilding2 = _worldView.addBuilding(44, 19, 40);
		BuildingInteriorAnimationPanel bankBuildingPanel2 = new BuildingInteriorAnimationPanel(this, "Bank 2", new city.bank.gui.BankAnimationPanel());
		bankBuilding2.setBuildingPanel(bankBuildingPanel2);
		Bank bank2 = new Bank("Bank 2", bankBuilding2, bankBuildingPanel2);
		Directory.addPlace(bank2);
		_buildingCardLayoutPanel.add( bankBuildingPanel2, bankBuildingPanel2.getName() );
		cPanel.currentBuildingPanel.addBuilding(bank2.name());
		_buildingInteriorAnimationPanels.add(bankBuildingPanel2);
		
		//Initializing houses
		for(int i = 1; i < 3; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(8, 13 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + i, new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing apartments
		for(int i = 1; i < 3; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(10, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(10, 15 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + i, new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing more houses
		for(int i = 1; i < 3; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + (i + 5), new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i + 5, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
		    WorldViewBuilding b4 = _worldView.addBuilding(12, 15 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "House " + (i + 5), new city.home.gui.HouseAnimationPanel());
			b4.setBuildingPanel(bp4);
			House house = new House("House " + i + 5, b4, bp4);
			Directory.addPlace(house);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(house.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		//Initializing more apartments
		for(int i = 1; i < 3; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(12, 3 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}
		
		for(int i = 3; i < 6; i++){
			WorldViewBuilding b4 = _worldView.addBuilding(12, 15 + 2*i, 20);
			BuildingInteriorAnimationPanel bp4 = new BuildingInteriorAnimationPanel(this, "Apartment " + (i + 5), new city.home.gui.ApartmentAnimationPanel());
			b4.setBuildingPanel(bp4);
			ApartmentBuilding apartment = new ApartmentBuilding("Apartment", b4, bp4);
			Directory.addPlace(apartment);
			_buildingCardLayoutPanel.add( bp4, bp4.getName() );
			cPanel.currentBuildingPanel.addBuilding(apartment.name());
			_buildingInteriorAnimationPanels.add(bp4);
		}*/
		
		/*
		//Create the BuildingPanel for each Building object
		ArrayList<WorldViewBuilding> worldViewBuildings = _worldView.getBuildings();
		for ( int i=0; i<worldViewBuildings.size(); i++ )
		{
			WorldViewBuilding b = worldViewBuildings.get(i);
			BuildingInteriorAnimationPanel bp = new BuildingInteriorAnimationPanel(this,i);
			b.setBuildingPanel( bp );
			_buildingCardLayoutPanel.add( bp, "Building " + i );
		}
		
		
		for (int i = 3; i < 6; i++) {
			WorldViewBuilding b = new WorldViewBuilding( i, 1, 40 );
			
			worldViewBuildings.add( b );
		}
		for ( int i=0; i<2; i++ ) {
			for ( int j=0; j<5; j++ ) {
				if(i == 1 && j == 2) continue;
				WorldViewBuilding b = new WorldViewBuilding( i, j, 30 );
				
				worldViewBuildings.add( b );
			}
		}
		*/
		/*int xdim = 60;
	    int ydim = 30;
	    grid = new Semaphore[xdim][ydim];
	    for (int i=0; i<xdim; i++)
	    	for (int j=0; j<ydim; j++)
	    		grid[i][j] = new Semaphore(1,true);
	    //set access to all buildings to false
	    for (int i=8; i<16; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=8; i<16; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=24; i<36; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=24; i<36; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=44; i<52; i++)
	    	for (int j=5; j<9; j++)
	    		grid[i][j].tryAcquire();
	    for (int i=44; i<52; i++)
	    	for (int j=19; j<25; j++)
	    		grid[i][j].tryAcquire();*/
		//The code below will add a tabbed panel to hold all the control panels.  Should take the right third of the window
		
		this.add(cPanel, Component.RIGHT_ALIGNMENT);
		this.pack();		
		this.setVisible(true);
		
		Time.startTimer();
	}
	
	public WorldView getWorldView() { return _worldView; }
	
	 public void displayBuildingPanel(BuildingInteriorAnimationPanel bp ) {
		//System.out.println("MainGui: showing building " + bp.getName() + "'s AnimationPanel." );
		((CardLayout) _buildingCardLayoutPanel.getLayout()).show(_buildingCardLayoutPanel, bp.getName());
		cPanel.updateBuildingInfo(bp);
	 }
	
	/**
	 * Main routine to create an instance of the MainGui window
	 */
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		MainGui gui = new MainGui();
	}
}
