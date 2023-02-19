/*
 * Author: Andrew Roney
 * Date: 02/18/2023
 * Project: Project 3
 * Description: The Entities class is a class that creates a collection of entities. It is used to create the collection of entities that are displayed in the simulation.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Entities implements Iterable<Drawable> {

	private static final Entities INSTANCE = new Entities();

	public static Entities getInstance() {
		return INSTANCE;
	}

	private final Collection<Drawable> entities;
	private final Collection<TrafficLight> trafficLights;

	//Contructor: Creates a new collection of entities
	private Entities() {
		entities = new ArrayList<Drawable>();
		trafficLights = new ArrayList<TrafficLight>();
	}

	//Method: Adds an entity to the collection of entities
	public boolean add(Drawable o) {
		if (o instanceof TrafficLight) {
			this.trafficLights.add((TrafficLight) o);
		}
		return this.entities.add(o);
	}

	//Method: Adds a collection of entities to the collection of entities
	@Override
	public Iterator<Drawable> iterator() {
		return this.entities.iterator();
	}

	//Method: Returns the collection of entities
	public Iterable<TrafficLight> trafficLights() {
		return this.trafficLights;//return the collection of traffic lights
	}

	//Method: Returns the maximum y location of the entities
	public double maxXLocation() {
		return 3000;//Always end at 3000
		
	}

	public double minXLocation() {
		return 0;//Always start at 0
		
	}

}
