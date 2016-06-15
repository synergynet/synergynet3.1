package synergynet3.activitypack1.table.fishes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.activitypack1.table.fishes.birdmodel.Bird;
import synergynet3.activitypack1.table.fishes.birdmodel.Food;
import synergynet3.activitypack1.table.fishes.birdmodel.Obstacle;
import synergynet3.activitypack1.table.fishes.birdmodel.Predator;

import com.jme3.math.Vector2f;

/**
 * The Class BirdCollection.
 */
public class BirdCollection
{

	/** The birds. */
	private List<Bird> birds = new ArrayList<Bird>();

	/** The eatable bird count. */
	private int eatableBirdCount;

	/** The root. */
	private IContainer root;

	/**
	 * Instantiates a new bird collection.
	 *
	 * @param root
	 *            the root
	 */
	public BirdCollection(IContainer root)
	{
		this.root = root;
	}

	/**
	 * Adds the bird.
	 *
	 * @param b
	 *            the b
	 */
	public void addBird(Bird b)
	{
		birds.add(b);
		IItem rep = createRepresentationForBird(b);
		root.addItem(rep);
		b.setRepresentation(rep);
		if (birdIsEatable(b))
		{
			eatableBirdCount++;
		}
	}

	/**
	 * Bird at index.
	 *
	 * @param i
	 *            the i
	 * @return the bird
	 */
	public Bird birdAtIndex(int i)
	{
		return birds.get(i);
	}

	/**
	 * Eatable bird count.
	 *
	 * @return the int
	 */
	public int eatableBirdCount()
	{
		return eatableBirdCount;
	}

	/**
	 * Removes the bird.
	 *
	 * @param b
	 *            the b
	 */
	public void removeBird(Bird b)
	{
		birds.remove(b);
		root.removeItem(b.getRepresentation());
		if (birdIsEatable(b))
		{
			eatableBirdCount--;
		}
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size()
	{
		return birds.size();
	}

	/**
	 * Update.
	 */
	public void update()
	{
		for (Bird b : birds)
		{
			b.updateRepresentationPosition();
			b.updateRepresentationRotation();
		}
	}

	/**
	 * Bird is eatable.
	 *
	 * @param b
	 *            the b
	 * @return true, if successful
	 */
	private boolean birdIsEatable(Bird b)
	{
		return b.getColor().equals(Color.blue) || b.getColor().equals(Color.green) || b.getColor().equals(Color.yellow);
	}

	/**
	 * Creates the representation for bird.
	 *
	 * @param b
	 *            the b
	 * @return the i item
	 */
	private IItem createRepresentationForBird(Bird b)
	{
		IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		try
		{

			IImage img = stage.getContentFactory().create(IImage.class, "bird", UUID.randomUUID());
			img.setSize(new Vector2f(24, 24));
			if (b instanceof Predator)
			{
				img.setImage("synergynet3/activitypack1/table/fishes/red.png");
			}
			else if (b instanceof Food)
			{
				img.setImage("synergynet3/activitypack1/table/fishes/food.png");
			}
			else if (b instanceof Obstacle)
			{
				img.setImage("synergynet3/activitypack1/table/fishes/obstacle.png");
			}
			else if (b.getColor().equals(Color.blue))
			{
				img.setImage("synergynet3/activitypack1/table/fishes/blue.png");
			}
			else if (b.getColor().equals(Color.green))
			{
				img.setImage("synergynet3/activitypack1/table/fishes/green.png");
			}
			else if (b.getColor().equals(Color.yellow))
			{
				img.setImage("synergynet3/activitypack1/table/fishes/yellow.png");
			}

			return img;

		}
		catch (ContentTypeNotBoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
