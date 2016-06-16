package synergynet3.museum.table.mainapp.ptmViewer;

import jpview.gui.PTMCanvas;
import jpview.ptms.PTM;
import jpview.transforms.PixelTransformOp;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

import com.jme3.math.Vector2f;

/**
 * The listener interface for receiving PTM events. The class that is interested
 * in processing a PTM event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addPTMListener<code> method. When
 * the PTM event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PTMEvent
 */
public class PTMListener extends MultiTouchEventAdapter
{

	/** The canvas. */
	private PTMCanvas canvas;

	/** The parent item. */
	private IItem parentItem;

	/** The pixel transform op. */
	private PixelTransformOp pixelTransformOp;

	/** The ptm. */
	private PTM ptm;

	/** The ptm event dispatcher. */
	private PTMEventDispatcher ptmEventDispatcher;

	/** The stage. */
	private IStage stage;

	/** The height. */
	private float width, height;

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.MultiTouchEventAdapter#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event)
	{

		// localisation transformation
		Vector2f newPos = event.getPosition();
		newPos = stage.tableToWorld(newPos);
		newPos = newPos.subtract(parentItem.getWorldLocation());
		newPos = newPos.mult(1 / parentItem.getRelativeScale());
		newPos.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);
		newPos = newPos.add(new Vector2f(width / 2, height / 2));
		newPos = newPos.mult(canvas.getDisplayWidth() / width);

		pixelTransformOp.transformPixels(canvas.getPixels(), ptm, Math.round(newPos.x), Math.round(newPos.y));
		canvas.paintImmediately(0, 0, canvas.getDisplayWidth(), canvas.getDisplayHeight());

		ptmEventDispatcher.generateImage();

	}

	/**
	 * Sets the values.
	 *
	 * @param ptm
	 *            the ptm
	 * @param pixelTransformOp
	 *            the pixel transform op
	 * @param canvas
	 *            the canvas
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param stage
	 *            the stage
	 * @param parentItem
	 *            the parent item
	 * @param ptmEventDispatcher
	 *            the ptm event dispatcher
	 */
	public void setValues(PTM ptm, PixelTransformOp pixelTransformOp, PTMCanvas canvas, float width, float height, IStage stage, IItem parentItem, PTMEventDispatcher ptmEventDispatcher)
	{
		this.ptm = ptm;
		this.pixelTransformOp = pixelTransformOp;
		this.canvas = canvas;
		this.width = width;
		this.height = height;
		this.stage = stage;
		this.parentItem = parentItem;
		this.ptmEventDispatcher = ptmEventDispatcher;
	}

}
