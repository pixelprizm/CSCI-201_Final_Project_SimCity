package gui;
import java.awt.Dimension;
import java.awt.geom.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class WorldViewBuilding extends JButton {
	BuildingInteriorAnimationPanel myBuildingPanel;
	int x;
	int y;

	// PROPERTIES
	public int positionX() { return (int)x; }
	public int positionY() { return (int)y; }

	public WorldViewBuilding(String filePath, int x, int y)
	{
		super(new ImageIcon(filePath));
		this.x = x;
		this.y = y;
	}

	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}

	public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
		myBuildingPanel = bp;
	}
}
