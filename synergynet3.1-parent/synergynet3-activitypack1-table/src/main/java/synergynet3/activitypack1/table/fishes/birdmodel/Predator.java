/**
 * File Name: Predator.cs Date: 06/01/2006 Copyright (c) 2006 Michael LaLena.
 * All rights reserved. (www.lalena.com) Permission to use, copy, modify, and
 * distribute this Program and its documentation, if any, for any purpose and
 * without fee is hereby granted, provided that: (i) you not charge any fee for
 * the Program, and the Program not be incorporated by you in any software or
 * code for which compensation is expected or received; (ii) the copyright
 * notice listed above appears in all copies; (iii) both the copyright notice
 * and this Agreement appear in all supporting documentation; and (iv) the name
 * of Michael LaLena or lalena.com not be used in advertising or publicity
 * pertaining to distribution of the Program without specific, written prior
 * permission.
 */

package synergynet3.activitypack1.table.fishes.birdmodel;

import java.awt.Color;

/**
 * The predator is a type of bird that tries to eat the other birds. Once the
 * predator eats enough birds to satisfy its hunger, it is removed from the map.
 *
 * @author Michael LaLena
 * @version 1.0
 */
public class Predator extends Bird {

	// How many birds this predator must eat before being removed from the map.
	/** The current hunger. */
	int currentHunger;

	/**
	 * Constructor for the predator. Places the predator at a random location
	 */
	public Predator() {
		super(Color.red);
	}

	/**
	 * This is the constructor for the predator.
	 *
	 * @param x The X coordinate of the Predator
	 * @param y The Y coordinate of the Predator
	 */
	Predator(int x, int y) {
		super(x, y, 0, Color.red);
	}

	/**
	 * Reduces the hunger value by one.
	 */
	public void eatBird() {
		currentHunger--;
	}

	/**
	 * Set the hunger value
	 *
	 * @return The hunger value for this predator.
	 */
	public int getHunger() {
		return currentHunger;
	}

	/**
	 * Set the hunger value
	 *
	 * @param hunger How many birds should this predator eat before being
	 *            removed from the map.
	 */
	public void setHunger(int hunger) {
		currentHunger = hunger;
	}
}
