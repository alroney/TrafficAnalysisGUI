/*
 * Name: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The CarRow class is a class that creates a row of input fields for the user to enter the information for a car. It is used to create the rows of input fields that are displayed in the GUI.
 * 				It has a constructor that creates the row of input fields and adds it to the Entities class. It also creates a thread that updates the timeRunningInSec variable every second.
 * 				
 */

import java.util.Optional;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CarRow extends JPanel {
	
	//Class: InputField is a private class that extends JPanel and implements DocumentListener interface
	private class InputField extends JPanel implements DocumentListener {
		private static final long serialVersionUID = 1L;

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

		@Override
		public boolean requestFocusInWindow() {
			return this.textField.requestFocusInWindow();
		}

		public synchronized String getText() {
			return this.textField.getText();
		}

		public synchronized void setText(String str) {
			this.textField.setText(str);
		}

		public synchronized boolean isEditable() {
			return this.textField.isEditable();
		}

		//Method: 
		public synchronized void setEditable(boolean b) {
			this.textField.setEditable(b);
		}

		//Method: handles the change in the input field
		private void handleChange() {
			boolean valid = true;
			if (CarRow.this.getXInput().isPresent()) {
				CarRow.this.xInput.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));//sets the border of the x input to black if the input is valid
			} 
			else {
				CarRow.this.xInput.textField.setBorder(BorderFactory.createLineBorder(Color.RED));//sets the border of the x input to red if the input is not valid
				valid = false;
			}

			if (CarRow.this.getSpeedInput().isPresent()) {
				CarRow.this.speedInput.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));//sets the border of the speed input to black if the input is valid
			} 
			else {
				CarRow.this.speedInput.textField.setBorder(BorderFactory.createLineBorder(Color.RED));//sets the border of the speed input to red if the input is not valid
				valid = false;
			}

			CarRow.this.saveBtn.setEnabled(valid);
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			this.handleChange();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			this.handleChange();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			this.handleChange();
		}
	}

	private InputField xInput = new InputField("X(m): ");
	private InputField speedInput = new InputField("Speed(m/s): ");
	private JButton saveBtn = new JButton("save");


	
	//Constructor: creates a new CarRow object
	public CarRow() {
		this.setLayout(new GridLayout(1, 4));//sets the layout to a grid layout with 4 columns
		this.add(this.xInput);
		this.add(this.speedInput);
		this.add(this.saveBtn);

		this.setXInput(0);//sets the default x position to 0
		this.setSpeedInput(30);//sets the default speed to 30 m/s

		this.saveBtn.addActionListener(e -> this.save());//adds an action listener to the saveBtn
	}

	//Method: saves the x and speed inputs
	public void save() {
		this.remove(this.saveBtn);//removes the save saveBtn
		this.revalidate();//revalidates the panel to update the layout
	}

	//region - GETTERS - gets input field and window info

		//Method: gets the focal state of the xInput
		@Override
		public boolean requestFocusInWindow() {
			return this.xInput.requestFocusInWindow();//returns true if the xInput is focused
		}

		//Method: gets the xInput and returns it as a double
		public Optional<Double> getXInput() {
			try {
				return Optional.of(Double.parseDouble(this.xInput.getText()));//returns the xInput as a double if it can be parsed. The Optional class is used to avoid null pointer exceptions
			} 
			catch (Exception ex) {
				return Optional.empty();//returns an empty Optional if the xInput cannot be parsed
			}
		}

		//Method: gets the speedInput and returns it as a double
		public Optional<Double> getSpeedInput() {
			try {
				return Optional.of(Double.parseDouble(this.speedInput.getText()));//returns the speedInput as a double if it can be parsed. The Optional class is used to avoid null pointer exceptions
			} 
			catch (Exception ex) {
				return Optional.empty();//returns an empty Optional if the speedInput cannot be parsed
			}
		}

		//Method: gets the editable state of the xInput
		public boolean isEditable() {
			return this.xInput.isEditable();
		}
	//endregion


	//region - SETTERS - sets input field info and saveBtn state

		//Method: sets the editable state of the xInput and speedInput
		public void setEditable(boolean val) {
			this.xInput.setEditable(val);
			this.speedInput.setEditable(val);
		}

		//Method: sets the x input to the value of the input
		public void setXInput(double xCoord) {
			this.xInput.setText(String.format("%.1f", xCoord));
		}

		//Method: sets the border color of the x input
		public void setXInputBorder(Color color) {
			this.xInput.setBorder(BorderFactory.createLineBorder(color));
		}


		//Method: sets the speed input to the value of the input
		public void setSpeedInput(double speed) {
			this.speedInput.setText(Double.toString(speed));
		}

		//Method: sets the border color of the speed input
		public void setSpeedInputBorder(Color color) {
			this.speedInput.setBorder(BorderFactory.createLineBorder(color));
		}

		//Method: sets the saveBtn to be enabled or disabled
		public void setButtonEnabled(boolean val) {
			this.saveBtn.setEnabled(val);
		}
	//endregion

	//Method: adds an action listener to the saveBtn
	public void addActionListener(ActionListener l) {
		this.saveBtn.addActionListener(l);
	}

}
