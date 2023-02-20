/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The TrafficLight class is a class that creates a traffic light. It is used to create the traffic lights that are displayed in the simulation.
 * 				It has a constructor that creates the traffic light and adds it to the Entities class. It also creates a thread that updates the timeRunningInSec variable every second.
 */

/* UML Class Diagram
 * ----------------------------------------------------------------------------------------------------
 * | TrafficLight                                                                                      |
 * | --------------------------------------------------------------------------------------------------|
 * | -HEIGHT: int                                                                                      |
 * | -timeGreen: double                                                                                |
 * | -timeYellow: double                                                                               |
 * | -timeRed: double                                                                                  |
 * | -x: double                                                                                        |
 * | -lock: ReentrantReadWriteLock                                                                     |
 * | -timeRunningInSec: long                                                                           |
 * | --------------------------------------------------------------------------------------------------|
 * | +TrafficLight(greenInSec: double, yellowInSec: double, redInSec: double, x: double)                |
 * | +draw(g: Graphics): void                                                                           |
 * | +cycleTime(): double                                                                              |
 * | +timeIntoCurrentCycle(): double                                                                   |
 * | +getColor(): Color                                                                                |
 * | +getGreenTime(): double                                                                           |
 * | +getYellowTime(): double                                                                          |
 * | +getRedTime(): double                                                                             |
 * | +getX(): double                                                                                   |
 * | +setGreenTime(greenInSec: double): void                                                           |
 * | +setYellowTime(yellowInSec: double): void                                                         |
 * | +setRedTime(redInSec: double): void                                                               |
 * | +setX(x: double): void                                                                            |
 * | +reset(): void                                                                                    |
 * | --------------------------------------------------------------------------------------------------|
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TrafficLight implements Drawable {
	//region - VARIABLES - create the variables used in this class
		public static final int HEIGHT = 60;

		private final double timeGreen;
		private final double timeYellow;
		private final double timeRed;
		private final double x;

		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		private long timeRunningInSec = 0L;
	//endregion

	//Constructor: create a new traffic light
	public TrafficLight(double greenInSec, double yellowInSec, double redInSec, double x) {
		this.timeGreen = greenInSec;
		this.timeYellow = yellowInSec;
		this.timeRed = redInSec;
		this.x = x;

		PausableThread timer = new PausableThread(() -> {//create a new thread that updates the timeRunningInSec variable every second
			try {
				Thread.sleep(1_000);
			} 
			catch (InterruptedException e) {
				// ignore
			}

			this.lock.writeLock().lock();//lock the lock
			try {
				this.timeRunningInSec++;//increment the timeRunningInSec variable
			} 
			finally {
				this.lock.writeLock().unlock();//unlock the lock
			}
		});
		timer.start();

		Entities.getInstance().add(this);//add the traffic light to the Entities class so it can be drawn and updated in the simulation
	}

	//Method: draw the traffic light
	@Override
	public void draw(Graphics g) {
		final int length = 30;

		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.x);//get the x location of the traffic light

		g.setColor(this.getColor());//set the color of the traffic light
		g.fillRect(location - (length / 2), 0, length, HEIGHT);//draw the traffic light
		
		g.setColor(Color.BLACK);//set the color of the outline of the traffic light
		g.drawRect(location - (length / 2), 0, length, HEIGHT);//draw the outline of the traffic light
	}

	//Method: get the color of the light based on the time into the current cycle
	private double cycleTime() {
		return this.timeGreen + this.timeYellow + this.timeRed;//return the total time of the traffic light cycle
	}

	//Method: get the time into the current cycle
	private double timeIntoCurrentCycle() {
	    this.lock.readLock().lock();
	    try {
			return this.timeRunningInSec % this.cycleTime();//return the time into the current cycle
	    } 
		finally {
			this.lock.readLock().unlock();
	    }
	}

	//Method: get the color of the traffic light
	private Color getColor() {
		double currentTime = this.timeIntoCurrentCycle();
		if (currentTime <= this.timeGreen) {
			return Color.GREEN;
		} 
		else if (currentTime <= (this.timeGreen + this.timeYellow)) {
			return Color.YELLOW;
		} 
		else {
			return Color.RED;
		}
	}

	//Method: check if the traffic light is green
	public boolean isRed() {
		return this.getColor() == Color.RED;
	}

	//Method: get the x location of the traffic light
	@Override
	public double getXLocation() {
		return this.x;
	}

}
