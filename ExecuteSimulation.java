/*
 * Name: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The ExecuteSimulation class is the main class of the program. It creates the frame and the panels that will be used in the program. It also contains the code that will be used to control the running variable.
 * 				The running variable is used to control the simulation. It is set to true when the simulation is running and set to false when the simulation is paused. The running variable is used to control the execution of the threads.
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

class ExecuteSimulation {
	//region - VARIABLE DECLARATIONS - Declaration of most variables used in the class
		private static final ReentrantReadWriteLock runningLock = new ReentrantReadWriteLock();//this is the lock that will be used to control the running variable and the condition variable that will be used to wait for the running variable to be set to true

		private static final Lock r = runningLock.readLock();//this is the lock that will be used to read the running variable
		private static final Lock w = runningLock.writeLock();//this is the lock that will be used to write to the running variable
		private static final Condition restarted = w.newCondition();//this is the condition variable that will be used to wait for the running variable to be set to true

		private static boolean running = false;
	//endregion

	//region - GETTERS AND SETTERS - Declaration of the getters and setters for the running variable
		//Method: checks if the running variable is true
		public static boolean isRunning() {
			r.lock();//lock the read lock
			try {//try to return the running variable
				return running;
			} 
			finally {//finally unlock the read lock. The finally block will always be executed even if an exception is thrown
				r.unlock();//unlock the read lock
			}
		}

		//Method: sets the running variable to the value of the val parameter
		public static void isRunning(boolean val) {
			w.lock();
			try {
				running = val;//set the running variable to the value of the val parameter
				restarted.signalAll();//signal all threads that are waiting on the condition variable that the running variable has been set to true
			} 
			finally {
				w.unlock();//unlock the write lock
			}
		}

		//Method: waits for the running variable to be set to true
		public static void waitForRestart() {
			w.lock();
			try {
				System.out.println(String.format("%s: Calling waitForRestart", Thread.currentThread().getName()));//print out the name of the current thread

				while (!running) {
					System.out.println(String.format("%s: Still not runing going to wait", Thread.currentThread().getName()));//print out the name of the current thread
					restarted.await();//wait for the running variable to be set to true
					System.out.println(String.format("%s: Finished waiting", Thread.currentThread().getName()));//print out the name of the current thread

				}
			} 
			catch (InterruptedException e) {
				e.printStackTrace();//print out the stack trace of the exception
			} 
			finally {//finally unlock the write lock. The finally block will always be executed even if an exception is thrown
				w.unlock();//unlock the write lock
			}
		}
	//endregion

	//Main: the main method of the program
	public static void main(String[] args) {
		JFrame frame = new JFrame("Traffic Simulation");

		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		pane.add(new ControlPanel());
		pane.add(SimulationPanel.getInstance());
		frame.setContentPane(pane);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
