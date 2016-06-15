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

/**
 * The Class JMEColourRectangle.
 */
@ImplementsContentItem(target = IColourRectangle.class)
public class JMEColourRectangle extends JMEItem implements IColourRectangle, IInitable
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEColourRectangle.class.getName());

	/** The bottom left. */
	private ColorRGBA bottomLeft = ColorRGBA.White;

	/** The bottom right. */
	private ColorRGBA bottomRight = ColorRGBA.White;

	/** The mat. */
	private Material mat;

	/** The quad. */
	private CenteredQuad quad;

	/** The quad geometry. */
	private Geometry quadGeometry;

	/** The top left. */
	private ColorRGBA topLeft = ColorRGBA.White;

	/** The top right. */
	private ColorRGBA topRight = ColorRGBA.White;

	/**
	 * Instantiates a new JME colour rectangle.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public JMEColourRectangle(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.shapes.IColourRectangle#enableTransparency()
	 */
	@Override
	public void enableTransparency()
	{
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getHeight()
	 */
	@Override
	public float getHeight()
	{
		return quad.getHeight();
	}

	// @Override
	// public void setZOrder(int zOrder) {
	// super.setZOrder(zOrder);
	// Vector3f newZOrder = quadGeometry.getWorldTranslation().clone();
	// newZOrder.z = zOrder;
	// quadGeometry.getParent().worldToLocal(newZOrder,
	// quadGeometry.getLocalTranslation());
	// }

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial()
	{
		return quadGeometry;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getSize()
	 */
	@Override
	public Vector2f getSize()
	{
		return new Vector2f(quad.getWidth(), quad.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#getWidth()
	 */
	@Override
	public float getWidth()
	{
		return quad.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager)
	{
		quad = new CenteredQuad(100, 100);
		quadGeometry = new Geometry("_quad_geom", quad);

		float[] colorArray = new float[4 * 4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();

		mat = new Material(assetManager, "Common/MatDefs/Misc/VertexColor.j3md");
		mat.getAdditionalRenderState().setAlphaTest(true);
		quadGeometry.setMaterial(mat);

		ItemMap.register(quadGeometry, this);
		log.fine("Attaching colour rectangle quad geometry!");
		attachChild(quadGeometry);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.shapes.IColourRectangle#setGradientBackground
	 * (multiplicity3.csys.gfx.Gradient)
	 */
	@Override
	public void setGradientBackground(Gradient g)
	{
		switch (g.getDirection())
		{
			case HORIZONTAL:
			{
				topLeft = g.getFrom();
				bottomLeft = g.getFrom();
				topRight = g.getTo();
				bottomRight = g.getTo();
				break;
			}
			case VERTICAL:
			{
				topLeft = g.getFrom();
				topRight = g.getFrom();
				bottomLeft = g.getTo();
				bottomRight = g.getTo();
				break;
			}
			case DIAGONAL:
			{
				topLeft = g.getFrom();
				topRight = g.getFrom();
				bottomLeft = g.getFrom();
				bottomRight = g.getTo();
				break;
			}
		}
		updateColours();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#setSize(float,
	 * float)
	 */
	@Override
	public void setSize(float width, float height)
	{
		quad = new CenteredQuad(width, height);
		quadGeometry.setMesh(quad);
		float[] colorArray = new float[4 * 4];
		quadGeometry.getMesh().setBuffer(Type.Color, 4, colorArray);
		updateColours();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.shapes.IRectangularItem#setSize(com.jme3.math
	 * .Vector2f)
	 */
	@Override
	public void setSize(Vector2f size)
	{
		setSize(size.x, size.y);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.shapes.IColourRectangle#setSolidBackgroundColour
	 * (com.jme3.math.ColorRGBA)
	 */
	@Override
	public void setSolidBackgroundColour(ColorRGBA colorRGBA)
	{
		this.topLeft = colorRGBA;
		this.topRight = colorRGBA;
		this.bottomLeft = colorRGBA;
		this.bottomRight = colorRGBA;
		updateColours();
	}

	/**
	 * Update colours.
	 */
	private void updateColours()
	{
		FloatBuffer fb = (FloatBuffer) quadGeometry.getMesh().getBuffer(Type.Color).getData();
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

}
