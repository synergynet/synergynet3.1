/*
 * File Name: Bird.cs Date: 06/01/2006 Copyright (c) 2006 Michael LaLena. All
 * rights reserved. (www.lalena.com) Permission to use, copy, modify, and
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

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * This class represents one of the birds, either green or blue. Birds try to
 * flock with birds of the same color and avoid different color birds,
 * predators, and obstacles.
 *
 * @author Michael LaLena
 * @version 1.0
 */
public class Bird {
	// The range at which birds can detect the birds, predators, and obstacles
	/** The Detection range. */
	public static int DetectionRange;

	// The range at which birds will separate from each other to avoid
	// something.
	// With low values flocks will be tighter, and birds will not separate to
	// avoid
	// obstacles and other birds. With higher values, ther flock will be spread
	// apart and a flock will be more likely split in two when faced with an
	// obstacle.
	/** The Separation range. */
	public static int SeparationRange;

	// Display the separation and detection ranges on the map.
	/** The show ranges. */
	static boolean showRanges = false;

	// The speed of the bird. This is set by the simulator, and can be changed
	// on the fly.
	/** The current speed. */
	private double currentSpeed; // speed of Bird

	// The direction that the bird is facing in degrees.
	/** The current theta. */
	private float currentTheta;

	// maximum turningr radius in degrees. This is set by the simulator, and can
	// be changed on the fly.
	/** The max turn theta. */
	private float maxTurnTheta;

	// The size of the map containing the birds.

	// The color of this bird.
	/** The color. */
	protected Color color;

	// The current location of this bird.
	/** The location. */
	protected Vector2f location = new Vector2f(0, 0);

	/** The rep. */
	IItem rep;

	/**
	 * This constructor sets a random location and direction for the bird.
	 *
	 * @param birdColor The color of the bird.
	 */
	public Bird(Color birdColor) {
		// call the other constructor.
		this((float) (Math.random() * Map.map.width),
				(float) (Math.random() * Map.map.height),
				(int) (Math.random() * 360), birdColor);
	}

	/**
	 * This is the constructor for the birds.
	 *
	 * @param x Starting X coordinate of the Bird
	 * @param y Starting Y coordinate of the Bird
	 * @param theta Starting angle of the Bird - the direction the bird is
	 *            facing in degrees
	 * @param birdColor The color of the bird
	 */
	Bird(float x, float y, int theta, Color birdColor) {
		location.x = x;
		location.y = y;
		currentTheta = theta;
		color = birdColor;
	}

	/**
	 * Get the color of this bird
	 *
	 * @return The color of this bird
	 */
	public Color getColor() {
		return color;
	}

	// /**
	// * Draw the bird
	// *
	// * @param g The graphics object to draw the bird on.
	// */
	// public void draw(Graphics g) {
	// g.setColor(this.color);
	// g.fillArc(location.x - 12, location.y - 12, 24, 24, currentTheta + 180 -
	// 20, 40);
	//
	// if (showRanges) {
	// drawRanges(g);
	// }
	// }

	/**
	 * Draws the range circles around a bird. In case the bird is close to one
	 * of the edges of the map, it will draw up to 4 circles for each bird so
	 * that the circles are visible on the other edge of the map.
	 *
	 * @param g The graphics object to draw the bird on.
	 */
	// public void drawRanges(Graphics g) {
	// drawCircles(g, location.x, location.y);
	//
	// int radius = Obstacle.DetectionRange;
	//
	// boolean top = (location.y < DetectionRange);
	// boolean bottom = (location.y > map.height - DetectionRange);
	// // if the obstacle radius overlaps an edge, then display a second
	// obstacle
	// // with a center point outside the range of the displayed area.
	// // This way, the birds can react to it before wrapping, and it will also
	// get drawn.
	// if (location.x < DetectionRange) { // if left
	// drawCircles(g, map.width + location.x, location.y);
	// if (top) {
	// drawCircles(g, map.width + location.x, map.height + location.y);
	// }
	// else if (bottom) {
	// drawCircles(g, map.width + location.x, location.y - map.height);
	// }
	// } else if (location.x > map.width - DetectionRange) { // if right
	// drawCircles(g, location.x - map.width, location.y);
	// if (top) {
	// drawCircles(g, location.x - map.width, map.height + location.y);
	// }
	// else if (bottom) {
	// drawCircles(g, location.x - map.width, location.y - map.height);
	// }
	// }
	// if (top) {
	// drawCircles(g, location.x, map.height + location.y);
	// }
	// else if (bottom) {
	// drawCircles(g, location.x, location.y - map.height);
	// }
	// }

	/**
	 * Draws the range circles around the bird. The inner circle is the point at
	 * which birds will separate to aviod the obstacle. The outer circle is the
	 * range at which the birds can detect the obstacle.
	 *
	 * @param g The graphics object to draw the bird on.
	 * @param x The X coordinate of the bird
	 * @param y The Y coordinate of the bird
	 */
	// protected void drawCircles( Graphics g, int x, int y ) {
	// g.setColor(new Color((int)color.getRed()/2, (int)color.getGreen()/2,
	// (int)color.getBlue()/2 ));
	// g.drawOval(x-DetectionRange, y-DetectionRange, 2*DetectionRange,
	// 2*DetectionRange);
	// g.setColor(color);
	// g.drawOval(x-SeparationRange, y-SeparationRange, 2*SeparationRange,
	// 2*SeparationRange);
	// }
	//
	/**
	 * Get the distance in pixels between this bird and another
	 *
	 * @param otherBird The other bird to measure the distance between
	 * @return The distance to the other bird
	 */
	public float getDistance(Bird otherBird) {
		float dX = otherBird.getLocation().x - location.x;
		float dY = otherBird.getLocation().y - location.y;

		return (float) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}

	/**
	 * Get the distance in pixels between this bird and a point
	 *
	 * @param p The point to measure the distance between
	 * @return The distance between this bird and the point
	 */
	public float getDistance(Vector2f p) {
		float dX = p.x - location.x;
		float dY = p.y - location.y;

		return (float) Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}

	/**
	 * Get the current location of this bird
	 *
	 * @return The location of this bird
	 */
	public Vector2f getLocation() {
		return location;
	}

	/**
	 * Get the current direction that the bird is facing
	 *
	 * @return The Maximum Theta for this bird
	 */
	public float getMaxTurnTheta() {
		return maxTurnTheta;
	}

	/**
	 * Gets the representation.
	 *
	 * @return the representation
	 */
	public IItem getRepresentation() {
		return rep;
	}

	/**
	 * Get the current direction of this bird
	 *
	 * @return The direction that this bird is facing
	 */
	public float getTheta() {
		return currentTheta;
	}

	/**
	 * Causes the bird to attempt to face a new direction. Based on
	 * maxTurnTheta, the bird may not be able to complete the turn.
	 *
	 * @param newHeading The direction in degrees that the bird should turn
	 *            toward.
	 */
	public void move(float newHeading) {
		// determine if it is better to turn left or right for the new heading
		float left = ((newHeading - currentTheta) + 360) % 360;
		float right = ((currentTheta - newHeading) + 360) % 360;

		// after deciding which way to turn, find out if we can turn that much
		float thetaChange = 0;
		if (left < right) {
			// if left > than the max turn, then we can't fully adopt the new
			// heading
			thetaChange = Math.min(maxTurnTheta, left);
		} else {
			// right turns are negative degrees
			thetaChange = -Math.min(maxTurnTheta, right);
		}

		// Make the turn
		currentTheta = (currentTheta + thetaChange + 360) % 360;

		// Now move currentSpeed pixels in the direction the bird now faces.
		// Note: Because values are truncated, a speed of 1 will result in no
		// movement unless the bird is moving exactly vertically or
		// horizontally.
		location.x += (float) (currentSpeed * Math
				.cos((currentTheta * Math.PI) / 180.0)) + Map.map.width;
		location.x %= Map.map.width;
		location.y -= (float) (currentSpeed * Math
				.sin((currentTheta * Math.PI) / 180.0)) - Map.map.height;
		location.y %= Map.map.height;
	}

	/**
	 * Sets the location.
	 *
	 * @param pos the new location
	 */
	public void setLocation(Vector2f pos) {
		location.x = pos.x;
		location.y = pos.y;
	}

	/**
	 * Set the maximum turn capability of the bird for each movement.
	 *
	 * @param theta The new maximum turning theta in degrees
	 */
	public void setMaxTurnTheta(float theta) {
		maxTurnTheta = theta;
	}

	/**
	 * Sets the representation.
	 *
	 * @param rep the new representation
	 */
	public void setRepresentation(IItem rep) {
		this.rep = rep;
	}

	/**
	 * Set the current speed of the bird
	 *
	 * @param speed The new speed for the bird
	 */
	public void setSpeed(double speed) {
		currentSpeed = speed;
	}

	/**
	 * Update representation position.
	 */
	public void updateRepresentationPosition() {
		this.rep.setRelativeLocation(new Vector2f(location.x, location.y));
	}

	/**
	 * Update representation rotation.
	 */
	public void updateRepresentationRotation() {
		this.rep.setRelativeRotation(FastMath.DEG_TO_RAD
				* ((90 - +this.currentTheta) + 180));

	}

}
