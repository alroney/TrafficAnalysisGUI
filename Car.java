/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: 
 * This class represents a car object. It implements the Drawable and Updatable interfaces.
 * 				It has a thread that updates the car's location every tick. It also checks for traffic lights
 * 				and stops if it is red.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Car implements Drawable, Updatable {
	//region - FIELDS - Initialization
		private static int TICKS_PER_SECOND = 120;
		private static final long TICK_TIME_IN_NS = 1_000_000_000 / TICKS_PER_SECOND;
		public static final int HEIGHT = 40;
		public static final int WIDTH = 50;

		private final ReentrantReadWriteLock xLock = new ReentrantReadWriteLock();
		private double x;
		private final double y;
		private final double speedPerTick;
		private long lastLoopTimeNs = System.nanoTime();
		private final CarRow row;
		private final PausableThread thread;
	//endregion

	//Constructor: create a new car object and add it to the entities list
	public Car(double x, double y, double speedPerSec, CarRow row) {
		this.x = x;
		this.y = y;
		this.speedPerTick = speedPerSec / TICKS_PER_SECOND;
		this.row = row;
		Entities.getInstance().add(this);
		this.thread = new PausableThread(this);
		this.thread.start();
	}

	//Method: update the car's location
	@Override
	public void update() {
		this.lastLoopTimeNs = System.nanoTime();//get the current time

		double currLocation = this.getXLocation();//get the car's current location
		double nextDestination = currLocation + this.speedPerTick;//calculate the car's nextDestination

		for (TrafficLight tl : Entities.getInstance().trafficLights()) {//check for traffic lights
			if (tl.isRed()) {//if the traffic light is red
				double tlLocation = tl.getXLocation() - 100;//get the location of the traffic light and subtract 100 to get the location of the stop line
				System.out.println("found red traffic light at " + tlLocation);//print the location of the traffic light
				if (tlLocation >= currLocation && tlLocation <= nextDestination) {//if the traffic light is between the car's current location and its nextDestination
					nextDestination = Math.min(tlLocation, nextDestination);//set the nextDestination to the traffic light's location so the car stops at the traffic light
				}
			}
		}

		this.setXLocation(nextDestination);
		this.row.setXInput(nextDestination);

		try {//wait for the next tick
			long nsElapsed = System.nanoTime() - lastLoopTimeNs;
			long waitTimeInNs = TICK_TIME_IN_NS - nsElapsed;
			if (0 < waitTimeInNs) {//if we're ahead of schedule, wait
				long waitTimeInMs = waitTimeInNs / 1_000_000;
				Thread.sleep(waitTimeInMs);
			}
		} 
		catch (InterruptedException e) {
			return;//if we're interrupted, stop
		}
	}

	//Method: draw the car
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);//set the color to purple
		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.getXLocation());//convert the x location to pixels
		g.fillRect(location - (HEIGHT / 2), (int) y, WIDTH, HEIGHT);//draw the car
		g.fillOval(location - (HEIGHT / 2) + 5, (int) y, WIDTH + 15, HEIGHT);//draw the car's hood
	}


	//Method: get the car's x location
	@Override
	public double getXLocation() {
		this.xLock.readLock().lock();
		try {
			return this.x;
		} 
		finally {
			this.xLock.readLock().unlock();
		}
	}


	//Method: get the car's y location
	public double getYLocation() {
		return this.y;
	}


	//Method: set the car's x location
	public void setXLocation(double val) {
		this.xLock.writeLock().lock();
		try {
			if( val > 3000) {//if the car is past the end of the road
				val = 0;//reset the car's location to the beginning of the road
			}
			this.x = val;
		} 
		finally {
			this.xLock.writeLock().unlock();
		}
	}

	

}
