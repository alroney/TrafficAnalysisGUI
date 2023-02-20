/*
 * Name: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The ControlPanel class is a JPanel that contains the buttons to start and pause the simulation. It also contains the CarControlPanel and TrafficLightControlPanel.
 * 				It has a constructor that creates the buttons and adds them to the panel. It also adds the CarControlPanel and TrafficLightControlPanel to the panel.
 */

/* UML class diagram
 * ----------------------------------------------------------------------------------------------------
 * | ControlPanel																						|
 * | ------------------------------------------------------------------------------------------------- |
 * | none
 * | ------------------------------------------------------------------------------------------------- |
 * | + ControlPanel()																					|
 * ----------------------------------------------------------------------------------------------------

 */

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	// Constructor: create a new control panel
	public ControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		//region - MAIN CONTROL PANEL - create panels for the buttons, cars and traffic lights panels
			JPanel mainCtrlPanel = new JPanel();//create a new panel for the buttons
			mainCtrlPanel.setLayout(new GridLayout(1, 2, 10, 10));

			CarControlPanel carCtrlPanel = new CarControlPanel();//create a new car control panel
			mainCtrlPanel.add(carCtrlPanel);//add the car control panel to the main control panel

			TrafficLightControlPanel trafficCtrlPanel = new TrafficLightControlPanel();//create a new trafficCtrlPanel light control panel
			mainCtrlPanel.add(trafficCtrlPanel);//add the trafficCtrlPanel light control panel to the main control panel
		//endregion
		this.add(mainCtrlPanel);//add the main control panel to the control panel

		//region - ACTION BUTTON - create a multi-purpose button to start, pause and resume the simulation
			JButton actionButton = new JButton();

			actionButton.setText("Start");//set the starting text of the button to "Start"
			actionButton.setPreferredSize(new Dimension(100, 25));
			actionButton.addActionListener(e -> {
				if (ExecuteSimulation.isRunning()) {//if the simulation is running when the button is clicked
					ExecuteSimulation.isRunning(false);
					actionButton.setText("Resume");//set the text of the button to "Resume"
					carCtrlPanel.isPaused(true);
					trafficCtrlPanel.isPaused(true);
				}
				else {//if the simulation is not running when the button is clicked
					ExecuteSimulation.isRunning(true);
					actionButton.setText("Pause");//set the text of the button to "Pause"
					carCtrlPanel.isPaused(false);
					trafficCtrlPanel.isPaused(false);
				}
			});
		//endregion
		this.add(actionButton);
	}

}
