package synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;

import org.apache.poi.hslf.model.Shape;

/**
 * The Class Convertor.
 */
public class Convertor
{

	/** The shape. */
	private Shape shape;

	/** The slide size. */
	private Dimension slideSize;

	/**
	 * Instantiates a new convertor.
	 *
	 * @param shape
	 *            the shape
	 * @param slideSize
	 *            the slide size
	 */
	public Convertor(Shape shape, Dimension slideSize)
	{
		this.shape = shape;
		this.slideSize = slideSize;
	}

	/**
	 * Gets the size from shape bounds.
	 *
	 * @return the size from shape bounds
	 */
	public Float getSizeFromShapeBounds()
	{
		Rectangle2D rectangle = shape.getLogicalAnchor2D();
		Point2D.Float size = new Point2D.Float((float) rectangle.getWidth(), (float) rectangle.getHeight());
		return convertFloatToScaleZeroToOne(size.x, size.y);
	}

	/**
	 * Convert float to scale zero to one.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the float
	 */
	protected Float convertFloatToScaleZeroToOne(float x, float y)
	{
		return new Point2D.Float(x / slideSize.width, y / slideSize.height);
	}

	/**
	 * Gets the position from shape bounds.
	 *
	 * @return the position from shape bounds
	 */
	protected Float getPositionFromShapeBounds()
	{
		Rectangle2D rectangle = shape.getAnchor2D();
		float xMiddle = (float) (rectangle.getX() + (rectangle.getWidth() / 2.0));
		float yMiddle = (float) (rectangle.getY() + (rectangle.getHeight() / 2.0));
		Point2D.Float positionZeroToOne = convertFloatToScaleZeroToOne(xMiddle, yMiddle);
		return positionZeroToOne;
	}

	/**
	 * Interpret moveable property from background fill colour.
	 *
	 * @return true, if successful
	 */
	protected boolean interpretMoveablePropertyFromBackgroundFillColour()
	{
		return shape.getFill().getForegroundColor().getRed() < 200f;
	}
}
