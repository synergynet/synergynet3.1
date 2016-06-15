/*
 * File Name: Obstacle.cs Date: 06/01/2006 Copyright (c) 2006 Michael LaLena.
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
 * This obstacle class represents an item on the map that all birds try to
 * avoid. Obstacles do not move.
 *
 * @author Michael LaLena
 * @version 1.0
 */
public class Obstacle extends Bird
{
	/**
	 * This is the constructor for the obstacle.
	 *
	 * @param x
	 *            The X coordinate of the Obstacle
	 * @param y
	 *            The Y coordinate of the Obstacle
	 */
	public Obstacle(float x, float y)
	{
		super(x, y, 0, Color.gray);
	}

	/**
	 * The obstacle class overrides the move function to do nothing.
	 *
	 * @param angle
	 *            not used
	 */
	public void move(int angle)
	{
		// obstacles do not move
	}

}
