/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The Drawable interface is an interface that creates a drawable object. It is used to create the entities that are displayed in the simulation.
 */

/* UML Class Diagram
 * ----------------------------------------------------------------------------------------------------
 * | Drawable                                                                                          |
 * | --------------------------------------------------------------------------------------------------|
 * | +draw(g: Graphics): void                                                                           |
 * | +getXLocation(): double                                                                           |
 * ------------------------------------------------------------------------------------------------------
 */

import java.awt.Graphics;

public interface Drawable {
	public void draw(Graphics g);

	public double getXLocation();
}
