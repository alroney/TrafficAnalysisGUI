/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The TrafficLightRow class is a class that creates a row of traffic lights. It is used to create the rows of traffic lights that are displayed in the simulation.
 * 				It has a constructor that creates the row of traffic lights and adds it to the Entities class. It also creates a thread that updates the timeRunningInSec variable every second.
 */

import java.util.Optional;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TrafficLightRow extends JPanel {

	//region - INPUT FIELD - Class: InputField is a private class that extends JPanel and implements DocumentListener interface
		private class InputField extends JPanel implements DocumentListener {

			private final JLabel label;
			private final JTextField textField;

			//Constructor: creates a new InputField object
			private InputField(String label) {
				this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

				this.label = new JLabel(label);
				this.textField = new JTextField();
				this.textField.getDocument().addDocumentListener(this);

				this.add(this.label);
				this.add(this.textField);
			}

			//Method: requests focus in the input field
			@Override
			public boolean requestFocusInWindow() {
				return this.textField.requestFocusInWindow();
			}

			//Method: gets the text from the input field
			public String getText() {
				return this.textField.getText();
			}

			//Method: sets the text in the input field
			public void setText(String str) {
				this.textField.setText(str);
			}

			//Method: gets the editable status of the input field
			public boolean isEditable() {
				return this.textField.isEditable();
			}

			//Method: sets the editable status of the input field	
			public void setEditable(boolean b) {
				this.textField.setEditable(b);
			}

			//Method: handles the change in the input field
			private void handleChange() {
				boolean valid = true;
				if (TrafficLightRow.this.getGreen().isPresent()) {
					TrafficLightRow.this.green.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));//sets the border of the input field to black if the input is valid
				} 
				else {
					TrafficLightRow.this.green.textField.setBorder(BorderFactory.createLineBorder(Color.RED));//sets the border of the input field to red if the input is invalid
					valid = false;
				}

				if (TrafficLightRow.this.getYellow().isPresent()) {
					TrafficLightRow.this.yellow.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));//sets the border of the input field to black if the input is valid
				} 
				else {
					TrafficLightRow.this.yellow.textField.setBorder(BorderFactory.createLineBorder(Color.RED));//sets the border of the input field to red if the input is invalid
					valid = false;
				}

				if (TrafficLightRow.this.getRed().isPresent()) {
					TrafficLightRow.this.red.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));//sets the border of the input field to black if the input is valid
				} 
				else {
					TrafficLightRow.this.red.textField.setBorder(BorderFactory.createLineBorder(Color.RED));//sets the border of the input field to red if the input is invalid
					valid = false;
				}

				TrafficLightRow.this.saveBtn.setEnabled(valid);//sets the button to enabled if all the inputs are valid
			}

			//Method: handles the change in the input field
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				this.handleChange();
			}

			//Method: handles the change in the input field
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				this.handleChange();
			}

			//Method: handles the change in the input field
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				this.handleChange();
			}
		}
	//endregion

	//region - VARIABLES - creates the varriable for the TrafficLightRow class
		private InputField green = new InputField("Sec Green:");
		private InputField yellow = new InputField("Sec Yellow:");
		private InputField red = new InputField("Sec Red:");
		private InputField xInput = new InputField("X:");
		private JButton saveBtn = new JButton("save");
	//endregion

	//Constructor: creates a new TrafficLightRow object
	public TrafficLightRow() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		this.add(this.green);
		this.add(this.yellow);
		this.add(this.red);
		this.add(this.xInput);
		this.add(this.saveBtn);

		this.setGreen(10);
		this.setYellow(2);
		this.setRed(12);
		this.setXInput(0);

		this.saveBtn.addActionListener(e -> this.save());
	}

	//Method: saves the input values
	public void save() {
		this.remove(this.saveBtn);
		this.revalidate();
	}

	//Method: gets the focus of the input field
	@Override
	public boolean requestFocusInWindow() {
		return this.green.requestFocusInWindow();
	}

	//Method: gets the x value from the input field
	public Optional<Double> getGreen() {
		try {
			return Optional.of(Double.parseDouble(this.green.getText()));
		} 
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	//Method: sets the x value in the input field
	public void setGreen(double val) {
		this.green.setText(Double.toString(val));
	}

	//Method: gets the y value from the input field
	public Optional<Double> getYellow() {
		try {
			return Optional.of(Double.parseDouble(this.yellow.getText()));
		} 
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	//Method: sets the y value in the input field
	public void setYellow(double val) {
		this.yellow.setText(Double.toString(val));
	}

	//Method: gets the value from the input field
	public Optional<Double> getRed() {
		try {
			return Optional.of(Double.parseDouble(this.red.getText()));
		} 
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	//Method: sets the value in the input field
	public void setRed(double val) {
		this.red.setText(Double.toString(val));
	}

	//Method: gets the value from the input field
	public Optional<Double> getXInput() {
		try {
			return Optional.of(Double.parseDouble(this.xInput.getText()));
		} 
		catch (Exception ex) {
			return Optional.empty();
		}
	}

	//Method: sets the value in the input field
	public void setXInput(double val) {
		this.xInput.setText(Double.toString(val));
	}

	//Method: gets the editable status of the input field
	public boolean isEditable() {
		return this.green.isEditable();
	}

	//Method: sets the editable status of the input field
	public void setEditable(boolean val) {
		this.green.setEditable(val);
		this.yellow.setEditable(val);
		this.red.setEditable(val);
		this.xInput.setEditable(val);
	}

	//Method: adds an action listener to the button
	public void addActionListener(ActionListener l) {
		this.saveBtn.addActionListener(l);
	}

}
