package gui;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class WorldViewBuilding extends JButton {
	BuildingInteriorAnimationPanel myBuildingPanel;
	int x;
	int y;
	Image image;
	public ImageIcon icon;

	// PROPERTIES
	public int positionX() { return (int)x; }
	public int positionY() { return (int)y; }
	public int width() { return icon.getIconWidth(); }
	public int height() { return icon.getIconHeight(); }

	public WorldViewBuilding(String filePath, int x, int y)
	{
		try {
			image = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			System.out.println("Could not find image at: " + filePath);
			//e.printStackTrace();
		}
		icon = new ImageIcon(image);
		this.setIcon(icon);
		this.x = x;
		this.y = y;
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}

	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}

	public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
		myBuildingPanel = bp;
	}
}
