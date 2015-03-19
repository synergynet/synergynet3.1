package synergynet3.activitypack1.core.lightbox.ppt2lightbox.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;

import org.apache.poi.hslf.model.Shape;

public class Convertor {
	private Shape shape;
	private Dimension slideSize;

	public Convertor(Shape shape, Dimension slideSize) {
		this.shape = shape;
		this.slideSize = slideSize;
	}
	
	public Float getSizeFromShapeBounds() {
		Rectangle2D rectangle = shape.getLogicalAnchor2D();
		Point2D.Float size = new Point2D.Float(
				(float)rectangle.getWidth(),
				(float)rectangle.getHeight());
		return convertFloatToScaleZeroToOne(size.x, size.y);
	}
	
	protected Float convertFloatToScaleZeroToOne(float x, float y) {
		return new Point2D.Float(
				x / (float)slideSize.width,
				y / (float)slideSize.height);
	}

	protected Float getPositionFromShapeBounds() {
		Rectangle2D rectangle = shape.getAnchor2D();
		float xMiddle = (float)(rectangle.getX() + rectangle.getWidth() / 2.0);
		float yMiddle = (float)(rectangle.getY() + rectangle.getHeight() / 2.0);
		Point2D.Float positionZeroToOne = convertFloatToScaleZeroToOne(xMiddle, yMiddle);
		return positionZeroToOne;
	}
	
	protected boolean interpretMoveablePropertyFromBackgroundFillColour() {
		return shape.getFill().getForegroundColor().getRed() < 200f;
	}
}
