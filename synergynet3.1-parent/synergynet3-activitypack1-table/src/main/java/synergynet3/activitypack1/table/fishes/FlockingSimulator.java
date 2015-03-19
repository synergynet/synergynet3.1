package synergynet3.activitypack1.table.fishes;

import java.awt.Color;

import multiplicity3.csys.animation.elements.AnimationElement;
import synergynet3.activitypack1.table.fishes.birdmodel.Bird;
import synergynet3.activitypack1.table.fishes.birdmodel.Flock;
import synergynet3.activitypack1.table.fishes.birdmodel.Food;
import synergynet3.activitypack1.table.fishes.birdmodel.Obstacle;
import synergynet3.activitypack1.table.fishes.birdmodel.Predator;

import com.jme3.math.Vector2f;

public class FlockingSimulator extends AnimationElement {
	private static final int MAX_BIRDS = 150;
	// These are the upper limits for the sliders
    final int MAXIMUM_BIRDS = 50;
    final int MAXIMUM_SPEED = 30;
    final int MAXIMUM_HUNGER = 10;
    final int MAXIMUM_DISTANCE = 200;
    
    // These are the default values for the sliders, also used for a reset
    int DEFAULT_NUMBER_GREEN = 10;
    int DEFAULT_NUMBER_BLUE = 15;
    int DEFAULT_NUMBER_RED = 3;
    int DEFAULT_GREEN_THETA = 15;
    int DEFAULT_BLUE_THETA = 10;
    int DEFAULT_RED_THETA = 20;
    int DEFAULT_GREEN_SPEED = 5;
    int DEFAULT_BLUE_SPEED = 6;
    int DEFAULT_RED_SPEED = 7;
    int DEFAULT_RED_HUNGER = 3;
    int DEFAULT_OBSTACLE_SEPARATE = 30;
    int DEFAULT_OBSTACLE_DETECT = 60;
    
    // These are the values set by the sliders
    int numberOfGreenBirds;
    int numberOfBlueBirds;
    int numberOfRedPredators;
    int greenBirdSpeed;
    int blueBirdSpeed;
    int redPredatorSpeed;
    int greenBirdMaxTheta;
    int blueBirdMaxTheta;
    int redPredatorMaxTheta;
    int redPredatorHunger;
    int separateDistance;
    int detectDistance;
	private Flock flock;
	private BirdCollection birds;
	
	public FlockingSimulator(BirdCollection birds) {
		this.birds = birds;
	}
    
	public void initWithDefaults() {
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
        
//        for (int i=0; i < numberOfGreenBirds; i++) {
//            Bird bird = new Bird(Color.green);
//            bird.setSpeed( greenBirdSpeed );
//            bird.setMaxTurnTheta( greenBirdMaxTheta );
//            flock.addBird(bird);
//        }
//        for (int i=0; i < numberOfBlueBirds; i++) {
//            Bird bird = new Bird(Color.blue);
//            bird.setSpeed( blueBirdSpeed );
//            bird.setMaxTurnTheta( blueBirdMaxTheta );
//            flock.addBird(bird);
//        }
        for (int i=0; i < numberOfRedPredators; i++) {
            Predator bird = new Predator();
            bird.setSpeed( redPredatorSpeed );
            bird.setMaxTurnTheta( redPredatorMaxTheta );
            bird.setHunger( redPredatorHunger );
            flock.addBird(bird);
        }
	}
	
	public void addBirdsAt(Color c, Vector2f pos) {
		if(birds.eatableBirdCount() > MAX_BIRDS) return;
		for (int i=0; i < numberOfGreenBirds; i++) {
            Bird bird = new Bird(c);
            bird.setSpeed( greenBirdSpeed );
            bird.setMaxTurnTheta( greenBirdMaxTheta );
            bird.setLocation(pos);
            flock.addBird(bird);
        }
	}

	
	public void addPredatorsBirdsAt(Vector2f pos) {
		//if(birds.size() > MAX_BIRDS) return;
        for (int i=0; i < numberOfRedPredators; i++) {
            Predator bird = new Predator();
            bird.setSpeed( redPredatorSpeed );
            bird.setMaxTurnTheta( redPredatorMaxTheta );
            bird.setHunger( redPredatorHunger );
            bird.setLocation(pos);
            flock.addBird(bird);
        }
	}
	
	public void addFoodAt(Vector2f pos) {
		Food f = new Food(pos.x, pos.y);
		flock.addBird(f);
	}
	
	public void addObstacleAt(Vector2f pos) {
		Obstacle o = new Obstacle(pos.x, pos.y);
		flock.addBird(o);
	}

	
	@Override
	public void updateAnimationState(float tpf) {
		flock.move();
		birds.update();
	}

	

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementStart(float tpf) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}









}
