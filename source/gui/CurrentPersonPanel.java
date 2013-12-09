package gui;

import gui.trace.AlertTag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import city.Directory;
import city.PersonAgent;

public class CurrentPersonPanel extends JPanel implements ActionListener
{
	JPanel view;
	JPanel buttonPanel;
	
	JPanel infoPanel;
	JCheckBox action;
	JCheckBox action2;
	JCheckBox action3;
	JCheckBox action4;
	JButton addSelectedActions;
	PersonAgent _currentlySelectedPerson;
	
	JLabel nameField;
	JLabel moneyField;
	JLabel currentRoleField;
	JScrollPane peopleButtons;
	ControlPanel cPanel;
	private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	private final Dimension buttonDimension = new Dimension(340, 30);
	
	public CurrentPersonPanel(ControlPanel cp)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		cPanel = cp;
		this.setLayout(new BorderLayout());
		infoPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		nameField = new JLabel("Person Name:");
		moneyField = new JLabel("Person Money:");
		currentRoleField = new JLabel("Person Current Role:");
		infoPanel.add(nameField); 
		infoPanel.add(moneyField); 
		infoPanel.add(currentRoleField); 
		
		 action = new JCheckBox("Go To OmarRestaurant");
		 action.setMinimumSize(buttonDimension);
		 action.setMaximumSize(buttonDimension);
         
		 action2 = new JCheckBox("Go To YixinRestaurant");
		 action2.setMinimumSize(buttonDimension);
		 action2.setMaximumSize(buttonDimension);
         
		 action3 = new JCheckBox("Go To EricRestaurant");
		 action3.setMinimumSize(buttonDimension);
		 action3.setMaximumSize(buttonDimension);
         
		 action4 = new JCheckBox("Go To RyanRestaurant");
		 action4.setMinimumSize(buttonDimension);
		 action4.setMaximumSize(buttonDimension);
         
		 addSelectedActions = new JButton("Add Actions");
		 addSelectedActions.setMinimumSize(buttonDimension);
		 addSelectedActions.setMaximumSize(buttonDimension);
		 
		 addSelectedActions.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
            	 if(_currentlySelectedPerson != null){
	                 if(action.isSelected()){
	                	 _currentlySelectedPerson.addActionToDo("Go to Omar Restaurant");
	                 }
	                 if(action2.isSelected()){
	                	 _currentlySelectedPerson.addActionToDo("Go to Yixin Restaurant");
	                 }
	                 if(action3.isSelected()){
	                	 _currentlySelectedPerson.addActionToDo("Go to Eric Restaurant");
	                 }
	                 if(action4.isSelected()){
	                	 _currentlySelectedPerson.addActionToDo("Go to Ryan Restaurant");
	                 }
            	 }
             }
         });
		 
		 infoPanel.add(action);
		 infoPanel.add(action2);
		 infoPanel.add(action3);
		 infoPanel.add(action4);
		 infoPanel.add(addSelectedActions);
         
		this.add(infoPanel, BorderLayout.NORTH);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT/2));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("People"));
		view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		peopleButtons = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		peopleButtons.setViewportView(view);
		buttonPanel.add(peopleButtons, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.CENTER);
		this.validate();
	}

	public void addPerson(String name)
	{
		JButton newPersonButton = new JButton(name);
		newPersonButton.setBackground(Color.white);
		Dimension paneSize = peopleButtons.getSize();
		newPersonButton.setPreferredSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.setMinimumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.setMaximumSize(new Dimension(paneSize.width, paneSize.height/10));
		newPersonButton.addActionListener(this);
		view.add(newPersonButton);
		this.updateInfo(newPersonButton);
	}
	
	public void updateInfo(JButton selected)
	{
		for(PersonAgent tempPerson : Directory.personAgents())
		{
			if(tempPerson.name() == selected.getText())
			{
				_currentlySelectedPerson = tempPerson;
				nameField.setText("Person Name: " + tempPerson.name());
				moneyField.setText("Person Money: " + tempPerson.money() + "0");
			//	currentRoleField.setText("Current Role: Need to implement toString() for the different roles.");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// note: this method is called when you click a button in the list of people.
		
		updateInfo((JButton) e.getSource());
	}

}