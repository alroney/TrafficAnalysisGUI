/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The SimulationPanel class is a JPanel that contains the simulation. It is used to create the simulation panel that is displayed in the GUI.
 * 				
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Updatable {
	//region - VARIABLES - variables used in this class
		private static SimulationPanel INSTANCE = new SimulationPanel();

		public static final int WIDTH = 1280;
		public static final int HEIGHT = 480;
		private static final int TARGET_FPS = 1000;
		private static final long TARGET_FRAME_TIME_IN_NS = 1_000_000_000 / TARGET_FPS;

		private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		private long totalRunTimeInSecs = 0L;
		private long lastLoopTimeNs = System.nanoTime();
		private Entities entities = Entities.getInstance();
	//endregion

	public static SimulationPanel getInstance() {
		return INSTANCE;
	}


	//Constructor: create a new simulation panel
	private SimulationPanel() {
	    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		PausableThread timer = new PausableThread(() -> {//create a new thread that updates the totalRunTimeInSecs variable every second
			try {
				Thread.sleep(1_000);
			} 
			catch (InterruptedException e) {
				// ignore
			}

			this.lock.writeLock().lock();
			try {
				this.totalRunTimeInSecs++;//increment the totalRunTimeInSecs variable
			} 
			finally {//unlock the lock
				this.lock.writeLock().unlock();
			}
		});
		timer.setName("Timer");
		timer.start();
	}

	//Method: gets the distance between the left boundry and the right boundry
	public synchronized int getXInPixelsFromXInMeters(double xInMeters) {
		double distanceIntoTheScreen = xInMeters - this.getLeftBoundryInMeters();//get the distance into the screen
		double percentThroughTotalSpace = distanceIntoTheScreen / getWidthInMeters();//get the percent of the total pixels
		double thatPercentOfTotalPixels = percentThroughTotalSpace * this.getWidth();//get the x location of the traffic light

		return (int) Math.round(thatPercentOfTotalPixels);
	}

	//Method: gets the width of the simulation panel
	public synchronized double getWidthInMeters() {
		return this.getRightBoundryInMeters() - this.getLeftBoundryInMeters();
	}

	//Method: gets the left boundry
	public synchronized double getLeftBoundryInMeters() {
		return this.entities.minXLocation();
	}

	//Method: gets the right boundry
	public synchronized double getRightBoundryInMeters() {
	    return Math.max(this.entities.maxXLocation(), 0);
	}

	//Method: creates a new thread and starts it
	@Override
	public void addNotify() {
		super.addNotify();
		PausableThread thread = new PausableThread(this);
		thread.setName("SimulationPanel");
		thread.start();
	}

	//Method: paints the simulation panel and draws the entities
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Drawable o : this.entities) {//draws the entities
			o.draw(g);
		}

		long seconds;
		this.lock.readLock().lock();//locks the lock
		try {
			seconds = this.totalRunTimeInSecs;//gets the total run time in seconds
		} 
		finally {
			this.lock.readLock().unlock();//unlocks the lock
		}

		long hours = seconds / (60 * 60);
		seconds -= hours * (60 * 60);
		long minutes = seconds / 60;
		seconds -= minutes * 60;

		FontMetrics fm = g.getFontMetrics();//gets the font metrics
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);//formats as hours:minutes:seconds
		int timeWidth = fm.stringWidth(time);//gets the width of the time

		String min = String.format("%.1f" + " meters", this.getLeftBoundryInMeters());//formats the left boundry
		String max = String.format("%.1f" + " meters", this.getRightBoundryInMeters());//formats the right boundry
		int maxWidth = fm.stringWidth(max);

		final int MARGIN = 10;
		int fontHeight = fm.getHeight();
		int bottomY = this.getHeight() - fontHeight - MARGIN;//gets the bottom y value of the simulation panel

		g.setColor(Color.BLACK);//sets the color to black for the text
		g.drawString(min, MARGIN, bottomY);//draws the text on the simulation panel at the bottom left
		g.drawString(max, this.getWidth() - maxWidth - MARGIN, bottomY);//draws the text on the simulation panel at the bottom right
		g.drawString(time, this.getWidth() - timeWidth - MARGIN, MARGIN + fontHeight);//draws the text on the simulation panel at the top right
	}

	//Method: updates the simulation panel
	@Override
	public void update() {
		// Find time since last render
		this.lastLoopTimeNs = System.nanoTime();//gets the last loop time in nanoseconds

		this.repaint();//repaints the simulation panel

		// Figure out how long to sleep this thread for

		long nsElapsed = System.nanoTime() - lastLoopTimeNs;
		long waitTimeInNs = TARGET_FRAME_TIME_IN_NS - nsElapsed;
		if (0 < waitTimeInNs) {//if the wait time is greater than 0
			long waitTimeInMs = waitTimeInNs / 1_000_000;//convert the wait time to milliseconds
			try {
				Thread.sleep(waitTimeInMs);//sleep the thread
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

}
