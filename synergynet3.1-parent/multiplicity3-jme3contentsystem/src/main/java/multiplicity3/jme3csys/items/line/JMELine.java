package multiplicity3.jme3csys.items.line;

import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

/**
 * The Class JMELine.
 */
@ImplementsContentItem(target = ILine.class)
public class JMELine extends JMEItem implements ILine, IInitable, IItemListener {

	/** The q. */
	private static Quaternion q = new Quaternion();

	/** The Constant UP. */
	private static final Vector2f UP = new Vector2f(0, 1);

	/** The bottom left. */
	private ColorRGBA bottomLeft = ColorRGBA.White;

	/** The bottom right. */
	private ColorRGBA bottomRight = ColorRGBA.White;

	/** The dest item. */
	private IItem destItem;

	/** The end position. */
	private Vector2f endPosition = new Vector2f(0, 10);

	/** The item visibility changes line visibility. */
	private boolean itemVisibilityChangesLineVisibility = false;

	/** The line width. */
	private float lineWidth = 3f;

	/** The mat. */
	private Material mat;

	/** The min line length. */
	private float minLineLength = 1f;

	/** The mode. */
	private LineMode mode;

	/** The quad. */
	private CenteredQuad quad;

	/** The quad geometry. */
	private Geometry quadGeometry;

	/** The source item. */
	private IItem sourceItem;

	/** The start position. */
	private Vector2f startPosition = new Vector2f(0, 0);

	/** The top left. */
	private ColorRGBA topLeft = ColorRGBA.White;

	/** The top right. */
	private ColorRGBA topRight = ColorRGBA.White;

	/**
	 * Instantiates a new JME line.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public JMELine(String name, UUID uuid) {
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getDestinationItem()
	 */
	@Override
	public IItem getDestinationItem() {
		return destItem;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getEndPosition()
	 */
	@Override
	public Vector2f getEndPosition() {
		return endPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getLength()
	 */
	@Override
	public float getLength() {
		return destItem.getWorldLocation()
				.subtract(sourceItem.getWorldLocation()).length();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getMode()
	 */
	@Override
	public LineMode getMode() {
		return mode;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getSourceItem()
	 */
	@Override
	public IItem getSourceItem() {
		return sourceItem;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#getStartPosition()
	 */
	@Override
	public Vector2f getStartPosition() {
		return startPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager) {
		quad = new CenteredQuad(100, 100);
		quadGeometry = new Geometry("_quad_geom", quad);

		float[] colorArray = new float[4 * 4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();

		mat = new Material(assetManager,
				"multiplicity3/jme3csys/resources/shaders/VertexColour.j3md");
		quadGeometry.setMaterial(mat);

		ItemMap.register(quadGeometry, this);
		attachChild(quadGeometry);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorChanged(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorClicked(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorPressed(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemCursorReleased(
	 * multiplicity3.csys.items.item.IItem,
	 * multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemMoved(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemMoved(IItem item) {
		updateLinkedLine();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemRotated(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemRotated(IItem item) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemScaled(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemScaled(IItem item) {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemVisibilityChanged(
	 * multiplicity3.csys.items.item.IItem, boolean)
	 */
	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible) {
		if (itemVisibilityChangesLineVisibility && itemIsLinkedByThisLine(item)) {
			this.setVisible(isVisible);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemZOrderChanged(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemZOrderChanged(IItem item) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.line.ILine#setDestinationItem(multiplicity3.
	 * csys.items.item.IItem)
	 */
	@Override
	public void setDestinationItem(IItem item) {
		this.destItem = item;
		if (this.destItem != null) {
			this.destItem.removeItemListener(this);
		}
		this.destItem = item;
		this.destItem.addItemListener(this);
		setEndPosition(destItem.getWorldLocation());

		if ((this.sourceItem != null) && (this.destItem != null)) {
			this.mode = LineMode.LINKED;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.line.ILine#setEndPosition(com.jme3.math.Vector2f
	 * )
	 */
	@Override
	public void setEndPosition(Vector2f v) {
		this.endPosition = v;
		updateLine();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.line.ILine#setLineColour(com.jme3.math.ColorRGBA
	 * )
	 */
	@Override
	public void setLineColour(ColorRGBA c) {
		topLeft = c;
		topRight = c;
		bottomLeft = c;
		bottomRight = c;
		updateColours();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#
	 * setLineVisibilityChangesWithItemVisibility(boolean)
	 */
	@Override
	public void setLineVisibilityChangesWithItemVisibility(
			boolean autoVisibilityChange) {
		this.itemVisibilityChangesLineVisibility = autoVisibilityChange;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.line.ILine#setLineWidth(float)
	 */
	@Override
	public void setLineWidth(float width) {
		this.lineWidth = width;
		updateLinkedLine();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.line.ILine#setSourceItem(multiplicity3.csys.
	 * items.item.IItem)
	 */
	@Override
	public void setSourceItem(IItem item) {
		if (this.sourceItem != null) {
			this.sourceItem.removeItemListener(this);
		}
		this.sourceItem = item;
		this.sourceItem.addItemListener(this);
		setStartPosition(sourceItem.getWorldLocation());

		if ((this.sourceItem != null) && (this.destItem != null)) {
			this.mode = LineMode.LINKED;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.line.ILine#setStartPosition(com.jme3.math.Vector2f
	 * )
	 */
	@Override
	public void setStartPosition(Vector2f v) {
		this.startPosition = v;
		updateLine();
	}

	/**
	 * Gets the line height.
	 *
	 * @return the line height
	 */
	private float getLineHeight() {
		return Math.max(minLineLength, endPosition.subtract(startPosition)
				.length());
	}

	/**
	 * Item is linked by this line.
	 *
	 * @param item the item
	 * @return true, if successful
	 */
	private boolean itemIsLinkedByThisLine(IItem item) {
		return (item == sourceItem) || (item == destItem);
	}

	/**
	 * Update colours.
	 */
	private void updateColours() {
		FloatBuffer fb = (FloatBuffer) quad.getBuffer(Type.Color).getData();
		fb.rewind();
		fb.put(bottomLeft.r);
		fb.put(bottomLeft.g);
		fb.put(bottomLeft.b);
		fb.put(bottomLeft.a);
		fb.put(bottomRight.r);
		fb.put(bottomRight.g);
		fb.put(bottomRight.b);
		fb.put(bottomRight.a);
		fb.put(topRight.r);
		fb.put(topRight.g);
		fb.put(topRight.b);
		fb.put(topRight.a);
		fb.put(topLeft.r);
		fb.put(topLeft.g);
		fb.put(topLeft.b);
		fb.put(topLeft.a);
	}

	/**
	 * Update line.
	 */
	private void updateLine() {
		quad.updateGeometry(lineWidth, getLineHeight());
		Vector2f delta = endPosition.subtract(startPosition);
		Vector2f center = startPosition.add(delta.mult(0.5f));
		float angle = delta.angleBetween(UP);
		q = q.fromAngleAxis(angle, new Vector3f(0, 0, -1));

		Vector3f worldTranslation = quadGeometry.getWorldTranslation().clone();
		worldTranslation.x = center.x;
		worldTranslation.y = center.y;

		Vector3f localTranslation = new Vector3f();
		quadGeometry.getParent().worldToLocal(worldTranslation,
				localTranslation);

		quadGeometry.setLocalTranslation(localTranslation);

		quadGeometry.setLocalRotation(q);
	}

	// ***********

	/**
	 * Update linked line.
	 */
	private void updateLinkedLine() {
		if ((destItem == null) || (sourceItem == null)) {
			return;
		}
		this.startPosition = sourceItem.getWorldLocation().clone();
		this.endPosition = destItem.getWorldLocation().clone();
		updateLine();
	}

}
