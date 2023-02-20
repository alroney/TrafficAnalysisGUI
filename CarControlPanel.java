/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The CarControlPanel class is a JPanel that contains the buttons to add cars to the simulation.
 * 				It also contains the CarRow objects that represent the cars. It has a btnAddCar to add a new car.
 * 				When the btnAddCar is clicked, it creates a new CarRow object and adds it to the panel. 
 */

 /* UML class diagram
  * ----------------------------------------------------------------------------------------------------
  * | CarControlPanel																					|
  * | ------------------------------------------------------------------------------------------------- |
  * | - recommendedY: int																				|
  * | - btnAddCar: JButton																				|
  * | ------------------------------------------------------------------------------------------------- |
  * | + CarControlPanel()																				|
  * | + setAddCarButtonEnabled(boolean): void															|
  * | ------------------------------------------------------------------------------------------------- |
  */

import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CarControlPanel extends JPanel {

	private int recommendedY = TrafficLight.HEIGHT + 10;
	private final JButton btnAddCar = new JButton("Add Car");

	//Constructor: Creates the initial three cars
	public CarControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel carRows = new JPanel();
		carRows.setLayout(new BoxLayout(carRows, BoxLayout.PAGE_AXIS));
		this.add(carRows);

		for (int i = 0; i < 3; i++) {//Creates the initial three cars
			CarRow row = new CarRow();
			row.setSpeedInput((double) (ThreadLocalRandom.current().nextInt(30, 61)));//Sets the speed of the car to a random number between 30 and 60
			row.setEditable(false);//Makes the row uneditable
			new Car(0, recommendedY, row.getSpeedInput().get(), row);//Creates the car
			recommendedY += Car.HEIGHT + 10;//Sets the recommended y value for the next car
			row.save();//Saves the row

			carRows.add(row);//Adds the row to the panel
		}

		btnAddCar.addActionListener(e -> {//Adds a new car row
			btnAddCar.setEnabled(false);//Disables the add car button
			CarRow row = new CarRow();//Creates a new car row

			row.addActionListener(e2 -> {//Adds a new car when the row is saved and the add car button is enabled
				new Car(row.getXInput().get(), (double) recommendedY, row.getSpeedInput().get(), row);
				row.setEditable(false);//Makes the row uneditable
				btnAddCar.setEnabled(true);//Enables the add car button
				SimulationPanel.getInstance().update();//Updates the simulation panel

				recommendedY += Car.HEIGHT + 10;
				SimulationPanel.getInstance().update();
			});
			carRows.add(row);//Adds the row to the panel
			this.revalidate();//Updates the panel
		});
		this.add(btnAddCar);
	}

	//Method: Sets the add car button to enabled or disabled depending on the input boolean value
	public void isPaused(boolean b) {
		this.btnAddCar.setEnabled(b);
	}

}
