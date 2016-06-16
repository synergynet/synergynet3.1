package synergynet3.museum.table.mainapp;

import java.util.ArrayList;

import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.line.ILine;

/**
 * The Class LensVisiblePOI.
 */
public class LensVisiblePOI
{

	/** The lenses. */
	private ArrayList<Lens> lenses = new ArrayList<Lens>();

	/** The lense values. */
	private String lenseValues;

	/** The line. */
	private ILine line;

	/** The poi. */
	private IImage poi;

	/** The x. */
	private float x = 0;

	/** The y. */
	private float y = 0;

	/**
	 * Instantiates a new lens visible poi.
	 *
	 * @param lenseValues
	 *            the lense values
	 * @param poi
	 *            the poi
	 * @param line
	 *            the line
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public LensVisiblePOI(String lenseValues, IImage poi, ILine line, float x, float y)
	{
		this.lenseValues = lenseValues;
		this.poi = poi;
		this.line = line;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the poi
	 */
	public IImage getPoi()
	{
		return poi;
	}

	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * Removes the lens.
	 *
	 * @param lens
	 *            the lens
	 */
	public void removeLens(Lens lens)
	{
		if (lenses.contains(lens))
		{
			lenses.remove(lens);
		}
		if (lenses.size() == 0)
		{
			poi.setVisible(false);
		}
	}

	/**
	 * @param poi
	 *            the poi to set
	 */
	public void setPoi(IImage poi)
	{
		this.poi = poi;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x)
	{
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y)
	{
		this.y = y;
	}

	/**
	 * Update.
	 *
	 * @param lens
	 *            the lens
	 */
	public void update(Lens lens)
	{

		if (lens.isWithinFilter(poi.getRelativeLocation()))
		{
			if (lenseValues.equals(lens.getLensValue()))
			{
				canBeSeenByLens(lens);
			}
			else
			{
				cannotBeSeenByLens(lens);
			}
		}
		else
		{
			if (lenseValues.equals(lens.getLensValue()))
			{
				cannotBeSeenByLens(lens);
			}
			else
			{
				cannotBeSeenByLens(lens);
			}
		}
	}

	/**
	 * Can be seen by lens.
	 *
	 * @param lens
	 *            the lens
	 */
	private void canBeSeenByLens(Lens lens)
	{
		if (!lenses.contains(lens))
		{
			if (lenses.size() == 0)
			{
				poi.setVisible(true);
				if (!line.getSourceItem().isVisible())
				{
					line.setVisible(false);
				}
			}
			lenses.add(lens);
		}
	}

	/**
	 * Cannot be seen by lens.
	 *
	 * @param lens
	 *            the lens
	 */
	private void cannotBeSeenByLens(Lens lens)
	{
		if (lenses.contains(lens))
		{
			lenses.remove(lens);
		}
		if (lenses.size() == 0)
		{
			poi.setVisible(false);
		}
	}

}
