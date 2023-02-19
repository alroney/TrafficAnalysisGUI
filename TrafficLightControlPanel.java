/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The TrafficLightControlPanel class is a class that creates a control panel for the traffic lights. It is used to create the control panel that is displayed in the simulation.
 * 				It has a constructor that creates the control panel and adds it to the main control panel. It also adds the traffic lights to the control panel.
 */

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TrafficLightControlPanel extends JPanel {
	JButton tlButton = new JButton("Add Traffic Light");

	//Constructor: create a new traffic light control panel
	public TrafficLightControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel trafficLightRows = new JPanel();
		trafficLightRows.setLayout(new BoxLayout(trafficLightRows, BoxLayout.PAGE_AXIS));
		this.add(trafficLightRows);

		for (int i = 0; i < 3; i++) {//create 3 initial traffic lights
			TrafficLightRow tlRow = new TrafficLightRow();
			tlRow.setXInput((i + 1) * 1000);
			tlRow.setGreen((double) ThreadLocalRandom.current().nextInt(8, 12));//Sets the values of the traffic light row
			tlRow.setYellow((double) ThreadLocalRandom.current().nextInt(2, 4));//Sets the values of the traffic light row
			tlRow.setRed((double) ThreadLocalRandom.current().nextInt(8, 12));//Sets the values of the traffic light row
			new TrafficLight(tlRow.getGreen().get(), tlRow.getYellow().get(), tlRow.getRed().get(), tlRow.getXInput().get());//Creates the traffic light
			tlRow.setEditable(false);//Makes the row uneditable
			tlRow.save();//Saves the row
			trafficLightRows.add(tlRow);//Adds the row to the panel
		}

		tlButton.addActionListener(e -> {//traffic light button listener
			tlButton.setEnabled(false);
			TrafficLightRow tlRow = new TrafficLightRow();//Creates a new traffic light row

			tlRow.addActionListener(e2 -> {//Adds a new traffic light when the row is saved and the add traffic light button is enabled
				new TrafficLight(tlRow.getGreen().get(), tlRow.getYellow().get(), tlRow.getRed().get(), tlRow.getXInput().get());//Creates the traffic light
				tlRow.setEditable(false);
				tlButton.setEnabled(true);
				SimulationPanel.getInstance().update();//Updates the simulation panel
			});//end action listener 2
			trafficLightRows.add(tlRow);//Adds the row to the panel
			this.revalidate();//Updates the panel
		});//end action listener 1
		this.add(tlButton);//Adds the button to the panel

	}//end constructor

	//Method: Sets the add traffic light button to enabled or disabled depending on the input boolean value
	public void isPaused(boolean b) {
		this.tlButton.setEnabled(b);
	}

}
