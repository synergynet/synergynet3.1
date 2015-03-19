package multiplicity3.jme3csys.items.shapes;

import java.nio.FloatBuffer;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;

@ImplementsContentItem(target = IColourRectangle.class)
public class JMEColourRectangle extends JMEItem implements IColourRectangle, IInitable {
	private static final Logger log = Logger.getLogger(JMEColourRectangle.class.getName());
	
	private CenteredQuad quad;
	private Geometry quadGeometry;

	private Material mat;

	private ColorRGBA topLeft = ColorRGBA.White;
	private ColorRGBA topRight = ColorRGBA.White;
	private ColorRGBA bottomLeft = ColorRGBA.White;
	private ColorRGBA bottomRight = ColorRGBA.White;
	
	public JMEColourRectangle(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {		
		quad = new CenteredQuad(100, 100);	
		quadGeometry = new Geometry("_quad_geom", quad);

		float[] colorArray = new float[4*4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();
		
		mat = new Material(assetManager, "Common/MatDefs/Misc/VertexColor.j3md");
		mat.getAdditionalRenderState().setAlphaTest(true);
		quadGeometry.setMaterial(mat);
		
		ItemMap.register(quadGeometry, this);
		log.fine("Attaching colour rectangle quad geometry!");
		attachChild(quadGeometry);
	}
	
	public void enableTransparency(){
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}
	
//	@Override
//	public void setZOrder(int zOrder) {
//		super.setZOrder(zOrder);
//		Vector3f newZOrder = quadGeometry.getWorldTranslation().clone();
//		newZOrder.z = zOrder;
//		quadGeometry.getParent().worldToLocal(newZOrder, quadGeometry.getLocalTranslation());
//	}
	
	private void updateColours() {
		FloatBuffer fb = (FloatBuffer) quadGeometry.getMesh().getBuffer(Type.Color).getData();
        fb.rewind();
		fb.put(bottomLeft.r); fb.put(bottomLeft.g); fb.put(bottomLeft.b); fb.put(bottomLeft.a);
		fb.put(bottomRight.r); fb.put(bottomRight.g); fb.put(bottomRight.b); fb.put(bottomRight.a);
		fb.put(topRight.r); fb.put(topRight.g); fb.put(topRight.b); fb.put(topRight.a);
		fb.put(topLeft.r); fb.put(topLeft.g); fb.put(topLeft.b); fb.put(topLeft.a);
	}


	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}

	@Override
	public void setSize(float width, float height) {
		quad = new CenteredQuad(width, height);
		quadGeometry.setMesh(quad);		
		float[] colorArray = new float[4*4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();	
	}

	@Override
	public void setGradientBackground(Gradient g) {
		switch(g.getDirection()) {
		case HORIZONTAL: {
			topLeft = g.getFrom();
			bottomLeft = g.getFrom();
			topRight = g.getTo();
			bottomRight = g.getTo();
			break;
		}
		case VERTICAL: {
			topLeft = g.getFrom();
			topRight = g.getFrom();
			bottomLeft = g.getTo();
			bottomRight = g.getTo();
			break;
		}
		case DIAGONAL: {
			topLeft = g.getFrom();
			topRight = g.getFrom();
			bottomLeft = g.getFrom();
			bottomRight = g.getTo();
			break;
		}
		}
		updateColours();
	}

	@Override
	public void setSolidBackgroundColour(ColorRGBA colorRGBA) {
		this.topLeft = colorRGBA;
		this.topRight = colorRGBA;
		this.bottomLeft = colorRGBA;
		this.bottomRight = colorRGBA;
		updateColours();	
	}

	@Override
	public void setSize(Vector2f size) {
		setSize(size.x, size.y);
	}

	@Override
	public Vector2f getSize() {
		return new Vector2f(quad.getWidth(), quad.getHeight());
	}

	@Override
	public float getWidth() {
		return quad.getWidth();
	}

	@Override
	public float getHeight() {
		return quad.getHeight();
	}

}
