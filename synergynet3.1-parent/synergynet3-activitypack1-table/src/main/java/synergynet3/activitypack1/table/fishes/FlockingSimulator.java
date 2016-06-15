package synergynet3.activitypack1.table.fishes;

import java.awt.Color;

import multiplicity3.csys.animation.elements.AnimationElement;
import synergynet3.activitypack1.table.fishes.birdmodel.Bird;
import synergynet3.activitypack1.table.fishes.birdmodel.Flock;
import synergynet3.activitypack1.table.fishes.birdmodel.Food;
import synergynet3.activitypack1.table.fishes.birdmodel.Obstacle;
import synergynet3.activitypack1.table.fishes.birdmodel.Predator;

import com.jme3.math.Vector2f;

/**
 * The Class FlockingSimulator.
 */
public class FlockingSimulator extends AnimationElement
{

	/** The Constant MAX_BIRDS. */
	private static final int MAX_BIRDS = 150;
	/** The birds. */
	private BirdCollection birds;

	/** The flock. */
	private Flock flock;

	/** The blue bird max theta. */
	int blueBirdMaxTheta;

	/** The blue bird speed. */
	int blueBirdSpeed;

	/** The default blue speed. */
	int DEFAULT_BLUE_SPEED = 6;

	/** The default blue theta. */
	int DEFAULT_BLUE_THETA = 10;

	/** The default green speed. */
	int DEFAULT_GREEN_SPEED = 5;

	/** The default green theta. */
	int DEFAULT_GREEN_THETA = 15;

	/** The default number blue. */
	int DEFAULT_NUMBER_BLUE = 15;

	// These are the default values for the sliders, also used for a reset
	/** The default number green. */
	int DEFAULT_NUMBER_GREEN = 10;

	/** The default number red. */
	int DEFAULT_NUMBER_RED = 3;

	/** The default obstacle detect. */
	int DEFAULT_OBSTACLE_DETECT = 60;

	/** The default obstacle separate. */
	int DEFAULT_OBSTACLE_SEPARATE = 30;

	/** The default red hunger. */
	int DEFAULT_RED_HUNGER = 3;

	/** The default red speed. */
	int DEFAULT_RED_SPEED = 7;

	/** The default red theta. */
	int DEFAULT_RED_THETA = 20;

	/** The detect distance. */
	int detectDistance;

	/** The green bird max theta. */
	int greenBirdMaxTheta;

	/** The green bird speed. */
	int greenBirdSpeed;

	// These are the upper limits for the sliders
	/** The maximum birds. */
	final int MAXIMUM_BIRDS = 50;

	/** The maximum distance. */
	final int MAXIMUM_DISTANCE = 200;

	/** The maximum hunger. */
	final int MAXIMUM_HUNGER = 10;

	/** The maximum speed. */
	final int MAXIMUM_SPEED = 30;

	/** The number of blue birds. */
	int numberOfBlueBirds;

	// These are the values set by the sliders
	/** The number of green birds. */
	int numberOfGreenBirds;

	/** The number of red predators. */
	int numberOfRedPredators;

	/** The red predator hunger. */
	int redPredatorHunger;

	/** The red predator max theta. */
	int redPredatorMaxTheta;

	/** The red predator speed. */
	int redPredatorSpeed;

	/** The separate distance. */
	int separateDistance;

	/**
	 * Instantiates a new flocking simulator.
	 *
	 * @param birds
	 *            the birds
	 */
	public FlockingSimulator(BirdCollection birds)
	{
		this.birds = birds;
	}

	/**
	 * Adds the birds at.
	 *
	 * @param c
	 *            the c
	 * @param pos
	 *            the pos
	 */
	public void addBirdsAt(Color c, Vector2f pos)
	{
		if (birds.eatableBirdCount() > MAX_BIRDS)
		{
			return;
		}
		for (int i = 0; i < numberOfGreenBirds; i++)
		{
			Bird bird = new Bird(c);
			bird.setSpeed(greenBirdSpeed);
			bird.setMaxTurnTheta(greenBirdMaxTheta);
			bird.setLocation(pos);
			flock.addBird(bird);
		}
	}

	/**
	 * Adds the food at.
	 *
	 * @param pos
	 *            the pos
	 */
	public void addFoodAt(Vector2f pos)
	{
		Food f = new Food(pos.x, pos.y);
		flock.addBird(f);
	}

	/**
	 * Adds the obstacle at.
	 *
	 * @param pos
	 *            the pos
	 */
	public void addObstacleAt(Vector2f pos)
	{
		Obstacle o = new Obstacle(pos.x, pos.y);
		flock.addBird(o);
	}

	/**
	 * Adds the predators birds at.
	 *
	 * @param pos
	 *            the pos
	 */
	public void addPredatorsBirdsAt(Vector2f pos)
	{
		// if(birds.size() > MAX_BIRDS) return;
		for (int i = 0; i < numberOfRedPredators; i++)
		{
			Predator bird = new Predator();
			bird.setSpeed(redPredatorSpeed);
			bird.setMaxTurnTheta(redPredatorMaxTheta);
			bird.setHunger(redPredatorHunger);
			bird.setLocation(pos);
			flock.addBird(bird);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#elementStart(float
	 * )
	 */
	@Override
	public void elementStart(float tpf)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Inits the with defaults.
	 */
	public void initWithDefaults()
	{
		numberOfGreenBirds = DEFAULT_NUMBER_GREEN;
		numberOfBlueBirds = DEFAULT_NUMBER_BLUE;
		numberOfRedPredators = DEFAULT_NUMBER_RED;
		greenBirdMaxTheta = DEFAULT_GREEN_THETA;
		blueBirdMaxTheta = DEFAULT_BLUE_THETA;
		redPredatorMaxTheta = DEFAULT_RED_THETA;
		greenBirdSpeed = DEFAULT_GREEN_SPEED;
		blueBirdSpeed = DEFAULT_BLUE_SPEED;
		redPredatorSpeed = DEFAULT_RED_SPEED;
		redPredatorHunger = DEFAULT_RED_HUNGER;
		separateDistance = DEFAULT_OBSTACLE_SEPARATE;
		detectDistance = DEFAULT_OBSTACLE_DETECT;

		Bird.SeparationRange = separateDistance;
		Bird.DetectionRange = detectDistance;

		flock = new Flock(birds);

		Flock.SeparationRange = separateDistance;
		Flock.DetectionRange = detectDistance;

		// for (int i=0; i < numberOfGreenBirds; i++) {
		// Bird bird = new Bird(Color.green);
		// bird.setSpeed( greenBirdSpeed );
		// bird.setMaxTurnTheta( greenBirdMaxTheta );
		// flock.addBird(bird);
		// }
		// for (int i=0; i < numberOfBlueBirds; i++) {
		// Bird bird = new Bird(Color.blue);
		// bird.setSpeed( blueBirdSpeed );
		// bird.setMaxTurnTheta( blueBirdMaxTheta );
		// flock.addBird(bird);
		// }
		for (int i = 0; i < numberOfRedPredators; i++)
		{
			Predator bird = new Predator();
			bird.setSpeed(redPredatorSpeed);
			bird.setMaxTurnTheta(redPredatorMaxTheta);
			bird.setHunger(redPredatorHunger);
			flock.addBird(bird);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#updateAnimationState
	 * (float)
	 */
	@Override
	public void updateAnimationState(float tpf)
	{
		flock.move();
		birds.update();
	}

}
