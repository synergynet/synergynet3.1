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

@ImplementsContentItem(target = ILine.class)
public class JMELine extends JMEItem implements ILine, IInitable, IItemListener {

	private float lineWidth = 3f;
	private float minLineLength = 1f;
	private CenteredQuad quad;
	private Geometry quadGeometry;
	private Material mat;
	
	private ColorRGBA topLeft = ColorRGBA.White;
	private ColorRGBA topRight = ColorRGBA.White;
	private ColorRGBA bottomLeft = ColorRGBA.White;
	private ColorRGBA bottomRight = ColorRGBA.White;
	private IItem destItem;
	private IItem sourceItem;
	private Vector2f startPosition = new Vector2f(0, 0);
	private Vector2f endPosition = new Vector2f(0, 10);
	private LineMode mode;
	private boolean itemVisibilityChangesLineVisibility = false;
	
	public JMELine(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		quad = new CenteredQuad(100, 100);	
		quadGeometry = new Geometry("_quad_geom", quad);

		float[] colorArray = new float[4*4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();
		
		mat = new Material(assetManager, "multiplicity3/jme3csys/resources/shaders/VertexColour.j3md");
		quadGeometry.setMaterial(mat);
		
		ItemMap.register(quadGeometry, this);
		attachChild(quadGeometry);
	}
	
	private void updateColours() {
		FloatBuffer fb = (FloatBuffer) quad.getBuffer(Type.Color).getData();
        fb.rewind();
		fb.put(bottomLeft.r); fb.put(bottomLeft.g); fb.put(bottomLeft.b); fb.put(bottomLeft.a);
		fb.put(bottomRight.r); fb.put(bottomRight.g); fb.put(bottomRight.b); fb.put(bottomRight.a);
		fb.put(topRight.r); fb.put(topRight.g); fb.put(topRight.b); fb.put(topRight.a);
		fb.put(topLeft.r); fb.put(topLeft.g); fb.put(topLeft.b); fb.put(topLeft.a);
	}
	
	private static final Vector2f UP = new Vector2f(0, 1);
	private static Quaternion q = new Quaternion();
	
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
		quadGeometry.getParent().worldToLocal(worldTranslation, localTranslation);
		
		quadGeometry.setLocalTranslation(localTranslation);
		
		
		quadGeometry.setLocalRotation(q);
	}
	
	private void updateLinkedLine() {
		if(destItem == null || sourceItem == null) return;
		this.startPosition = sourceItem.getWorldLocation().clone();
		this.endPosition = destItem.getWorldLocation().clone();
		updateLine();
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}
	
	@Override
	public void setLineWidth(float width) {
		this.lineWidth = width;
		updateLinkedLine();
	}

	private float getLineHeight() {
		return Math.max(minLineLength,
				endPosition.subtract(startPosition).length());
	}
	
	@Override
	public void setEndPosition(Vector2f v) {
		this.endPosition = v;
		updateLine();
	}


	@Override
	public void setStartPosition(Vector2f v) {
		this.startPosition = v;
		updateLine();
	}

	@Override
	public void setSourceItem(IItem item) {
		if(this.sourceItem != null) {
			this.sourceItem.removeItemListener(this);
		}
		this.sourceItem = item;
		this.sourceItem.addItemListener(this);
		setStartPosition(sourceItem.getWorldLocation());
		
		if(this.sourceItem != null && this.destItem != null) {
			this.mode = LineMode.LINKED;
		}
	}

	@Override
	public void setDestinationItem(IItem item) {
		this.destItem = item;
		if(this.destItem != null) {
			this.destItem.removeItemListener(this);
		}
		this.destItem = item;		
		this.destItem.addItemListener(this);
		setEndPosition(destItem.getWorldLocation());
		
		if(this.sourceItem != null && this.destItem != null) {
			this.mode = LineMode.LINKED;
		}
	}
	
	@Override
	public void setLineVisibilityChangesWithItemVisibility(
			boolean autoVisibilityChange) {
		this.itemVisibilityChangesLineVisibility = autoVisibilityChange;
	}


	@Override
	public void itemMoved(IItem item) {
		updateLinkedLine();
	}


	@Override
	public void itemRotated(IItem item) {}

	@Override
	public void itemScaled(IItem item) {}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemZOrderChanged(IItem item) {}

	@Override
	public LineMode getMode() {
		return mode;
	}
	
	@Override
	public Vector2f getStartPosition() {
		return startPosition;
	}

	@Override
	public Vector2f getEndPosition() {
		return endPosition;
	}
	
	@Override
	public void setLineColour(ColorRGBA c) {
		topLeft = c;
		topRight = c;
		bottomLeft = c;
		bottomRight = c;
		updateColours();
	}

	@Override
	public float getLength() {
		return destItem.getWorldLocation().subtract(sourceItem.getWorldLocation()).length();
	}

	@Override
	public IItem getSourceItem() {
		return sourceItem;
	}

	@Override
	public IItem getDestinationItem() {
		return destItem;
	}

	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible) {
		if(itemVisibilityChangesLineVisibility && itemIsLinkedByThisLine(item)) {
			this.setVisible(isVisible);
		}
	}

	// ***********
	
	private boolean itemIsLinkedByThisLine(IItem item) {
		return item == sourceItem || item == destItem;
	}


}
