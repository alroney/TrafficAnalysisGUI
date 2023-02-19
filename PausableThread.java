/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The PausableThread class is a class that creates a thread that can be paused. It is used to create the thread that updates the totalRunTimeInSecs variable every second.
 */



public class PausableThread implements Runnable {

	private Thread thread = null;
	private Updatable updatable;
	private boolean stopped = false;

	//Constructor: Creates a new PausableThread object
	public PausableThread(Updatable o) {
		this.updatable = o;
		this.thread = new Thread(this);
	}

	//Method: Starts the thread
	public void start() {
		thread.start();
	}

	//Method: Sets the name of the thread
	public void setName(String name) {
		this.thread.setName(name);//Set the name of the thread
	}

	//Method: Updates the thread
	@Override
	public void run() {
		while (!stopped) {
			if (ExecuteSimulation.isRunning()) {
				this.updatable.update();//Update the thread
			} else {
				ExecuteSimulation.waitForRestart();//Wait for the simulation to be restarted
			}
		}
	}

	//Method: Stops the thread
	public void stop() {
		this.stopped = true;
	}

}
